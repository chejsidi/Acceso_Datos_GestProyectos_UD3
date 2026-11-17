package com.gestion.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "registro_accesos")
public class RegistroAcceso {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Texto y no relación con Usuario: también se registran intentos con usuarios inexistentes
    @Column(nullable = false, length = 30)
    private String usuario;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private boolean exito;

    public RegistroAcceso() {
    }

    public RegistroAcceso(String usuario, boolean exito) {
        this.usuario = usuario;
        this.exito = exito;
        this.fechaHora = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("%s | %-15s | %s", fechaHora.format(FORMATO), usuario, exito ? "CORRECTO" : "FALLIDO");
    }
}