package com.fastbite;

import com.fastbite.controller.CocinaController;
import com.fastbite.controller.InventarioController;
import com.fastbite.util.DatosPrueba;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Punto de entrada de la aplicación FastBite.
 * Carga la ventana principal con navegación por tabs.
 */
public class MainApp extends Application {

    private static final String TITULO = "FastBite — Sistema de Gestión";
    private static final double ANCHO  = 1200;
    private static final double ALTO   = 750;

    @Override
    public void start(Stage primaryStage) throws Exception {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // ── Módulo de Cocina ──────────────────────────────────────
        Tab tabCocina = new Tab("🍳 Cocina");
        tabCocina.setContent(cargarVista("/com/fastbite/views/cocina.fxml"));

        // ── Módulo de Inventario ──────────────────────────────────
        Tab tabInventario = new Tab("📦 Inventario");
        tabInventario.setContent(cargarVista("/com/fastbite/views/inventario.fxml"));

        tabPane.getTabs().addAll(tabCocina, tabInventario);

        // Carga datos de muestra si no existen archivos persistidos
        DatosPrueba.cargar();

        Scene scene = new Scene(tabPane, ANCHO, ALTO);
        primaryStage.setTitle(TITULO);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);

        // Guardar datos al cerrar la aplicación
        primaryStage.setOnCloseRequest(e -> {
            CocinaController.getInstance().guardarDatos();
            InventarioController.getInstance().guardarDatos();
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
