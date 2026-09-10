package com.tecsup.model;

import jakarta.persistence.*;

@Entity
@Table(name = "consultorio")
public class Consultorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_consultorio;

    private String codigo;
    private String nombre;
    private String piso;

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
}
