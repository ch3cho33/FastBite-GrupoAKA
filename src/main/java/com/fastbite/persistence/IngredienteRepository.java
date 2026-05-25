package com.fastbite.persistence;

import com.fastbite.exception.PersistenciaException;
import com.fastbite.model.AlertaStock;
import com.fastbite.model.Ingrediente;
import com.fastbite.model.MovimientoInventario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


 // Repository: Gestiona la persistencia de Ingrediente, Movimientos y Alertas.

public class IngredienteRepository {

    private final PersistenciaManager manager;

    public IngredienteRepository() {
        this.manager = PersistenciaManager.getInstance();
    }

    // Ingredientes

    public void guardarIngredientes(List<Ingrediente> ingredientes) {
        try {
            manager.guardarIngredientes(ingredientes);
        } catch (PersistenciaException e) {
            throw new PersistenciaException("inventario.json",
                    "No se pudo guardar el inventario", e);
        }
    }

    public List<Ingrediente> cargarIngredientes() {
        try {
            List<Ingrediente> lista = manager.cargarIngredientes();
            return lista != null ? lista : new ArrayList<>();
        } catch (PersistenciaException e) {
            System.err.println("[IngredienteRepository] Error cargando ingredientes: "
                    + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Optional<Ingrediente> buscarPorId(String id) {
        return cargarIngredientes().stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();
    }

    public Optional<Ingrediente> buscarPorNombre(String nombre) {
        return cargarIngredientes().stream()
                .filter(i -> i.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    // Movimientos

    public void guardarMovimientos(List<MovimientoInventario> movimientos) {
        try {
            manager.guardarMovimientos(movimientos);
        } catch (PersistenciaException e) {
            throw new PersistenciaException("movimientos.json",
                    "No se pudo guardar los movimientos", e);
        }
    }

    public List<MovimientoInventario> cargarMovimientos() {
        try {
            List<MovimientoInventario> lista = manager.cargarMovimientos();
            return lista != null ? lista : new ArrayList<>();
        } catch (PersistenciaException e) {
            System.err.println("[IngredienteRepository] Error cargando movimientos: "
                    + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Alertas

    public void guardarAlertas(List<AlertaStock> alertas) {
        try {
            manager.guardarAlertas(alertas);
        } catch (PersistenciaException e) {
            throw new PersistenciaException("alertas.json",
                    "No se pudo guardar las alertas", e);
        }
    }

    public List<AlertaStock> cargarAlertas() {
        try {
            List<AlertaStock> lista = manager.cargarAlertas();
            return lista != null ? lista : new ArrayList<>();
        } catch (PersistenciaException e) {
            System.err.println("[IngredienteRepository] Error cargando alertas: "
                    + e.getMessage());
            return new ArrayList<>();
        }
    }
}