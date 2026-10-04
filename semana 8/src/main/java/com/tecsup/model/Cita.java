package com.tecsup.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecsup.audit.AuditoriaListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "cita")
// Pregunta 2: registra automaticamente insert, update y delete de cada cita
@EntityListeners(AuditoriaListener.class)
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_cita;

    // RF-CIT-01: codigo automatico generado por el sistema
    private String codigo;

    // RF-CIT-03: paciente, especialidad, medico y consultorio se eligen en menus desplegables
    @NotNull(message = "El paciente es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_paciente")
    private Paciente paciente;

    @NotNull(message = "La especialidad es obligatoria")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_especialidad")
    private Especialidad especialidad;

    @NotNull(message = "El medico es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_medico")
    private Medico medico;

    @NotNull(message = "El consultorio es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_consultorio")
    private Consultorio consultorio;

    // RF-CIT-04: fecha y hora necesarias para verificar disponibilidad
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;

    private String tipo_atencion;

    @NotBlank(message = "El motivo de consulta es obligatorio")
    private String motivo;

    private String observaciones;

    // RF-CIT-01: estado automatico (PROGRAMADA) al registrar
    private String estado;

    public Cita() {
    }

    public Cita(Long id_cita, String codigo, Paciente paciente, Especialidad especialidad, Medico medico,
                Consultorio consultorio, LocalDate fecha, LocalTime hora, String tipo_atencion,
                String motivo, String observaciones, String estado) {
        this.id_cita = id_cita;
        this.codigo = codigo;
        this.paciente = paciente;
        this.especialidad = especialidad;
        this.medico = medico;
        this.consultorio = consultorio;
        this.fecha = fecha;
        this.hora = hora;
        this.tipo_atencion = tipo_atencion;
        this.motivo = motivo;
        this.observaciones = observaciones;
        this.estado = estado;
    }

    public Long getId_cita() {
        return id_cita;
    }

    public void setId_cita(Long id_cita) {
        this.id_cita = id_cita;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Consultorio getConsultorio() {
        return consultorio;
    }

    public void setConsultorio(Consultorio consultorio) {
        this.consultorio = consultorio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getTipo_atencion() {
        return tipo_atencion;
    }

    public void setTipo_atencion(String tipo_atencion) {
        this.tipo_atencion = tipo_atencion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
