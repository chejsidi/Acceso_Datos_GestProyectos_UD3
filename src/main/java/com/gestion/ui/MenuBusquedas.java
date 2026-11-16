package com.gestion.ui;

import com.gestion.service.PiezaService;
import com.gestion.service.ProveedorService;
import com.gestion.service.ProyectoService;
import com.gestion.util.Consola;

import java.math.BigDecimal;

public class MenuBusquedas extends MenuBase {

    private final ProveedorService proveedorService;
    private final PiezaService piezaService;
    private final ProyectoService proyectoService;

    public MenuBusquedas(ProveedorService proveedorService, PiezaService piezaService, ProyectoService proyectoService) {
        super("BÚSQUEDAS AVANZADAS", "Volver");
        this.proveedorService = proveedorService;
        this.piezaService = piezaService;
        this.proyectoService = proyectoService;
    }

    @Override
    protected String[] opciones() {
        return new String[]{
                "Proveedores (nombre, apellidos, dirección, pieza suministrada)",
                "Piezas (nombre, rango de precio, proveedor, proyecto)",
                "Proyectos (nombre, ciudad, proveedor, pieza)"
        };
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        System.out.println("Rellena los filtros que quieras (Enter = sin filtro).");
        switch (opcion) {
            case 1 -> buscarProveedores();
            case 2 -> buscarPiezas();
            case 3 -> buscarProyectos();
        }
    }

    private void buscarProveedores() {
        String nombre = Consola.leerFiltroTexto("Nombre: ", 50);
        String apellidos = Consola.leerFiltroTexto("Apellidos: ", 100);
        String direccion = Consola.leerFiltroTexto("Dirección: ", 150);
        String pieza = Consola.leerFiltroCodigo("Código de pieza suministrada: ");
        mostrarLista(proveedorService.busquedaAvanzada(nombre, apellidos, direccion, pieza));
    }

    private void buscarPiezas() {
        String nombre = Consola.leerFiltroTexto("Nombre: ", 50);
        BigDecimal minimo = Consola.leerFiltroPrecio("Precio mínimo: ");
        BigDecimal maximo = Consola.leerFiltroPrecio("Precio máximo: ");
        String proveedor = Consola.leerFiltroCodigo("Código de proveedor: ");
        String proyecto = Consola.leerFiltroCodigo("Código de proyecto destino: ");
        mostrarLista(piezaService.busquedaAvanzada(nombre, minimo, maximo, proveedor, proyecto));
    }

    private void buscarProyectos() {
        String nombre = Consola.leerFiltroTexto("Nombre: ", 50);
        String ciudad = Consola.leerFiltroTexto("Ciudad: ", 50);
        String proveedor = Consola.leerFiltroCodigo("Código de proveedor participante: ");
        String pieza = Consola.leerFiltroCodigo("Código de pieza utilizada: ");
        mostrarLista(proyectoService.busquedaAvanzada(nombre, ciudad, proveedor, pieza));
    }
}