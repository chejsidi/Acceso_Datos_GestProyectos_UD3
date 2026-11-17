package com.gestion.ui;

import com.gestion.service.ExportacionService;
import com.gestion.service.InformeService;
import com.gestion.service.PiezaService;
import com.gestion.service.ProveedorService;
import com.gestion.service.ProyectoService;
import com.gestion.service.GestionService;

import java.nio.file.Path;

public class MenuInformes extends MenuBase {

    private final InformeService informeService;
    private final ExportacionService exportacionService;
    private final ProveedorService proveedorService;
    private final PiezaService piezaService;
    private final ProyectoService proyectoService;
    private final GestionService gestionService;

    public MenuInformes(InformeService informeService,
                        ExportacionService exportacionService,
                        ProveedorService proveedorService,
                        PiezaService piezaService,
                        ProyectoService proyectoService,
                        GestionService gestionService) {
        super("INFORMES, ESTADÍSTICAS Y EXPORTACIÓN", "Volver");
        this.informeService = informeService;
        this.exportacionService = exportacionService;
        this.proveedorService = proveedorService;
        this.piezaService = piezaService;
        this.proyectoService = proyectoService;
        this.gestionService = gestionService;
    }

    @Override
    protected String[] opciones() {
        return new String[]{
                "Suministros por proveedor",
                "Suministros por pieza",
                "Suministros por proyecto",
                "Coste total por proyecto",
                "Piezas ordenadas por precio",
                "Proveedor más activo",
                "Proyecto más costoso",
                "Suministros ordenados por fecha",
                "Exportar proveedores a CSV",
                "Exportar piezas a CSV",
                "Exportar proyectos a CSV",
                "Exportar suministros a CSV"
        };
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> informeService.suministrosPorProveedor()
                    .forEach((k, v) -> System.out.printf("  %s → %d suministro(s)%n", k, v));
            case 2 -> informeService.suministrosPorPieza()
                    .forEach((k, v) -> System.out.printf("  %s → %d suministro(s)%n", k, v));
            case 3 -> informeService.suministrosPorProyecto()
                    .forEach((k, v) -> System.out.printf("  %s → %d suministro(s)%n", k, v));
            case 4 -> informeService.costeTotalPorProyecto()
                    .forEach((k, v) -> System.out.printf("  %s → %s €%n", k, v.toPlainString()));
            case 5 -> mostrarLista(informeService.piezasPorPrecioDesc());
            case 6 -> System.out.println(informeService.proveedorMasActivo());
            case 7 -> System.out.println(informeService.proyectoMasCostoso());
            case 8 -> mostrarLista(informeService.suministrosOrdenadosPorFecha());
            case 9  -> exportar(exportacionService.exportarProveedoresCsv(proveedorService.listarTodos()));
            case 10 -> exportar(exportacionService.exportarPiezasCsv(piezaService.listarTodas()));
            case 11 -> exportar(exportacionService.exportarProyectosCsv(proyectoService.listarTodos()));
            case 12 -> exportar(exportacionService.exportarSuministrosCsv(gestionService.listarTodas()));
        }
    }

    private void exportar(Path ruta) {
        exito("Archivo generado en: " + ruta.toAbsolutePath());
    }
}