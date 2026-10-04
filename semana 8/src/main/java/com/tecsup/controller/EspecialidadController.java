package com.tecsup.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tecsup.model.Especialidad;
import com.tecsup.model.Medico;
import com.tecsup.service.EspecialidadService;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    @Autowired
    private EspecialidadService service;

    @GetMapping
    public List<Especialidad> listar() {
        return service.listar();
    }

    // nombre obligatorio, unico y solo letras (validado en la entidad)
    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody Especialidad especialidad) {
        Especialidad existente = service.buscarPorNombre(especialidad.getNombre());
        if (existente != null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ya existe la especialidad " + especialidad.getNombre());
            return ResponseEntity.badRequest().body(error);
        }
        return ResponseEntity.status(201).body(service.guardar(especialidad));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Especialidad> obtener(@PathVariable Long id) {
        Especialidad e = service.obtener(id);
        if (e == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(e);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Especialidad e) {
        Especialidad existente = service.obtener(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        Especialidad conMismoNombre = service.buscarPorNombre(e.getNombre());
        if (conMismoNombre != null && !conMismoNombre.getId_especialidad().equals(id)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ya existe la especialidad " + e.getNombre());
            return ResponseEntity.badRequest().body(error);
        }
        existente.setNombre(e.getNombre());
        existente.setDescripcion(e.getDescripcion());
        return ResponseEntity.ok(service.guardar(existente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Especialidad e = service.obtener(id);
        if (e == null) {
            return ResponseEntity.notFound().build();
        }
        // Pregunta 1: integridad referencial, no se borra una especialidad con medicos
        List<Medico> medicos = service.medicosDeEspecialidad(id);
        if (medicos != null && !medicos.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error",
                    "No se puede eliminar: la especialidad tiene " + medicos.size() + " medico(s) asociado(s)"));
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Pregunta 1: medicos de la especialidad a traves de la relacion OneToMany
    @GetMapping("/{id}/medicos")
    public ResponseEntity<?> medicosDeEspecialidad(@PathVariable Long id) {
        List<Medico> medicos = service.medicosDeEspecialidad(id);
        if (medicos == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(medicos);
    }
}
