package com.tecsup.controller;

import com.tecsup.dto.EmpleadoDTO;
import com.tecsup.model.Empleado;
import com.tecsup.service.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/empleados")
public class EmpleadoController {

    private final EmpleadoService service;

    public EmpleadoController(EmpleadoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Empleado> crear(@Valid @RequestBody EmpleadoDTO dto) {
        Empleado empleado = convertir(dto);
        Empleado creado = service.guardar(empleado);
        return ResponseEntity.created(URI.create("/empleados/" + creado.getId())).body(creado);
    }

    @GetMapping
    public ResponseEntity<List<Empleado>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> obtener(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizar(@PathVariable Long id,
                                                @Valid @RequestBody EmpleadoDTO dto) {
        return service.buscarPorId(id)
                .map(empleado -> {
                    actualizarDatos(empleado, dto);
                    return ResponseEntity.ok(service.guardar(empleado));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private Empleado convertir(EmpleadoDTO dto) {
        Empleado empleado = new Empleado();
        actualizarDatos(empleado, dto);
        return empleado;
    }

    private void actualizarDatos(Empleado empleado, EmpleadoDTO dto) {
        empleado.setNombres(dto.getNombres());
        empleado.setApellidos(dto.getApellidos());
        empleado.setEmail(dto.getEmail());
        empleado.setSueldo(dto.getSueldo());
    }
}
