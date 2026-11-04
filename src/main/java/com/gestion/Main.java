package com.gestion;

import com.gestion.util.HibernateUtil;
import org.hibernate.Session;

public class Main {
    public static void main(String[] args) {
        System.out.println("Aplicación de Gestión de Proyectos - arrancando...");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.doWork(conexion ->
                    System.out.println("Conectado correctamente a: " + conexion.getMetaData().getURL()));
        } catch (Exception e) {
            System.out.println("ERROR: no se pudo conectar con la base de datos.");
            System.out.println("Comprueba que MySQL está arrancado y los datos de hibernate.cfg.xml.");
        } finally {
            HibernateUtil.cerrar();
        }
    }
}