package com.gestion;

import com.gestion.repository.*;
import com.gestion.service.*;
import com.gestion.ui.*;
import com.gestion.util.HibernateUtil;
import com.gestion.util.SesionActual;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Aplicación iniciada");

        System.out.println("""
        GESTIÓN DE PROYECTOS - UD3
        """);
        System.out.println("Conectando con la base de datos...");

        try {
            HibernateUtil.getSessionFactory();
        } catch (Exception e) {
            log.error("No se pudo conectar con la base de datos", e);
            System.out.println("ERROR: no se pudo conectar con la base de datos. ¿Está arrancado Docker?");
            return;
        }

        try {
            // Seguridad: sin login correcto no se accede a la aplicación
            RegistroAccesoRepository accesoRepository = new RegistroAccesoRepository();
            AuthService authService = new AuthService(new UsuarioRepository(), accesoRepository);
            if (!new PantallaLogin(authService).mostrar()) {
                return;
            }

            // Construcción de dependencias: repository → service → menú
            ProveedorService proveedorService = new ProveedorService(new ProveedorRepository());
            PiezaService piezaService = new PiezaService(new PiezaRepository());
            ProyectoService proyectoService = new ProyectoService(new ProyectoRepository());

            GestionRepository gestionRepository = new GestionRepository();
            GestionService gestionService = new GestionService(
                    gestionRepository, proveedorService, piezaService, proyectoService);
            InformeService informeService = new InformeService(
                    gestionRepository, proveedorService, piezaService, proyectoService);
            ExportacionService exportacionService = new ExportacionService();
            AuditoriaService auditoriaService = new AuditoriaService(
                    accesoRepository, new RegistroOperacionRepository());


            MenuPrincipal menuPrincipal = new MenuPrincipal(
                    new MenuProveedores(proveedorService),
                    new MenuPiezas(piezaService),
                    new MenuProyectos(proyectoService),
                    new MenuSuministros(gestionService),
                    new MenuBusquedas(proveedorService, piezaService, proyectoService, gestionService), // ← añadido gestionService
                    new MenuInformes(informeService, exportacionService,
                            proveedorService, piezaService, proyectoService, gestionService), // ← añadidos los 4 servicios extra
                    new MenuSeguridad(authService, auditoriaService)
            );

            menuPrincipal.mostrar();
            System.out.println("¡Hasta pronto!");
        } catch (Exception e) {
            log.error("Error inesperado en la aplicación", e);
            System.out.println("Error inesperado: " + e.getMessage());
        } finally {
            SesionActual.cerrar();
            HibernateUtil.cerrar();
            log.info("Aplicación cerrada");
        }
    }
}