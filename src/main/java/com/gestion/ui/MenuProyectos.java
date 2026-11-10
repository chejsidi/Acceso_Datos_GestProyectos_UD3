package com.gestion.ui;

import com.gestion.exception.ValidacionException;
import com.gestion.model.Proyecto;
import com.gestion.service.ProyectoService;
import com.gestion.util.Consola;

public class MenuProyectos extends MenuBase {

    private final ProyectoService service;

    public MenuProyectos(ProyectoService service) {
        super("GESTIÓN DE PROYECTOS", "Volver");
        this.service = service;
    }

    @Override
    protected String[] opciones() {
        return new String[]{
                "Alta de proyecto",
                "Baja de proyecto",
                "Modificar proyecto",
                "Buscar por código",
                "Buscar por nombre",
                "Buscar por ciudad",
                "Listar todos"
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
            case 6 -> mostrarLista(service.buscarPorCiudad(Consola.leerTexto("Ciudad (o parte): ", 50)));
            case 7 -> mostrarLista(service.listarTodos());
        }
    }

    private void alta() {
        String codigo = Consola.leerCodigo("Código: ");
        if (service.existe(codigo)) {
            throw new ValidacionException("Ya existe un proyecto con el código " + codigo + ".");
        }
        String nombre = Consola.leerTexto("Nombre: ", 50);
        String ciudad = Consola.leerTexto("Ciudad: ", 50);

        service.alta(new Proyecto(codigo, nombre, ciudad));
        exito("Proyecto dado de alta correctamente.");
    }

    private void baja() {
        Proyecto proyecto = service.buscarPorCodigo(Consola.leerCodigo("Código: "));
        System.out.println(proyecto);
        if (Consola.confirmar("¿Seguro que quieres eliminarlo?")) {
            service.baja(proyecto.getCodigo());
            exito("Proyecto eliminado.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private void modificar() {
        Proyecto proyecto = service.buscarPorCodigo(Consola.leerCodigo("Código: "));
        System.out.println("Pulsa Enter para mantener el valor actual.");
        proyecto.setNombre(Consola.leerTextoOpcional("Nombre", proyecto.getNombre(), 50));
        proyecto.setCiudad(Consola.leerTextoOpcional("Ciudad", proyecto.getCiudad(), 50));

        service.modificar(proyecto);
        exito("Proyecto modificado.");
    }
}