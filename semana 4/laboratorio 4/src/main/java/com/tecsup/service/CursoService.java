package com.tecsup.service;

import com.tecsup.models.Curso;
import com.tecsup.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CursoService {

    @Autowired
    private CursoRepository repository;

    @Transactional
    public Curso guardar(Curso curso) {
        return repository.save(curso);
    }

    public List<Curso> listar() {
        return repository.findAll();
    }

    public Curso obtener(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
