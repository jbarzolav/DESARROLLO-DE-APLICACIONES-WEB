# Laboratorio 13 de Service Discovery y API Gateway

El proyecto configura Eureka como registro de servicios y Spring Cloud Gateway como punto de entrada para las API de categorías, productos, clientes y pedidos. La actividad de clientes y pedidos se implementó con rutas explícitas en `gateway-service/src/main/resources/application.yaml`.

## Ejecución local

Desde esta carpeta, compila con Java 17 o superior y Maven.

```powershell
mvn clean verify
```

Ejecuta cada comando en una terminal distinta, desde la misma carpeta. Inicia Eureka primero y espera a que su panel esté disponible en [localhost:8761](http://localhost:8761/).

```powershell
java -jar eureka-server/target/eureka-server-1.0.0.jar
java -jar categoria-service/target/categoria-service-1.0.0.jar
java -jar producto-service/target/producto-service-1.0.0.jar
java -jar cliente-service/target/cliente-service-1.0.0.jar
java -jar pedido-service/target/pedido-service-1.0.0.jar
java -jar gateway-service/target/gateway-service-1.0.0.jar
```

Comprueba que el panel de Eureka muestre el Gateway y los cuatro servicios en estado `UP`. El Gateway puede tardar unos segundos en actualizar su registro local.

| Servicio | Puerto directo | URL a través del Gateway |
| --- | ---: | --- |
| Categoría | 8082 | [http://localhost:8080/api/categorias](http://localhost:8080/api/categorias) |
| Producto | 8083 | [http://localhost:8080/api/productos](http://localhost:8080/api/productos) |
| Cliente | 8081 | [http://localhost:8080/api/clientes](http://localhost:8080/api/clientes) |
| Pedido | 8084 | [http://localhost:8080/api/pedidos](http://localhost:8080/api/pedidos) |

El perfil predeterminado usa una base H2 en memoria por servicio. Las colecciones devuelven `[]` al iniciar y los datos de prueba se reinician al detener el servicio. Se conservaron los perfiles `dev` y `prod` de Semana 11 para usar con MySQL.

## Resultados de la verificación

El 18 de noviembre de 2026, `mvn clean verify` compiló los seis módulos. El panel de Eureka respondió con HTTP 200 y el registro mostró los cinco clientes en estado `UP`.

| Ruta | GET de la colección | POST de un registro | GET por ID |
| --- | ---: | ---: | ---: |
| `/api/categorias` | 200 | 201 | 200 |
| `/api/productos` | 200 | 201 | 200 |
| `/api/clientes` | 200 | 201 | 200 |
| `/api/pedidos` | 200 | 201 | 200 |

El pedido se creó con el cliente y el producto de la prueba, incluido un detalle. La consulta de la ruta automática `/cliente-service/api/clientes` también devolvió HTTP 200.

## Conclusiones

1. Eureka centraliza el registro. Su panel permite comprobar qué aplicaciones se registraron y cuáles figuran en estado `UP`.
2. El Gateway permite acceder a las cuatro API por el puerto 8080, aunque cada microservicio se ejecute en un puerto distinto.
3. Una URI `lb://` identifica el destino por su nombre en Eureka. Spring Cloud LoadBalancer resuelve la instancia a partir del registro.
4. Añadir rutas para clientes y pedidos permite publicar sus controladores existentes. El predicado `Path` cubre tanto la colección como las consultas por identificador.
5. El localizador de descubrimiento genera rutas con el nombre del servicio. Así, `/cliente-service/api/clientes` y la ruta explícita `/api/clientes` pueden llegar al mismo controlador.

Se utilizó Spring Boot 3.5.15, indicado en los metadatos del proyecto de la guía, y Spring Cloud 2025.0.0, indicado en su POM de ejemplo. La configuración usa el prefijo de [Spring Cloud Gateway Server WebFlux](https://spring.io/blog/2025/05/29/spring-cloud-2025-0-0-is-abvailable/).
