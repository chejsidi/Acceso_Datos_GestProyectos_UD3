package com.gestion.ui;

import com.gestion.util.Consola;

public class Ayuda {

    private Ayuda() {
    }

    public static void mostrar() {
        System.out.println("""

                ================== AYUDA ==================

                MÓDULOS
                  Proveedores  -> Altas, bajas, modificaciones y búsquedas.
                  Piezas       -> Altas, bajas, modificaciones y búsquedas.
                  Proyectos    -> Altas, bajas, modificaciones y búsquedas.
                  Suministros  -> Registra qué proveedor suministra qué pieza
                                  a qué proyecto, en qué fecha y cantidad.

                NAVEGACIÓN
                  Escribe el número de la opción y pulsa Enter.
                  La opción 0 vuelve al menú anterior o sale de la aplicación.

                FORMATO DE LOS DATOS
                  Códigos:  letras y números, máximo 10 (ej: PR001).
                            Se guardan siempre en mayúsculas.
                  Precios:  hasta 2 decimales, con coma o punto (ej: 12,50).
                  Fechas:   dd/mm/aaaa (ej: 05/11/2026). Enter = hoy.

                MODIFICACIONES
                  Se muestra el valor actual entre corchetes.
                  Pulsa Enter sin escribir nada para mantenerlo.

                INTEGRIDAD DE LOS DATOS
                  No se puede eliminar un proveedor, pieza o proyecto
                  que tenga suministros asociados. Elimina antes
                  esos suministros.
                ===========================================""");
        Consola.pausa();
    }
}