
package com.gestion.ui;

import com.gestion.exception.ValidacionException;
import com.gestion.model.Proveedor;
import com.gestion.service.ProveedorService;
import com.gestion.util.Consola;

public class MenuProveedores extends MenuBase {

    private final ProveedorService service;

    public MenuProveedores(ProveedorService service) {
        super("GESTIÓN DE PROVEEDORES", "Volver");
        this.service = service;
    }

    @Override
    protected String[] opciones() {
        return new String[]{
                "Alta de proveedor",
                "Baja de proveedor",
                "Modificar proveedor",
                "Buscar por código",
                "Buscar por nombre",
                "Buscar por dirección",
                "Listar todos"
        };
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> alta();
            case 2 -> baja();
            case 3 -> modificar();
            case 4 -> System.out.println(
                    service.buscarPorCodigo(Consola.leerCodigo("Código: "))
            );
            case 5 -> mostrarLista(
                    service.buscarPorNombre(
                            Consola.leerTexto("Nombre (o parte): ", 50)
                    )
            );
            case 6 -> mostrarLista(
                    service.buscarPorDireccion(
                            Consola.leerTexto("Dirección (o parte): ", 150)
                    )
            );
            case 7 -> mostrarLista(service.listarTodos());
        }
    }

    private void alta() {
        String codigo = Consola.leerCodigo("Código: ");

        if (service.existe(codigo)) {
            throw new ValidacionException(
                    "Ya existe un proveedor con el código " + codigo + "."
            );
        }

        String nombre = Consola.leerTexto("Nombre: ", 50);
        String apellidos = Consola.leerTexto("Apellidos: ", 100);
        String direccion = Consola.leerTexto("Dirección: ", 150);

        service.alta(new Proveedor(codigo, nombre, apellidos, direccion));

        exito("Proveedor dado de alta correctamente.");
    }

    private void baja() {
        Proveedor proveedor = service.buscarPorCodigo(
                Consola.leerCodigo("Código: ")
        );

        System.out.println(proveedor);

        if (Consola.confirmar("¿Seguro que quieres eliminarlo?")) {
            service.baja(proveedor.getCodigo());
            exito("Proveedor eliminado.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private void modificar() {
        Proveedor proveedor = service.buscarPorCodigo(
                Consola.leerCodigo("Código: ")
        );

        System.out.println("Pulsa Enter para mantener el valor actual.");

        proveedor.setNombre(
                Consola.leerTextoOpcional("Nombre", proveedor.getNombre(), 50)
        );

        proveedor.setApellidos(
                Consola.leerTextoOpcional("Apellidos", proveedor.getApellidos(), 100)
        );

        proveedor.setDireccion(
                Consola.leerTextoOpcional("Dirección", proveedor.getDireccion(), 150)
        );

        service.modificar(proveedor);

        exito("Proveedor modificado.");
    }
}
