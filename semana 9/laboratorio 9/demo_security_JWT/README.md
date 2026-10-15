# Laboratorio 9: autenticación JWT

## Ejecutar

Requiere Java 17 o superior y MySQL en `localhost:3306` con la base de datos
`security_db`. La conexión se configura en `src/main/resources/application.properties`:
usuario `root` y contraseña vacía; ajusta estos datos si tu instalación es distinta.

Si la base de datos aún no existe, créala en MySQL:

```sql
CREATE DATABASE IF NOT EXISTS security_db;
```

Desde esta carpeta:

```powershell
.\mvnw.cmd spring-boot:run
```

La clase principal es `com.tecsup.DemoSecurityJwtApplication`.
Hibernate actualiza las tablas y la aplicación inserta los roles faltantes
`USER`, `ADMIN` y `MODERATOR` al arrancar.

## Probar con Postman

1. `POST http://localhost:8080/api/auth/signup`, con Body → raw → JSON:

   ```json
   {"username":"alumno","email":"alumno@example.com","password":"secreto123"}
   ```

2. `POST http://localhost:8080/api/auth/signin`:

   ```json
   {"email":"alumno@example.com","password":"secreto123"}
   ```

3. Copia `accessToken` de la respuesta y úsalo en Authorization → Bearer Token.

| GET | Acceso |
| --- | --- |
| `/api/test/all` | Público |
| `/api/test/user` | USER o ADMIN |
| `/api/test/admin` | ADMIN |
| `/api/test/mod` | MODERATOR |

Como indica la práctica, el registro admite `"roles":["ADMIN"]`,
`"roles":["MODERATOR"]` o `"roles":["USER"]`. Si omites los roles o envías
una lista vacía, se asigna USER. Ejemplo del profesor:

```json
{"username":"admin","email":"admin@test.com","password":"123456","roles":["ADMIN"]}
```

También puedes asignar roles a una cuenta existente desde MySQL (ajusta el correo y el rol):

```sql
USE security_db;
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'alumno@example.com' AND r.name = 'ADMIN'
AND NOT EXISTS (
    SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id
);
```

## Pruebas automáticas

```powershell
.\mvnw.cmd test
```

Las pruebas utilizan H2 en memoria y no modifican `security_db`. Comprueban
registro, login, contraseñas cifradas, validaciones y permisos con JWT.
