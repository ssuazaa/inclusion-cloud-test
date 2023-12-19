# Documentación

### Tecnologías:

* Java 11
* Gradle 8.5
* Spring Boot 2.7.8
* MongoDB Embebido 4.0.21
* Arquitectura Hexagonal
* Programación Reactiva (Spring WebFlux)

### 0. Opcional: 
Existe un branch llamado java-17 que cuenta con la versión de Spring Boot 3.2.0

### 1. Ejecución de la api:

Seguir los siguientes pasos:

* Clonar repositorio:
* git clone https://github.com/ssuazaa/inclusion-cloud-test
* ejecutar el comando <code>./gradlew bootRun</code>

### 2. Documentación de la api

Al iniciar la aplicación en local la documentación se puede encontrar en esta url:
* http://localhost:8080/webjars/swagger-ui/index.html#/

### 3. Coverage:

Se puede visitar el siguiente enlace para verificar el coverage y otra información de la aplicación:

https://sonarcloud.io/summary/overall?id=ssuazaa_inclusion-cloud-test

### 4. Documentación flujos

* 4.1 Crear una nueva petición y operación:

Se debe ejecutar el serivicio y enviar el body:

POST -> http://localhost:8080/api/v1/problems

BODY ->
```json
{
  "amount": 2,
  "cases": [
    {
      "x": 7,
      "y": 5,
      "n": 12345
    },
    {
      "x": 5,
      "y": 0,
      "n": 4
    }
  ]
}
```

este servicio retorna un estado 201 y un header LOCATION con la url con la que se puede consultar información del Problem, por ejemplo un valor puede ser:

http://localhost:8080/api/v1/problems/6b879432-7351-4035-819b-d9b80b122e9a

* 4.2 Consultar por id:

Teniendo en cuenta la anterior url del header LOCATION, se debe ejecutar el serivicio:

GET -> http://localhost:8080/api/v1/problems/6b879432-7351-4035-819b-d9b80b122e9a

la respuesta puede tener 2 formas:

200 OK:

RESPONSE ->
```json
{
  "id": "6b879432-7351-4035-819b-d9b80b122e9a",
  "amount": 2,
  "cases": [
    {
      "x": 7,
      "y": 5,
      "n": 12345,
      "result": 12339
    },
    {
      "x": 5,
      "y": 0,
      "n": 4,
      "result": 0
    }
  ]
}
```

404 NOT_FOUND:

RESPONSE ->
```json
{
  "key": "PROBLEM_NOT_FOUND",
  "message": "Problem with id '6b879432-7351-4035-819b-d9b80b122e9a' was not found",
  "dateTime": "2023-12-11T16:47:34.053768437"
}
```

* 4.3 Consultar todos:

Se debe ejecutar el serivicio:

GET -> http://localhost:8080/api/v1/problems

RESPONSE ->
```json
[
  {
    "id": "6b879432-7351-4035-819b-d9b80b122e9a",
    "amount": 2,
    "cases": [
      {
        "x": 7,
        "y": 5,
        "n": 12345,
        "result": 12339
      },
      {
        "x": 5,
        "y": 0,
        "n": 4,
        "result": 0
      }
    ]
  }
]
```