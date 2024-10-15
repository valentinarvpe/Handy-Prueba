package Handy.Prueba

import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class ProductServiceSpec extends Specification implements ServiceUnitTest<ProductService> {

     void "test something"() {
        expect:
        service.doSomething()
     }
}
