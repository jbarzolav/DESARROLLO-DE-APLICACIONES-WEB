package com.tecsup.controller;
import com.tecsup.model.*;
import com.tecsup.services.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import jakarta.validation.Valid;
import java.util.*;
@RestController @RequestMapping("/api/clientes") public class ClienteController{
    private final ClienteService service;
    public ClienteController(ClienteService s){
        service=s;
    }
    @GetMapping public List<Cliente> listar(){
        return service.listar();
    }
    @GetMapping("/{id}") public Cliente obtener(@PathVariable Long id){
        return service.obtener(id);
    }
    @PostMapping public ResponseEntity<Cliente> guardar(@Valid @RequestBody Cliente e){
        e.setId(null);
        return ResponseEntity.status(201).body(service.guardar(e));
    }
    @PutMapping("/{id}") public Cliente actualizar(@PathVariable Long id,@Valid @RequestBody Cliente e){
        return service.actualizar(id,e);
    }
    @DeleteMapping("/{id}") public ResponseEntity<?> eliminar(@PathVariable Long id){
        service.eliminar(id);
        return ResponseEntity.ok("Eliminado correctamente");
    }
}
