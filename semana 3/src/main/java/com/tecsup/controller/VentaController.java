package com.tecsup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tecsup.model.Venta;
import com.tecsup.model.Cliente;
import com.tecsup.model.Empleado;
import com.tecsup.service.VentaService;
import com.tecsup.service.ClienteService;
import com.tecsup.service.EmpleadoService;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService service;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public List<Venta> listar() {
        return service.listar();
    }

    @PostMapping
    public ResponseEntity<Venta> guardar(@RequestBody Venta venta) {
        Cliente cliente = clienteService.obtener(venta.getCliente().getId_cliente());
        Empleado empleado = empleadoService.obtener(venta.getEmpleado().getId_empleado());
        venta.setCliente(cliente);
        venta.setEmpleado(empleado);
        Venta guardada = service.guardar(venta);
        Venta resultado = service.obtener(guardada.getId_venta());
        return ResponseEntity.status(201).body(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtener(@PathVariable Long id) {
        Venta v = service.obtener(id);
        if (v == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(v);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venta> actualizar(@PathVariable Long id, @RequestBody Venta v) {
        Venta existente = service.obtener(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        Cliente cliente = clienteService.obtener(v.getCliente().getId_cliente());
        Empleado empleado = empleadoService.obtener(v.getEmpleado().getId_empleado());
        existente.setFecha(v.getFecha());
        existente.setTotal(v.getTotal());
        existente.setCliente(cliente);
        existente.setEmpleado(empleado);
        service.guardar(existente);
        Venta resultado = service.obtener(id);
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Venta v = service.obtener(id);
        if (v == null) {
            return ResponseEntity.notFound().build();
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
