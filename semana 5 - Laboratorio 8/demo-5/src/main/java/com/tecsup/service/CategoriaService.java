package com.tecsup.service;

import com.tecsup.model.Categoria;
import com.tecsup.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository repo;

    public CategoriaService(CategoriaRepository repo) {
        this.repo = repo;
    }

    public List<Categoria> listar() {
        return repo.findAll();
    }

    public Categoria obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Categoria guardar(Categoria categoria) {
        return repo.save(categoria);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    public boolean existeNombre(String nombre, Long idExcluir) {
        return repo.findByNombreIgnoreCase(nombre).stream()
                .anyMatch(c -> idExcluir == null || !c.getId().equals(idExcluir));
    }

    public List<Categoria> buscarPorNombre(String nombre) {
        return repo.findByNombreContainingIgnoreCase(nombre);
    }
}
