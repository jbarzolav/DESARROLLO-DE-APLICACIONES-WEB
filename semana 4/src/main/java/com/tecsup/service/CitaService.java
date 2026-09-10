package com.tecsup.service;

import com.tecsup.model.Cita;
import com.tecsup.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class CitaService {

    @Autowired
    private CitaRepository repo;

    @Transactional(readOnly = true)
    public List<Cita> listar() {
        return repo.findAll();
    }

    @Transactional
    public Cita guardar(Cita c) {
        return repo.save(c);
    }

    @Transactional(readOnly = true)
    public Cita obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Transactional
    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existeCitaDuplicada(Long idMedico, LocalDate fecha, LocalTime hora) {
        List<Cita> citas = repo.findByMedicoFechaHora(idMedico, fecha, hora);
        return !citas.isEmpty();
    }

    @Transactional(readOnly = true)
    public List<Cita> buscarPorMedicoYFecha(Long idMedico, LocalDate fecha) {
        return repo.findByMedicoYFecha(idMedico, fecha);
    }

    @Transactional(readOnly = true)
    public List<Cita> buscarPorDniPaciente(String dni) {
        return repo.findByPacienteDni(dni);
    }

    @Transactional(readOnly = true)
    public List<Cita> buscarPorCodigo(String codigo) {
        return repo.findByCodigo(codigo);
    }

    @Transactional(readOnly = true)
    public List<Cita> buscarPorMedico(Long idMedico) {
        return repo.findByMedico(idMedico);
    }

    @Transactional(readOnly = true)
    public List<Cita> buscarPorEspecialidad(Long idEspecialidad) {
        return repo.findByEspecialidad(idEspecialidad);
    }

    @Transactional(readOnly = true)
    public List<Cita> buscarPorEstado(String estado) {
        return repo.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public List<Cita> historialPorPaciente(Long idPaciente) {
        return repo.findHistorialByPaciente(idPaciente);
    }

    public String generarCodigo() {
        long total = repo.count();
        return String.format("CIT-%06d", total + 1);
    }
}
