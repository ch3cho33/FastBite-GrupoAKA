package com.fastbite.exception;


// Lanzada cuando no se encuentra un pedido con el ID indicado.

public class PedidoNotFoundException extends FastBiteException {

    public PedidoNotFoundException(String pedidoId) {
        super("PEDIDO_NO_ENCONTRADO",
                "No se encontró el pedido con ID: " + pedidoId);
    }
}