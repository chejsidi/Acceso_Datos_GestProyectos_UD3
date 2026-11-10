package com.gestion;

import com.gestion.repository.ProveedorRepository;
import com.gestion.service.ProveedorService;
import com.gestion.ui.MenuProveedores;
import com.gestion.util.HibernateUtil;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.gestion.repository.PiezaRepository;
import com.gestion.repository.ProveedorRepository;
import com.gestion.repository.ProyectoRepository;
import com.gestion.service.PiezaService;
import com.gestion.service.ProveedorService;
import com.gestion.service.ProyectoService;
import com.gestion.ui.MenuPiezas;
import com.gestion.ui.MenuPrincipal;
import com.gestion.ui.MenuProveedores;
import com.gestion.ui.MenuProyectos;
import com.gestion.util.HibernateUtil;

import com.gestion.repository.GestionRepository;
import com.gestion.service.GestionService;
import com.gestion.ui.MenuSuministros;

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
            // Construcción de dependencias: repository → service → menú
            ProveedorService proveedorService = new ProveedorService(new ProveedorRepository());
            PiezaService piezaService = new PiezaService(new PiezaRepository());
            ProyectoService proyectoService = new ProyectoService(new ProyectoRepository());
            GestionService gestionService = new GestionService(
                    new GestionRepository(), proveedorService, piezaService, proyectoService);

            MenuPrincipal menuPrincipal = new MenuPrincipal(
                    new MenuProveedores(proveedorService),
                    new MenuPiezas(piezaService),
                    new MenuProyectos(proyectoService),
                    new MenuSuministros(gestionService)
            );
            new MenuProveedores(proveedorService).mostrar();
        } finally {
            HibernateUtil.cerrar();
        }

    }
}