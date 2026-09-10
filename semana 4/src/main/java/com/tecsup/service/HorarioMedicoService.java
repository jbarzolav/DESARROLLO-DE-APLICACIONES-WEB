package com.tecsup.service;

import com.tecsup.model.HorarioMedico;
import com.tecsup.repository.HorarioMedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HorarioMedicoService {

    @Autowired
    private HorarioMedicoRepository repo;

    @Transactional(readOnly = true)
    public List<HorarioMedico> listar() {
        return repo.findAll();
    }

    @Transactional
    public HorarioMedico guardar(HorarioMedico h) {
        return repo.save(h);
    }

    @Transactional(readOnly = true)
    public HorarioMedico obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Transactional
    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<HorarioMedico> buscarPorMedico(Long idMedico) {
        return repo.findByMedico(idMedico);
    }
}
