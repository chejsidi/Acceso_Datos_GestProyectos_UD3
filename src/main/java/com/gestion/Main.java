package com.gestion;

import com.gestion.repository.ProveedorRepository;
import com.gestion.service.ProveedorService;
import com.gestion.ui.MenuProveedores;
import com.gestion.util.HibernateUtil;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    // Silencia los mensajes INFO de Hibernate (temporal, hasta el commit de logging).
    // Debe ser un atributo estático: si fuera variable local, Java podría descartarlo
    private static final Logger LOG_HIBERNATE = Logger.getLogger("org.hibernate");

    public static void main(String[] args) {
        LOG_HIBERNATE.setLevel(Level.WARNING);

        try {
            HibernateUtil.getSessionFactory();
        } catch (Exception e) {
            System.out.println("ERROR: no se pudo conectar con la base de datos. ¿Está arrancado Docker?");
            return;
        }

        try {
            // Inyección de dependencias manual: se crea cada pieza y se le pasa a la siguiente
            ProveedorService proveedorService = new ProveedorService(new ProveedorRepository());
            new MenuProveedores(proveedorService).mostrar();
        } finally {
            HibernateUtil.cerrar();
        }
    }
}