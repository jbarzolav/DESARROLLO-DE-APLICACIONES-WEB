package com.tecsup.model;

import com.tecsup.audit.AuditoriaListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

@Entity
@Table(name = "horario_medico")
// Pregunta 2: registra automaticamente las operaciones sobre el horario
@EntityListeners(AuditoriaListener.class)
public class HorarioMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_horario;

    // RF-CIT-06: horario de atencion configurado por medico
    @NotNull(message = "El medico es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_medico")
    private Medico medico;

    @NotNull(message = "La especialidad es obligatoria")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_especialidad")
    private Especialidad especialidad;

    // RF-CIT-06: dia de la semana (lunes, martes, ...)
    @NotBlank(message = "El dia de la semana es obligatorio")
    private String dia_semana;

    // RF-CIT-04: hora_inicio y hora_fin determinan la disponibilidad del medico
    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime hora_inicio;

    @NotNull(message = "La hora fin es obligatoria")
    private LocalTime hora_fin;

    public HorarioMedico() {
    }

    public HorarioMedico(Long id_horario, Medico medico, Especialidad especialidad, String dia_semana,
                         LocalTime hora_inicio, LocalTime hora_fin) {
        this.id_horario = id_horario;
        this.medico = medico;
        this.especialidad = especialidad;
        this.dia_semana = dia_semana;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
    }

    public Long getId_horario() {
        return id_horario;
    }

    public void setId_horario(Long id_horario) {
        this.id_horario = id_horario;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public String getDia_semana() {
        return dia_semana;
    }

    public void setDia_semana(String dia_semana) {
        this.dia_semana = dia_semana;
    }

    public LocalTime getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(LocalTime hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public LocalTime getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(LocalTime hora_fin) {
        this.hora_fin = hora_fin;
    }
}
