package com.fastbite.fastbite.model;

public enum EstadoPedido {
    PENDIENTE,       // El pedido fue creado pero aún no se está preparando
    EN_PREPARACION,  // La cocina está preparando el pedido
    LISTO,           // El pedido está listo para ser entregado
    ENTREGADO,       // El pedido fue entregado al cliente
    CANCELADO        // El pedido fue cancelado
}