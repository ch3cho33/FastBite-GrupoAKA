package com.fastbite.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


 // Entity: Controla las existencias generales de ingredientes.
 // GRASP Information Expert: conoce la cantidad disponible de cada ingrediente.

public class Inventario {

    private static Inventario instancia;
    private List<Ingrediente> listaIngredientes;

    private Inventario() {
        this.listaIngredientes = new ArrayList<>();
    }

    public static Inventario getInstance() {
        if (instancia == null) {
            instancia = new Inventario();
        }
        return instancia;
    }

    // Reemplaza la instancia (usado al cargar persistencia)

    public static void setInstancia(Inventario inv) {
        instancia = inv;
    }

    public void agregarIngrediente(Ingrediente ingrediente) {
        // Evita duplicados por nombre
        boolean existe = listaIngredientes.stream()
                .anyMatch(i -> i.getNombre().equalsIgnoreCase(ingrediente.getNombre()));
        if (existe) {
            throw new IllegalArgumentException("Ya existe un ingrediente con ese nombre.");
        }
        listaIngredientes.add(ingrediente);
    }

    public void actualizarIngrediente(Ingrediente ingredienteActualizado) {
        for (int i = 0; i < listaIngredientes.size(); i++) {
            if (listaIngredientes.get(i).getId().equals(ingredienteActualizado.getId())) {
                listaIngredientes.set(i, ingredienteActualizado);
                return;
            }
        }
        throw new IllegalArgumentException("Ingrediente no encontrado para actualizar.");
    }

    public Optional<Ingrediente> buscarPorId(String id) {
        return listaIngredientes.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();
    }

    public Optional<Ingrediente> buscarPorNombre(String nombre) {
        return listaIngredientes.stream()
                .filter(i -> i.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }


     //GRASP Information Expert: el Inventario sabe cuáles ingredientes están bajo el mínimo permitido.

    public List<Ingrediente> obtenerIngredientesBajoStock() {
        List<Ingrediente> alertas = new ArrayList<>();
        for (Ingrediente ing : listaIngredientes) {
            if (ing.stockBajoMinimo()) {
                alertas.add(ing);
            }
        }
        return alertas;
    }

    public List<Ingrediente> getListaIngredientes() { return listaIngredientes; }
    public void setListaIngredientes(List<Ingrediente> lista) { this.listaIngredientes = lista; }
}