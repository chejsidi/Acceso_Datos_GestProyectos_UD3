package com.gestion.ui;

import com.gestion.dto.Tabla;
import com.gestion.exception.GestionException;
import com.gestion.util.Consola;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

// Plantilla común de todos los menús: muestra opciones, lee la elegida y captura cualquier error
public abstract class MenuBase {

    private final String titulo;
    private final String textoSalida;
    private static final Logger log = LoggerFactory.getLogger(MenuBase.class);

    protected MenuBase(String titulo, String textoSalida) {
        this.titulo = titulo;
        this.textoSalida = textoSalida;
    }

    protected abstract String[] opciones();

    protected abstract void ejecutarOpcion(int opcion);

    // Los submenús pausan tras cada operación; el menú principal lo desactiva
    protected boolean pausarDespuesDeOpcion() {
        return true;
    }

    public void mostrar() {
        while (true) {
            System.out.println("\n========== " + titulo + " ==========");
            String[] opciones = opciones();
            for (int i = 0; i < opciones.length; i++) {
                System.out.println((i + 1) + ". " + opciones[i]);
            }
            System.out.println("0. " + textoSalida);

            int opcion = Consola.leerEntero("Elige una opción: ", 0, opciones.length);
            if (opcion == 0) {
                return;
            }

            // Cualquier error se muestra y se vuelve al menú: el programa nunca se cierra de golpe
            try {
                ejecutarOpcion(opcion);
            } catch (GestionException e) {
                log.warn("Operación rechazada en {}: {}", titulo, e.getMessage());
                System.out.println("  ✗ " + e.getMessage());
            } catch (RuntimeException e) {
                log.error("Error inesperado en {}", titulo, e);
                System.out.println("  ✗ Error inesperado: " + e.getMessage());
            }

            if (pausarDespuesDeOpcion()) {
                Consola.pausa();
            }
        }
    }

    protected <T> void mostrarLista(List<T> lista) {
        if (lista.isEmpty()) {
            System.out.println("No se han encontrado resultados.");
            return;
        }
        lista.forEach(elemento -> System.out.println("  " + elemento));
        System.out.println("Total: " + lista.size());
    }

    // Imprime una tabla con columnas alineadas al ancho del valor más largo
    protected void imprimirTabla(Tabla tabla) {
        if (tabla.filas().isEmpty()) {
            System.out.println("No se han encontrado resultados.");
            return;
        }
        int columnas = tabla.columnas().size();
        int[] anchos = new int[columnas];
        for (int i = 0; i < columnas; i++) {
            anchos[i] = tabla.columnas().get(i).length();
            for (List<String> fila : tabla.filas()) {
                anchos[i] = Math.max(anchos[i], fila.get(i).length());
            }
        }

        StringBuilder formato = new StringBuilder();
        int anchoTotal = 0;
        for (int ancho : anchos) {
            formato.append("%-").append(ancho).append("s  ");
            anchoTotal += ancho + 2;
        }

        System.out.println(String.format(formato.toString(), tabla.columnas().toArray()));
        System.out.println("-".repeat(anchoTotal));
        tabla.filas().forEach(fila -> System.out.println(String.format(formato.toString(), fila.toArray())));
        System.out.println("Total: " + tabla.filas().size());
    }

    protected void exito(String mensaje) {
        System.out.println("  ✓ " + mensaje);
    }
}