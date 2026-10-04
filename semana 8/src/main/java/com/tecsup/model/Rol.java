package com.tecsup.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecsup.audit.AuditoriaListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

// Pregunta 3: rol del sistema (ADMINISTRADOR, MEDICO, RECEPCIONISTA)
@Entity
@Table(name = "rol")
// Pregunta 2: registra automaticamente las operaciones sobre los roles
@EntityListeners(AuditoriaListener.class)
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_rol;

    // nombre unico porque identifica el rol
    @NotBlank(message = "El nombre del rol es obligatorio")
    @Column(unique = true, nullable = false)
    private String nombre;

    private String descripcion;

    // Pregunta 3: activar / desactivar el rol
    private boolean activo = true;

    // Pregunta 1 y 3: relacion inversa OneToMany usuario -> rol
    @JsonIgnore
    @OneToMany(mappedBy = "rol", fetch = FetchType.LAZY)
    private List<Usuario> usuarios;

    public Rol() {
    }

    public Rol(Long id_rol, String nombre, String descripcion, boolean activo) {
        this.id_rol = id_rol;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public Long getId_rol() {
        return id_rol;
    }

    public void setId_rol(Long id_rol) {
        this.id_rol = id_rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
