package Handy.Prueba

import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class SaleServiceSpec extends Specification implements ServiceUnitTest<SaleService> {

     void "test something"() {
        expect:
        service.doSomething()
     }
}
