package com.tecsup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tecsup.audit.AuditoriaListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// Pregunta 3: usuario del sistema con rol asignado
@Entity
@Table(name = "usuario")
// Pregunta 2: registra automaticamente las operaciones sobre los usuarios
@EntityListeners(AuditoriaListener.class)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;

    // el usuario identifica el login, no puede repetirse
    @NotBlank(message = "El usuario es obligatorio")
    @Pattern(regexp = "^[a-zA-Z0-9._-]{4,20}$",
            message = "El usuario debe tener de 4 a 20 caracteres (letras, numeros, punto, guion)")
    @Column(unique = true, nullable = false)
    private String username;

    // se guarda encriptado con BCrypt y nunca se devuelve en el JSON
    // es obligatoria solo al registrar (se valida en el controlador)
    @Size(min = 6, message = "La contrasena debe tener al menos 6 caracteres")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[a-zA-ZáéíóúñÁÉÍÓÚÑ\\s]+$", message = "El nombre solo debe contener letras")
    private String nombre;

    @Email(message = "El email no tiene un formato valido")
    private String email;

    // Pregunta 3: activar / desactivar el usuario
    private boolean activo = true;

    // Pregunta 3: relacion Usuario -> Rol (muchos usuarios comparten un rol)
    @NotNull(message = "El rol es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    public Usuario() {
    }

    public Usuario(Long id_usuario, String username, String password, String nombre,
                   String email, boolean activo, Rol rol) {
        this.id_usuario = id_usuario;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.email = email;
        this.activo = activo;
        this.rol = rol;
    }

    public Long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
    }

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
        // una contrasena vacia se guarda como null para conservar la actual al editar
        this.password = (password == null || password.isBlank()) ? null : password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
