package com.tecsup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tecsup.model.Auditoria;
import com.tecsup.repository.AuditoriaRepository;

// Pregunta 2: consulta de la bitacora de auditoria (solo ADMINISTRADOR)
@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {

    @Autowired
    private AuditoriaRepository repository;

    @GetMapping
    public List<Auditoria> listar() {
        return repository.listarRecientes();
    }
}
