package com.gestion.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "proyectos")
public class Proyecto {

    @Id
    @Column(length = 10)
    private String codigo;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String ciudad;

    @OneToMany(mappedBy = "proyecto")
    private List<Gestion> gestiones = new ArrayList<>();

    public Proyecto() {
    }

    public Proyecto(String codigo, String nombre, String ciudad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.ciudad = ciudad;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public List<Gestion> getGestiones() { return gestiones; }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s)", codigo, nombre, ciudad);
    }
}