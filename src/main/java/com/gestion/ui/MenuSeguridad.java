package com.gestion.ui;

import com.gestion.service.AuditoriaService;
import com.gestion.service.AuthService;
import com.gestion.util.Consola;

import java.util.Arrays;

public class MenuSeguridad extends MenuBase {

    private final AuthService authService;
    private final AuditoriaService auditoriaService;

    public MenuSeguridad(AuthService authService, AuditoriaService auditoriaService) {
        super("SEGURIDAD Y AUDITORÍA", "Volver");
        this.authService = authService;
        this.auditoriaService = auditoriaService;
    }

    @Override
    protected String[] opciones() {
        return new String[]{
                "Registro de accesos (últimos 20)",
                "Registro de operaciones (últimas 30)",
                "Operaciones de un usuario",
                "Crear usuario",
                "Cambiar mi contraseña"
        };
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> mostrarLista(auditoriaService.ultimosAccesos(20));
            case 2 -> mostrarLista(auditoriaService.ultimasOperaciones(30));
            case 3 -> mostrarLista(auditoriaService.operacionesDeUsuario(Consola.leerTexto("Usuario: ", 30), 30));
            case 4 -> crearUsuario();
            case 5 -> cambiarPassword();
        }
    }

    private void crearUsuario() {
        String nombre = Consola.leerTexto("Nombre del nuevo usuario: ", 30);
        char[] password = Consola.leerNuevaPassword();
        try {
            authService.crearUsuario(nombre, password);
        } finally {
            Arrays.fill(password, ' ');
        }
        exito("Usuario creado.");
    }

    private void cambiarPassword() {
        char[] actual = Consola.leerPassword("Contraseña actual: ");
        char[] nueva = Consola.leerNuevaPassword();
        try {
            authService.cambiarPassword(actual, nueva);
        } finally {
            Arrays.fill(actual, ' ');
            Arrays.fill(nueva, ' ');
        }
        exito("Contraseña cambiada.");
    }
}