package com.tecsup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<Medico> guardar(@RequestBody Medico medico) {
        Especialidad especialidad = especialidadService.obtener(medico.getEspecialidad().getId_especialidad());
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
    public ResponseEntity<Medico> actualizar(@PathVariable Long id, @RequestBody Medico m) {
        Medico existente = service.obtener(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        Especialidad especialidad = especialidadService.obtener(m.getEspecialidad().getId_especialidad());
        existente.setNombre(m.getNombre());
        existente.setApellido(m.getApellido());
        existente.setDni(m.getDni());
        existente.setTelefono(m.getTelefono());
        existente.setEspecialidad(especialidad);
        return ResponseEntity.ok(service.guardar(existente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Medico m = service.obtener(id);
        if (m == null) {
            return ResponseEntity.notFound().build();
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
