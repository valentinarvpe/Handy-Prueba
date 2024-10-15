package Handy.Prueba

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class LogSpec extends Specification implements DomainUnitTest<Log> {

     void "test domain constraints"() {
        when:
        Log domain = new Log()
        //TODO: Set domain props here

        then:
        domain.validate()
     }
}
