package com.tecsup.service;

import com.tecsup.model.Especialidad;
import com.tecsup.model.Medico;
import com.tecsup.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EspecialidadService {

    @Autowired
    private EspecialidadRepository repo;

    @Transactional(readOnly = true)
    public List<Especialidad> listar() {
        return repo.findAll();
    }

    @Transactional
    public Especialidad guardar(Especialidad e) {
        return repo.save(e);
    }

    @Transactional(readOnly = true)
    public Especialidad obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    // valida que el nombre de la especialidad no se repita
    @Transactional(readOnly = true)
    public Especialidad buscarPorNombre(String nombre) {
        return repo.findByNombre(nombre);
    }

    // Pregunta 1: consulta la relacion OneToMany especialidad -> medicos
    @Transactional(readOnly = true)
    public List<Medico> medicosDeEspecialidad(Long id) {
        return repo.findById(id).map(Especialidad::getMedicos).orElse(null);
    }

    @Transactional
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
