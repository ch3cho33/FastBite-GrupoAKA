package com.fastbite.model;

import java.util.UUID;

/**
 * Entity: Representa un producto del menú FastBite.
 * GRASP Information Expert: conoce su precio y estado activo.
 */
public class Producto {

    private String id;
    private String nombre;
    private String descripcion;
    private double precio;
    private boolean activo;
    private String categoriaId;

    public Producto() {
        this.id = UUID.randomUUID().toString();
        this.activo = true;
    }

    public Producto(String nombre, String descripcion, double precio, String categoriaId) {
        this();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoriaId = categoriaId;
    }

    // GRASP Information Expert: el Producto conoce su estado activo
    public boolean estaActivo() {
        return activo;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getCategoriaId() { return categoriaId; }
    public void setCategoriaId(String categoriaId) { this.categoriaId = categoriaId; }

    @Override
    public String toString() {
        return nombre + " ($" + String.format("%.0f", precio) + ")";
    }
}