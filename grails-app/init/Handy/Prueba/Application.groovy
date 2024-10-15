package Handy.Prueba

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import groovy.transform.CompileStatic
import grails.plugins.metadata.*
import io.github.cdimascio.dotenv.Dotenv
import org.springframework.scheduling.annotation.EnableScheduling

@CompileStatic
@EnableScheduling  // Habilita las tareas programadas
class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        Dotenv dotenv = Dotenv.load()
        // Establece las variables de entorno para usarlas en el application.yml
        System.setProperty("API_HANDY_TOKEN", dotenv.get("API_HANDY_TOKEN"))
        GrailsApp.run(Application, args)
    }
}
