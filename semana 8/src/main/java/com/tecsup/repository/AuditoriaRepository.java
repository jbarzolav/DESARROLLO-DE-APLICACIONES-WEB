package com.tecsup.repository;

import com.tecsup.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {

    // bitacora ordenada de la operacion mas reciente a la mas antigua
    @Query("select a from Auditoria a order by a.id_auditoria desc")
    List<Auditoria> listarRecientes();
}
