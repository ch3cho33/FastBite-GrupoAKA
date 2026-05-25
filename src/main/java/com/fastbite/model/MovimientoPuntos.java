package com.fastbite.model;

// Movimientos de canjeo de puntos

public class MovimientoPuntos {

    // Tipos posibles de movimiento
    public static final String TIPO_GANAR = "Ganó puntos";
    public static final String TIPO_CANJEAR = "Canjeó puntos";

    // Datos
    private String tipo;
    private int cantidad;
    private String descripcion;
    private String fecha;

    // Constructor
    public MovimientoPuntos(String tipo, int cantidad, String descripcion, String fecha) {
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    // Getters

    public String getTipo() {
        return tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFecha() {
        return fecha;
    }
}









