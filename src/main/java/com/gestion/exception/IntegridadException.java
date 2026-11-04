package com.gestion.exception;

// Operación que rompería la lógica (ej: borrar un proveedor con suministros)
public class IntegridadException extends GestionException {

    public IntegridadException(String mensaje) {
        super(mensaje);
    }
}