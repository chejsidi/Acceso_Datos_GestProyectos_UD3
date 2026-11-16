package com.gestion.dto;

import java.math.BigDecimal;

public record ConsumoPieza(String codigo, String nombre, Long cantidad, BigDecimal coste) {
}