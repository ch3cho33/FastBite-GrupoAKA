package com.fastbite.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

// Entity: Registra cada entrada o salida de stock para trazabilidad.

public class MovimientoInventario {

    public enum TipoMovimiento {
        ENTRADA("Entrada"),
        SALIDA("Salida"),
        AJUSTE("Ajuste");

        private final String descripcion;
        TipoMovimiento(String descripcion) { this.descripcion = descripcion; }
        public String getDescripcion() { return descripcion; }

        @Override
        public String toString() { return descripcion; }
    }

    private String id;
    private String ingredienteId;
    private String nombreIngrediente;
    private TipoMovimiento tipo;
    private double cantidad;
    private double stockAntes;
    private double stockDespues;
    private LocalDateTime fecha;
    private String referencia;  // ej: ID del pedido que lo generó

    public MovimientoInventario() {
        this.id = UUID.randomUUID().toString();
        this.fecha = LocalDateTime.now();
    }

    public MovimientoInventario(Ingrediente ingrediente, TipoMovimiento tipo,
                                double cantidad, String referencia) {
        this();
        this.ingredienteId = ingrediente.getId();
        this.nombreIngrediente = ingrediente.getNombre();
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.stockAntes = ingrediente.getCantidad();
        this.referencia = referencia;
    }

    public String getFechaFormateada() {
        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIngredienteId() { return ingredienteId; }
    public void setIngredienteId(String ingredienteId) { this.ingredienteId = ingredienteId; }

    public String getNombreIngrediente() { return nombreIngrediente; }
    public void setNombreIngrediente(String nombreIngrediente) { this.nombreIngrediente = nombreIngrediente; }

    public TipoMovimiento getTipo() { return tipo; }
    public void setTipo(TipoMovimiento tipo) { this.tipo = tipo; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public double getStockAntes() { return stockAntes; }
    public void setStockAntes(double stockAntes) { this.stockAntes = stockAntes; }

    public double getStockDespues() { return stockDespues; }
    public void setStockDespues(double stockDespues) { this.stockDespues = stockDespues; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    @Override
    public String toString() {
        return "[" + getFechaFormateada() + "] " + tipo.getDescripcion()
                + " - " + nombreIngrediente + ": " + cantidad;
    }
}