package com.gestion.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "piezas")
public class Pieza {

    @Id
    @Column(length = 10)
    private String codigo;

    @Column(nullable = false, length = 50)
    private String nombre;

    // precision = dígitos totales, scale = decimales (ej: 99999999.99), para el dinero viene bn
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(length = 255)
    private String descripcion;

    @OneToMany(mappedBy = "pieza")
    private List<Gestion> gestiones = new ArrayList<>();

    public Pieza() {
    }

    public Pieza(String codigo, String nombre, BigDecimal precio, String descripcion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<Gestion> getGestiones() { return gestiones; }

    @Override
    public String toString() {
        return String.format("[%s] %s - %.2f € - %s", codigo, nombre, precio, descripcion);
    }
}