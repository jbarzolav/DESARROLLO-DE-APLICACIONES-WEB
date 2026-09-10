package com.tecsup.repository;

import com.tecsup.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    @Query("SELECT c FROM Cita c WHERE c.medico.id_medico = :idMedico AND c.fecha = :fecha AND c.hora = :hora AND c.estado <> 'CANCELADA'")
    List<Cita> findByMedicoFechaHora(@Param("idMedico") Long idMedico, @Param("fecha") LocalDate fecha, @Param("hora") LocalTime hora);

    @Query("SELECT c FROM Cita c WHERE c.medico.id_medico = :idMedico AND c.fecha = :fecha ORDER BY c.hora")
    List<Cita> findByMedicoYFecha(@Param("idMedico") Long idMedico, @Param("fecha") LocalDate fecha);

    @Query("SELECT c FROM Cita c WHERE c.paciente.dni = :dni")
    List<Cita> findByPacienteDni(@Param("dni") String dni);

    @Query("SELECT c FROM Cita c WHERE c.codigo LIKE %:codigo%")
    List<Cita> findByCodigo(@Param("codigo") String codigo);

    @Query("SELECT c FROM Cita c WHERE c.medico.id_medico = :idMedico")
    List<Cita> findByMedico(@Param("idMedico") Long idMedico);

    @Query("SELECT c FROM Cita c WHERE c.especialidad.id_especialidad = :idEspecialidad")
    List<Cita> findByEspecialidad(@Param("idEspecialidad") Long idEspecialidad);

    @Query("SELECT c FROM Cita c WHERE c.estado = :estado")
    List<Cita> findByEstado(@Param("estado") String estado);

    @Query("SELECT c FROM Cita c WHERE c.paciente.id_paciente = :idPaciente ORDER BY c.fecha DESC")
    List<Cita> findHistorialByPaciente(@Param("idPaciente") Long idPaciente);
}
