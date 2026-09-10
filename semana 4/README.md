# Módulo de Citas Médicas

API REST desarrollada en Spring Boot para la gestión de citas médicas. Permite registrar pacientes, médicos, especialidades, consultorios, horarios y citas médicas con validación de disponibilidad.

## Requerimientos Funcionales

- **RF-CIT-01:** Registrar citas médicas con todos los datos (paciente, médico, consultorio, fecha, hora, motivo y estado)
- **RF-CIT-03:** Seleccionar datos de la cita desde menús desplegables (paciente, especialidad, médico, consultorio, fecha y hora)
- **RF-CIT-04:** Verificar disponibilidad del médico antes de registrar una cita
- **RF-CIT-05:** Evitar citas duplicadas (mismo médico, misma fecha y hora)
- **RF-CIT-06:** Configurar horarios de atención de los médicos por día de la semana
- **RF-CIT-07:** Mostrar agenda médica por médico y fecha específica
- **RF-CIT-17:** Buscar citas por código, DNI, médico, especialidad o estado
- **RF-CIT-18:** Consultar historial de citas de un paciente

## Cómo Ejecutar

1. Crear la base de datos o importar el archivo `citas_medicas.sql`

2. Ejecutar la aplicación:
```bash
mvn spring-boot:run
```

3. Abrir el navegador en:
```
http://localhost:8080/index.html
```

## Estructura del Proyecto

- `model/` - Entidades: Paciente, Medico, Especialidad, Consultorio, Cita, HorarioMedico
- `repository/` - Repositorios con consultas JPQL
- `service/` - Lógica de negocio con transacciones
- `controller/` - Endpoints REST
- `static/` - Frontend HTML
