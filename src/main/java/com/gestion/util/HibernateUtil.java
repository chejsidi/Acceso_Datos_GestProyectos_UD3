package com.gestion.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

//Gestiona la SessionFactory de Hibernate (patrón Singleton). Para luego sacar las Sessions para cada operacion

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    private HibernateUtil() {
    }

    /**
     * Devuelve la SessionFactory, creándola la primera vez que se pide.
     * Lee la configuración del archivo hibernate.cfg.xml.
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration()
                    .configure()            // carga hibernate.cfg.xml
                    .buildSessionFactory();
        }
        return sessionFactory;
    }

    /**
     * Cierra la SessionFactory y libera las conexiones.
     */
    public static void cerrar() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}