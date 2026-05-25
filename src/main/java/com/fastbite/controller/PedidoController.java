package com.fastbite.controller;

import com.fastbite.exception.PedidoNotFoundException;
import com.fastbite.exception.ValidacionException;
import com.fastbite.model.EstadoPedido;
import com.fastbite.model.ItemPedido;
import com.fastbite.model.Pedido;
import com.fastbite.model.Producto;
import com.fastbite.persistence.PersistenciaManager;

import java.util.ArrayList;
import java.util.List;


 // Control: Coordina el registro y gestión de pedidos.
 // GRASP Controller: caso de uso "Registrar Pedido".

public class PedidoController {

    private static PedidoController instancia;
    private List<Pedido> pedidos;
    private final PersistenciaManager persistencia;

    private PedidoController() {
        this.persistencia = PersistenciaManager.getInstance();
        this.pedidos = new ArrayList<>(persistencia.cargarPedidos());
    }

    public static PedidoController getInstance() {
        if (instancia == null) instancia = new PedidoController();
        return instancia;
    }

    // Crear Pedido
    public Pedido crearPedido(String tipoPedido) {
        if (tipoPedido == null || tipoPedido.isBlank())
            throw new ValidacionException("tipoPedido", "El tipo de pedido es obligatorio.");
        Pedido pedido = new Pedido(tipoPedido.trim());
        pedidos.add(pedido);
        CocinaController.getInstance().recibirPedido(pedido);
        guardarDatos();
        return pedido;
    }

    // Registra pedido ya construido
    public void crearPedido(Pedido pedido) {
        if (pedido == null)
            throw new ValidacionException("pedido", "El pedido no puede ser nulo.");
        if (pedido.getItems().isEmpty())
            throw new ValidacionException("items", "El pedido no tiene productos.");
        pedidos.add(pedido);
        CocinaController.getInstance().recibirPedido(pedido);
        guardarDatos();
    }

    public void agregarProducto(String pedidoId, Producto producto, int cantidad) {
        if (cantidad <= 0)
            throw new ValidacionException("cantidad", "La cantidad debe ser mayor a 0.");
        Pedido pedido = buscarPorIdOLanzar(pedidoId);
        pedido.agregarItem(new ItemPedido(producto, cantidad));
        guardarDatos();
    }

    public void cambiarEstado(String pedidoId, EstadoPedido nuevoEstado) {
        Pedido pedido = buscarPorIdOLanzar(pedidoId);
        pedido.cambiarEstado(nuevoEstado);
        guardarDatos();
    }

    public List<Pedido> obtenerTodos()      { return new ArrayList<>(pedidos); }

    public List<Pedido> obtenerPendientes() {
        return pedidos.stream()
                .filter(p -> p.getEstado() == EstadoPedido.PENDIENTE)
                .toList();
    }

    public List<Pedido> obtenerPorEstado(EstadoPedido estado) {
        return pedidos.stream().filter(p -> p.getEstado() == estado).toList();
    }

    public Pedido buscarPorId(String id) {
        return pedidos.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    private Pedido buscarPorIdOLanzar(String id) {
        return pedidos.stream().filter(p -> p.getId().equals(id)).findFirst()
                .orElseThrow(() -> new PedidoNotFoundException(id));
    }

    public void guardarDatos() { persistencia.guardarPedidos(pedidos); }
}