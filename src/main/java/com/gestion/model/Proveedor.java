package com.gestion.model;

import jakarta.persistence.*;

@Entity
@Table(name = "proveedores")
public class Proveedor {

    // Código alfanumérico introducido por el usuario, sin autogenerar
    @Id
    @Column(length = 10)
    private String codigo;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(nullable = false, length = 150)
    private String direccion;

    // Constructor vacío obligatorio para Hibernate
    public Proveedor() {
    }

    public Proveedor(String codigo, String nombre, String apellidos, String direccion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    @Override
    public String toString() {
        return String.format("[%s] %s %s - %s", codigo, nombre, apellidos, direccion);
    }
}