package com.tecsup.controller;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tecsup.model.Usuario;
import com.tecsup.service.UsuarioService;

// Pregunta 5: inicio y cierre de sesion, redirige al frontend segun el rol
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public static final String ROL_ADMIN = "ADMINISTRADOR";
    public static final String ROL_MEDICO = "MÉDICO";
    public static final String ROL_RECEPCIONISTA = "RECEPCIONISTA";

    @Autowired
    private UsuarioService usuarioService;

    // Pregunta 5: validar credenciales y crear la sesion
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Credenciales credenciales,
                                   HttpServletRequest request) {
        Usuario usuario = usuarioService.buscarPorUsername(credenciales.getUsername());
        if (usuario == null || !usuarioService.verificarPassword(credenciales.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(401).body(mensaje("Usuario o contrasena incorrectos"));
        }
        if (!usuario.isActivo()) {
            return ResponseEntity.status(403).body(mensaje("Su usuario esta desactivado, contacte al administrador"));
        }

        HttpSession sesion = request.getSession(true);
        sesion.setAttribute("usuario", usuario.getUsername());
        sesion.setAttribute("nombre", usuario.getNombre());
        sesion.setAttribute("rol", usuario.getRol().getNombre());

        return ResponseEntity.ok(datosSesion(usuario));
    }

    // Pregunta 5: la pagina lo usa para saber quien tiene la sesion abierta
    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest request) {
        Usuario usuario = usuarioActual(request);
        if (usuario == null) {
            return ResponseEntity.status(401).body(mensaje("Debe iniciar sesion"));
        }
        return ResponseEntity.ok(datosSesion(usuario));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession sesion = request.getSession(false);
        if (sesion != null) {
            sesion.invalidate();
        }
        return ResponseEntity.ok(mensaje("Sesion cerrada"));
    }

    private Usuario usuarioActual(HttpServletRequest request) {
        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("usuario") == null) {
            return null;
        }
        return usuarioService.buscarPorUsername((String) sesion.getAttribute("usuario"));
    }

    // redireccion segun el rol: cada rol entra a su vista principal
    private Map<String, Object> datosSesion(Usuario usuario) {
        Map<String, Object> datos = new HashMap<>();
        datos.put("username", usuario.getUsername());
        datos.put("nombre", usuario.getNombre());
        datos.put("rol", usuario.getRol().getNombre());
        datos.put("redirect", "index.html");
        return datos;
    }

    private Map<String, String> mensaje(String texto) {
        Map<String, String> error = new HashMap<>();
        error.put("error", texto);
        return error;
    }

    // cuerpo del login
    public static class Credenciales {
        @NotBlank(message = "El usuario es obligatorio")
        private String username;

        @NotBlank(message = "La contrasena es obligatoria")
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
