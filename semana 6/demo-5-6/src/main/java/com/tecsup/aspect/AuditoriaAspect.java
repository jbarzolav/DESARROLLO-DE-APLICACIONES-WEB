package com.tecsup.aspect;

import com.tecsup.model.Producto;
import com.tecsup.service.AuditoriaService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class AuditoriaAspect {

    @Autowired
    private AuditoriaService auditoriaService;

    // =====================================================================
    //  PARTE 2: captura de parámetros del método con JoinPoint
    // =====================================================================

    /** Devuelve los parámetros con los que se invocó el método */
    private String obtenerParametros(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return "(sin parámetros)";
        }
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            sb.append(arg).append(" | ");
        }
        return sb.toString();
    }

    /** Extrae el ID de un objeto Producto */
    private String obtenerId(Object objeto) {
        if (objeto instanceof Producto p && p.getId() != null) {
            return String.valueOf(p.getId());
        }
        return "desconocido";
    }

    // =====================================================================
    //  CREAR - parámetro dinámico: el ID del producto guardado
    // =====================================================================
    @AfterReturning(
            pointcut = "execution(* com.tecsup.service.ProductoService.guardar(..))",
            returning = "resultado"
    )
    public void auditarGuardar(JoinPoint joinPoint, Object resultado) {

        String id = obtenerId(resultado);
        String detalle = "Se registró producto con ID: " + id;

        System.out.println("  [AUDITORIA] método=" + joinPoint.getSignature().getName()
                + " parámetros=" + obtenerParametros(joinPoint));

        auditoriaService.registrar("CREAR", joinPoint.getSignature().getName(), detalle);
    }

    // =====================================================================
    //  ELIMINAR - parámetro dinámico: el ID recibido por el método
    // =====================================================================
    @AfterReturning(
            pointcut = "execution(* com.tecsup.service.ProductoService.eliminar(..))",
            returning = "resultado"
    )
    public void auditarEliminar(JoinPoint joinPoint, Object resultado) {

        Object[] args = joinPoint.getArgs();
        String id = (args.length > 0 && args[0] != null) ? String.valueOf(args[0]) : "desconocido";
        String detalle = "Se eliminó producto con ID: " + id;

        System.out.println("  [AUDITORIA] método=" + joinPoint.getSignature().getName()
                + " parámetros=" + obtenerParametros(joinPoint));

        auditoriaService.registrar("ELIMINAR", joinPoint.getSignature().getName(), detalle);
    }

    // =====================================================================
    //  ACTUALIZAR - parámetro dinámico: ID del producto actualizado
    //  Ejemplo esperado: "Se actualizó producto con ID: 5"
    // =====================================================================
    @AfterReturning(
            pointcut = "execution(* com.tecsup.service.ProductoService.actualizar(..))",
            returning = "resultado"
    )
    public void auditarActualizar(JoinPoint joinPoint, Object resultado) {

        String id = obtenerId(resultado);
        String detalle = "Se actualizó producto con ID: " + id;

        System.out.println("  [AUDITORIA] método=" + joinPoint.getSignature().getName()
                + " parámetros=" + obtenerParametros(joinPoint));

        auditoriaService.registrar("ACTUALIZAR", joinPoint.getSignature().getName(), detalle);
    }

    // =====================================================================
    //  LISTAR - información dinámica: cantidad de registros obtenidos
    // =====================================================================
    @AfterReturning(
            pointcut = "execution(* com.tecsup.service.ProductoService.listar(..))",
            returning = "resultado"
    )
    public void auditarListar(JoinPoint joinPoint, Object resultado) {

        int cantidad = (resultado instanceof List<?> lista) ? lista.size() : 0;
        String detalle = "Se listaron " + cantidad + " productos";

        System.out.println("  [AUDITORIA] método=" + joinPoint.getSignature().getName()
                + " parámetros=" + obtenerParametros(joinPoint));

        auditoriaService.registrar("LISTAR", joinPoint.getSignature().getName(), detalle);
    }
}
