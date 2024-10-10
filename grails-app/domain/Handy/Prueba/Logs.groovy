package Handy.Prueba

class Logs {
    int id
    String description

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