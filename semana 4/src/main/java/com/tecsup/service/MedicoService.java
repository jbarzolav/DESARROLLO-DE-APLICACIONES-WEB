package com.tecsup.service;

import com.tecsup.model.Medico;
import com.tecsup.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository repo;

    @Transactional(readOnly = true)
    public List<Medico> listar() {
        return repo.findAll();
    }

    @Transactional
    public Medico guardar(Medico m) {
        return repo.save(m);
    }

    @Transactional(readOnly = true)
    public Medico obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Transactional
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
