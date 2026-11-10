package com.gestion.ui;

import com.gestion.exception.ValidacionException;
import com.gestion.model.Pieza;
import com.gestion.service.PiezaService;
import com.gestion.util.Consola;

import java.math.BigDecimal;

public class MenuPiezas extends MenuBase {

    private final PiezaService service;

    public MenuPiezas(PiezaService service) {
        super("GESTIÓN DE PIEZAS", "Volver");
        this.service = service;
    }

    @Override
    protected String[] opciones() {
        return new String[]{
                "Alta de pieza",
                "Baja de pieza",
                "Modificar pieza",
                "Buscar por código",
                "Buscar por nombre",
                "Listar todas"
        };
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> alta();
            case 2 -> baja();
            case 3 -> modificar();
            case 4 -> System.out.println(service.buscarPorCodigo(Consola.leerCodigo("Código: ")));
            case 5 -> mostrarLista(service.buscarPorNombre(Consola.leerTexto("Nombre (o parte): ", 50)));
            case 6 -> mostrarLista(service.listarTodas());
        }
    }

    private void alta() {
        String codigo = Consola.leerCodigo("Código: ");
        if (service.existe(codigo)) {
            throw new ValidacionException("Ya existe una pieza con el código " + codigo + ".");
        }
        String nombre = Consola.leerTexto("Nombre: ", 50);
        BigDecimal precio = Consola.leerPrecio("Precio (€): ");
        String descripcion = Consola.leerTexto("Descripción: ", 255);

        service.alta(new Pieza(codigo, nombre, precio, descripcion));
        exito("Pieza dada de alta correctamente.");
    }

    private void baja() {
        Pieza pieza = service.buscarPorCodigo(Consola.leerCodigo("Código: "));
        System.out.println(pieza);
        if (Consola.confirmar("¿Seguro que quieres eliminarla?")) {
            service.baja(pieza.getCodigo());
            exito("Pieza eliminada.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private void modificar() {
        Pieza pieza = service.buscarPorCodigo(Consola.leerCodigo("Código: "));
        System.out.println("Pulsa Enter para mantener el valor actual.");
        pieza.setNombre(Consola.leerTextoOpcional("Nombre", pieza.getNombre(), 50));
        pieza.setPrecio(Consola.leerPrecioOpcional("Precio (€)", pieza.getPrecio()));
        pieza.setDescripcion(Consola.leerTextoOpcional("Descripción", pieza.getDescripcion(), 255));

        service.modificar(pieza);
        exito("Pieza modificada.");
    }
}