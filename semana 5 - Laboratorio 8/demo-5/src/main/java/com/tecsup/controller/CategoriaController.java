package com.tecsup.controller;

import com.tecsup.dto.CategoriaDTO;
import com.tecsup.model.Categoria;
import com.tecsup.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        Categoria categoria = service.obtener(id);
        return categoria == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(categoria);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Categoria>> buscar(@RequestParam String nombre) {
        return ResponseEntity.ok(service.buscarPorNombre(nombre));
    }

    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody CategoriaDTO dto) {
        if (service.existeNombre(dto.getNombre(), null)) {
            return conflicto("Ya existe una categoría con ese nombre");
        }
        return ResponseEntity.status(201).body(service.guardar(convertir(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
            @Valid @RequestBody CategoriaDTO dto) {
        Categoria existente = service.obtener(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        if (service.existeNombre(dto.getNombre(), id)) {
            return conflicto("Ya existe otra categoría con ese nombre");
        }
        existente.setNombre(dto.getNombre());
        existente.setDescripcion(dto.getDescripcion());
        return ResponseEntity.ok(service.guardar(existente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (service.obtener(id) == null) {
            return ResponseEntity.notFound().build();
        }
        service.eliminar(id);
        return ResponseEntity.ok("Categoría eliminada correctamente");
    }

    private Categoria convertir(CategoriaDTO dto) {
        return new Categoria(null, dto.getNombre(), dto.getDescripcion());
    }

    private ResponseEntity<Map<String, String>> conflicto(String mensaje) {
        Map<String, String> error = new HashMap<>();
        error.put("error", mensaje);
        return ResponseEntity.status(409).body(error);
    }
}
