package com.tecsup.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Pregunta 2: bitacora de auditoria generada automaticamente por el sistema
@Entity
@Table(name = "auditoria")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_auditoria;

    // usuario que realizo la operacion
    private String usuario;

    // fecha y hora exacta de la operacion
    private LocalDateTime fecha_hora;

    // REGISTRO, MODIFICACION o ELIMINACION
    private String operacion;

    // entidad afectada (Cita, Paciente, Medico, Usuario, ...)
    private String entidad;

    // identificador del registro afectado
    private String id_registro;

    public Auditoria() {
    }

    public Auditoria(String usuario, LocalDateTime fecha_hora, String operacion,
                     String entidad, String id_registro) {
        this.usuario = usuario;
        this.fecha_hora = fecha_hora;
        this.operacion = operacion;
        this.entidad = entidad;
        this.id_registro = id_registro;
    }

    public Long getId_auditoria() {
        return id_auditoria;
    }

    public void setId_auditoria(Long id_auditoria) {
        this.id_auditoria = id_auditoria;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(LocalDateTime fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getId_registro() {
        return id_registro;
    }

    public void setId_registro(String id_registro) {
        this.id_registro = id_registro;
    }
}
