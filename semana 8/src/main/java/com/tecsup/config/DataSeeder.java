package com.tecsup.config;

import com.tecsup.model.Rol;
import com.tecsup.model.Usuario;
import com.tecsup.repository.RolRepository;
import com.tecsup.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

// Pregunta 3: crea los 3 roles basicos y un usuario por rol la primera vez que arranca
@Component
public class DataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (rolRepository.count() > 0 || usuarioRepository.count() > 0) {
            return; // la base de datos ya tiene datos, no se vuelve a sembrar
        }

        Rol admin = crearRol("ADMINISTRADOR", "Acceso total: usuarios, roles y modulos del sistema");
        Rol medico = crearRol("MÉDICO", "Pacientes e historias clinicas");
        Rol recepcionista = crearRol("RECEPCIONISTA", "Pacientes y citas");

        crearUsuario("admin", "admin123", "Administrador del Sistema", "admin@clinica.pe", admin);
        crearUsuario("medico", "medico123", "Doctor Carlos Garcia", "medico@clinica.pe", medico);
        crearUsuario("recepcionista", "recep123", "Recepcion de Citas", "recepcion@clinica.pe", recepcionista);

        log.info("Sembrador ejecutado: 3 roles y 3 usuarios creados");
    }

    private Rol crearRol(String nombre, String descripcion) {
        Rol r = new Rol();
        r.setNombre(nombre);
        r.setDescripcion(descripcion);
        r.setActivo(true);
        return rolRepository.save(r);
    }

    private void crearUsuario(String username, String password, String nombre, String email, Rol rol) {
        // se guarda el hash BCrypt, nunca la contrasena en plano
        Usuario u = new Usuario();
        u.setUsername(username);
        u.setPassword(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(password));
        u.setNombre(nombre);
        u.setEmail(email);
        u.setActivo(true);
        u.setRol(rol);
        usuarioRepository.save(u);
    }
}
