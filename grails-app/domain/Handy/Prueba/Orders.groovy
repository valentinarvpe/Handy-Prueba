package Handy.Prueba

class Orders {

    int id
    String descripcion
    BigDecimal total

    Orders(int id, String descripcion, BigDecimal total) {
        this.id = id
        this.descripcion = descripcion
        this.total = total
    }

    static constraints = {
    }
    static mapping = {
        id generator: 'assigned'
        version false
    }
}