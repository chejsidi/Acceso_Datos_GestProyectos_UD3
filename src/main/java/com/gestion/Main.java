package com.gestion;

import com.gestion.util.Consola;
import com.gestion.util.HibernateUtil;
import org.hibernate.Session;

public class Main {
    public static void main(String[] args) {
        String codigo = Consola.leerCodigo("Código: ");
        int cantidad = Consola.leerEntero("Cantidad (1-1000): ", 1, 1000);
        var precio = Consola.leerPrecio("Precio: ");
        var fecha = Consola.leerFecha("Fecha");
        System.out.println(codigo + " | " + cantidad + " | " + precio + " | " + fecha);
    }
}