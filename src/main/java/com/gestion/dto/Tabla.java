package com.gestion.dto;

import java.util.List;

public record Tabla(String titulo, List<String> columnas, List<List<String>> filas) {
}