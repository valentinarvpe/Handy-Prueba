package Handy.Prueba

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class SaleOrderSpec extends Specification implements DomainUnitTest<SaleOrder> {

     void "test domain constraints"() {
        when:
        SaleOrder domain = new SaleOrder()
        //TODO: Set domain props here

        then:
        domain.validate()
     }
}
