package Handy.Prueba

import org.springframework.scheduling.annotation.Scheduled

class ProductService {

    @Scheduled(initialDelay = 5000L, fixedDelay = 5000L)
    def exectuteJob() {
        println("Se ejecuta job para gestión de productos")
    }
}