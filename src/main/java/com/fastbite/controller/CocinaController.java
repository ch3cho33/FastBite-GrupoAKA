package com.fastbite.controller;

import com.fastbite.model.*;
import com.fastbite.persistence.PersistenciaManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Control: Coordina el flujo de preparación de pedidos en cocina.

public class CocinaController {

    private static CocinaController instancia;

    private List<Pedido> pedidos;
    private final PersistenciaManager persistencia;
    private final InventarioController inventarioController;

    private CocinaController() {
        this.persistencia = PersistenciaManager.getInstance();
        this.inventarioController = InventarioController.getInstance();
        cargarDatos();
    }

    public static CocinaController getInstance() {
        if (instancia == null) {
            instancia = new CocinaController();
        }
        return instancia;
    }

    // Gestión de Pedidos - Vista Principal

    public List<Pedido> obtenerPedidosPendientes() {
        return pedidos.stream()
                .filter(p -> p.getEstado() == EstadoPedido.PENDIENTE)
                .toList();
    }

    // Obtener pedidos en prepración
    public List<Pedido> obtenerPedidosEnPreparacion() {
        return pedidos.stream()
                .filter(p -> p.getEstado() == EstadoPedido.EN_PREPARACION)
                .toList();
    }

    // Obtiene todos los pedidos activos
    public List<Pedido> obtenerPedidosActivos() {
        return pedidos.stream()
                .filter(p -> p.getEstado() == EstadoPedido.PENDIENTE
                        || p.getEstado() == EstadoPedido.EN_PREPARACION)
                .toList();
    }

    // Cambiar estado de preparación
    public void iniciarPreparacion(String pedidoId) {
        Pedido pedido = obtenerPedidoPorId(pedidoId);
        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new IllegalStateException(
                    "Solo se pueden iniciar pedidos PENDIENTES. Estado actual: " + pedido.getEstado());
        }
        pedido.cambiarEstado(EstadoPedido.EN_PREPARACION);
        guardarDatos();
    }

    // Cambiar estado a listo
    public void marcarItemListo(String pedidoId, String itemId) {
        Pedido pedido = obtenerPedidoPorId(pedidoId);
        ItemPedido item = pedido.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Ítem no encontrado: " + itemId));
        item.marcarListo();
        guardarDatos();
    }

    // Marcar pedido completo como Listo
    public void marcarPedidoListo(String pedidoId) {
        Pedido pedido = obtenerPedidoPorId(pedidoId);

        if (pedido.getEstado() != EstadoPedido.EN_PREPARACION) {
            throw new IllegalStateException(
                    "El pedido debe estar EN PREPARACIÓN para marcarlo listo.");
        }

        // Marcar todos los ítems como listos
        for (ItemPedido item : pedido.getItems()) {
            item.setEstado(EstadoPedido.LISTO);
        }

        pedido.cambiarEstado(EstadoPedido.LISTO);
        guardarDatos();

        // Notificación
        System.out.println("[COCINA] Pedido " + pedido.getNumeroPedido() + " marcado como LISTO.");
    }

    //Cambio estado de vista
    public void cambiarEstadoPedido(String pedidoId, EstadoPedido nuevoEstado) {
        Pedido pedido = obtenerPedidoPorId(pedidoId);
        EstadoPedido estadoActual = pedido.getEstado();

        // Validar transiciones permitidas
        if (!esTransicionValida(estadoActual, nuevoEstado)) {
            throw new IllegalStateException(
                    "Transición no válida: " + estadoActual + " → " + nuevoEstado);
        }

        pedido.cambiarEstado(nuevoEstado);

        // Si el pedido pasa a LISTO, marcar todos los ítems
        if (nuevoEstado == EstadoPedido.LISTO) {
            pedido.getItems().forEach(i -> i.setEstado(EstadoPedido.LISTO));
        }

        guardarDatos();
    }

    // Agregar nuevo pedido al sistema
    public void recibirPedido(Pedido pedido) {
        pedidos.add(pedido);
        guardarDatos();
        System.out.println("[COCINA] Nuevo pedido recibido: " + pedido.getNumeroPedido());
    }

    public Optional<Pedido> buscarPedidoPorId(String pedidoId) {
        return pedidos.stream().filter(p -> p.getId().equals(pedidoId)).findFirst();
    }

    public List<Pedido> obtenerTodosLosPedidos() {
        return new ArrayList<>(pedidos);
    }


    // Validaciones
    private boolean esTransicionValida(EstadoPedido actual, EstadoPedido nuevo) {
        return switch (actual) {
            case PENDIENTE      -> nuevo == EstadoPedido.EN_PREPARACION || nuevo == EstadoPedido.CANCELADO;
            case EN_PREPARACION -> nuevo == EstadoPedido.LISTO || nuevo == EstadoPedido.CANCELADO;
            case LISTO          -> nuevo == EstadoPedido.ENTREGADO;
            default             -> false;
        };
    }

    private Pedido obtenerPedidoPorId(String id) {
        return pedidos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado: " + id));
    }

    // Persistencia
    private void cargarDatos() {
        pedidos = new ArrayList<>(persistencia.cargarPedidos());
    }

    public void guardarDatos() {
        persistencia.guardarPedidos(pedidos);
    }
}