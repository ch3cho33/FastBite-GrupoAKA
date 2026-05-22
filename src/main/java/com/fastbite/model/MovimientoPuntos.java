package com.fastbite.model;

// Esta clase representa UN movimiento de puntos
// Cada vez que alguien gana o canjea puntos, se guarda un Movimiento
public class MovimientoPuntos {

    // Tipos posibles de movimiento
    public static final String TIPO_GANAR   = "Ganó puntos";
    public static final String TIPO_CANJEAR = "Canjeó puntos";

    // ── Datos del movimiento ───────────────────────────────────────
    private String tipo;        // "Ganó puntos" o "Canjeó puntos"
    private int cantidad;       // cuántos puntos fueron
    private String descripcion; // ej: "Compra en tienda"
    private String fecha;       // ej: "22/05/2026"

    // ── Constructor ────────────────────────────────────────────────
    public MovimientoPuntos(String tipo, int cantidad, String descripcion, String fecha) {
        this.tipo        = tipo;
        this.cantidad    = cantidad;
        this.descripcion = descripcion;
        this.fecha       = fecha;
    }

    // ── Getters ────────────────────────────────────────────────────

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









