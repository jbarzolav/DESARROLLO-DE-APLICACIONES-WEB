package com.tecsup.security;

import java.io.Writer;

import com.tecsup.audit.AuditoriaContext;
import com.tecsup.controller.AuthController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

// Pregunta 5: valida en el backend que el usuario tenga sesion
// y el rol permitido para cada modulo del sistema
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String ruta = request.getRequestURI();

        // el login siempre esta disponible
        if (ruta.equals("/api/auth/login")) {
            return true;
        }

        HttpSession sesion = request.getSession(false);
        String usuario = sesion == null ? null : (String) sesion.getAttribute("usuario");
        String rol = sesion == null ? null : (String) sesion.getAttribute("rol");

        // 401: no ha iniciado sesion
        if (usuario == null) {
            responder(response, 401, "Debe iniciar sesion");
            return false;
        }

        // usuario actual para la bitacora de auditoria (Pregunta 2)
        AuditoriaContext.set(usuario);

        // 403: la sesion existe pero su rol no tiene acceso al modulo
        if (!permitido(ruta, request.getMethod(), rol)) {
            AuditoriaContext.clear();
            responder(response, 403, "El rol " + rol + " no tiene permiso sobre " + ruta);
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        AuditoriaContext.clear();
    }

    // matriz de permisos por rol (tabla de la Pregunta 5)
    private boolean permitido(String ruta, String metodo, String rol) {
        boolean lectura = metodo.equalsIgnoreCase("GET") || metodo.equalsIgnoreCase("HEAD");
        String admin = AuthController.ROL_ADMIN;
        String medico = AuthController.ROL_MEDICO;
        String recep = AuthController.ROL_RECEPCIONISTA;

        // usuarios, roles y bitacora: solo ADMINISTRADOR
        if (ruta.startsWith("/api/usuarios") || ruta.startsWith("/api/roles")
                || ruta.startsWith("/api/auditoria")) {
            return admin.equals(rol);
        }

        // sesion propia
        if (ruta.startsWith("/api/auth")) {
            return true;
        }

        // pacientes: todos ven, solo ADMINISTRADOR y RECEPCIONISTA modifican
        if (ruta.startsWith("/api/pacientes")) {
            return admin.equals(rol) || recep.equals(rol) || (lectura && medico.equals(rol));
        }

        // citas: ADMINISTRADOR y RECEPCIONISTA gestionan, MEDICO solo consulta (historial y agenda)
        if (ruta.startsWith("/api/citas")) {
            return admin.equals(rol) || recep.equals(rol) || (lectura && medico.equals(rol));
        }

        // horarios: ADMINISTRADOR y MEDICO configuran, RECEPCIONISTA solo consulta
        if (ruta.startsWith("/api/horarios")) {
            return admin.equals(rol) || medico.equals(rol) || (lectura && recep.equals(rol));
        }

        // modulos del sistema (medicos, especialidades, consultorios): solo ADMINISTRADOR modifica
        if (ruta.startsWith("/api/medicos") || ruta.startsWith("/api/especialidades")
                || ruta.startsWith("/api/consultorios")) {
            return admin.equals(rol) || (lectura && (medico.equals(rol) || recep.equals(rol)));
        }

        // cualquier otro modulo: solo ADMINISTRADOR
        return admin.equals(rol);
    }

    private void responder(HttpServletResponse response, int codigo, String texto) throws Exception {
        response.setStatus(codigo);
        response.setContentType("application/json;charset=UTF-8");
        Writer writer = response.getWriter();
        writer.write("{\"error\":\"" + texto.replace("\"", "'") + "\"}");
        writer.flush();
    }
}
