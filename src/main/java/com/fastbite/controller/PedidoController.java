package com.fastbite.controller;

import com.fastbite.model.EstadoPedido;
import com.fastbite.model.ItemPedido;
import com.fastbite.model.Pedido;
import com.fastbite.model.Producto;
import com.fastbite.persistence.PersistenciaManager;

import java.util.ArrayList;
import java.util.List;

public class PedidoController {

    private static PedidoController instancia;
    private List<Pedido> pedidos;
    private final PersistenciaManager persistencia;

    private PedidoController() {
        this.persistencia = PersistenciaManager.getInstance();
        this.pedidos = new ArrayList<>(persistencia.cargarPedidos());
    }

    public static PedidoController getInstance() {
        if (instancia == null) {
            instancia = new PedidoController();
        }
        return instancia;
    }

    public Pedido crearPedido(String tipoPedido) {
        Pedido pedido = new Pedido(tipoPedido);
        pedidos.add(pedido);
        CocinaController.getInstance().recibirPedido(pedido);
        guardarDatos();
        return pedido;
    }

    public void agregarProducto(String pedidoId, Producto producto, int cantidad) {
        Pedido pedido = buscarPorId(pedidoId);
        if (pedido == null) return;
        pedido.agregarItem(new ItemPedido(producto, cantidad));
        guardarDatos();
    }

    public void cambiarEstado(String pedidoId, EstadoPedido nuevoEstado) {
        Pedido pedido = buscarPorId(pedidoId);
        if (pedido != null) {
            pedido.cambiarEstado(nuevoEstado);
            guardarDatos();
        }
    }

    public List<Pedido> obtenerTodos() {
        return new ArrayList<>(pedidos);
    }

    public List<Pedido> obtenerPendientes() {
        return pedidos.stream()
                .filter(p -> p.getEstado() == EstadoPedido.PENDIENTE)
                .toList();
    }

    private Pedido buscarPorId(String id) {
        return pedidos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void guardarDatos() {
        persistencia.guardarPedidos(pedidos);
    }
}