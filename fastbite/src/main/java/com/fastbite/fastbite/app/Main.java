package com.fastbite.fastbite.app;

import com.fastbite.fastbite.controller.PedidoController;
import com.fastbite.fastbite.model.EstadoPedido;
import com.fastbite.fastbite.model.Pedido;
import com.fastbite.fastbite.model.Producto;

import java.util.ArrayList;

/**
 * Clase temporal para probar el modelo, controlador y persistencia.
 * Eliminar cuando se conecte la Vista JavaFX.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("====== FASTBITE - PRUEBA DEL SISTEMA ======\n");

        // -------------------------------------------------------
        // 1. Crear el controlador
        //    Al crearlo, carga automáticamente pedidos.json si existe
        // -------------------------------------------------------
        PedidoController controller = new PedidoController();

        // -------------------------------------------------------
        // 2. Crear productos del menú
        // -------------------------------------------------------
        Producto hamburguesa = new Producto(1, "Hamburguesa Clásica", "Carne, queso y vegetales", 18000, "Hamburguesas");
        Producto papas       = new Producto(2, "Papas Fritas",        "Porción grande crujiente", 7500,  "Acompañamientos");
        Producto gaseosa     = new Producto(3, "Gaseosa 350ml",       "Coca-Cola o Pepsi",        4000,  "Bebidas");

        // -------------------------------------------------------
        // 3. Crear pedidos
        // -------------------------------------------------------
        System.out.println("--- Creando pedidos ---");
        Pedido pedido1 = controller.crearPedido("Carlos Gómez");
        Pedido pedido2 = controller.crearPedido("Ana Martínez");

        // -------------------------------------------------------
        // 4. Agregar productos a los pedidos
        // -------------------------------------------------------
        System.out.println("\n--- Agregando productos ---");
        controller.agregarProducto(pedido1.getNumeroPedido(), hamburguesa, 2);
        controller.agregarProducto(pedido1.getNumeroPedido(), papas, 1);
        controller.agregarProducto(pedido1.getNumeroPedido(), gaseosa, 2);

        controller.agregarProducto(pedido2.getNumeroPedido(), hamburguesa, 1);
        controller.agregarProducto(pedido2.getNumeroPedido(), gaseosa, 1);

        // -------------------------------------------------------
        // 5. Cambiar estado de un pedido
        // -------------------------------------------------------
        System.out.println("\n--- Cambiando estados ---");
        controller.cambiarEstadoPedido(pedido1.getNumeroPedido(), EstadoPedido.EN_PREPARACION);
        controller.cambiarEstadoPedido(pedido2.getNumeroPedido(), EstadoPedido.LISTO);

        // -------------------------------------------------------
        // 6. Imprimir pedidos en consola
        // -------------------------------------------------------
        System.out.println("\n--- Pedidos actuales ---");
        ArrayList<Pedido> pedidos = controller.obtenerPedidos();
        for (Pedido p : pedidos) {
            System.out.println(p);
            System.out.println();
        }

        // -------------------------------------------------------
        // 7. Guardar pedidos en pedidos.json
        // -------------------------------------------------------
        System.out.println("--- Guardando en JSON ---");
        controller.guardarPedidos();

        // -------------------------------------------------------
        // 8. Crear un nuevo controlador y cargar desde JSON
        //    Simula cerrar y volver a abrir la app
        // -------------------------------------------------------
        System.out.println("\n--- Simulando reinicio de la app ---");
        PedidoController controllerNuevo = new PedidoController();

        System.out.println("\n--- Pedidos cargados desde JSON ---");
        ArrayList<Pedido> pedidosCargados = controllerNuevo.obtenerPedidos();

        if (pedidosCargados.isEmpty()) {
            System.out.println("No se encontraron pedidos guardados.");
        } else {
            for (Pedido p : pedidosCargados) {
                System.out.println("Pedido #" + p.getNumeroPedido()
                        + " | Cliente: " + p.getNombreCliente()
                        + " | Estado: "  + p.getEstado()
                        + " | Total: $"  + String.format("%,.2f", p.calcularTotal()));
            }
        }

        System.out.println("\n====== FIN DE LA PRUEBA ======");
    }
}