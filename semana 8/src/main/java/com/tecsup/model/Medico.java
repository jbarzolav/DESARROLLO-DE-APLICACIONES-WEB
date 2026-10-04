package com.tecsup.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecsup.audit.AuditoriaListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;

@Entity
@Table(name = "medico")
// Pregunta 2: registra automaticamente las operaciones sobre el medico
@EntityListeners(AuditoriaListener.class)
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_medico;

    // RF-CIT-03: el medico se selecciona desde menu desplegable
    @NotBlank(message = "El nombre del medico es obligatorio")
    @Pattern(regexp = "^[a-zA-ZáéíóúñÁÉÍÓÚÑ\\s]+$", message = "El nombre solo debe contener letras")
    private String nombre;

    @NotBlank(message = "El apellido del medico es obligatorio")
    @Pattern(regexp = "^[a-zA-ZáéíóúñÁÉÍÓÚÑ\\s]+$", message = "El apellido solo debe contener letras")
    private String apellido;

    @NotBlank(message = "El DNI del medico es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 digitos")
    private String dni;

    @NotBlank(message = "El telefono es obligatorio")
    @Pattern(regexp = "\\d{9}", message = "El telefono debe tener exactamente 9 digitos")
    private String telefono;

    @NotNull(message = "La especialidad es obligatoria")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_especialidad")
    private Especialidad especialidad;

    // Pregunta 1: relacion inversa OneToMany (un medico tiene muchas citas)
    @JsonIgnore
    @OneToMany(mappedBy = "medico", fetch = FetchType.LAZY)
    private List<Cita> citas;

    // Pregunta 1: relacion inversa OneToMany (un medico tiene muchos horarios)
    @JsonIgnore
    @OneToMany(mappedBy = "medico", fetch = FetchType.LAZY)
    private List<HorarioMedico> horarios;

    public Medico() {
    }

    public Medico(Long id_medico, String nombre, String apellido, String dni, String telefono, Especialidad especialidad) {
        this.id_medico = id_medico;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
        this.especialidad = especialidad;
    }

    public Long getId_medico() {
        return id_medico;
    }

    public void setId_medico(Long id_medico) {
        this.id_medico = id_medico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }

    public List<HorarioMedico> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<HorarioMedico> horarios) {
        this.horarios = horarios;
    }
}
