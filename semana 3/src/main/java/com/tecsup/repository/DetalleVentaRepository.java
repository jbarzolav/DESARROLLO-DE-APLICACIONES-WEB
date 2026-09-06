package com.tecsup.repository;

import com.tecsup.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    @Override
    @EntityGraph(attributePaths = {"venta", "producto"})
    List<DetalleVenta> findAll();

    @Override
    @EntityGraph(attributePaths = {"venta", "producto"})
    Optional<DetalleVenta> findById(Long id);
}
