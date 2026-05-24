package com.fastbite;

import com.fastbite.controller.CocinaController;
import com.fastbite.controller.InventarioController;
import com.fastbite.controller.PedidoController;
import com.fastbite.util.DatosPrueba;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        DatosPrueba.cargar();

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // --- Tab Cocina (tu parte) ---
        Tab tabCocina = new Tab("Cocina");
        tabCocina.setContent(cargarVista("/com/fastbite/views/cocina.fxml"));

        // --- Tab Inventario (tu parte) ---
        Tab tabInventario = new Tab("Inventario");
        tabInventario.setContent(cargarVista("/com/fastbite/views/inventario.fxml"));

        // --- Tab Clientes (compañeros) ---
        Tab tabClientes = new Tab("Clientes");
        try {
            tabClientes.setContent(cargarVista("/com/fastbite/views/Cliente.fxml"));
        } catch (Exception e) {
            System.err.println("No se pudo cargar Cliente.fxml: " + e.getMessage());
        }

        // --- Tab Productos (compañeros) ---
        Tab tabProductos = new Tab("Productos");
        try {
            tabProductos.setContent(cargarVista("/com/fastbite/views/Productos.fxml"));
        } catch (Exception e) {
            System.err.println("No se pudo cargar Productos.fxml: " + e.getMessage());
        }

        tabPane.getTabs().addAll(tabCocina, tabInventario, tabClientes, tabProductos);

        Scene scene = new Scene(tabPane, 1200, 750);
        primaryStage.setTitle("FastBite - Sistema de Gestion");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);

        primaryStage.setOnCloseRequest(e -> {
            CocinaController.getInstance().guardarDatos();
            InventarioController.getInstance().guardarDatos();
            PedidoController.getInstance().guardarDatos();
            Platform.exit();
        });

        primaryStage.show();
    }

    private Pane cargarVista(String ruta) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
        return loader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }
}