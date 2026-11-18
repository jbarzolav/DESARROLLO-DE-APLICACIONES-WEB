package com.tecsup.services;

import com.tecsup.client.CategoriaClient;
import com.tecsup.dto.CategoriaDTO;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Producto;
import com.tecsup.repository.ProductoRepository;
import feign.FeignException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {
    private final ProductoRepository repo;
    private final CategoriaClient categorias;

    public ProductoService(ProductoRepository repo, CategoriaClient categorias) {
        this.repo = repo;
        this.categorias = categorias;
    }

    public List<Producto> listar() {
        var productos = repo.findAll();
        Map<Long, CategoriaDTO> categoriasConsultadas = new HashMap<>();
        for (Producto producto : productos) {
            if (producto.getCategoriaId() != null) {
                producto.setCategoria(categoriasConsultadas.computeIfAbsent(
                        producto.getCategoriaId(), this::buscarCategoria));
            }
        }
        return productos;
    }

    public Producto obtener(Long id) {
        return completarCategoria(buscarProducto(id));
    }

    public Producto guardar(Producto producto) {
        completarCategoria(producto);
        var guardado = repo.save(producto);
        guardado.setCategoria(producto.getCategoria());
        return guardado;
    }

    public Producto actualizar(Long id, Producto producto) {
        buscarProducto(id);
        producto.setId(id);
        return guardar(producto);
    }

    public void eliminar(Long id) {
        repo.delete(buscarProducto(id));
    }

    private Producto buscarProducto(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    private Producto completarCategoria(Producto producto) {
        if (producto.getCategoriaId() != null) {
            producto.setCategoria(buscarCategoria(producto.getCategoriaId()));
        }
        return producto;
    }

    private CategoriaDTO buscarCategoria(Long id) {
        try {
            return categorias.obtenerCategoria(id);
        } catch (FeignException.NotFound error) {
            throw new ResourceNotFoundException("Categoría no encontrada con id: " + id);
        }
    }
}
