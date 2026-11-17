package com.gestion.ui;

import com.gestion.service.GestionService;
import com.gestion.service.PiezaService;
import com.gestion.service.ProveedorService;
import com.gestion.service.ProyectoService;
import com.gestion.util.Consola;

public class MenuBusquedas extends MenuBase {

    private final ProveedorService proveedorService;
    private final PiezaService piezaService;
    private final ProyectoService proyectoService;
    private final GestionService gestionService;

    public MenuBusquedas(ProveedorService proveedorService,
                         PiezaService piezaService,
                         ProyectoService proyectoService,
                         GestionService gestionService) {
        super("BÚSQUEDAS AVANZADAS", "Volver");
        this.proveedorService = proveedorService;
        this.piezaService = piezaService;
        this.proyectoService = proyectoService;
        this.gestionService = gestionService;
    }

    @Override
    protected String[] opciones() {
        return new String[]{
                "Suministros de un proveedor",
                "Suministros de una pieza",
                "Suministros de un proyecto",
                "Proveedores por nombre",
                "Proveedores por dirección",
                "Piezas por nombre",
                "Proyectos por nombre",
                "Proyectos por ciudad"
        };
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> mostrarLista(gestionService.buscarPorProveedor(
                    Consola.leerCodigo("Código de proveedor: ")));
            case 2 -> mostrarLista(gestionService.buscarPorPieza(
                    Consola.leerCodigo("Código de pieza: ")));
            case 3 -> mostrarLista(gestionService.buscarPorProyecto(
                    Consola.leerCodigo("Código de proyecto: ")));
            case 4 -> mostrarLista(proveedorService.buscarPorNombre(
                    Consola.leerTexto("Nombre (o parte): ", 50)));
            case 5 -> mostrarLista(proveedorService.buscarPorDireccion(
                    Consola.leerTexto("Dirección (o parte): ", 150)));
            case 6 -> mostrarLista(piezaService.buscarPorNombre(
                    Consola.leerTexto("Nombre (o parte): ", 50)));
            case 7 -> mostrarLista(proyectoService.buscarPorNombre(
                    Consola.leerTexto("Nombre (o parte): ", 50)));
            case 8 -> mostrarLista(proyectoService.buscarPorCiudad(
                    Consola.leerTexto("Ciudad (o parte): ", 100)));
        }
    }
}