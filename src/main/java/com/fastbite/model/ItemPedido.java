package com.fastbite.model;

import java.util.UUID;

/**
 * Entity: Representa cada producto dentro de un pedido.
 * GRASP Information Expert: calcula su propio subtotal.
 */
public class ItemPedido {

    private String id;
    private String productoId;
    private String nombreProducto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    private EstadoPedido estado; // estado individual del ítem en cocina

    public ItemPedido() {
        this.id = UUID.randomUUID().toString();
        this.estado = EstadoPedido.PENDIENTE;
    }

    public ItemPedido(Producto producto, int cantidad) {
        this();
        this.productoId = producto.getId();
        this.nombreProducto = producto.getNombre();
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecio();
        calcularSubtotal();
    }

    /**
     * GRASP Information Expert: el ItemPedido conoce cantidad y precio,
     * por eso es responsable de calcular su subtotal.
     */
    public void calcularSubtotal() {
        this.subtotal = this.cantidad * this.precioUnitario;
    }

    public void marcarListo() {
        this.estado = EstadoPedido.LISTO;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProductoId() { return productoId; }
    public void setProductoId(String productoId) { this.productoId = productoId; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
        calcularSubtotal();
    }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }

    @Override
    public String toString() {
        return cantidad + "x " + nombreProducto + " - $" + String.format("%.0f", subtotal);
    }
}