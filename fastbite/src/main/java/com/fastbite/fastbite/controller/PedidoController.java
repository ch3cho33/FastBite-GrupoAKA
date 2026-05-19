package com.fastbite.fastbite.controller;

import com.fastbite.fastbite.model.EstadoPedido;
import com.fastbite.fastbite.model.ItemPedido;
import com.fastbite.fastbite.model.Pedido;
import com.fastbite.fastbite.model.Producto;
import com.fastbite.fastbite.persistence.PedidoRepository;

import java.util.ArrayList;


public class PedidoController {

    // Lista de todos los pedidos de la sesión
    private ArrayList<Pedido> pedidos;

    // Contador para asignar IDs automáticos
    private int contadorId;

    // Repositorio para guardar y cargar en JSON
    private PedidoRepository repository;

    // Constructor
    public PedidoController() {
        this.repository = new PedidoRepository();
        this.pedidos    = repository.cargarPedidos(); // Carga pedidos guardados al iniciar
        this.contadorId = pedidos.size() + 1;         // El ID continúa desde donde quedó
    }

    public Pedido crearPedido(String nombreCliente) {
        Pedido pedido = new Pedido(contadorId, nombreCliente);
        pedidos.add(pedido);
        contadorId++;
        return pedido;
    }

    public void agregarProducto(int numeroPedido, Producto producto, int cantidad) {
        Pedido pedido = buscarPedido(numeroPedido);

        if (pedido == null) {
            System.out.println("No existe el pedido #" + numeroPedido);
            return;
        }

        ItemPedido item = new ItemPedido(producto, cantidad);
        pedido.agregarItem(item);
    }

    public ArrayList<Pedido> obtenerPedidos() {
        return pedidos;
    }

    public void cambiarEstadoPedido(int numeroPedido, EstadoPedido nuevoEstado) {
        Pedido pedido = buscarPedido(numeroPedido);

        if (pedido == null) {
            System.out.println("No existe el pedido #" + numeroPedido);
            return;
        }

        pedido.cambiarEstado(nuevoEstado);
    }


    public void guardarPedidos() {
        repository.guardarPedidos(pedidos);
    }

    public void cargarPedidos() {
        pedidos    = repository.cargarPedidos();
        contadorId = pedidos.size() + 1;
    }

    private Pedido buscarPedido(int numeroPedido) {
        for (Pedido pedido : pedidos) {
            if (pedido.getNumeroPedido() == numeroPedido) {
                return pedido;
            }
        }
        return null;
    }
}