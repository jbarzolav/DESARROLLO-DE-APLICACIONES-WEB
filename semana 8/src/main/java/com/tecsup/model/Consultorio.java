package com.tecsup.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.List;

@Entity
@Table(name = "consultorio")
public class Consultorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_consultorio;

    // codigo obligatorio, unico y con formato de letras, numeros o guion (CONS-01)
    @NotBlank(message = "El codigo del consultorio es obligatorio")
    @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "El codigo solo debe contener letras, numeros o guion")
    @Column(unique = true)
    private String codigo;

    @NotBlank(message = "El nombre del consultorio es obligatorio")
    private String nombre;

    @NotBlank(message = "El piso del consultorio es obligatorio")
    private String piso;

    // Pregunta 1: relacion inversa OneToMany (un consultorio tiene muchas citas)
    @JsonIgnore
    @OneToMany(mappedBy = "consultorio", fetch = FetchType.LAZY)
    private List<Cita> citas;

    public Consultorio() {
    }

    public Consultorio(Long id_consultorio, String codigo, String nombre, String piso) {
        this.id_consultorio = id_consultorio;
        this.codigo = codigo;
        this.nombre = nombre;
        this.piso = piso;
    }

    public Long getId_consultorio() {
        return id_consultorio;
    }

    public void setId_consultorio(Long id_consultorio) {
        this.id_consultorio = id_consultorio;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }
}
