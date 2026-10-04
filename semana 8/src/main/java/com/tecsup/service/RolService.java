package com.tecsup.service;

import com.tecsup.model.Rol;
import com.tecsup.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Pregunta 3: operaciones CRUD sobre los roles
@Service
public class RolService {

    @Autowired
    private RolRepository repo;

    @Transactional(readOnly = true)
    public List<Rol> listar() {
        return repo.findAll();
    }

    @Transactional
    public Rol guardar(Rol r) {
        return repo.save(r);
    }

    @Transactional(readOnly = true)
    public Rol obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Rol buscarPorNombre(String nombre) {
        return repo.findByNombre(nombre);
    }

    @Transactional
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
