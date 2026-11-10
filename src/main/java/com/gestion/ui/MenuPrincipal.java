package com.gestion.ui;

public class MenuPrincipal extends MenuBase {

    private final MenuProveedores menuProveedores;
    private final MenuPiezas menuPiezas;
    private final MenuProyectos menuProyectos;

    public MenuPrincipal(MenuProveedores menuProveedores, MenuPiezas menuPiezas, MenuProyectos menuProyectos) {
        super("MENÚ PRINCIPAL", "Salir");
        this.menuProveedores = menuProveedores;
        this.menuPiezas = menuPiezas;
        this.menuProyectos = menuProyectos;
    }

    @Override
    protected String[] opciones() {
        return new String[]{
                "Gestión de proveedores",
                "Gestión de piezas",
                "Gestión de proyectos",
                "Ayuda"
        };
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> menuProveedores.mostrar();
            case 2 -> menuPiezas.mostrar();
            case 3 -> menuProyectos.mostrar();
            case 4 -> Ayuda.mostrar();
        }
    }

    @Override
    protected boolean pausarDespuesDeOpcion() {
        return false;
    }
}