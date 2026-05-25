package com.fastbite.controller;

import com.fastbite.model.*;
import com.fastbite.persistence.PersistenciaManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

  // Control: Coordina entradas, salidas y validaciones del inventario.
  // GRASP Controller: caso de uso "Gestionar Inventario".
  // GRASP Low Coupling: la vista InventarioView solo habla con este controller.

public class InventarioController {

    private static InventarioController instancia;

    private Inventario inventario;
    private List<MovimientoInventario> movimientos;
    private List<AlertaStock> alertas;
    private final PersistenciaManager persistencia;

    private InventarioController() {
        this.persistencia = PersistenciaManager.getInstance();
        cargarDatos();
    }

    public static InventarioController getInstance() {
        if (instancia == null) {
            instancia = new InventarioController();
        }
        return instancia;
    }

    // Ingredientes

    // Registrar ingredientes y valida stock
    public void registrarIngrediente(String nombre, double cantidad,
                                     double stockMinimo, String unidad) {
        validarNombre(nombre);
        if (cantidad < 0) throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        if (stockMinimo < 0) throw new IllegalArgumentException("El stock mínimo no puede ser negativo.");
        if (unidad == null || unidad.isBlank()) throw new IllegalArgumentException("La unidad es requerida.");

        Ingrediente ingrediente = new Ingrediente(nombre.trim(), cantidad, stockMinimo, unidad.trim());
        inventario.agregarIngrediente(ingrediente);

        // Registrar movimiento de entrada inicial
        if (cantidad > 0) {
            registrarMovimientoInterno(ingrediente,
                    MovimientoInventario.TipoMovimiento.ENTRADA, cantidad, "REGISTRO INICIAL");
        }

        verificarAlertaStock(ingrediente);
        guardarDatos();
    }

    // Registrar stock de ingrediente ya existente

    public void registrarEntradaStock(String ingredienteId, double cantidad) {
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");

        Ingrediente ingrediente = obtenerIngredientePorId(ingredienteId);
        double stockAntes = ingrediente.getCantidad();

        ingrediente.actualizarStock(cantidad);
        inventario.actualizarIngrediente(ingrediente);

        MovimientoInventario mov = registrarMovimientoInterno(
                ingrediente, MovimientoInventario.TipoMovimiento.ENTRADA, cantidad, "ENTRADA MANUAL");
        mov.setStockDespues(ingrediente.getCantidad());
        mov.setStockAntes(stockAntes);

        // Resolver alertas existentes si el stock subió sobre el mínimo
        if (!ingrediente.stockBajoMinimo()) {
            resolverAlertasDeIngrediente(ingredienteId);
        }

        guardarDatos();
    }

    // Descuenta stock de un ingrediente al confirmar preparación de pedido
    public void descontarStock(String ingredienteId, double cantidad, String referenciaPedido) {
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");

        Ingrediente ingrediente = obtenerIngredientePorId(ingredienteId);
        double stockAntes = ingrediente.getCantidad();

        // Excepción
        ingrediente.descontarStock(cantidad);
        inventario.actualizarIngrediente(ingrediente);

        MovimientoInventario mov = registrarMovimientoInterno(
                ingrediente, MovimientoInventario.TipoMovimiento.SALIDA, cantidad, referenciaPedido);
        mov.setStockAntes(stockAntes);
        mov.setStockDespues(ingrediente.getCantidad());

        verificarAlertaStock(ingrediente);
        guardarDatos();
    }

    // Actualiza datos de un ingrediente existente
    public void actualizarIngrediente(Ingrediente ingredienteActualizado) {
        validarNombre(ingredienteActualizado.getNombre());
        inventario.actualizarIngrediente(ingredienteActualizado);
        verificarAlertaStock(ingredienteActualizado);
        guardarDatos();
    }

    // Consultas

    public List<Ingrediente> obtenerIngredientes() {
        return new ArrayList<>(inventario.getListaIngredientes());
    }

    public Optional<Ingrediente> buscarIngredientePorId(String id) {
        return inventario.buscarPorId(id);
    }

    public Optional<Ingrediente> buscarIngredientePorNombre(String nombre) {
        return inventario.buscarPorNombre(nombre);
    }

    public List<AlertaStock> obtenerAlertasActivas() {
        return alertas.stream()
                .filter(a -> !a.isResuelta())
                .toList();
    }

    public List<MovimientoInventario> obtenerMovimientos() {
        return new ArrayList<>(movimientos);
    }

    public List<Ingrediente> obtenerIngredientesBajoStock() {
        return inventario.obtenerIngredientesBajoStock();
    }

    // Métodos privados

    private Ingrediente obtenerIngredientePorId(String id) {
        return inventario.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Ingrediente no encontrado: " + id));
    }

    private MovimientoInventario registrarMovimientoInterno(Ingrediente ingrediente,
                                                            MovimientoInventario.TipoMovimiento tipo, double cantidad, String referencia) {
        MovimientoInventario mov = new MovimientoInventario(ingrediente, tipo, cantidad, referencia);
        movimientos.add(mov);
        return mov;
    }

    private void verificarAlertaStock(Ingrediente ingrediente) {
        if (ingrediente.stockBajoMinimo()) {
            // Solo genera alerta si no hay una activa para este ingrediente
            boolean yaExiste = alertas.stream()
                    .anyMatch(a -> a.getIngredienteId().equals(ingrediente.getId()) && !a.isResuelta());
            if (!yaExiste) {
                AlertaStock alerta = new AlertaStock(ingrediente);
                alertas.add(alerta);
            }
        }
    }

    private void resolverAlertasDeIngrediente(String ingredienteId) {
        alertas.stream()
                .filter(a -> a.getIngredienteId().equals(ingredienteId) && !a.isResuelta())
                .forEach(AlertaStock::resolver);
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es requerido.");
        }
    }

    // Persistencia

    private void cargarDatos() {
        List<Ingrediente> ingredientes = persistencia.cargarIngredientes();
        inventario = Inventario.getInstance();
        inventario.setListaIngredientes(ingredientes);

        movimientos = persistencia.cargarMovimientos();
        alertas = persistencia.cargarAlertas();
    }

    public void guardarDatos() {
        persistencia.guardarIngredientes(inventario.getListaIngredientes());
        persistencia.guardarMovimientos(movimientos);
        persistencia.guardarAlertas(alertas);
    }
}