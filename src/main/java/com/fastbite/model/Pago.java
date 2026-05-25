package com.fastbite.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


 // Entity: Representa el pago de un pedido.
 // GRASP Information Expert: conoce el monto pagado y calcula el cambio.

public class Pago {

    public enum MetodoPago {
        EFECTIVO("Efectivo"),
        TARJETA("Tarjeta"),
        TRANSFERENCIA("Transferencia");

        private final String descripcion;
        MetodoPago(String d) { this.descripcion = d; }
        public String getDescripcion() { return descripcion; }
        @Override public String toString() { return descripcion; }
    }

    private String id;
    private String pedidoId;
    private MetodoPago metodo;
    private double totalPedido;
    private double montoPagado;
    private double cambio;
    private LocalDateTime fecha;
    private boolean procesado;

    public Pago() {
        this.id = UUID.randomUUID().toString();
        this.fecha = LocalDateTime.now();
        this.procesado = false;
    }

    public Pago(String pedidoId, MetodoPago metodo,
                double totalPedido, double montoPagado) {
        this();
        this.pedidoId = pedidoId;
        this.metodo = metodo;
        this.totalPedido = totalPedido;
        this.montoPagado = montoPagado;
        this.cambio = Math.max(0, montoPagado - totalPedido);
    }

    public boolean esSuficiente() { return montoPagado >= totalPedido; }
    public void procesar() { this.procesado = true; }

    public String getFechaFormateada() {
        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPedidoId() { return pedidoId; }
    public void setPedidoId(String p){ this.pedidoId = p; }
    public MetodoPago getMetodo() { return metodo; }
    public void setMetodo(MetodoPago m) { this.metodo = m; }
    public double getTotalPedido() { return totalPedido; }
    public void setTotalPedido(double t) { this.totalPedido = t; }
    public double getMontoPagado() { return montoPagado; }
    public void setMontoPagado(double m) {
        this.montoPagado = m;
        this.cambio = Math.max(0, m - totalPedido);
    }
    public double getCambio()  { return cambio; }
    public void setCambio(double c) { this.cambio = c; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime f) { this.fecha = f; }
    public boolean isProcesado() { return procesado; }
    public void setProcesado(boolean p) { this.procesado = p; }
}