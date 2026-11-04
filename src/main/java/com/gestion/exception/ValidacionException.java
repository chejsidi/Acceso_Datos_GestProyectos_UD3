package com.gestion.exception;

// Datos que no cumplen las reglas (formato, rangos, campos obligatorios...)
public class ValidacionException extends GestionException {

    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}