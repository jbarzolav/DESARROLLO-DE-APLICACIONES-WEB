package com.tecsup.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tecsup.model.Rol;
import com.tecsup.model.Usuario;
import com.tecsup.service.RolService;
import com.tecsup.service.UsuarioService;

// Pregunta 3: CRUD de usuarios (registrar, listar, editar, activar/desactivar, asignar rol)
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private RolService rolService;

    @GetMapping
    public List<Usuario> listar() {
        return service.listar();
    }

    // Pregunta 3: registrar un usuario y asignarle un rol
    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody Usuario usuario) {
        // la contrasena es obligatoria solo al registrar el usuario
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "La contrasena es obligatoria");
            return ResponseEntity.badRequest().body(error);
        }
        if (service.buscarPorUsername(usuario.getUsername()) != null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ya existe el usuario " + usuario.getUsername());
            return ResponseEntity.badRequest().body(error);
        }
        Rol rol = usuario.getRol() == null ? null : rolService.obtener(usuario.getRol().getId_rol());
        if (rol == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "El rol indicado no existe");
            return ResponseEntity.badRequest().body(error);
        }
        if (!rol.isActivo()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "El rol " + rol.getNombre() + " esta desactivado");
            return ResponseEntity.badRequest().body(error);
        }
        usuario.setRol(rol);
        return ResponseEntity.status(201).body(service.crear(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtener(@PathVariable Long id) {
        Usuario u = service.obtener(id);
        if (u == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(u);
    }

    // Pregunta 3: editar datos del usuario y cambiarle el rol
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Usuario u) {
        Usuario existente = service.obtener(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        Usuario conMismoUsername = service.buscarPorUsername(u.getUsername());
        if (conMismoUsername != null && !conMismoUsername.getId_usuario().equals(id)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ya existe el usuario " + u.getUsername());
            return ResponseEntity.badRequest().body(error);
        }
        Rol rol = u.getRol() == null ? null : rolService.obtener(u.getRol().getId_rol());
        if (rol == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "El rol indicado no existe");
            return ResponseEntity.badRequest().body(error);
        }
        u.setRol(rol);
        return ResponseEntity.ok(service.actualizar(existente, u));
    }

    // Pregunta 3: activar o desactivar el usuario (quedar deshabilitado para ingresar)
    @PatchMapping("/{id}/activo")
    public ResponseEntity<?> cambiarActivo(@PathVariable Long id,
                                           @RequestBody Map<String, Boolean> body,
                                           HttpServletRequest request) {
        Usuario u = service.obtener(id);
        if (u == null) {
            return ResponseEntity.notFound().build();
        }
        boolean activo = Boolean.TRUE.equals(body.get("activo"));
        // el usuario de la sesion actual no puede desactivarse a si mismo
        Object sesion = request.getSession(false) == null ? null
                : request.getSession(false).getAttribute("usuario");
        if (!activo && u.getUsername().equals(sesion)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "No puede desactivar su propia cuenta");
            return ResponseEntity.badRequest().body(error);
        }
        return ResponseEntity.ok(service.cambiarActivo(id, activo));
    }

    // Pregunta 3: eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id, HttpServletRequest request) {
        Usuario u = service.obtener(id);
        if (u == null) {
            return ResponseEntity.notFound().build();
        }
        Object sesion = request.getSession(false) == null ? null
                : request.getSession(false).getAttribute("usuario");
        if (u.getUsername().equals(sesion)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "No puede eliminar la cuenta con la que inicio sesion");
            return ResponseEntity.badRequest().body(error);
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
