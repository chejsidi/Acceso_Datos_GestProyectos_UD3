package com.gestion.service;

import com.gestion.model.Gestion;
import com.gestion.model.Pieza;
import com.gestion.model.Proveedor;
import com.gestion.model.Proyecto;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportacionService {

    private static final DateTimeFormatter FORMATO_ARCHIVO =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    // Genera el nombre del archivo con timestamp para no sobreescribir
    private Path rutaArchivo(String prefijo, String extension) {
        String nombre = prefijo + "_" + LocalDateTime.now().format(FORMATO_ARCHIVO) + extension;
        return Paths.get(System.getProperty("user.home"), nombre);
    }

    public Path exportarProveedoresCsv(List<Proveedor> proveedores) {
        Path ruta = rutaArchivo("proveedores", ".csv");
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(ruta, StandardCharsets.UTF_8))) {
            pw.println("codigo,nombre,apellidos,direccion");
            for (Proveedor p : proveedores) {
                pw.printf("%s,%s,%s,%s%n",
                        escaparCsv(p.getCodigo()),
                        escaparCsv(p.getNombre()),
                        escaparCsv(p.getApellidos()),
                        escaparCsv(p.getDireccion()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al exportar proveedores: " + e.getMessage(), e);
        }
        return ruta;
    }

    public Path exportarPiezasCsv(List<Pieza> piezas) {
        Path ruta = rutaArchivo("piezas", ".csv");
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(ruta, StandardCharsets.UTF_8))) {
            pw.println("codigo,nombre,precio,descripcion");
            for (Pieza p : piezas) {
                pw.printf("%s,%s,%s,%s%n",
                        escaparCsv(p.getCodigo()),
                        escaparCsv(p.getNombre()),
                        p.getPrecio().toPlainString(),
                        escaparCsv(p.getDescripcion()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al exportar piezas: " + e.getMessage(), e);
        }
        return ruta;
    }

    public Path exportarProyectosCsv(List<Proyecto> proyectos) {
        Path ruta = rutaArchivo("proyectos", ".csv");
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(ruta, StandardCharsets.UTF_8))) {
            pw.println("codigo,nombre,ciudad");
            for (Proyecto p : proyectos) {
                pw.printf("%s,%s,%s%n",
                        escaparCsv(p.getCodigo()),
                        escaparCsv(p.getNombre()),
                        escaparCsv(p.getCiudad()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al exportar proyectos: " + e.getMessage(), e);
        }
        return ruta;
    }

    public Path exportarSuministrosCsv(List<Gestion> suministros) {
        Path ruta = rutaArchivo("suministros", ".csv");
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(ruta, StandardCharsets.UTF_8))) {
            pw.println("id,proveedor,pieza,proyecto,cantidad,fecha");
            for (Gestion g : suministros) {
                pw.printf("%d,%s,%s,%s,%d,%s%n",
                        g.getId(),
                        escaparCsv(g.getProveedor().getCodigo()),
                        escaparCsv(g.getPieza().getCodigo()),
                        escaparCsv(g.getProyecto().getCodigo()),
                        g.getCantidad(),
                        g.getFecha().toString());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al exportar suministros: " + e.getMessage(), e);
        }
        return ruta;
    }

    // Envuelve en comillas si el valor contiene comas o comillas
    private String escaparCsv(String valor) {
        if (valor == null) return "";
        if (valor.contains(",") || valor.contains("\"") || valor.contains("\n")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }
}