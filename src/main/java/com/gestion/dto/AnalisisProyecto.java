package com.gestion.dto;

import com.gestion.model.Proyecto;

import java.math.BigDecimal;
import java.util.List;

public record AnalisisProyecto(Proyecto proyecto, long numeroProveedores,
                               List<ConsumoPieza> piezas, BigDecimal costeTotal) {
}