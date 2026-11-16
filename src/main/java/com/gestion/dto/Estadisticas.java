package com.gestion.dto;

import java.math.BigDecimal;
import java.util.List;

public record Estadisticas(long proveedores, long piezas, long proyectos,
                           long suministros, long unidades, BigDecimal costeTotal,
                           List<Ranking> topProveedores, List<Ranking> topPiezas, List<Ranking> topProyectos) {
}