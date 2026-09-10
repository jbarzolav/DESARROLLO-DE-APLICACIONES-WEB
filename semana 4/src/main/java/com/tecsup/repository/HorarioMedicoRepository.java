package com.tecsup.repository;

import com.tecsup.model.HorarioMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HorarioMedicoRepository extends JpaRepository<HorarioMedico, Long> {

    @Query("SELECT h FROM HorarioMedico h WHERE h.medico.id_medico = :idMedico")
    List<HorarioMedico> findByMedico(@Param("idMedico") Long idMedico);
}
