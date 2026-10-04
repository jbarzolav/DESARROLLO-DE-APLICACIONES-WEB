package com.tecsup.repository;

import com.tecsup.model.HorarioMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface HorarioMedicoRepository extends JpaRepository<HorarioMedico, Long> {

    @Query("SELECT h FROM HorarioMedico h WHERE h.medico.id_medico = :idMedico")
    List<HorarioMedico> findByMedico(@Param("idMedico") Long idMedico);

    // RF-CIT-04: verifica que el medico tenga horario configurado ese dia
    // y que la hora solicitada este dentro de hora_inicio y hora_fin
    @Query("SELECT h FROM HorarioMedico h " +
           "WHERE h.medico.id_medico = :idMedico " +
           "AND LOWER(h.dia_semana) = LOWER(:dia) " +
           "AND h.hora_inicio <= :hora " +
           "AND h.hora_fin >= :hora")
    List<HorarioMedico> findDisponibilidad(@Param("idMedico") Long idMedico,
                                           @Param("dia") String dia,
                                           @Param("hora") LocalTime hora);
}
