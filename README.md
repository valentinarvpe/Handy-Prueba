# Handy-Prueba
Repositorio creado para la prueba tecnica solicitada por HANDY
Este proyecto es un restapi, y su función principal es gestionar los pedidos, que se generan en HANDY para así sincronizar con un erp

# Indice
- Instalación
- Uso

## Instalación
Clona el repositorio
````
bash git clone
https://github.com/valentinarvpe/Handy-Prueba.git

Instalar JDK 17 en adelante
Instalar Gradle
Instalar Grails >> Puede instalar SDK y luego ejecutar
sdk install grails

Compila las dependencias
cd Handy-Prueba
gradle clean
gradle build

## Grails 6.2.1 Documentation

- [User Guide](https://docs.grails.org/6.2.1/guide/index.html)
- [API Reference](https://docs.grails.org/6.2.1/api/index.html)
- [Grails Guides](https://guides.grails.org/index.html)
---

## Feature views-json documentation

- [Grails JSON Views documentation](https://views.grails.org/)

````
## USO
Para iniciar el proyecto ejecuta
````
gradle run
````
Tener en cuenta que esta aplicación usa como variable de entorno el token que consume el servicio de traer los pedidos desde Handy.
- Cree su archivo .env 
- Cree la variable API_HANDY_TOKEN
- Agregue su token
