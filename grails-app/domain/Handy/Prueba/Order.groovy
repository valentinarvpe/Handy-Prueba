package Handy.Prueba

class Order {

    int id
    String descripcion
    BigDecimal total

    Order(Integer id, String descripcion, BigDecimal total) {
        this.id = id
        this.descripcion = descripcion
        this.total = total
    }

    static constraints = {
    }

}