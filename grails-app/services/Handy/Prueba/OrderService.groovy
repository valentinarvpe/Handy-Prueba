package Handy.Prueba

import grails.gorm.transactions.Transactional
import groovy.json.JsonSlurper
import io.github.cdimascio.dotenv.Dotenv
import org.springframework.beans.factory.annotation.Value

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
        timer.scheduleAtFixedRate(task, 5000, 3 * 60 * 1000)
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
            def orders = data.salesOrders ? []
            //println(json.parseText(connection.content.text))
            //println(data.salesOrders)
            saveOrders(data.salesOrders)
            syncronizedOrders(data.salesOrders.collect {
                it.id
            })


        }
    }

    @Transactional
    def saveOrders(List data) {
        def message = []
        Map response = new HashMap()
        data.each { item ->
            {
                JsonSlurper slurper = new JsonSlurper()
                print(item.getAt("id"))
                print(item.getAt("sellerComment"))
                print(item.getAt("totalSales"))
                try {
                    Orders order = new Orders(item.getAt("id"), item.getAt("sellerComment"), item.getAt("totalSales"))
                    order.save(flush: true)
                    message.add("Pedido #${order.id} guardado correctamente en nuestra BD")
                } catch (Exception ex) {
                    ex.printStackTrace()
                    message.add("Error al guardar pedido")
                }
                Logs log = new Logs(1, "Transacción de guardado")
                log.save(flush:true)
            }
        }
        //response.put(message)
        println(message)
    }

    @Transactional
    def syncronizedOrders(List<String> dataHandy) {
        println(dataHandy)
        def orderList = Orders.findAll()
        def idsLocalOrders =  orderList.collect {
            it.id
        }
        idsLocalOrders.each {idOrderLocal -> {
            if(!dataHandy.contains(idOrderLocal)){
                Order orderToDelete = Order.findById(idOrderLocal)
                if(orderToDelete){
                    orderToDelete.delete(flush:true)
                    println("Pedido ${idOrderLocal} eliminado correctamente")
                }
            }
        }}

    }
}