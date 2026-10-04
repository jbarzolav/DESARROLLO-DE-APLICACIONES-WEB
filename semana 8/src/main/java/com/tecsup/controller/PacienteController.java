package com.tecsup.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tecsup.model.Cita;
import com.tecsup.model.Paciente;
import com.tecsup.service.PacienteService;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService service;

    @GetMapping
    public List<Paciente> listar() {
        return service.listar();
    }

    // RF-CIT-01: registrar paciente con DNI, nombre, apellido, telefono y email validos
    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody Paciente paciente) {
        // RF-CIT-17: el DNI identifica al paciente, no puede repetirse
        Paciente existente = service.buscarPorDni(paciente.getDni());
        if (existente != null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ya existe un paciente con DNI " + paciente.getDni());
            return ResponseEntity.badRequest().body(error);
        }
        return ResponseEntity.status(201).body(service.guardar(paciente));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> obtener(@PathVariable Long id) {
        Paciente p = service.obtener(id);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(p);
    }

    // RF-CIT-17: buscar paciente por DNI para buscar sus citas
    @GetMapping("/dni/{dni}")
    public ResponseEntity<Paciente> buscarPorDni(@PathVariable String dni) {
        Paciente p = service.buscarPorDni(dni);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(p);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Paciente p) {
        Paciente existente = service.obtener(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        // RF-CIT-17: el DNI no puede repetirse en otro paciente
        Paciente conMismoDni = service.buscarPorDni(p.getDni());
        if (conMismoDni != null && !conMismoDni.getId_paciente().equals(id)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ya existe otro paciente con DNI " + p.getDni());
            return ResponseEntity.badRequest().body(error);
        }
        existente.setNombre(p.getNombre());
        existente.setApellido(p.getApellido());
        existente.setDni(p.getDni());
        existente.setTelefono(p.getTelefono());
        existente.setEmail(p.getEmail());
        return ResponseEntity.ok(service.guardar(existente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Paciente p = service.obtener(id);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        // Pregunta 1: integridad referencial, no se borra un paciente con citas
        List<Cita> citas = service.citasDePaciente(id);
        if (citas != null && !citas.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error",
                    "No se puede eliminar: el paciente tiene " + citas.size() + " cita(s) asociada(s)"));
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Pregunta 1: citas del paciente a traves de la relacion OneToMany
    @GetMapping("/{id}/citas")
    public ResponseEntity<?> citasDelPaciente(@PathVariable Long id) {
        List<Cita> citas = service.citasDePaciente(id);
        if (citas == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(citas);
    }
}
