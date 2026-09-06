package com.tecsup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tecsup.model.DetalleVenta;
import com.tecsup.model.Venta;
import com.tecsup.model.Producto;
import com.tecsup.service.DetalleVentaService;
import com.tecsup.service.VentaService;
import com.tecsup.service.ProductoService;

@RestController
@RequestMapping("/api/detalle-ventas")
public class DetalleVentaController {

    @Autowired
    private DetalleVentaService service;

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<DetalleVenta> listar() {
        return service.listar();
    }

    @PostMapping
    public ResponseEntity<DetalleVenta> guardar(@RequestBody DetalleVenta detalle) {
        Venta venta = ventaService.obtener(detalle.getVenta().getId_venta());
        Producto producto = productoService.obtener(detalle.getProducto().getId_producto());
        detalle.setVenta(venta);
        detalle.setProducto(producto);
        DetalleVenta guardado = service.guardar(detalle);
        DetalleVenta resultado = service.obtener(guardado.getId_detalle());
        return ResponseEntity.status(201).body(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleVenta> obtener(@PathVariable Long id) {
        DetalleVenta d = service.obtener(id);
        if (d == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(d);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleVenta> actualizar(@PathVariable Long id, @RequestBody DetalleVenta d) {
        DetalleVenta existente = service.obtener(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        Venta venta = ventaService.obtener(d.getVenta().getId_venta());
        Producto producto = productoService.obtener(d.getProducto().getId_producto());
        existente.setVenta(venta);
        existente.setProducto(producto);
        existente.setCantidad(d.getCantidad());
        existente.setPrecio(d.getPrecio());
        existente.setSubtotal(d.getSubtotal());
        service.guardar(existente);
        DetalleVenta resultado = service.obtener(id);
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        DetalleVenta d = service.obtener(id);
        if (d == null) {
            return ResponseEntity.notFound().build();
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
