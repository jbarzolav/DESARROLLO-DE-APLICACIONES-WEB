package com.tecsup.audit;

// Pregunta 2: guarda el usuario autenticado de la peticion actual
// para que el listener de auditoria pueda registrar quien hizo la operacion
public final class AuditoriaContext {

    private static final ThreadLocal<String> USUARIO = new ThreadLocal<>();

    private AuditoriaContext() {
    }

    public static void set(String usuario) {
        USUARIO.set(usuario);
    }

    // si nadie inicio sesion (arranque del sistema) se registra como "sistema"
    public static String get() {
        String usuario = USUARIO.get();
        return usuario == null ? "sistema" : usuario;
    }

    public static void clear() {
        USUARIO.remove();
    }
}
