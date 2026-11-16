package com.gestion.service;

import com.gestion.dto.AnalisisProyecto;
import com.gestion.dto.ConsumoPieza;
import com.gestion.dto.Estadisticas;
import com.gestion.exception.ValidacionException;
import com.gestion.model.Gestion;
import com.gestion.model.Proyecto;
import com.gestion.repository.GestionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class InformeService {

    private static final int TOP = 5;

    private final GestionRepository gestionRepository;
    private final ProveedorService proveedorService;
    private final PiezaService piezaService;
    private final ProyectoService proyectoService;

    public InformeService(GestionRepository gestionRepository, ProveedorService proveedorService,
                          PiezaService piezaService, ProyectoService proyectoService) {
        this.gestionRepository = gestionRepository;
        this.proveedorService = proveedorService;
        this.piezaService = piezaService;
        this.proyectoService = proyectoService;
    }

    public List<Gestion> historicoProveedor(String codigo, LocalDate desde, LocalDate hasta) {
        proveedorService.buscarPorCodigo(codigo);
        if (desde.isAfter(hasta)) {
            throw new ValidacionException("La fecha inicial no puede ser posterior a la final.");
        }
        return gestionRepository.historicoProveedor(codigo, desde, hasta);
    }

    public AnalisisProyecto analizarProyecto(String codigo) {
        Proyecto proyecto = proyectoService.buscarPorCodigo(codigo);
        List<ConsumoPieza> piezas = gestionRepository.consumoPiezasDeProyecto(codigo);
        // Suma de los costes de cada pieza
        BigDecimal costeTotal = piezas.stream()
                .map(ConsumoPieza::coste)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new AnalisisProyecto(proyecto, gestionRepository.contarProveedoresDeProyecto(codigo), piezas, costeTotal);
    }

    public Estadisticas estadisticasGlobales() {
        return new Estadisticas(
                proveedorService.contar(),
                piezaService.contar(),
                proyectoService.contar(),
                gestionRepository.contar(),
                gestionRepository.totalUnidades(),
                gestionRepository.costeTotal(),
                gestionRepository.proveedoresMasActivos(TOP),
                gestionRepository.piezasMasUtilizadas(TOP),
                gestionRepository.proyectosConMasSuministros(TOP));
    }

    public List<Gestion> todosLosSuministros() {
        return gestionRepository.listarTodos();
    }
}