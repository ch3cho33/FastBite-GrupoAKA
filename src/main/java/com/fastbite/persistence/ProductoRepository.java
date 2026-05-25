package com.fastbite.persistence;

import com.fastbite.exception.PersistenciaException;
import com.fastbite.model.Producto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


 // Repository: Gestiona la persistencia de Producto en JSON.

public class ProductoRepository {

    private final PersistenciaManager manager;

    public ProductoRepository() {
        this.manager = PersistenciaManager.getInstance();
    }

    public void guardar(List<Producto> productos) {
        try {
            manager.guardarProductos(productos);
        } catch (PersistenciaException e) {
            throw new PersistenciaException("productos.json",
                    "No se pudo guardar los productos", e);
        }
    }

    public List<Producto> cargarTodos() {
        try {
            List<Producto> lista = manager.cargarProductos();
            return lista != null ? lista : new ArrayList<>();
        } catch (PersistenciaException e) {
            System.err.println("[ProductoRepository] Error cargando productos: "
                    + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Optional<Producto> buscarPorId(String id) {
        return cargarTodos().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public Optional<Producto> buscarPorNombre(String nombre) {
        return cargarTodos().stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    public List<Producto> buscarPorCategoria(String categoriaId) {
        return cargarTodos().stream()
                .filter(p -> categoriaId.equals(p.getCategoriaId()))
                .toList();
    }

    public List<Producto> cargarActivos() {
        return cargarTodos().stream()
                .filter(Producto::isActivo)
                .toList();
    }

    public void agregar(Producto producto, List<Producto> listaActual) {
        listaActual.add(producto);
        guardar(listaActual);
    }

    public void actualizar(Producto productoActualizado, List<Producto> listaActual) {
        for (int i = 0; i < listaActual.size(); i++) {
            if (listaActual.get(i).getId().equals(productoActualizado.getId())) {
                listaActual.set(i, productoActualizado);
                break;
            }
        }
        guardar(listaActual);
    }

    public void eliminar(String id, List<Producto> listaActual) {
        listaActual.removeIf(p -> p.getId().equals(id));
        guardar(listaActual);
    }
}