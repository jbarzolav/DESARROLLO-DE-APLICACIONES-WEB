package com.tecsup.util;

import com.tecsup.model.Role;
import com.tecsup.model.User;
import com.tecsup.repository.RoleRepository;
import com.tecsup.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initData(UserRepository userRepo,
                               RoleRepository roleRepo,
                               PasswordEncoder encoder) {
        return args -> {

            // 🔹 Crear roles si no existen
            Role roleUser = roleRepo.findByName("ROLE_USER")
                    .orElseGet(() -> {
                        Role r = new Role();
                        r.setName("ROLE_USER");
                        return roleRepo.save(r);
                    });

            Role roleAdmin = roleRepo.findByName("ROLE_ADMIN")
                    .orElseGet(() -> {
                        Role r = new Role();
                        r.setName("ROLE_ADMIN");
                        return roleRepo.save(r);
                    });

            // 🔹 Nuevo rol: ROLE_MANAGER
            Role roleManager = roleRepo.findByName("ROLE_MANAGER")
                    .orElseGet(() -> {
                        Role r = new Role();
                        r.setName("ROLE_MANAGER");
                        return roleRepo.save(r);
                    });

            // 🔹 Usuario USER (contraseña cambiada y actualizada en la BD)
            User user = userRepo.findByUsername("user").orElseGet(() -> {
                User u = new User();
                u.setUsername("user");
                u.setRoles(Set.of(roleUser));
                return u;
            });
            user.setPassword(encoder.encode("user123")); // 🔐 nueva contraseña
            userRepo.save(user);

            // 🔹 Usuario ADMIN (contraseña cambiada y actualizada en la BD)
            User admin = userRepo.findByUsername("admin").orElseGet(() -> {
                User u = new User();
                u.setUsername("admin");
                u.setRoles(Set.of(roleAdmin));
                return u;
            });
            admin.setPassword(encoder.encode("admin123")); // 🔐 nueva contraseña
            userRepo.save(admin);

            // 🔹 Nuevo usuario MANAGER
            User manager = userRepo.findByUsername("manager").orElseGet(() -> {
                User u = new User();
                u.setUsername("manager");
                u.setRoles(Set.of(roleManager));
                return u;
            });
            manager.setPassword(encoder.encode("manager123")); // 🔐 contraseña
            userRepo.save(manager);

            System.out.println("✔ Datos iniciales cargados correctamente");
        };
    }
}
