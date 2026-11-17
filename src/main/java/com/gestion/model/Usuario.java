package com.gestion.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @Column(length = 30)
    private String nombre;

    // Nunca se guarda la contraseña: solo su hash y la sal con la que se calculó
    @Column(nullable = false, length = 100)
    private String hash;

    @Column(nullable = false, length = 50)
    private String sal;

    @Column(nullable = false)
    private LocalDateTime fechaAlta;

    public Usuario() {
    }

    public Usuario(String nombre, String hash, String sal) {
        this.nombre = nombre;
        this.hash = hash;
        this.sal = sal;
        this.fechaAlta = LocalDateTime.now();
    }

    public String getNombre() { return nombre; }

    public String getHash() { return hash; }
    public void setHash(String hash) { this.hash = hash; }

    public String getSal() { return sal; }
    public void setSal(String sal) { this.sal = sal; }

    public LocalDateTime getFechaAlta() { return fechaAlta; }

    // Sin hash ni sal: este texto acaba en la auditoría
    @Override
    public String toString() {
        return "Usuario " + nombre;
    }
}