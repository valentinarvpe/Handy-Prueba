package Handy.Prueba

class Logs {
    int id
    String description
    Date date = new Date()

    Logs(String description) {
        this.description = description
    }
    static mapping = {
        id generator: 'increment'
        version false
    }
    static constraints = {
    }
}