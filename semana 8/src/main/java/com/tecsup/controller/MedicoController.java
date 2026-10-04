package com.tecsup.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tecsup.model.Cita;
import com.tecsup.model.HorarioMedico;
import com.tecsup.model.Medico;
import com.tecsup.model.Especialidad;
import com.tecsup.service.MedicoService;
import com.tecsup.service.EspecialidadService;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    @Autowired
    private MedicoService service;

    @Autowired
    private EspecialidadService especialidadService;

    @GetMapping
    public List<Medico> listar() {
        return service.listar();
    }

    // RF-CIT-03: el medico se registra con especialidad asignada
    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody Medico medico) {
        // RF-CIT-03: la especialidad debe existir
        Especialidad especialidad = especialidadService.obtener(medico.getEspecialidad().getId_especialidad());
        if (especialidad == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "La especialidad indicada no existe");
            return ResponseEntity.badRequest().body(error);
        }
        medico.setEspecialidad(especialidad);
        return ResponseEntity.status(201).body(service.guardar(medico));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medico> obtener(@PathVariable Long id) {
        Medico m = service.obtener(id);
        if (m == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(m);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Medico m) {
        Medico existente = service.obtener(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        Especialidad especialidad = especialidadService.obtener(m.getEspecialidad().getId_especialidad());
        if (especialidad == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "La especialidad indicada no existe");
            return ResponseEntity.badRequest().body(error);
        }
        existente.setNombre(m.getNombre());
        existente.setApellido(m.getApellido());
        existente.setDni(m.getDni());
        existente.setTelefono(m.getTelefono());
        existente.setEspecialidad(especialidad);
        return ResponseEntity.ok(service.guardar(existente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Medico m = service.obtener(id);
        if (m == null) {
            return ResponseEntity.notFound().build();
        }
        // Pregunta 1: integridad referencial, no se borra un medico con citas
        List<Cita> citas = service.citasDeMedico(id);
        if (citas != null && !citas.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error",
                    "No se puede eliminar: el medico tiene " + citas.size() + " cita(s) asociada(s)"));
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Pregunta 1: citas del medico a traves de la relacion OneToMany
    @GetMapping("/{id}/citas")
    public ResponseEntity<?> citasDelMedico(@PathVariable Long id) {
        List<Cita> citas = service.citasDeMedico(id);
        if (citas == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(citas);
    }

    // Pregunta 1: horarios del medico a traves de la relacion OneToMany
    @GetMapping("/{id}/horarios")
    public ResponseEntity<?> horariosDelMedico(@PathVariable Long id) {
        List<HorarioMedico> horarios = service.horariosDeMedico(id);
        if (horarios == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(horarios);
    }
}
