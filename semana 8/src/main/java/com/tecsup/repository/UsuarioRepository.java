package com.tecsup.repository;

import com.tecsup.model.Rol;
import com.tecsup.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsername(String username);

    // Pregunta 1: ayuda a mantener la integridad al verificar usuarios de un rol
    long countByRol(Rol rol);
}
