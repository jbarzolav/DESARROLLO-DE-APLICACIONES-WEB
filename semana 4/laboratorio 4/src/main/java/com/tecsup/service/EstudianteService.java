package com.tecsup.service;

import com.tecsup.models.Estudiante;
import com.tecsup.models.Curso;
import com.tecsup.repository.EstudianteRepository;
import com.tecsup.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Transactional
    public Estudiante guardar(Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }

    public List<Estudiante> listar() {
        return estudianteRepository.findAll();
    }

    public Estudiante obtener(Long id) {
        return estudianteRepository.findById(id).orElse(null);
    }

    @Transactional
    public void eliminar(Long id) {
        estudianteRepository.deleteById(id);
    }

    @Transactional
    public Estudiante inscribirCurso(Long estudianteId, Long cursoId) {
        Estudiante estudiante = estudianteRepository.findById(estudianteId).orElse(null);
        Curso curso = cursoRepository.findById(cursoId).orElse(null);
        if (estudiante != null && curso != null) {
            estudiante.getCursos().add(curso);
            curso.getEstudiantes().add(estudiante);
            estudianteRepository.save(estudiante);
        }
        return estudiante;
    }

    @Transactional
    public void desinscribirCurso(Long estudianteId, Long cursoId) {
        Estudiante estudiante = estudianteRepository.findById(estudianteId).orElse(null);
        Curso curso = cursoRepository.findById(cursoId).orElse(null);
        if (estudiante != null && curso != null) {
            estudiante.getCursos().remove(curso);
            curso.getEstudiantes().remove(estudiante);
            estudianteRepository.save(estudiante);
        }
    }
}
