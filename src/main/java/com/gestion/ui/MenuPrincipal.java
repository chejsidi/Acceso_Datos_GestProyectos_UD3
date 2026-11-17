package com.gestion.ui;

public class MenuPrincipal extends MenuBase {

    private final MenuProveedores menuProveedores;
    private final MenuPiezas menuPiezas;
    private final MenuProyectos menuProyectos;
    private final MenuSuministros menuSuministros;
    private final MenuBusquedas menuBusquedas;
    private final MenuInformes menuInformes;
    private final MenuSeguridad menuSeguridad;

    public MenuPrincipal(MenuProveedores menuProveedores, MenuPiezas menuPiezas, MenuProyectos menuProyectos,
                         MenuSuministros menuSuministros, MenuBusquedas menuBusquedas, MenuInformes menuInformes,
                         MenuSeguridad menuSeguridad) {
        super("MENÚ PRINCIPAL", "Salir");
        this.menuProveedores = menuProveedores;
        this.menuPiezas = menuPiezas;
        this.menuProyectos = menuProyectos;
        this.menuSuministros = menuSuministros;
        this.menuBusquedas = menuBusquedas;
        this.menuInformes = menuInformes;
        this.menuSeguridad = menuSeguridad;
    }

    @Override
    protected String[] opciones() {
        return new String[]{
                "Gestión de proveedores",
                "Gestión de piezas",
                "Gestión de proyectos",
                "Gestión de suministros",
                "Búsquedas avanzadas",
                "Informes, estadísticas y exportación",
                "Seguridad y auditoría",
                "Ayuda"
        };
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> menuProveedores.mostrar();
            case 2 -> menuPiezas.mostrar();
            case 3 -> menuProyectos.mostrar();
            case 4 -> menuSuministros.mostrar();
            case 5 -> menuBusquedas.mostrar();
            case 6 -> menuInformes.mostrar();
            case 7 -> menuSeguridad.mostrar();
            case 8 -> Ayuda.mostrar();
        }
    }

    @Override
    protected boolean pausarDespuesDeOpcion() {
        return false;
    }
}