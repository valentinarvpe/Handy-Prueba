package Handy.Prueba

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class LogsSpec extends Specification implements DomainUnitTest<Logs> {

     void "test domain constraints"() {
        when:
        Logs domain = new Logs()
        //TODO: Set domain props here

        then:
        domain.validate()
     }
}
