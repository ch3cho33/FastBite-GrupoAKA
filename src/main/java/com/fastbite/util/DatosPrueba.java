package com.fastbite.util;

import com.fastbite.controller.CocinaController;
import com.fastbite.controller.InventarioController;
import com.fastbite.model.*;

/**
 * Utilidad para cargar datos de prueba en desarrollo.
 * Llama a DatosPrueba.cargar() desde MainApp si los archivos están vacíos.
 */
public class DatosPrueba {

    public static void cargar() {
        cargarInventario();
        cargarPedidos();
    }

    private static void cargarInventario() {
        InventarioController inv = InventarioController.getInstance();

        // Solo carga si no hay ingredientes
        if (!inv.obtenerIngredientes().isEmpty()) return;

        try {
            inv.registrarIngrediente("Harina de trigo",  50.0, 10.0, "kg");
            inv.registrarIngrediente("Aceite de cocina", 20.0,  5.0, "litros");
            inv.registrarIngrediente("Sal",              10.0,  2.0, "kg");
            inv.registrarIngrediente("Tomate",           30.0,  8.0, "kg");
            inv.registrarIngrediente("Carne molida",     15.0,  5.0, "kg");
            inv.registrarIngrediente("Pan hamburguesa",  40.0, 10.0, "unidades");
            inv.registrarIngrediente("Lechuga",           5.0,  3.0, "kg");
            inv.registrarIngrediente("Queso",             4.0,  5.0, "kg"); // bajo mínimo
            inv.registrarIngrediente("Papas",            25.0,  8.0, "kg");
            inv.registrarIngrediente("Pollo",             2.0,  6.0, "kg"); // bajo mínimo
        } catch (Exception e) {
            System.err.println("Error cargando inventario de prueba: " + e.getMessage());
        }
    }

    private static void cargarPedidos() {
        CocinaController cocina = CocinaController.getInstance();

        // Solo carga si no hay pedidos
        if (!cocina.obtenerTodosLosPedidos().isEmpty()) return;

        try {
            // Pedido 1 – Pendiente
            Pedido p1 = new Pedido("Mesa");
            Producto hamburguesa = new Producto("Hamburguesa Clásica", "", 18000, "cat-1");
            Producto papas       = new Producto("Papas Fritas",         "", 8000, "cat-1");
            p1.agregarItem(new ItemPedido(hamburguesa, 2));
            p1.agregarItem(new ItemPedido(papas, 1));
            p1.setObservaciones("Sin cebolla en la hamburguesa.");
            cocina.recibirPedido(p1);

            // Pedido 2 – En preparación
            Pedido p2 = new Pedido("Llevar");
            Producto pollo = new Producto("Pechuga a la Plancha", "", 15000, "cat-2");
            p2.agregarItem(new ItemPedido(pollo, 1));
            cocina.recibirPedido(p2);
            cocina.iniciarPreparacion(p2.getId());

            // Pedido 3 – Pendiente
            Pedido p3 = new Pedido("Domicilio");
            Producto combo = new Producto("Combo Familiar", "", 45000, "cat-3");
            p3.agregarItem(new ItemPedido(combo, 1));
            p3.setObservaciones("Entregar antes de las 7pm.");
            cocina.recibirPedido(p3);

        } catch (Exception e) {
            System.err.println("Error cargando pedidos de prueba: " + e.getMessage());
        }
    }
}