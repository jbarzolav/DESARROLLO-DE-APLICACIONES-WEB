package com.tecsup.service;

import com.tecsup.model.Paciente;
import com.tecsup.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository repo;

    @Transactional(readOnly = true)
    public List<Paciente> listar() {
        return repo.findAll();
    }

    @Transactional
    public Paciente guardar(Paciente p) {
        return repo.save(p);
    }

    @Transactional(readOnly = true)
    public Paciente obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Paciente buscarPorDni(String dni) {
        return repo.findByDni(dni);
    }

    @Transactional
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
