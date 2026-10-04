package com.tecsup.audit;

import com.tecsup.model.Auditoria;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

// Pregunta 2: escucha los ciclos de vida de cada entidad y escribe en la bitacora
// REGISTRO (insert), MODIFICACION (update) y ELIMINACION (delete)
public class AuditoriaListener {

    // inyectado por AuditoriaRecorder cuando Spring termina de arrancar
    private static AuditoriaRecorder recorder;

    public static void setRecorder(AuditoriaRecorder r) {
        recorder = r;
    }

    @PostPersist
    public void registrar(Object entidad) {
        guardar("REGISTRO", entidad);
    }

    @PostUpdate
    public void modificar(Object entidad) {
        guardar("MODIFICACION", entidad);
    }

    @PostRemove
    public void eliminar(Object entidad) {
        guardar("ELIMINACION", entidad);
    }

    private void guardar(String operacion, Object entidad) {
        if (recorder == null) {
            return;
        }
        try {
            Auditoria auditoria = new Auditoria(
                    AuditoriaContext.get(),
                    LocalDateTime.now(),
                    operacion,
                    entidad.getClass().getSimpleName(),
                    idDe(entidad));
            recorder.registrar(auditoria);
        } catch (Exception e) {
            // un fallo de la bitacora nunca debe romper la operacion del negocio
            System.err.println("No se pudo registrar la auditoria: " + e.getMessage());
        }
    }

    // extrae el id del registro (getId_cita, getId_paciente, getId_usuario, ...)
    private String idDe(Object entidad) {
        try {
            for (Method metodo : entidad.getClass().getMethods()) {
                if (metodo.getName().startsWith("getId") && metodo.getParameterCount() == 0) {
                    return String.valueOf(metodo.invoke(entidad));
                }
            }
        } catch (Exception ignored) {
        }
        return "-";
    }
}
