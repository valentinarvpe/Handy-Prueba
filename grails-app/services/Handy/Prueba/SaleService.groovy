package Handy.Prueba

import grails.gorm.transactions.Transactional
import grails.plugin.json.builder.JsonOutput
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import io.github.cdimascio.dotenv.Dotenv
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import groovy.util.logging.Slf4j
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Service
@Slf4j
class SaleService {

    static lazyInit = false

    @Scheduled(initialDelay = 5000L, fixedRate = 50000L)
    def scheduledTask() {
        println "Tarea programada ejecutándose..."
        getConexion()
    }

    def getConexion() {
        try {
            Dotenv dotenv = Dotenv.load()
            def date = LocalDateTime.now()
            def startDate = parseDate(date.with(LocalTime.MIDNIGHT))
            def endDate = parseDate(date.plusDays(1))

            // Codifica los parámetros de la URL
            def encodedStartDate = URLEncoder.encode(startDate, "UTF-8")
            def encodedEndDate = URLEncoder.encode(endDate, "UTF-8")

            def url = "https://hub.handy.la/api/v2/salesOrder?start=${encodedStartDate}&end=${encodedEndDate}"
            HttpURLConnection connection = url.toURL().openConnection()
            connection.setRequestMethod("GET")
            connection.setRequestProperty("Authorization", "Bearer ${dotenv.get("API_HANDY_TOKEN")}")
            def data = parserData(connection)
            if (data) {
                transactActions(data)
            }
        } catch (Exception ex) {
            ex.printStackTrace()
            log.error("Error al consumir servicio de Handy ", ex.printStackTrace())
        }
    }

    def transactActions(data) {
        data.each {
            if (validateOrder(it.id)) {
                saveOrder(it)
            }
            syncronizedOrders(data.collect {
                it.id
            })
        }
    }

    def parseDate(date) {
        def formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        def dateFormatted = date.format(formatter)
        println("fecha formateada ${dateFormatted}")
        return dateFormatted
    }

    def parserData(connection) {
        JsonSlurper json = new JsonSlurper()
        if (connection.responseCode == 200) {
            def data = json.parseText(connection.content.text)
            List orders = data.salesOrders
            if (data.salesOrders.size() > 0) {
                def jsonSlurper = new JsonSlurper()
                def dataorders = orders.collect {
                    def jsonBuilder = new JsonBuilder(it)
                    it = jsonBuilder.content
                }
                return dataorders
            }
        }
    }

    @Transactional
    def saveOrder(dataOrder) {
        try {
            def dataCustomer = dataOrder.customer
            def jsonString = JsonOutput.toJson(dataCustomer)
            def customer = new JsonSlurper().parseText(jsonString)
            SaleOrder saleOrder = new SaleOrder
                    (dataOrder.id, customer.description, customer.code, customer.city,
                            dataOrder.sellerComment, dataOrder.operativeStatus, dataOrder.totalSales)
            if (!saleOrder.validate()) {
                log.warn("El pedido #${dataOrder.id} no es valido")
                return
            }
            saleOrder.save(flush: true)
            def message = "El pedido #${dataOrder.id} se guardó correctamente "
            saveLog("Transacción en saveOrder : ${message}")
            log.info(message)
        } catch (Exception ex) {
            log.error("No se pudo realizar ninguna transacción ${ex.message}")
        }

    }

    @Transactional
    def validateOrder(id) {
        SaleOrder order = SaleOrder.get(id)
        if (!order) {
            return true
        }
        log.warn "Este pedido ya se encuentra registrado "
        return false
    }

    @Transactional
    def saveLog(String text) {
        try {
            Log log = new Log(new Date(), text)
            log.save(flush: true)
        } catch (Exception ex) {
            log.error("Error al guardar en la tabla de logs ${ex.getMessage()}")
        }
    }

    @Transactional
    def syncronizedOrders(List dataOrdersHandy) {
        def localOrderIds = SaleOrder.findAll().collect {
            it.id
        }
        localOrderIds.each {
            localIdOrder ->
                {
                    if (!dataOrdersHandy.contains(localIdOrder)) {
                        deleteOrder(localIdOrder)
                    }
                }
        }
    }

    @Transactional
    def deleteOrder(idOrderLocal) {
        try {
            SaleOrder orderToDelete = SaleOrder.get(idOrderLocal)
            orderToDelete.delete(flush: true)
            def message = "Pedido ${idOrderLocal} eliminado correctamente"
            log.info(message)
            saveLog("Transacción en deleteOrder: ${message}")
        } catch (Exception ex) {
            log.error("Error al eliminar el pedido", ex.message)
        }
    }
}