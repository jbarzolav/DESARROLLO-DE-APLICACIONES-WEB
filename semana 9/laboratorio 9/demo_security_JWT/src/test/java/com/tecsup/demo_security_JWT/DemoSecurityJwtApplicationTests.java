package com.tecsup.demo_security_JWT;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import com.tecsup.entity.User;
import com.tecsup.repository.UserRepository;
import com.tecsup.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import tools.jackson.databind.ObjectMapper;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DemoSecurityJwtApplicationTests {

    @Autowired MockMvc mvc;
    @Autowired UserRepository users;
    @Autowired RoleRepository roles;
    @Autowired PasswordEncoder encoder;
    @Autowired ObjectMapper mapper;

	@Test
	void contextLoads() {
		assertTrue(roles.findByName("USER").isPresent());
		assertTrue(roles.findByName("ADMIN").isPresent());
		assertTrue(roles.findByName("MODERATOR").isPresent());
	}

    @Test
    void publicAndProtectedRoutes() throws Exception {
        mvc.perform(get("/api/test/all"))
                .andExpect(status().isOk()).andExpect(content().string("Public Content"));
        mvc.perform(get("/api/test/user")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/test/user").header("Authorization", "Bearer invalid"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void signupLoginAndUserPermissions() throws Exception {
        String signup = """
                {"username":"alumno","email":"alumno@example.com","password":"secreto123"}
                """;
        mvc.perform(post("/api/auth/signup").contentType(MediaType.APPLICATION_JSON).content(signup))
                .andExpect(status().isOk());
        User user = users.findByEmail("alumno@example.com").orElseThrow();
        assertNotEquals("secreto123", user.getPassword());
        assertTrue(encoder.matches("secreto123", user.getPassword()));
        mvc.perform(post("/api/auth/signup").contentType(MediaType.APPLICATION_JSON).content(signup))
                .andExpect(status().isBadRequest());
        String token = login("alumno@example.com", "alumno");
        mvc.perform(get("/api/test/user").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
        mvc.perform(get("/api/test/admin").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
        mvc.perform(get("/api/test/mod").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
        mvc.perform(post("/api/auth/signin").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"email":"alumno@example.com","password":"incorrecta"}
                        """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rejectsInvalidFieldsAndUnknownRoles() throws Exception {
        mvc.perform(post("/api/auth/signup").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"username":"","email":"invalido","password":"123"}
                        """))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/auth/signin").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
        for (String role : new String[]{"UNKNOWN", ""}) {
            mvc.perform(post("/api/auth/signup").contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {"username":"intruso","email":"intruso@example.com",
                             "password":"secreto123","roles":["%s"]}
                            """.formatted(role)))
                    .andExpect(status().isBadRequest());
        }
        assertFalse(users.existsByEmail("intruso@example.com"));
    }

    @Test
    void adminAndModeratorPermissions() throws Exception {
        for (String role : new String[]{"ADMIN", "MODERATOR"}) {
            String email = role.toLowerCase() + "@example.com";
            mvc.perform(post("/api/auth/signup").contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {"username":"%s","email":"%s","password":"secreto123","roles":["%s"]}
                            """.formatted(role, email, role)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("Usuario registrado correctamente"));
            String token = login(email, role);
            String allowed = role.equals("ADMIN") ? "/api/test/admin" : "/api/test/mod";
            String denied = role.equals("ADMIN") ? "/api/test/mod" : "/api/test/admin";
            mvc.perform(get(allowed).header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk());
            mvc.perform(get(denied).header("Authorization", "Bearer " + token))
                    .andExpect(status().isForbidden());
        }
    }

    private String login(String email, String username) throws Exception {
        String response = mvc.perform(post("/api/auth/signin").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"email":"%s","password":"secreto123"}
                        """.formatted(email)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andReturn().getResponse().getContentAsString();
        return mapper.readTree(response).get("accessToken").asText();
    }

}
