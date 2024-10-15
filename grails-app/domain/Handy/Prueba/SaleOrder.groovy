package Handy.Prueba

class SaleOrder {
    Integer id
    String nameClient
    String idClient
    String city
    String description
    String status
    BigDecimal total

    SaleOrder(Integer id, String nameClient, String idClient, String city, String description, String status, BigDecimal total) {
        this.id = id
        this.nameClient = nameClient
        this.idClient = idClient
        this.city = city
        this.description = description
        this.status = status
        this.total = total
    }
    static constraints = {
        description nullable: true
    }

    static  mapping = {
        version false
        id generator: 'assigned'
    }
}