package com.tecsup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<Consultorio> guardar(@RequestBody Consultorio consultorio) {
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
    public ResponseEntity<Consultorio> actualizar(@PathVariable Long id, @RequestBody Consultorio c) {
        Consultorio existente = service.obtener(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        existente.setCodigo(c.getCodigo());
        existente.setNombre(c.getNombre());
        existente.setPiso(c.getPiso());
        return ResponseEntity.ok(service.guardar(existente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Consultorio c = service.obtener(id);
        if (c == null) {
            return ResponseEntity.notFound().build();
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
