package com.tecsup.service;

import com.tecsup.model.DetalleVenta;
import com.tecsup.repository.DetalleVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DetalleVentaService {

    @Autowired
    private DetalleVentaRepository repo;

    @Transactional(readOnly = true)
    public List<DetalleVenta> listar() {
        return repo.findAll();
    }

    @Transactional
    public DetalleVenta guardar(DetalleVenta d) {
        return repo.save(d);
    }

    @Transactional(readOnly = true)
    public DetalleVenta obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Transactional
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
