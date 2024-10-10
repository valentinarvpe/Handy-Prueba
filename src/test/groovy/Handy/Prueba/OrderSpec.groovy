package Handy.Prueba

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class OrderSpec extends Specification implements DomainUnitTest<Order> {

     void "test domain constraints"() {
        when:
        Order domain = new Order()

        then:
        domain.validate()
     }
     void "orderSave" () {
         given: "a new order sale"
         def orderSale = new Orders(id: 1, descripcion: "COMENTARIO TEST", totalSales: 110000)
         when: "saving the order sale"
         orderSale.save(flush:true)
         then: "Order saved"
         Orders.count() == 1
         Orders.first().descripcion == "COMENTARIO TEST"
         //.Wip testing..///
     }
}
