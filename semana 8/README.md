# Sistema de Citas Médicas

Aplicación web para agendar citas médicas. Permite registrar pacientes, médicos,
especialidades, consultorios y horarios, crear citas sin duplicarlas, controlar quién
puede entrar al sistema según su rol y dejar registrada cada operación en una bitácora.

- **Backend:** Java 17, Spring Boot 3.4.5, Spring Data JPA (Hibernate), MySQL
- **Frontend:** páginas HTML con JavaScript (no necesita instalación extra)
- **Base de datos:** `citas_medicas`

---

## Cómo ejecutar el proyecto

1. Iniciar MySQL (XAMPP → Start en el módulo MySQL).
2. Importar la base de datos. Desde una terminal:
   ```bash
   mysql -u root < citas_medicas.sql
   ```
   También se puede importar desde phpMyAdmin → Importar.
3. Levantar la aplicación:
   ```bash
   mvn spring-boot:run
   ```
4. Abrir el navegador en:
   ```
   http://localhost:8080/login.html
   ```

> La primera vez que arranca, el sistema crea solo los 3 roles y 3 usuarios de ejemplo.
> No hay que insertarlos a mano.

---

## Usuarios de prueba

| Usuario | Contraseña | Rol |
|---|---|---|
| `admin` | `admin123` | ADMINISTRADOR |
| `medico` | `medico123` | MÉDICO |
| `recepcionista` | `recep123` | RECEPCIONISTA |

---

## Qué puede hacer cada rol

| Módulo | ADMINISTRADOR | MÉDICO | RECEPCIONISTA |
|---|---|---|---|
| Usuarios, roles y bitácora | ✔ total | ✘ | ✘ |
| Pacientes | ✔ total | solo ver | ✔ total |
| Citas | ✔ total | solo ver (historial) | ✔ total |
| Horarios | ✔ total | ✔ total | solo ver |
| Médicos, especialidades, consultorios | ✔ total | solo ver | solo ver |

El control se valida en dos lugares:

- **Backend:** sin sesión responde `401`, con sesión pero sin permiso responde `403`.
- **Frontend:** cada pestaña se muestra solo si el rol tiene permiso, y al iniciar
  sesión el usuario es redirigido a su vista principal.

---

## Requerimientos funcionales (Evaluación 01)

- **RF-CIT-01:** registrar citas con todos los datos (paciente, médico, especialidad,
  consultorio, fecha, hora, motivo y estado).
- **RF-CIT-03:** elegir los datos de la cita desde menús desplegables.
- **RF-CIT-04:** verificar que el médico esté disponible antes de registrar la cita.
- **RF-CIT-05:** evitar citas duplicadas (mismo médico, misma fecha y hora).
- **RF-CIT-06:** configurar horarios de atención de los médicos por día de la semana.
- **RF-CIT-07:** mostrar la agenda médica por médico y fecha.
- **RF-CIT-17:** buscar citas por código, DNI, médico, especialidad o estado.
- **RF-CIT-18:** consultar el historial de citas de un paciente.

---

## Nuevas funcionalidades (Evaluación 02)

### 1. Relaciones entre entidades (Hibernate/JPA)

Las entidades están relacionadas con anotaciones JPA y claves foráneas en la base de
datos:

- Paciente → muchas Citas (`@OneToMany` / `@ManyToOne`)
- Médico → muchas Citas y muchos Horarios
- Especialidad → muchos Médicos
- Consultorio → muchas Citas
- Usuario → un Rol

Cada entidad padre tiene un endpoint con sus hijos, por ejemplo:
`GET /api/pacientes/{id}/citas`. Si el padre no existe responde `404`; y no se puede
eliminar un registro que otro esté usando (responde `400`).

### 2. Auditoría (bitácora)

Cada alta, modificación y baja se guarda sola en la tabla `auditoria` con:

- usuario que hizo la operación
- fecha y hora
- operación (REGISTRO, MODIFICACION, ELIMINACION)
- entidad afectada
- identificador del registro

Se consulta desde la pestaña **Bitácora** del sistema o desde
`GET /api/auditoria` (solo ADMINISTRADOR).

### 3. Gestión de usuarios y roles

- CRUD completo de usuarios: registrar, editar, activar/desactivar, asignar rol.
- CRUD completo de roles: registrar, editar, activar/desactivar.
- Relación `Usuario → Rol` con JPA.
- Contraseñas guardadas con hash BCrypt (nunca se guarda ni se devuelve la contraseña).
- Reglas: username y nombre de rol no se pueden repetir; un usuario no puede
  desactivarse ni eliminarse a sí mismo; no se borra un rol que esté en uso.

### 4. Frontend de usuarios y roles

Formularios y listados de usuarios y roles en la pestaña **Usuarios** y **Roles** de
`index.html`, integrados con el backend (no hay que tocar la base de datos).

### 5. Control de acceso por rol

- `login.html` inicia sesión y redirige según el rol.
- `AuthInterceptor` valida sesión y permisos en cada petición de la API.
- Las contraseñas se envían con hash BCrypt.

---

## Validaciones de datos

| Dato | Regla |
|---|---|
| DNI | exactamente 8 dígitos, no se puede repetir |
| Teléfono | 9 dígitos |
| Email | formato válido |
| Nombres y apellidos | solo letras (con tildes y ñ) |
| Fecha de la cita | no puede ser anterior al día de hoy |
| Hora del horario | inicio debe ser menor que fin |
| Especialidad | nombre obligatorio, solo letras, no se puede repetir |
| Consultorio | código (formato `CONS-01`), nombre y piso obligatorios; código único |
| Usuario | username único, contraseña obligatoria al registrar, rol obligatorio |
| Rol | nombre único |

Cuando un dato es incorrecto, la API responde `400` indicando exactamente cuál campo
tiene el error, y el mensaje aparece en pantalla.

---

## Estructura del proyecto

```
src/main/java/com/tecsup/
├── model/        entidades (tablas de la base de datos)
├── repository/   acceso a datos (Spring Data JPA)
├── service/      lógica y transacciones
├── controller/   endpoints REST (API)
├── audit/        bitácora automática (AuditoriaListener, Recorder, Context)
├── config/       arranque (DataSeeder) y registro del interceptor (WebConfig)
├── security/     control de sesión y permisos (AuthInterceptor)
└── exception/    manejo global de errores de validación

src/main/resources/static/
├── login.html    inicio de sesión
├── index.html    sistema completo (pacientes, citas, agenda, usuarios, roles, bitácora)
├── citas.html    módulo de citas (solo ADMINISTRADOR y RECEPCIONISTA)
└── horarios.html módulo de horarios (solo ADMINISTRADOR y MÉDICO)
```

---

## Endpoints principales

| Método y ruta | Descripción |
|---|---|
| `POST /api/auth/login` | iniciar sesión |
| `GET /api/auth/me` | usuario de la sesión actual |
| `POST /api/auth/logout` | cerrar sesión |
| `GET/POST /api/pacientes`, `PUT/DELETE /api/pacientes/{id}` | CRUD pacientes |
| `GET /api/pacientes/{id}/citas` | citas de un paciente |
| `GET/POST /api/citas`, `PUT/DELETE /api/citas/{id}` | CRUD citas |
| `GET /api/citas/agenda?medico=&fecha=` | agenda del día |
| `GET/POST /api/usuarios`, `PUT /api/usuarios/{id}` | CRUD usuarios |
| `PUT /api/usuarios/{id}/activar?activo=` | activar/desactivar usuario |
| `GET/POST /api/roles`, `PUT /api/roles/{id}` | CRUD roles |
| `GET /api/auditoria` | bitácora (solo ADMINISTRADOR) |

---

## Credenciales y datos de conexión

- Base de datos: `citas_medicas` (usuario `root`, sin contraseña por defecto en XAMPP)
- Puerto: `8080`
- Si cambias la contraseña de MySQL, actualízala en
  `src/main/resources/application.properties`.
