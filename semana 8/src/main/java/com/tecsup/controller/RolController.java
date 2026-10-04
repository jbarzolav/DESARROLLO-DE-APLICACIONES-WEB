package com.tecsup.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tecsup.model.Rol;
import com.tecsup.service.RolService;
import com.tecsup.service.UsuarioService;

// Pregunta 3: CRUD de roles (registrar, listar, editar, activar/desactivar)
@RestController
@RequestMapping("/api/roles")
public class RolController {

    @Autowired
    private RolService service;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Rol> listar() {
        return service.listar();
    }

    // Pregunta 3: registrar un rol nuevo
    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody Rol rol) {
        Rol existente = service.buscarPorNombre(rol.getNombre());
        if (existente != null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ya existe el rol " + rol.getNombre());
            return ResponseEntity.badRequest().body(error);
        }
        return ResponseEntity.status(201).body(service.guardar(rol));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rol> obtener(@PathVariable Long id) {
        Rol r = service.obtener(id);
        if (r == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(r);
    }

    // Pregunta 3: editar un rol existente
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Rol r) {
        Rol existente = service.obtener(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        Rol conMismoNombre = service.buscarPorNombre(r.getNombre());
        if (conMismoNombre != null && !conMismoNombre.getId_rol().equals(id)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ya existe el rol " + r.getNombre());
            return ResponseEntity.badRequest().body(error);
        }
        existente.setNombre(r.getNombre());
        existente.setDescripcion(r.getDescripcion());
        existente.setActivo(r.isActivo());
        return ResponseEntity.ok(service.guardar(existente));
    }

    // Pregunta 3: activar o desactivar un rol
    @PatchMapping("/{id}/activo")
    public ResponseEntity<?> cambiarActivo(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        Rol rol = service.obtener(id);
        if (rol == null) {
            return ResponseEntity.notFound().build();
        }
        rol.setActivo(Boolean.TRUE.equals(body.get("activo")));
        return ResponseEntity.ok(service.guardar(rol));
    }

    // Pregunta 1: integridad referencial, no se borra un rol que usan usuarios
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Rol rol = service.obtener(id);
        if (rol == null) {
            return ResponseEntity.notFound().build();
        }
        long usuarios = usuarioService.contarPorRol(rol);
        if (usuarios > 0) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "No se puede eliminar: " + usuarios + " usuario(s) tienen el rol " + rol.getNombre());
            return ResponseEntity.badRequest().body(error);
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
