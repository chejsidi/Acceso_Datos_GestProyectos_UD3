package com.gestion.ui;

import com.gestion.exception.GestionException;
import com.gestion.service.AuthService;
import com.gestion.util.Consola;

import java.util.Arrays;

public class PantallaLogin {

    private static final int MAX_INTENTOS = 3;

    private final AuthService authService;

    public PantallaLogin(AuthService authService) {
        this.authService = authService;
    }

    // Devuelve true si el usuario ha iniciado sesión correctamente
    public boolean mostrar() {
        if (!authService.hayUsuarios()) {
            crearPrimerUsuario();
        }

        System.out.println("\n========== INICIO DE SESIÓN ==========");
        for (int intento = 1; intento <= MAX_INTENTOS; intento++) {
            String nombre = Consola.leerTexto("Usuario: ", 30);
            char[] password = Consola.leerPassword("Contraseña: ");
            boolean correcto;
            try {
                correcto = authService.login(nombre, password);
            } finally {
                Arrays.fill(password, ' ');
            }
            if (correcto) {
                System.out.println("  ✓ Bienvenido/a, " + nombre + ".");
                return true;
            }
            // Mensaje genérico: no revelamos si falla el usuario o la contraseña
            System.out.println("  ✗ Usuario o contraseña incorrectos. Intentos restantes: " + (MAX_INTENTOS - intento));
        }
        System.out.println("Demasiados intentos fallidos. La aplicación se cerrará.");
        return false;
    }

    // Primera ejecución: sin usuarios no se podría entrar, así que se crea el administrador
    private void crearPrimerUsuario() {
        System.out.println("\nNo hay usuarios registrados. Crea el usuario administrador.");
        while (true) {
            try {
                String nombre = Consola.leerTexto("Nombre de usuario: ", 30);
                char[] password = Consola.leerNuevaPassword();
                try {
                    authService.crearUsuario(nombre, password);
                } finally {
                    Arrays.fill(password, ' ');
                }
                System.out.println("  ✓ Usuario creado. Ya puedes iniciar sesión.");
                return;
            } catch (GestionException e) {
                System.out.println("  ✗ " + e.getMessage());
            }
        }
    }
}