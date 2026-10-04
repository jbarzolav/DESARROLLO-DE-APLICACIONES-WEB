package com.tecsup.repository;

import com.tecsup.model.Consultorio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultorioRepository extends JpaRepository<Consultorio, Long> {
    Consultorio findByCodigo(String codigo);
}
