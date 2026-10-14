package com.tecsup.repository;

import com.tecsup.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNombreIgnoreCase(String nombre);

    List<Categoria> findByNombreContainingIgnoreCase(String nombre);
}
