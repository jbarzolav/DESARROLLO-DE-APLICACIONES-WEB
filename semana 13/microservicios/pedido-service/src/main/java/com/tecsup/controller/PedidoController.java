package com.tecsup.controller;
import com.tecsup.model.*;
import com.tecsup.services.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import jakarta.validation.Valid;
import java.util.*;
@RestController @RequestMapping("/api/pedidos") public class PedidoController{
    private final PedidoService service;
    public PedidoController(PedidoService s){
        service=s;
    }
    @GetMapping public List<Pedido> listar(){
        return service.listar();
    }
    @GetMapping("/{id}") public Pedido obtener(@PathVariable Long id){
        return service.obtener(id);
    }
    @PostMapping public ResponseEntity<Pedido> guardar(@Valid @RequestBody Pedido e){
        e.setIdPedido(null);
        return ResponseEntity.status(201).body(service.guardar(e));
    }
    @PutMapping("/{id}") public Pedido actualizar(@PathVariable Long id,@Valid @RequestBody Pedido e){
        return service.actualizar(id,e);
    }
    @DeleteMapping("/{id}") public ResponseEntity<?> eliminar(@PathVariable Long id){
        service.eliminar(id);
        return ResponseEntity.ok("Eliminado correctamente");
    }
}
