package com.gestion.exception;

// Excepción base de la aplicación: el menú captura este tipo y muestra el mensaje
public class GestionException extends RuntimeException {

    public GestionException(String mensaje) {
        super(mensaje);
    }

    public GestionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}