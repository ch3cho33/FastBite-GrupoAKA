package com.fastbite;

import com.fastbite.controller.CocinaController;
import com.fastbite.controller.InventarioController;
import com.fastbite.controller.PedidoController;
import com.fastbite.exception.PersistenciaException;
import com.fastbite.util.AlertaUtil;
import com.fastbite.util.DatosPrueba;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            DatosPrueba.cargar();
        } catch (Exception e) {
            AlertaUtil.advertencia("Aviso", "Iniciando con datos de ejemplo.");
        }

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-background-color:#0f172a;");

        tabPane.getTabs().addAll(
                crearTab("🧾  Pedidos",    "/com/fastbite/views/pedidos.fxml"),
                crearTab("🍳  Cocina",     "/com/fastbite/views/cocina.fxml"),
                crearTab("📦  Inventario", "/com/fastbite/views/inventario.fxml"),
                crearTab("👤  Clientes",   "/com/fastbite/views/Cliente.fxml"),
                crearTab("🍔  Productos",  "/com/fastbite/views/Productos.fxml")
        );

        primaryStage.setOnCloseRequest(e -> {
            try {
                CocinaController.getInstance().guardarDatos();
                InventarioController.getInstance().guardarDatos();
                PedidoController.getInstance().guardarDatos();
            } catch (PersistenciaException ex) {
                AlertaUtil.error("Error al guardar",
                        "No se pudieron guardar algunos datos: " + ex.getMessage());
            } finally {
                Platform.exit();
            }
        });

        Scene scene = new Scene(tabPane, 1280, 780);
        primaryStage.setTitle("FastBite — Sistema de Gestión");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(650);
        primaryStage.show();
    }

    private Tab crearTab(String nombre, String rutaFxml) {
        Tab tab = new Tab(nombre);
        try {
            tab.setContent(cargarVista(rutaFxml));
        } catch (IOException e) {
            tab.setContent(panelError(nombre, e.getMessage()));
            System.err.println("[MainApp] Error cargando " + rutaFxml + ": " + e.getMessage());
        } catch (Exception e) {
            tab.setContent(panelError(nombre, e.getMessage()));
            System.err.println("[MainApp] Error inesperado " + rutaFxml + ": " + e.getMessage());
        }
        return tab;
    }

    private Pane cargarVista(String ruta) throws IOException {
        return new FXMLLoader(getClass().getResource(ruta)).load();
    }

    private Pane panelError(String modulo, String detalle) {
        StackPane pane = new StackPane();
        pane.setStyle("-fx-background-color:#0f172a;");
        Label label = new Label("No se pudo cargar '" + modulo + "'.\n" + detalle);
        label.setStyle("-fx-text-fill:#ef4444; -fx-font-size:14px;");
        label.setWrapText(true);
        pane.getChildren().add(label);
        return pane;
    }

    public static void main(String[] args) { launch(args); }
}