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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApp extends Application {

    private static final String TITULO = "FastBite — Sistema de Gestión";
    private static final double ANCHO  = 1200;
    private static final double ALTO   = 750;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar datos de muestra primero (opcional)
        DatosPrueba.cargar();

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Módulo Cocina
        Tab tabCocina = new Tab("🍳 Cocina");
        tabCocina.setContent(cargarVista("/com/fastbite/views/cocina.fxml"));

        // Módulo Inventario
        Tab tabInventario = new Tab("📦 Inventario");
        tabInventario.setContent(cargarVista("/com/fastbite/views/inventario.fxml"));

        // Módulo Fidelización (Cliente)
        Tab tabFidelizacion = new Tab("Fidelización");
        tabFidelizacion.setContent(cargarVista("/com/fastbite/views/Cliente.fxml"));

        // Agregar todas las pestañas
        tabPane.getTabs().addAll(tabCocina, tabInventario, tabFidelizacion);

        Scene scene = new Scene(tabPane, ANCHO, ALTO);
        primaryStage.setTitle(TITULO);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);

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