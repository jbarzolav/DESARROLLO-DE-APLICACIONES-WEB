package com.tecsup.controller;
import com.tecsup.model.*;
import com.tecsup.services.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import jakarta.validation.Valid;
import java.util.*;
@RestController @RequestMapping("/api/productos") public class ProductoController{
    private final ProductoService service;
    public ProductoController(ProductoService s){
        service=s;
    }
    @GetMapping public List<Producto> listar(){
        return service.listar();
    }
    @GetMapping("/{id}") public Producto obtener(@PathVariable Long id){
        return service.obtener(id);
    }
    @PostMapping public ResponseEntity<Producto> guardar(@Valid @RequestBody Producto e){
        e.setId(null);
        return ResponseEntity.status(201).body(service.guardar(e));
    }
    @PutMapping("/{id}") public Producto actualizar(@PathVariable Long id,@Valid @RequestBody Producto e){
        return service.actualizar(id,e);
    }
    @DeleteMapping("/{id}") public ResponseEntity<?> eliminar(@PathVariable Long id){
        service.eliminar(id);
        return ResponseEntity.ok("Eliminado correctamente");
    }
}
