package com.fastbite.controller;

import com.fastbite.exception.ValidacionException;
import com.fastbite.model.EstadoPedido;
import com.fastbite.model.Pago;
import com.fastbite.model.Pago.MetodoPago;
import com.fastbite.model.Pedido;


 // Control: Gestiona el procesamiento de pagos.
 // GRASP Controller: caso de uso "Procesar Pago".

public class PagoController {

    private static PagoController instancia;

    private PagoController() {}

    public static PagoController getInstance() {
        if (instancia == null) instancia = new PagoController();
        return instancia;
    }

    // Proceso pago de pedido
    public Pago procesarPago(Pedido pedido, MetodoPago metodo, double montoPagado) {
        if (pedido == null)
            throw new ValidacionException("pedido", "El pedido no puede ser nulo.");
        if (pedido.getItems().isEmpty())
            throw new ValidacionException("items", "El pedido no tiene productos.");
        if (metodo == null)
            throw new ValidacionException("metodo", "Selecciona un método de pago.");

        // Para tarjeta/transferencia el monto exacto es el total
        if (metodo != MetodoPago.EFECTIVO) {
            montoPagado = pedido.getTotal();
        }

        if (montoPagado < pedido.getTotal()) {
            throw new ValidacionException("montoPagado",
                    String.format("Monto insuficiente. Total: $%.0f | Recibido: $%.0f",
                            pedido.getTotal(), montoPagado));
        }

        Pago pago = new Pago(pedido.getId(), metodo, pedido.getTotal(), montoPagado);
        pago.procesar();

        // Actualizar estado del pedido a Pendiente
        pedido.cambiarEstado(EstadoPedido.PENDIENTE);

        return pago;
    }
}