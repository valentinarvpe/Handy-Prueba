package Handy.Prueba

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class OrdersSpec extends Specification implements DomainUnitTest<Orders> {

    //TEST PARA VALIDAR EL GUARDADO
    void "test save order"() {
        when:
        def order = new Orders(11, "PRUEBAS DE GUARDADO", BigDecimal.ONE)
        then:
        order.validate()
        and:
        order.descripcion == "PRUEBAS DE GUARDADO"
    }
}
