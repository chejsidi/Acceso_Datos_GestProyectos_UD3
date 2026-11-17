package com.gestion.service;

import com.gestion.model.Gestion;
import com.gestion.model.Pieza;
import com.gestion.model.Proveedor;
import com.gestion.model.Proyecto;
import com.gestion.repository.GestionRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InformeService {

    private final GestionRepository gestionRepository;
    private final ProveedorService proveedorService;
    private final PiezaService piezaService;
    private final ProyectoService proyectoService;

    public InformeService(GestionRepository gestionRepository,
                          ProveedorService proveedorService,
                          PiezaService piezaService,
                          ProyectoService proyectoService) {
        this.gestionRepository = gestionRepository;
        this.proveedorService = proveedorService;
        this.piezaService = piezaService;
        this.proyectoService = proyectoService;
    }

    // Total de suministros por proveedor (código → cantidad de registros)
    public Map<String, Long> suministrosPorProveedor() {
        return gestionRepository.listarTodos().stream()
                .collect(Collectors.groupingBy(
                        g -> g.getProveedor().getCodigo(),
                        Collectors.counting()
                ));
    }

    // Total de suministros por pieza
    public Map<String, Long> suministrosPorPieza() {
        return gestionRepository.listarTodos().stream()
                .collect(Collectors.groupingBy(
                        g -> g.getPieza().getCodigo(),
                        Collectors.counting()
                ));
    }

    // Total de suministros por proyecto
    public Map<String, Long> suministrosPorProyecto() {
        return gestionRepository.listarTodos().stream()
                .collect(Collectors.groupingBy(
                        g -> g.getProyecto().getCodigo(),
                        Collectors.counting()
                ));
    }

    // Coste total por proyecto (suma de precio * cantidad de cada suministro)
    public Map<String, BigDecimal> costeTotalPorProyecto() {
        return gestionRepository.listarTodos().stream()
                .collect(Collectors.groupingBy(
                        g -> g.getProyecto().getCodigo(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                g -> g.getPieza().getPrecio()
                                        .multiply(BigDecimal.valueOf(g.getCantidad())),
                                BigDecimal::add
                        )
                ));
    }

    // Piezas ordenadas por precio descendente
    public List<Pieza> piezasPorPrecioDesc() {
        return piezaService.listarTodas().stream()
                .sorted(Comparator.comparing(Pieza::getPrecio).reversed())
                .toList();
    }

    // Proveedor con más suministros
    public Proveedor proveedorMasActivo() {
        return suministrosPorProveedor().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> proveedorService.buscarPorCodigo(e.getKey()))
                .orElseThrow(() -> new IllegalStateException("No hay suministros registrados."));
    }

    // Proyecto con mayor coste acumulado
    public Proyecto proyectoMasCostoso() {
        return costeTotalPorProyecto().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> proyectoService.buscarPorCodigo(e.getKey()))
                .orElseThrow(() -> new IllegalStateException("No hay suministros registrados."));
    }

    // Todos los suministros ordenados por fecha descendente
    public List<Gestion> suministrosOrdenadosPorFecha() {
        return gestionRepository.listarTodos().stream()
                .sorted(Comparator.comparing(Gestion::getFecha).reversed())
                .toList();
    }
}