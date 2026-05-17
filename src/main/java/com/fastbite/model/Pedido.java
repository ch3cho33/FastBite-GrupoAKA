package com.fastbite.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entity: Representa la orden completa realizada por un cliente.
 * GRASP Information Expert: calcula subtotal, IVA y total del pedido.
 * GRASP Creator: crea instancias de ItemPedido.
 */
public class Pedido {

    private static final double IVA = 0.19;

    private String id;
    private String numeroPedido;
    private String tipoPedido;       // "mesa" | "domicilio" | "llevar"
    private EstadoPedido estado;
    private List<ItemPedido> items;
    private double subtotal;
    private double iva;
    private double total;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String clienteId;        // opcional
    private String observaciones;

    public Pedido() {
        this.id = UUID.randomUUID().toString();
        this.numeroPedido = "P-" + System.currentTimeMillis() % 10000;
        this.estado = EstadoPedido.PENDIENTE;
        this.items = new ArrayList<>();
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    public Pedido(String tipoPedido) {
        this();
        this.tipoPedido = tipoPedido;
    }

    /**
     * GRASP Creator: Pedido agrega ItemPedido porque los contiene.
     */
    public void agregarItem(ItemPedido item) {
        this.items.add(item);
        calcularTotal();
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void removerItem(ItemPedido item) {
        this.items.remove(item);
        calcularTotal();
        this.fechaActualizacion = LocalDateTime.now();
    }

    /**
     * GRASP Information Expert: Pedido conoce todos sus ítems y
     * es responsable de calcular el total completo.
     */
    public void calcularTotal() {
        this.subtotal = items.stream()
                .mapToDouble(ItemPedido::getSubtotal)
                .sum();
        this.iva = this.subtotal * IVA;
        this.total = this.subtotal + this.iva;
    }

    /**
     * Cambia el estado del pedido con validación de transición.
     */
    public void cambiarEstado(EstadoPedido nuevoEstado) {
        this.estado = nuevoEstado;
        this.fechaActualizacion = LocalDateTime.now();
    }

    /**
     * Verifica si todos los ítems del pedido están listos.
     * Usado por CocinaController para marcar el pedido como LISTO.
     */
    public boolean todosItemsListos() {
        return !items.isEmpty() &&
                items.stream().allMatch(i -> i.getEstado() == EstadoPedido.LISTO);
    }

    public String getFechaCreacionFormateada() {
        return fechaCreacion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNumeroPedido() { return numeroPedido; }
    public void setNumeroPedido(String numeroPedido) { this.numeroPedido = numeroPedido; }

    public String getTipoPedido() { return tipoPedido; }
    public void setTipoPedido(String tipoPedido) { this.tipoPedido = tipoPedido; }

    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }

    public List<ItemPedido> getItems() { return items; }
    public void setItems(List<ItemPedido> items) {
        this.items = items;
        calcularTotal();
    }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getIva() { return iva; }
    public void setIva(double iva) { this.iva = iva; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    @Override
    public String toString() {
        return numeroPedido + " [" + estado.getDescripcion() + "] - $" + String.format("%.0f", total);
    }
}