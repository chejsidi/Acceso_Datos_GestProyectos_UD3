package com.gestion.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "registro_operaciones")
public class RegistroOperacion {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final int MAX_DETALLE = 500;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false, length = 30)
    private String usuario;

    @Column(nullable = false, length = 15)
    private String operacion;

    @Column(nullable = false, length = 30)
    private String entidad;

    @Column(nullable = false, length = 30)
    private String identificador;

    @Column(length = MAX_DETALLE)
    private String detalle;

    public RegistroOperacion() {
    }

    public RegistroOperacion(String usuario, String operacion, String entidad, String identificador, String detalle) {
        this.fechaHora = LocalDateTime.now();
        this.usuario = usuario;
        this.operacion = operacion;
        this.entidad = entidad;
        this.identificador = identificador;
        this.detalle = detalle.length() > MAX_DETALLE ? detalle.substring(0, MAX_DETALLE) : detalle;
    }

    @Override
    public String toString() {
        return String.format("%s | %-12s | %-12s | %-10s %-8s | %s",
                fechaHora.format(FORMATO), usuario, operacion, entidad, identificador, detalle);
    }
}