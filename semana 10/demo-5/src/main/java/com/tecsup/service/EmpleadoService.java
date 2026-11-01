package com.tecsup.service;

import com.tecsup.model.Empleado;
import com.tecsup.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService {

    private final EmpleadoRepository repository;

    public EmpleadoService(EmpleadoRepository repository) {
        this.repository = repository;
    }

    public List<Empleado> listar() {
        return repository.findAll();
    }

    public Empleado guardar(Empleado empleado) {
        return repository.save(empleado);
    }

    public Optional<Empleado> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
