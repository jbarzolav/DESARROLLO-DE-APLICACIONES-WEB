package com.tecsup.aspect;

import com.tecsup.service.AuditoriaService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ErrorAspect {

    @Autowired
    private AuditoriaService auditoriaService;

    // =====================================================================
    //  PARTE 3: los errores ya no solo se muestran en consola,
    //           también se registran en la tabla auditoria_log
    // =====================================================================
    @AfterThrowing(
            pointcut = "execution(* com.tecsup.service.*.*(..))",
            throwing = "ex"
    )
    public void capturarError(JoinPoint joinPoint, Throwable ex) {

        // --- Información dinámica tomada del JoinPoint y de la excepción ---
        String metodo = joinPoint.getSignature().getName();                          // nombre del método
        String clase  = joinPoint.getSignature().getDeclaringType().getSimpleName(); // nombre de la clase
        Object[] args = joinPoint.getArgs();                                         // parámetros

        String mensaje = (ex.getMessage() != null) ? ex.getMessage() : "(sin mensaje)";

        // Usamos la causa raíz para un mensaje más limpio y corto
        Throwable causa = ex;
        while (causa.getCause() != null && causa.getCause() != causa) {
            causa = causa.getCause();
        }
        if (causa.getMessage() != null && !causa.getMessage().isBlank()) {
            mensaje = causa.getMessage();
        }
        if (mensaje.length() > 150) {
            mensaje = mensaje.substring(0, 150) + "...";
        }

        String detalle = "Error en " + clase + "." + metodo + "()"
                + " -> " + ex.getClass().getSimpleName() + ": " + mensaje;

        // 1) Consola: mensaje completo + parámetros capturados con JoinPoint
        System.out.println("ERROR AOP: " + detalle);
        System.out.println("ERROR AOP - parámetros: " + java.util.Arrays.toString(args));

        // 2) Base de datos: tabla auditoria_log
        if (detalle.length() > 255) {
            detalle = detalle.substring(0, 255);   // la columna es VARCHAR(255)
        }

        try {
            auditoriaService.registrar("ERROR", metodo, detalle);
        } catch (Exception e) {
            // Evita recursión infinita si falla el propio registro de auditoría
            System.out.println("ERROR AOP (no se pudo registrar en auditoría): " + e.getMessage());
        }
    }
}
