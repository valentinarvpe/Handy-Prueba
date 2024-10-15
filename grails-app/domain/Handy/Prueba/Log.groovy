package Handy.Prueba

class Log {

    Integer id
    Date dateRegister
    String text

    Log(Date dateRegister, String text) {
        this.dateRegister = dateRegister
        this.text = text
    }

    static constraints = {
    }

    static mapping = {
        version(false)
    }
}