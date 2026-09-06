package com.tecsup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tecsup.model.Producto;
import com.tecsup.model.Categoria;
import com.tecsup.service.ProductoService;
import com.tecsup.service.CategoriaService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService service;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<Producto> listar() {
        return service.listar();
    }

    @PostMapping
    public ResponseEntity<Producto> guardar(@RequestBody Producto producto) {
        Categoria categoria = categoriaService.obtener(producto.getCategoria().getId_categoria());
        producto.setCategoria(categoria);
        Producto guardado = service.guardar(producto);
        Producto resultado = service.obtener(guardado.getId_producto());
        return ResponseEntity.status(201).body(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtener(@PathVariable Long id) {
        Producto p = service.obtener(id);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(p);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto p) {
        Producto existente = service.obtener(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        Categoria categoria = categoriaService.obtener(p.getCategoria().getId_categoria());
        existente.setNombre(p.getNombre());
        existente.setDescripcion(p.getDescripcion());
        existente.setPrecio(p.getPrecio());
        existente.setStock(p.getStock());
        existente.setCategoria(categoria);
        service.guardar(existente);
        Producto resultado = service.obtener(id);
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Producto p = service.obtener(id);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
