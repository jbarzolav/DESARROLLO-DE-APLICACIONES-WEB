package com.tecsup.controller;
import com.tecsup.model.*;
import com.tecsup.services.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import jakarta.validation.Valid;
import java.util.*;
@RestController @RequestMapping("/api/categorias") public class CategoriaController{
    private final CategoriaService service;
    public CategoriaController(CategoriaService s){
        service=s;
    }
    @GetMapping public List<Categoria> listar(){
        return service.listar();
    }
    @GetMapping("/{id}") public Categoria obtener(@PathVariable Long id){
        return service.obtener(id);
    }
    @PostMapping public ResponseEntity<Categoria> guardar(@Valid @RequestBody com.tecsup.dto.CategoriaDTO dto){
        Categoria e=new Categoria();
        e.setNombre(dto.getNombre());
        e.setEstado(dto.getEstado());
        return ResponseEntity.status(201).body(service.guardar(e));
    }
    @PutMapping("/{id}") public Categoria actualizar(@PathVariable Long id,@Valid @RequestBody com.tecsup.dto.CategoriaDTO dto){
        Categoria e=new Categoria();
        e.setNombre(dto.getNombre());
        e.setEstado(dto.getEstado());
        return service.actualizar(id,e);
    }
    @DeleteMapping("/{id}") public ResponseEntity<?> eliminar(@PathVariable Long id){
        service.eliminar(id);
        return ResponseEntity.ok("Eliminado correctamente");
    }
}
