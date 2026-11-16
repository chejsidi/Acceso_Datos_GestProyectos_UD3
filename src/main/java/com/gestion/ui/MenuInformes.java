package com.gestion.ui;

import com.gestion.dto.AnalisisProyecto;
import com.gestion.dto.Estadisticas;
import com.gestion.dto.Ranking;
import com.gestion.dto.Tabla;
import com.gestion.model.Gestion;
import com.gestion.service.ExportacionService;
import com.gestion.service.InformeService;
import com.gestion.util.Consola;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MenuInformes extends MenuBase {

    private final InformeService informeService;
    private final ExportacionService exportacionService;

    public MenuInformes(InformeService informeService, ExportacionService exportacionService) {
        super("INFORMES Y ESTADÍSTICAS", "Volver");
        this.informeService = informeService;
        this.exportacionService = exportacionService;
    }

    @Override
    protected String[] opciones() {
        return new String[]{
                "Histórico de un proveedor",
                "Análisis de un proyecto",
                "Estadísticas globales",
                "Exportar todos los suministros"
        };
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> historico();
            case 2 -> analisis();
            case 3 -> estadisticas();
            case 4 -> exportar(tablaSuministros("suministros", informeService.todosLosSuministros()));
        }
    }

    private void historico() {
        String codigo = Consola.leerCodigo("Código del proveedor: ");
        LocalDate desde = Consola.leerFecha("Desde");
        LocalDate hasta = Consola.leerFecha("Hasta");

        Tabla tabla = tablaSuministros("historico_" + codigo, informeService.historicoProveedor(codigo, desde, hasta));
        imprimirTabla(tabla);
        ofrecerExportacion(tabla);
    }

    private void analisis() {
        AnalisisProyecto analisis = informeService.analizarProyecto(Consola.leerCodigo("Código del proyecto: "));

        System.out.println("\nProyecto: " + analisis.proyecto());
        System.out.println("Proveedores distintos: " + analisis.numeroProveedores());
        System.out.println("Coste total estimado: " + analisis.costeTotal().toPlainString() + " €\n");

        List<List<String>> filas = new ArrayList<>();
        analisis.piezas().forEach(c -> filas.add(List.of(
                c.codigo(), c.nombre(), String.valueOf(c.cantidad()), c.coste().toPlainString())));
        if (!filas.isEmpty()) {
            filas.add(List.of("TOTAL", "", "", analisis.costeTotal().toPlainString()));
        }

        Tabla tabla = new Tabla("analisis_" + analisis.proyecto().getCodigo(),
                List.of("Código", "Pieza", "Unidades", "Coste (€)"), filas);
        imprimirTabla(tabla);
        ofrecerExportacion(tabla);
    }

    private void estadisticas() {
        Estadisticas e = informeService.estadisticasGlobales();

        System.out.printf("%nProveedores: %d | Piezas: %d | Proyectos: %d%n", e.proveedores(), e.piezas(), e.proyectos());
        System.out.printf("Suministros: %d | Unidades suministradas: %d | Coste total: %s €%n%n",
                e.suministros(), e.unidades(), e.costeTotal().toPlainString());
        System.out.println("Rankings (top 5 por unidades suministradas):");

        List<List<String>> filas = new ArrayList<>();
        agregarRanking(filas, "Proveedor", e.topProveedores());
        agregarRanking(filas, "Pieza", e.topPiezas());
        agregarRanking(filas, "Proyecto", e.topProyectos());

        Tabla tabla = new Tabla("estadisticas",
                List.of("Categoría", "Código", "Nombre", "Suministros", "Unidades"), filas);
        imprimirTabla(tabla);
        ofrecerExportacion(tabla);
    }

    private void agregarRanking(List<List<String>> filas, String categoria, List<Ranking> ranking) {
        ranking.forEach(r -> filas.add(List.of(
                categoria, r.codigo(), r.nombre(), String.valueOf(r.suministros()), String.valueOf(r.unidades()))));
    }

    // Convierte una lista de suministros en Tabla, incluyendo el importe (cantidad x precio)
    private Tabla tablaSuministros(String titulo, List<Gestion> gestiones) {
        List<List<String>> filas = gestiones.stream().map(g -> List.of(
                String.valueOf(g.getId()),
                g.getFecha().format(Consola.FORMATO_FECHA),
                g.getProveedor().getCodigo() + " - " + g.getProveedor().getNombre(),
                g.getPieza().getCodigo() + " - " + g.getPieza().getNombre(),
                g.getProyecto().getCodigo() + " - " + g.getProyecto().getNombre(),
                String.valueOf(g.getCantidad()),
                g.getPieza().getPrecio().multiply(BigDecimal.valueOf(g.getCantidad())).toPlainString()
        )).toList();
        return new Tabla(titulo,
                List.of("Id", "Fecha", "Proveedor", "Pieza", "Proyecto", "Cantidad", "Importe (€)"), filas);
    }

    private void ofrecerExportacion(Tabla tabla) {
        if (!tabla.filas().isEmpty() && Consola.confirmar("\n¿Quieres exportar el resultado?")) {
            exportar(tabla);
        }
    }

    private void exportar(Tabla tabla) {
        if (tabla.filas().isEmpty()) {
            System.out.println("No hay datos que exportar.");
            return;
        }
        int formato = Consola.leerEntero("Formato (1 = CSV/Excel, 2 = XML): ", 1, 2);
        Path fichero = formato == 1 ? exportacionService.exportarCSV(tabla) : exportacionService.exportarXML(tabla);
        exito("Exportado en: " + fichero.toAbsolutePath());
    }
}