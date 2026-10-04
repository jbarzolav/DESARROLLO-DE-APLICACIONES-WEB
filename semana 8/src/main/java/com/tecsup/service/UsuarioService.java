package com.tecsup.service;

import com.tecsup.model.Usuario;
import com.tecsup.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Pregunta 3: operaciones CRUD sobre los usuarios del sistema
@Service
public class UsuarioService {

    // encripta y verifica las contrasenas con BCrypt
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private UsuarioRepository repo;

    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return repo.findAll();
    }

    @Transactional
    public Usuario guardar(Usuario u) {
        return repo.save(u);
    }

    @Transactional(readOnly = true)
    public Usuario obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorUsername(String username) {
        return repo.findByUsername(username);
    }

    // encripta la contrasena antes de guardarla en la base de datos
    @Transactional
    public Usuario crear(Usuario u) {
        u.setPassword(encoder.encode(u.getPassword()));
        return repo.save(u);
    }

    // encripta solo si llega una contrasena nueva, si viene vacia conserva la actual
    @Transactional
    public Usuario actualizar(Usuario existente, Usuario nuevo) {
        existente.setUsername(nuevo.getUsername());
        existente.setNombre(nuevo.getNombre());
        existente.setEmail(nuevo.getEmail());
        existente.setActivo(nuevo.isActivo());
        existente.setRol(nuevo.getRol());
        if (nuevo.getPassword() != null && !nuevo.getPassword().isBlank()) {
            existente.setPassword(encoder.encode(nuevo.getPassword()));
        }
        return repo.save(existente);
    }

    // compara la contrasena en plano contra el hash guardado
    public boolean verificarPassword(String enPlano, String hash) {
        return encoder.matches(enPlano, hash);
    }

    // Pregunta 3: activar o desactivar el usuario
    @Transactional
    public Usuario cambiarActivo(Long id, boolean activo) {
        Usuario u = repo.findById(id).orElse(null);
        if (u != null) {
            u.setActivo(activo);
            u = repo.save(u);
        }
        return u;
    }

    // Pregunta 1: cantidad de usuarios que usan un rol (integridad referencial)
    @Transactional(readOnly = true)
    public long contarPorRol(com.tecsup.model.Rol rol) {
        return repo.countByRol(rol);
    }

    @Transactional
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
