package com.tecsup.controller;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tecsup.model.*;
import com.tecsup.service.*;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private EspecialidadService especialidadService;

    @Autowired
    private ConsultorioService consultorioService;

    @Autowired
    private HorarioMedicoService horarioMedicoService;

    @GetMapping
    public List<Cita> listar() {
        return citaService.listar();
    }

    // RF-CIT-01: registrar cita con todos los datos
    // RF-CIT-04: verificar disponibilidad del medico contra su horario
    // RF-CIT-05: evitar citas duplicadas (mismo medico, fecha y hora)
    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody Cita cita) {
        Paciente paciente = pacienteService.obtener(cita.getPaciente().getId_paciente());
        Medico medico = medicoService.obtener(cita.getMedico().getId_medico());
        Especialidad especialidad = especialidadService.obtener(cita.getEspecialidad().getId_especialidad());
        Consultorio consultorio = consultorioService.obtener(cita.getConsultorio().getId_consultorio());

        if (paciente == null || medico == null || especialidad == null || consultorio == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Entidad no encontrada");
            return ResponseEntity.badRequest().body(error);
        }

        // RF-CIT-05: evitar citas duplicadas
        boolean duplicada = citaService.existeCitaDuplicada(medico.getId_medico(), cita.getFecha(), cita.getHora());
        if (duplicada) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "El medico ya tiene una cita programada en esa fecha y hora (RF-CIT-05)");
            return ResponseEntity.badRequest().body(error);
        }

        // RF-CIT-04: verificar disponibilidad segun horario configurado (RF-CIT-06)
        String dia = cita.getFecha().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es"));
        boolean disponible = horarioMedicoService.medicoDisponible(
                medico.getId_medico(), dia, cita.getHora());
        if (!disponible) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "El medico no esta disponible el " + dia + " a las " + cita.getHora()
                    + ". Verifique el horario de atencion (RF-CIT-04)");
            return ResponseEntity.badRequest().body(error);
        }

        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setEspecialidad(especialidad);
        cita.setConsultorio(consultorio);
        cita.setCodigo(citaService.generarCodigo());
        cita.setEstado("PROGRAMADA");

        return ResponseEntity.status(201).body(citaService.guardar(cita));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> obtener(@PathVariable Long id) {
        Cita c = citaService.obtener(id);
        if (c == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(c);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Cita c) {
        Cita existente = citaService.obtener(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        Paciente paciente = pacienteService.obtener(c.getPaciente().getId_paciente());
        Medico medico = medicoService.obtener(c.getMedico().getId_medico());
        Especialidad especialidad = especialidadService.obtener(c.getEspecialidad().getId_especialidad());
        Consultorio consultorio = consultorioService.obtener(c.getConsultorio().getId_consultorio());

        if (paciente == null || medico == null || especialidad == null || consultorio == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Entidad no encontrada");
            return ResponseEntity.badRequest().body(error);
        }

        // RF-CIT-05: evitar duplicadas al actualizar (excluyendo la propia cita)
        boolean duplicada = citaService.existeCitaDuplicada(medico.getId_medico(), c.getFecha(), c.getHora());
        if (duplicada && !existente.getFecha().equals(c.getFecha())) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "El medico ya tiene una cita programada en esa fecha y hora (RF-CIT-05)");
            return ResponseEntity.badRequest().body(error);
        }

        // RF-CIT-04: verificar disponibilidad al actualizar
        String dia = c.getFecha().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es"));
        boolean disponible = horarioMedicoService.medicoDisponible(
                medico.getId_medico(), dia, c.getHora());
        if (!disponible) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "El medico no esta disponible el " + dia + " a las " + c.getHora()
                    + ". Verifique el horario de atencion (RF-CIT-04)");
            return ResponseEntity.badRequest().body(error);
        }

        existente.setPaciente(paciente);
        existente.setMedico(medico);
        existente.setEspecialidad(especialidad);
        existente.setConsultorio(consultorio);
        existente.setFecha(c.getFecha());
        existente.setHora(c.getHora());
        existente.setTipo_atencion(c.getTipo_atencion());
        existente.setMotivo(c.getMotivo());
        existente.setObservaciones(c.getObservaciones());
        existente.setEstado(c.getEstado());

        return ResponseEntity.ok(citaService.guardar(existente));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Cita> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Cita existente = citaService.obtener(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        existente.setEstado(body.get("estado"));
        return ResponseEntity.ok(citaService.guardar(existente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Cita c = citaService.obtener(id);
        if (c == null) {
            return ResponseEntity.notFound().build();
        }
        citaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // RF-CIT-07: mostrar agenda medica del dia
    @GetMapping("/agenda/{idMedico}")
    public List<Cita> agendaMedico(@PathVariable Long idMedico, @RequestParam String fecha) {
        return citaService.buscarPorMedicoYFecha(idMedico, LocalDate.parse(fecha));
    }

    // RF-CIT-17: buscar citas por DNI del paciente
    @GetMapping("/buscar/dni/{dni}")
    public List<Cita> buscarPorDni(@PathVariable String dni) {
        return citaService.buscarPorDniPaciente(dni);
    }

    // RF-CIT-17: buscar citas por codigo
    @GetMapping("/buscar/codigo/{codigo}")
    public List<Cita> buscarPorCodigo(@PathVariable String codigo) {
        return citaService.buscarPorCodigo(codigo);
    }

    // RF-CIT-17: buscar citas por medico
    @GetMapping("/buscar/medico/{idMedico}")
    public List<Cita> buscarPorMedico(@PathVariable Long idMedico) {
        return citaService.buscarPorMedico(idMedico);
    }

    // RF-CIT-17: buscar citas por especialidad
    @GetMapping("/buscar/especialidad/{idEspecialidad}")
    public List<Cita> buscarPorEspecialidad(@PathVariable Long idEspecialidad) {
        return citaService.buscarPorEspecialidad(idEspecialidad);
    }

    // RF-CIT-17: buscar citas por estado
    @GetMapping("/buscar/estado/{estado}")
    public List<Cita> buscarPorEstado(@PathVariable String estado) {
        return citaService.buscarPorEstado(estado);
    }

    // RF-CIT-18: historial de citas del paciente en orden cronologico descendente
    @GetMapping("/historial/{idPaciente}")
    public List<Cita> historialPaciente(@PathVariable Long idPaciente) {
        return citaService.historialPorPaciente(idPaciente);
    }
}
