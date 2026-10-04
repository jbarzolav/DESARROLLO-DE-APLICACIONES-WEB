package com.tecsup.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tecsup.model.Cita;
import com.tecsup.model.Consultorio;
import com.tecsup.service.ConsultorioService;

@RestController
@RequestMapping("/api/consultorios")
public class ConsultorioController {

    @Autowired
    private ConsultorioService service;

    @GetMapping
    public List<Consultorio> listar() {
        return service.listar();
    }

    // codigo, nombre y piso obligatorios, codigo unico (validado en la entidad)
    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody Consultorio consultorio) {
        Consultorio existente = service.buscarPorCodigo(consultorio.getCodigo());
        if (existente != null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ya existe el consultorio con codigo " + consultorio.getCodigo());
            return ResponseEntity.badRequest().body(error);
        }
        return ResponseEntity.status(201).body(service.guardar(consultorio));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consultorio> obtener(@PathVariable Long id) {
        Consultorio c = service.obtener(id);
        if (c == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(c);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Consultorio c) {
        Consultorio existente = service.obtener(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        Consultorio conMismoCodigo = service.buscarPorCodigo(c.getCodigo());
        if (conMismoCodigo != null && !conMismoCodigo.getId_consultorio().equals(id)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ya existe el consultorio con codigo " + c.getCodigo());
            return ResponseEntity.badRequest().body(error);
        }
        existente.setCodigo(c.getCodigo());
        existente.setNombre(c.getNombre());
        existente.setPiso(c.getPiso());
        return ResponseEntity.ok(service.guardar(existente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Consultorio c = service.obtener(id);
        if (c == null) {
            return ResponseEntity.notFound().build();
        }
        // Pregunta 1: integridad referencial, no se borra un consultorio con citas
        List<Cita> citas = service.citasDeConsultorio(id);
        if (citas != null && !citas.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error",
                    "No se puede eliminar: el consultorio tiene " + citas.size() + " cita(s) asociada(s)"));
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Pregunta 1: citas del consultorio a traves de la relacion OneToMany
    @GetMapping("/{id}/citas")
    public ResponseEntity<?> citasDelConsultorio(@PathVariable Long id) {
        List<Cita> citas = service.citasDeConsultorio(id);
        if (citas == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(citas);
    }
}
