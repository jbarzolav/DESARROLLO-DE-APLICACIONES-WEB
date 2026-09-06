package com.tecsup.service;

import com.tecsup.model.Venta;
import com.tecsup.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VentaService {

    @Autowired
    private VentaRepository repo;

    @Transactional(readOnly = true)
    public List<Venta> listar() {
        return repo.findAll();
    }

    @Transactional
    public Venta guardar(Venta v) {
        Venta guardada = repo.save(v);
        return repo.findById(guardada.getId_venta()).orElse(guardada);
    }

    @Transactional(readOnly = true)
    public Venta obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Transactional
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
