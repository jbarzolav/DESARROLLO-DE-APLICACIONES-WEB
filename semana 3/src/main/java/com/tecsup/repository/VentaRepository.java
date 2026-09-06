package com.tecsup.repository;

import com.tecsup.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Override
    @EntityGraph(attributePaths = {"cliente", "empleado"})
    List<Venta> findAll();

    @Override
    @EntityGraph(attributePaths = {"cliente", "empleado"})
    Optional<Venta> findById(Long id);
}
