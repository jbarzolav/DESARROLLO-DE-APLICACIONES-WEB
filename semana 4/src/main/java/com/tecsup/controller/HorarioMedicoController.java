package com.tecsup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tecsup.model.HorarioMedico;
import com.tecsup.model.Medico;
import com.tecsup.model.Especialidad;
import com.tecsup.service.HorarioMedicoService;
import com.tecsup.service.MedicoService;
import com.tecsup.service.EspecialidadService;

@RestController
@RequestMapping("/api/horarios")
public class HorarioMedicoController {

    @Autowired
    private HorarioMedicoService service;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private EspecialidadService especialidadService;

    @GetMapping
    public List<HorarioMedico> listar() {
        return service.listar();
    }

    @PostMapping
    public ResponseEntity<HorarioMedico> guardar(@RequestBody HorarioMedico horario) {
        Medico medico = medicoService.obtener(horario.getMedico().getId_medico());
        Especialidad especialidad = especialidadService.obtener(horario.getEspecialidad().getId_especialidad());
        horario.setMedico(medico);
        horario.setEspecialidad(especialidad);
        return ResponseEntity.status(201).body(service.guardar(horario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HorarioMedico> obtener(@PathVariable Long id) {
        HorarioMedico h = service.obtener(id);
        if (h == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(h);
    }

    @GetMapping("/medico/{idMedico}")
    public List<HorarioMedico> buscarPorMedico(@PathVariable Long idMedico) {
        return service.buscarPorMedico(idMedico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HorarioMedico> actualizar(@PathVariable Long id, @RequestBody HorarioMedico h) {
        HorarioMedico existente = service.obtener(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        Medico medico = medicoService.obtener(h.getMedico().getId_medico());
        Especialidad especialidad = especialidadService.obtener(h.getEspecialidad().getId_especialidad());
        existente.setMedico(medico);
        existente.setEspecialidad(especialidad);
        existente.setDia_semana(h.getDia_semana());
        existente.setHora_inicio(h.getHora_inicio());
        existente.setHora_fin(h.getHora_fin());
        return ResponseEntity.ok(service.guardar(existente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        HorarioMedico h = service.obtener(id);
        if (h == null) {
            return ResponseEntity.notFound().build();
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
