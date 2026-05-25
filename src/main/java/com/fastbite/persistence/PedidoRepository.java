package com.fastbite.persistence;

import com.fastbite.exception.PersistenciaException;
import com.fastbite.model.Pedido;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


 //Repository: Gestiona la persistencia de Pedido en JSON.
 // GRASP Low Coupling: los controllers no acceden al JSON directamente.

public class PedidoRepository {

    private final PersistenciaManager manager;

    public PedidoRepository() {
        this.manager = PersistenciaManager.getInstance();
    }

    // Guarda la lista completa de pedidos en pedidos.json
    public void guardar(List<Pedido> pedidos) {
        try {
            manager.guardarPedidos(pedidos);
        } catch (PersistenciaException e) {
            throw new PersistenciaException("pedidos.json",
                    "No se pudo guardar los pedidos", e);
        }
    }

    // Carga todos los pedidos desde pedidos.json

    public List<Pedido> cargarTodos() {
        try {
            List<Pedido> pedidos = manager.cargarPedidos();
            return pedidos != null ? pedidos : new ArrayList<>();
        } catch (PersistenciaException e) {
            System.err.println("[PedidoRepository] Error cargando pedidos: "
                    + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Busca un pedido por su ID
    public Optional<Pedido> buscarPorId(String id) {
        return cargarTodos().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    //Agrega un pedido nuevo y guarda
    public void agregar(Pedido pedido, List<Pedido> listaActual) {
        listaActual.add(pedido);
        guardar(listaActual);
    }

    // Actualiza un pedido existente en la lista y guarda
    public void actualizar(Pedido pedidoActualizado, List<Pedido> listaActual) {
        for (int i = 0; i < listaActual.size(); i++) {
            if (listaActual.get(i).getId().equals(pedidoActualizado.getId())) {
                listaActual.set(i, pedidoActualizado);
                break;
            }
        }
        guardar(listaActual);
    }
}