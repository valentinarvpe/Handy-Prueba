package Handy.Prueba

import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class OrderServiceSpec extends Specification implements ServiceUnitTest<OrderService> {

     void "test something"() {
        expect:
        service.doSomething()
     }
}
