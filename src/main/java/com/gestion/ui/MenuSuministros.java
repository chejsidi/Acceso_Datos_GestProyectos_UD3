package com.gestion.ui;

import com.gestion.model.Gestion;
import com.gestion.model.Pieza;
import com.gestion.model.Proveedor;
import com.gestion.model.Proyecto;
import com.gestion.service.GestionService;
import com.gestion.util.Consola;

import java.time.LocalDate;

public class MenuSuministros extends MenuBase {

    private static final int CANTIDAD_MAXIMA = 1_000_000;

    private final GestionService service;

    public MenuSuministros(GestionService service) {
        super("GESTIÓN DE SUMINISTROS", "Volver");
        this.service = service;
    }

    @Override
    protected String[] opciones() {
        return new String[]{
                "Alta de suministro",
                "Baja de suministro",
                "Modificar suministro",
                "Buscar por id",
                "Suministros de un proveedor",
                "Suministros de una pieza",
                "Suministros de un proyecto",
                "Listar todos"
        };
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> alta();
            case 2 -> baja();
            case 3 -> modificar();
            case 4 -> System.out.println(service.buscarPorId(leerId()));
            case 5 -> mostrarLista(service.buscarPorProveedor(Consola.leerCodigo("Código del proveedor: ")));
            case 6 -> mostrarLista(service.buscarPorPieza(Consola.leerCodigo("Código de la pieza: ")));
            case 7 -> mostrarLista(service.buscarPorProyecto(Consola.leerCodigo("Código del proyecto: ")));
            case 8 -> mostrarLista(service.listarTodas());
        }
    }

    // Cada código se valida al escribirlo: si no existe, se lanza la excepción y volvemos al menú
    private void alta() {
        Proveedor proveedor = service.obtenerProveedor(Consola.leerCodigo("Código del proveedor: "));
        System.out.println("  → " + proveedor.getNombre() + " " + proveedor.getApellidos());

        Pieza pieza = service.obtenerPieza(Consola.leerCodigo("Código de la pieza: "));
        System.out.println("  → " + pieza.getNombre());

        Proyecto proyecto = service.obtenerProyecto(Consola.leerCodigo("Código del proyecto: "));
        System.out.println("  → " + proyecto.getNombre());

        LocalDate fecha = Consola.leerFecha("Fecha");
        int cantidad = Consola.leerEntero("Cantidad: ", 1, CANTIDAD_MAXIMA);

        service.alta(new Gestion(fecha, cantidad, proveedor, pieza, proyecto));
        exito("Suministro registrado correctamente.");
    }

    private void baja() {
        Gestion gestion = service.buscarPorId(leerId());
        System.out.println(gestion);
        if (Consola.confirmar("¿Seguro que quieres eliminarlo?")) {
            service.baja(gestion.getId());
            exito("Suministro eliminado.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private void modificar() {
        Gestion gestion = service.buscarPorId(leerId());
        System.out.println(gestion);
        System.out.println("Pulsa Enter para mantener el valor actual.");

        gestion.setProveedor(service.obtenerProveedor(
                Consola.leerCodigoOpcional("Código del proveedor", gestion.getProveedor().getCodigo())));
        gestion.setPieza(service.obtenerPieza(
                Consola.leerCodigoOpcional("Código de la pieza", gestion.getPieza().getCodigo())));
        gestion.setProyecto(service.obtenerProyecto(
                Consola.leerCodigoOpcional("Código del proyecto", gestion.getProyecto().getCodigo())));
        gestion.setFecha(Consola.leerFechaOpcional("Fecha", gestion.getFecha()));
        gestion.setCantidad(Consola.leerEnteroOpcional("Cantidad", gestion.getCantidad(), 1, CANTIDAD_MAXIMA));

        service.modificar(gestion);
        exito("Suministro modificado.");
    }

    private Long leerId() {
        return (long) Consola.leerEntero("Id del suministro: ", 1, Integer.MAX_VALUE);
    }
}