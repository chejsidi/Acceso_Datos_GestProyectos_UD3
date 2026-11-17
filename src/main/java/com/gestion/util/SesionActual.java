package com.gestion.util;

// Guarda qué usuario ha iniciado sesión, para la auditoría
public class SesionActual {

    private static String usuario;

    private SesionActual() {
    }

    public static void iniciar(String nombre) {
        usuario = nombre;
    }

    public static void cerrar() {
        usuario = null;
    }

    // Sin sesión (por ejemplo, en los tests) las operaciones se atribuyen a "sistema"
    public static String nombreUsuario() {
        return usuario != null ? usuario : "sistema";
    }
}