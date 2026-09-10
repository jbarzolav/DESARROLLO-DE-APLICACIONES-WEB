# Módulo de Citas Médicas

API REST desarrollada en Spring Boot para el manage de citas médicas.

## Requerimientos Funcionales

| RF | Descripción |
|----|-------------|
| RF-CIT-01 | Registrar citas médicas |
| RF-CIT-03 | Seleccionar paciente, especialidad, médico, consultorio, fecha y hora |
| RF-CIT-04 | Verificar disponibilidad del médico antes de registrar |
| RF-CIT-05 | Evitar citas duplicadas (mismo médico, fecha y hora) |
| RF-CIT-06 | Configurar horarios de atención de los médicos |
| RF-CIT-07 | Mostrar agenda médica por médico y fecha |
| RF-CIT-17 | Buscar citas por código, DNI, médico, especialidad y estado |
| RF-CIT-18 | Consultar historial de citas por paciente |

## Tecnologías

- Java 17
- Spring Boot 3.4.5
- Spring Data JPA
- MySQL (XAMPP)
- HTML, CSS, JavaScript

## Cómo Ejecutar

1. Iniciar XAMPP y crear la base de datos:
```sql
CREATE DATABASE citas_medicas;
```

2. Ejecutar la aplicación:
```bash
mvn spring-boot:run
```

3. Abrir el navegador en:
```
http://localhost:8080/index.html
```

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET/POST | /api/pacientes | Listar/Crear pacientes |
| GET/PUT/DELETE | /api/pacientes/{id} | Obtener/Actualizar/Eliminar paciente |
| GET/POST | /api/especialidades | Listar/Crear especialidades |
| GET/POST | /api/consultorios | Listar/Crear consultorios |
| GET/POST | /api/medicos | Listar/Crear médicos |
| GET/POST | /api/citas | Listar/Crear citas |
| GET/PUT/DELETE | /api/citas/{id} | Obtener/Actualizar/Eliminar cita |
| PATCH | /api/citas/{id}/estado | Cambiar estado de cita |
| GET | /api/citas/agenda/{idMedico}?fecha=YYYY-MM-DD | Ver agenda del médico |
| GET | /api/citas/buscar/codigo/{codigo} | Buscar por código |
| GET | /api/citas/buscar/dni/{dni} | Buscar por DNI del paciente |
| GET | /api/citas/historial/{idPaciente} | Historial de citas del paciente |
| GET/POST | /api/horarios | Listar/Crear horarios |
| GET | /api/horarios/medico/{idMedico} | Horarios por médico |

## Pantalla Principal

![Pantalla Principal](imagen%20principal.jpeg)
