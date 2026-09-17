package com.tecsup.controllers;

import com.tecsup.models.Estudiante;
import com.tecsup.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteService service;

    @PostMapping
    public Estudiante crear(@RequestBody Estudiante estudiante) {
        return service.guardar(estudiante);
    }

    @GetMapping
    public List<Estudiante> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Estudiante obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    @PostMapping("/{estudianteId}/cursos/{cursoId}")
    public Estudiante inscribirCurso(@PathVariable Long estudianteId, @PathVariable Long cursoId) {
        return service.inscribirCurso(estudianteId, cursoId);
    }

    @DeleteMapping("/{estudianteId}/cursos/{cursoId}")
    public void desinscribirCurso(@PathVariable Long estudianteId, @PathVariable Long cursoId) {
        service.desinscribirCurso(estudianteId, cursoId);
    }
}
