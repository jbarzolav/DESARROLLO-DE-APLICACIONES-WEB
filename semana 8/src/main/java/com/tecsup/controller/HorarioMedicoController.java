package com.tecsup.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

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

    // RF-CIT-06: configurar horario de atencion del medico por dia de la semana
    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody HorarioMedico horario) {
        // RF-CIT-06: la hora de inicio debe ser anterior a la hora fin
        if (horario.getHora_inicio().isAfter(horario.getHora_fin())
                || horario.getHora_inicio().equals(horario.getHora_fin())) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "La hora de inicio debe ser anterior a la hora fin (RF-CIT-06)");
            return ResponseEntity.badRequest().body(error);
        }

        Medico medico = medicoService.obtener(horario.getMedico().getId_medico());
        Especialidad especialidad = especialidadService.obtener(horario.getEspecialidad().getId_especialidad());
        if (medico == null || especialidad == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Medico o especialidad no encontrado");
            return ResponseEntity.badRequest().body(error);
        }
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

    // RF-CIT-06: horarios configurados de un medico
    @GetMapping("/medico/{idMedico}")
    public List<HorarioMedico> buscarPorMedico(@PathVariable Long idMedico) {
        return service.buscarPorMedico(idMedico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody HorarioMedico h) {
        HorarioMedico existente = service.obtener(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        // RF-CIT-06: la hora de inicio debe ser anterior a la hora fin
        if (h.getHora_inicio().isAfter(h.getHora_fin())
                || h.getHora_inicio().equals(h.getHora_fin())) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "La hora de inicio debe ser anterior a la hora fin (RF-CIT-06)");
            return ResponseEntity.badRequest().body(error);
        }
        Medico medico = medicoService.obtener(h.getMedico().getId_medico());
        Especialidad especialidad = especialidadService.obtener(h.getEspecialidad().getId_especialidad());
        if (medico == null || especialidad == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Medico o especialidad no encontrado");
            return ResponseEntity.badRequest().body(error);
        }
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
