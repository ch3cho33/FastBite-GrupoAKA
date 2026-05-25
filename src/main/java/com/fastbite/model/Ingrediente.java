package com.fastbite.model;

import java.util.UUID;


 // Entity: Representa un insumo/ingrediente del inventario FastBite.
 // GRASP Information Expert: conoce su stock y verifica nivel mínimo.

public class Ingrediente {

    private String id;
    private String nombre;
    private double cantidad;
    private double stockMinimo;
    private String unidad;

    public Ingrediente() {
        this.id = UUID.randomUUID().toString();
    }

    public Ingrediente(String nombre, double cantidad, double stockMinimo, String unidad) {
        this();
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.stockMinimo = stockMinimo;
        this.unidad = unidad;
    }


     // GRASP Information Expert: el Ingrediente conoce su cantidad y es responsable de actualizarla.

    public void actualizarStock(double cantidadEntrada) {
        if (cantidadEntrada <= 0) {
            throw new IllegalArgumentException("La cantidad de entrada debe ser mayor a 0.");
        }
        this.cantidad += cantidadEntrada;
    }


     //Descuenta stock. Lanza excepción si no hay suficiente.

    public void descontarStock(double cantidadUso) {
        if (cantidadUso <= 0) {
            throw new IllegalArgumentException("La cantidad a descontar debe ser mayor a 0.");
        }
        if (this.cantidad < cantidadUso) {
            throw new IllegalStateException("Stock insuficiente para '" + nombre + "'. Disponible: " + cantidad + " " + unidad);
        }
        this.cantidad -= cantidadUso;
    }

    // GRASP Information Expert: verifica si el stock está en nivel crítico.

    public boolean stockBajoMinimo() {
        return this.cantidad <= this.stockMinimo;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public double getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(double stockMinimo) { this.stockMinimo = stockMinimo; }

    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }

    @Override
    public String toString() {
        return nombre + " [" + String.format("%.2f", cantidad) + " " + unidad + "]";
    }
}