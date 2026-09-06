package com.tecsup.service;

import com.tecsup.model.Producto;
import com.tecsup.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository repo;

    @Transactional(readOnly = true)
    public List<Producto> listar() {
        return repo.findAll();
    }

    @Transactional
    public Producto guardar(Producto p) {
        return repo.save(p);
    }

    @Transactional(readOnly = true)
    public Producto obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Transactional
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
