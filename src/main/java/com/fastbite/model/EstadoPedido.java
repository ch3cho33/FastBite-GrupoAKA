package com.fastbite.model;

// Estados posibles de un pedido en el sistema FastBite.

public enum EstadoPedido {
    PENDIENTE("Pendiente"),
    EN_PREPARACION("En preparación"),
    LISTO("Listo"),
    ENTREGADO("Entregado"),
    CANCELADO("Cancelado");

    private final String descripcion;

    EstadoPedido(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}