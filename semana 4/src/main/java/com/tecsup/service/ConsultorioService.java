package com.tecsup.service;

import com.tecsup.model.Consultorio;
import com.tecsup.repository.ConsultorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConsultorioService {

    @Autowired
    private ConsultorioRepository repo;

    @Transactional(readOnly = true)
    public List<Consultorio> listar() {
        return repo.findAll();
    }

    @Transactional
    public Consultorio guardar(Consultorio c) {
        return repo.save(c);
    }

    @Transactional(readOnly = true)
    public Consultorio obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Transactional
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
