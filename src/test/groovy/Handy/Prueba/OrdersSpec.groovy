package Handy.Prueba

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class OrdersSpec extends Specification implements DomainUnitTest<Orders> {

     void "test domain constraints"() {
        when:
        Orders domain = new Orders()
        //TODO: Set domain props here

        then:
        domain.validate()
     }
}
