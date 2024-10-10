package Handy.Prueba

class BootStrap {
    OrderService orderService

    def init = { servletContext ->
        orderService.execute()
    }
    def destroy = {
    }
}