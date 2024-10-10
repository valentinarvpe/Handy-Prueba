package Handy.Prueba

import grails.gorm.transactions.Transactional
import groovy.json.JsonSlurper
import io.github.cdimascio.dotenv.Dotenv

class OrderService {

    def execute() {
        Timer timer = new Timer()
        TimerTask task = new TimerTask() {
            @Override
            void run() {
                print("Ejecución de tarea programada ${new Date()}")
                getOrdersfromHandy()
            }
        }
        //10 * 60 * 10000 >> Se ejecuta a los 10 minutos
        //5 * 60 * 5000 > se ejecuta a los 5
        timer.scheduleAtFixedRate(task, 8000, 10 * 60 * 10000)
        //timer.scheduleAtFixedRate(task, 8000, 3 * 60 * 1000)
    }

    def getOrdersfromHandy() {
        Dotenv dotenv = Dotenv.load()
        def url = "https://hub.handy.la/api/v2/salesOrder"
        HttpURLConnection connection = url.toURL().openConnection()
        connection.setRequestMethod("GET")
        connection.setRequestProperty("Authorization", "Bearer ${dotenv.get("API_HANDY_TOKEN")}")
        println(connection.responseCode)
        JsonSlurper json = new JsonSlurper()
        if (connection.responseCode == 200) {
            def data = json.parseText(connection.content.text)
            if (data.salesOrders.size()) {
                println(saveOrders(data.salesOrders))
                syncronizedOrders(data.salesOrders.collect {
                    it.id
                })
            } else {
                println("No se encontró ningún pedido en Handy")
            }
        } else {
            println("Hubo un error al conectarse al api. Por favor valide su token de autorizacion")
        }
    }

    //Metodo que guarda los pedidos que vienen de HANDY
    @Transactional
    def saveOrders(List data) {
        def message = []
        Map response = new HashMap()
        data.each { item ->
            {
                int id = item.getAt("id")?.toInteger()
                BigDecimal total = item.getAt("totalSales")?.toBigDecimal()
                def description = item.getAt("sellerComment") == null ? "Sin descripción" : item.getAt("sellerComment")
                Orders order = new Orders(id, description, total)
                if (order.validate()) {
                    try {
                        order.save(flush: true, failOnError: true)
                        message.add("Pedido #${order.id} guardado correctamente en nuestra BD")
                    } catch (Exception ex) {
                        ex.printStackTrace()
                        message.add("Error al guardar pedido")
                    }
                } else {
                    println(order.errors)
                }
                Logs log = new Logs("Transacción de guardado")
                log.save(flush: true)
            }
        }
        response.put("response", message)
        return response
    }

    //Metodo que elimina pedidos en local cuando no se encuentra en HANDY
    @Transactional
    def syncronizedOrders(List<String> dataHandy) {
        def orderList = Orders.findAll()
        def idsLocalOrders = orderList.collect {
            it.id
        }
        idsLocalOrders.each { idOrderLocal ->
            {
                if (!dataHandy.contains(idOrderLocal)) {
                    try {
                        Orders orderToDelete = Orders.findById(idOrderLocal)
                        if (orderToDelete) {
                            orderToDelete.delete(flush: true)
                            println("Pedido ${idOrderLocal} eliminado correctamente")
                            Logs log = new Logs("Transacción de elimindo")
                            log.save(flush: true)
                        }
                    } catch (Exception ex) {
                        println("No se pudo eliminar el pedido ${ex.getMessage()}")
                    }
                }
            }
        }

    }
}