# Laboratorio 12 de comunicación entre microservicios

Eureka registra los cuatro servicios de negocio. OpenFeign consulta `categoria-service` desde `producto-service` y consulta `cliente-service` y `producto-service` desde `pedido-service`. Los clientes Feign usan los nombres registrados en Eureka y cada servicio tiene su propia base de datos.

## Ejecutar en Windows

Requisitos de la guía: **JDK 25**, Maven, Spring Boot **3.5.15** y Spring Cloud **2025.0.0**. Desde la carpeta `microservicios`, ejecuta `mvn clean verify`. Luego inicia cada módulo en una terminal distinta. Inicia Eureka primero y abre su panel en [localhost:8761](http://localhost:8761/).

```powershell
java -jar eureka-server/target/eureka-server-1.0.0.jar
java -jar categoria-service/target/categoria-service-1.0.0.jar
java -jar producto-service/target/producto-service-1.0.0.jar
java -jar cliente-service/target/cliente-service-1.0.0.jar
java -jar pedido-service/target/pedido-service-1.0.0.jar
```

| Servicio | Puerto local | Ruta |
| --- | ---: | --- |
| Eureka | 8761 | `/` |
| Categorías | 8082 | `/api/categorias` |
| Productos | 8083 | `/api/productos` |
| Clientes | 8081 | `/api/clientes` |
| Pedidos | 8084 | `/api/pedidos` |

Espera a que los cuatro servicios aparezcan como `UP` y a que los clientes de Eureka actualicen su registro local. El perfil predeterminado usa cuatro bases H2 independientes en memoria. Los registros se reinician al detener los servicios. Para usar MySQL, activa el perfil `dev`, crea las bases indicadas en los archivos `application-dev.yml` y configura `DB_USER` y `DB_PASSWORD`.

## Probar la comunicación

Ejecuta las solicitudes en Postman o desde PowerShell. Cada bloque de datos crea un registro de prueba. El orden usa los ID que devuelven los servicios.

```powershell
$headers = @{ ContentType = 'application/json; charset=utf-8' }

$categoria = Invoke-RestMethod -Method Post -Uri 'http://localhost:8082/api/categorias' @headers -Body '{"nombre":"Tecnología","estado":"ACTIVO"}'
$producto = Invoke-RestMethod -Method Post -Uri 'http://localhost:8083/api/productos' @headers -Body (
    @{ nombre = 'Laptop'; precio = 2500; stock = 10; categoriaId = $categoria.id } | ConvertTo-Json
)
$cliente = Invoke-RestMethod -Method Post -Uri 'http://localhost:8081/api/clientes' @headers -Body (
    (@{ nombre = 'Carlos'; apellidos = 'Ramirez'; direccion = 'Av. de prueba 100'; estado = 'ACTIVO'; nro_dni = '01234567' }) | ConvertTo-Json
)
$pedido = Invoke-RestMethod -Method Post -Uri 'http://localhost:8084/api/pedidos' @headers -Body (
    @{ clienteId = $cliente.id; productoId = $producto.id; cantidad = 2 } | ConvertTo-Json
)

Invoke-RestMethod 'http://localhost:8083/api/productos' | ConvertTo-Json -Depth 5
$url = "http://localhost:8084/api/pedidos/$($pedido.id)"
Invoke-RestMethod $url | ConvertTo-Json -Depth 5
Invoke-RestMethod -Method Patch -Uri "$url/estado" @headers -Body '{"estado":"ATENDIDO"}'
Invoke-RestMethod -Method Patch -Uri "$url/estado" @headers -Body '{"estado":"ANULADO"}'
```

La consulta por ID devuelve `id`, `cantidad`, `estado` y los objetos `cliente` y `producto`. Los pedidos se crean como `PENDIENTE`. El estado se actualiza por `PATCH /api/pedidos/{id}/estado` y el registro sigue disponible tras anularlo.

Las API devuelven HTTP 400 si un pedido carece de identificadores o su cantidad es menor que uno, si el cliente omite campos obligatorios o proporciona un DNI que no tiene ocho dígitos, o si el producto carece de nombre o precio positivo. El DNI se envía como texto para conservar los ceros iniciales.

## Resultados de la verificación

Eureka mostró los cuatro servicios en estado `UP`. Las pruebas HTTP crearon una categoría, un producto, un cliente y un pedido con código 201. La consulta del pedido devolvió HTTP 200 con este resultado.

```json
{
  "id": 1,
  "cantidad": 2,
  "estado": "PENDIENTE",
  "cliente": { "id": 1, "nombre": "Carlos", "apellido": "Ramírez" },
  "producto": { "id": 1, "nombre": "Laptop", "precio": 2500.0 }
}
```

El listado de productos devolvió la categoría Tecnología. Las solicitudes con datos inválidos devolvieron HTTP 400. El pedido pasó a `ATENDIDO` y luego a `ANULADO`, siguió disponible en su consulta por ID y devolvió HTTP 405 ante un intento de borrado.

## Conclusiones

1. Eureka centraliza el descubrimiento. El panel permite comprobar el registro de categorías, productos, clientes y pedidos.
2. OpenFeign permite declarar una llamada REST con una interfaz Java y resolver su destino mediante el nombre registrado en Eureka.
3. Productos conserva `categoriaId` y obtiene la categoría por REST para completar la respuesta. El objeto con los datos de categoría es transitorio.
4. Pedidos mantiene los identificadores, la cantidad y el estado en su propia base. Al leer un pedido, completa los datos de cliente y producto con dos llamadas REST.
5. Validar los datos de entrada evita guardar pedidos con cantidades inválidas. Cambiar el estado a `ANULADO` conserva el pedido y permite consultarlo después.
