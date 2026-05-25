package com.fastbite.controller;

import com.fastbite.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


 // Boundary: Controlador JavaFX de la vista de Inventario: inventario.fxml.
 // Solo interactúa con InventarioController, nunca con entidades directamente.
 //  GRASP Low Coupling: la vista no modifica Ingrediente ni Inventario directamente.

public class InventarioViewController implements Initializable {

    // Controller del inventario
    private final InventarioController invController = InventarioController.getInstance();

    // ─FXML Ingredientes
    @FXML private TableView<Ingrediente>               tablaIngredientes;
    @FXML private TableColumn<Ingrediente, String>     colNombre;
    @FXML private TableColumn<Ingrediente, Double>     colCantidad;
    @FXML private TableColumn<Ingrediente, String>     colUnidad;
    @FXML private TableColumn<Ingrediente, Double>     colStockMin;
    @FXML private TableColumn<Ingrediente, String>     colEstadoIng;
    @FXML private TextField                            txtBusqueda;
    @FXML private Button                               btnEliminar;

    // FXMl Formulario
    @FXML private Label                                lblTituloFormulario;
    @FXML private TextField                            txtNombre;
    @FXML private TextField                            txtCantidad;
    @FXML private ComboBox<String>                     cmbUnidad;
    @FXML private TextField                            txtStockMinimo;
    @FXML private TextField                            txtEntradaStock;
    @FXML private Button                               btnAgregarStock;
    @FXML private Button                               btnGuardar;
    @FXML private Label                                lblMensajeForm;

    // FXML Alertas
    @FXML private TableView<AlertaStock>               tablaAlertas;
    @FXML private TableColumn<AlertaStock, String>     colAlertaNivel;
    @FXML private TableColumn<AlertaStock, String>     colAlertaIngrediente;
    @FXML private TableColumn<AlertaStock, String>     colAlertaMensaje;
    @FXML private TableColumn<AlertaStock, String>     colAlertaFecha;
    @FXML private Label                                lblTotalAlertas;

    // FXML Moviemientos
    @FXML private TableView<MovimientoInventario>                       tablaMovimientos;
    @FXML private TableColumn<MovimientoInventario, String>             colMovFecha;
    @FXML private TableColumn<MovimientoInventario, MovimientoInventario.TipoMovimiento> colMovTipo;
    @FXML private TableColumn<MovimientoInventario, String>             colMovIngrediente;
    @FXML private TableColumn<MovimientoInventario, Double>             colMovCantidad;
    @FXML private TableColumn<MovimientoInventario, Double>             colMovStockAntes;
    @FXML private TableColumn<MovimientoInventario, Double>             colMovStockDespues;
    @FXML private TableColumn<MovimientoInventario, String>             colMovReferencia;

    // FXML Barra de estado
    @FXML private Label lblTotalIngredientes;
    @FXML private Label lblBajoStock;
    @FXML private Label lblStatusMsg;

    // Estado Interno
    private Ingrediente ingredienteSeleccionado;
    private boolean modoEdicion = false;
    private final ObservableList<Ingrediente>          ingredientesObs  = FXCollections.observableArrayList();
    private final ObservableList<AlertaStock>          alertasObs       = FXCollections.observableArrayList();
    private final ObservableList<MovimientoInventario> movimientosObs   = FXCollections.observableArrayList();

    // Incializar
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablaIngredientes();
        configurarTablaAlertas();
        configurarTablaMovimientos();
        configurarFormulario();
        configurarListeners();
        actualizarTodo();
    }

    // Configuración Inicial

    private void configurarTablaIngredientes() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colUnidad.setCellValueFactory(new PropertyValueFactory<>("unidad"));
        colStockMin.setCellValueFactory(new PropertyValueFactory<>("stockMinimo"));

        // Color tabla
        colEstadoIng.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String val, boolean empty) {
                super.updateItem(val, empty);
                if (empty) { setText(null); setStyle(""); return; }
                Ingrediente ing = getTableView().getItems().get(getIndex());
                if (ing.stockBajoMinimo()) {
                    setText("⚠ Bajo");
                    setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
                } else {
                    setText("✓ OK");
                    setStyle("-fx-text-fill: #16a34a;");
                }
            }
        });

        // Formato decimal en cantidad y stock mínimo
        colCantidad.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double val, boolean empty) {
                super.updateItem(val, empty);
                setText(empty || val == null ? null : String.format("%.2f", val));
            }
        });
        colStockMin.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double val, boolean empty) {
                super.updateItem(val, empty);
                setText(empty || val == null ? null : String.format("%.2f", val));
            }
        });

        tablaIngredientes.setItems(ingredientesObs);
    }

    private void configurarTablaAlertas() {
        colAlertaNivel.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getNivel().getDescripcion()));
        colAlertaIngrediente.setCellValueFactory(new PropertyValueFactory<>("nombreIngrediente"));
        colAlertaMensaje.setCellValueFactory(new PropertyValueFactory<>("mensaje"));
        colAlertaFecha.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String v, boolean empty) {
                super.updateItem(v, empty);
                if (empty) { setText(null); return; }
                setText(getTableView().getItems().get(getIndex()).getFechaFormateada());
            }
        });

        // Color según nivel de alerta
        colAlertaNivel.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String nivel, boolean empty) {
                super.updateItem(nivel, empty);
                if (empty || nivel == null) { setText(null); setStyle(""); return; }
                setText(nivel);
                setStyle(switch (nivel) {
                    case "Crítico"     -> "-fx-text-fill: #dc2626; -fx-font-weight: bold;";
                    case "Bajo"        -> "-fx-text-fill: #d97706; -fx-font-weight: bold;";
                    default            -> "-fx-text-fill: #2563eb;";
                });
            }
        });

        tablaAlertas.setItems(alertasObs);
    }

    private void configurarTablaMovimientos() {
        colMovFecha.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String v, boolean empty) {
                super.updateItem(v, empty);
                if (empty) { setText(null); return; }
                setText(getTableView().getItems().get(getIndex()).getFechaFormateada());
            }
        });
        colMovTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colMovIngrediente.setCellValueFactory(new PropertyValueFactory<>("nombreIngrediente"));
        colMovCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colMovStockAntes.setCellValueFactory(new PropertyValueFactory<>("stockAntes"));
        colMovStockDespues.setCellValueFactory(new PropertyValueFactory<>("stockDespues"));
        colMovReferencia.setCellValueFactory(new PropertyValueFactory<>("referencia"));

        colMovTipo.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(MovimientoInventario.TipoMovimiento tipo, boolean empty) {
                super.updateItem(tipo, empty);
                if (empty || tipo == null) { setText(null); setStyle(""); return; }
                setText(tipo.getDescripcion());
                setStyle(tipo == MovimientoInventario.TipoMovimiento.ENTRADA
                        ? "-fx-text-fill: #16a34a;"
                        : "-fx-text-fill: #dc2626;");
            }
        });

        tablaMovimientos.setItems(movimientosObs);
    }

    private void configurarFormulario() {
        cmbUnidad.setItems(FXCollections.observableArrayList(
                "kg", "g", "litros", "ml", "unidades", "porciones", "cajas", "bolsas"));
    }

    private void configurarListeners() {
        tablaIngredientes.getSelectionModel().selectedItemProperty().addListener(
                (obs, ant, nuevo) -> {
                    ingredienteSeleccionado = nuevo;
                    if (nuevo != null) cargarIngredienteEnFormulario(nuevo);
                    btnEliminar.setDisable(nuevo == null);
                    btnAgregarStock.setDisable(nuevo == null);
                });
    }

    // handlers FXML

    @FXML
    private void handleActualizar() {
        actualizarTodo();
        mostrarMensajeStatus("Lista actualizada.", false);
    }

    @FXML
    private void handleBuscar() {
        String texto = txtBusqueda.getText().toLowerCase().trim();
        if (texto.isEmpty()) {
            ingredientesObs.setAll(invController.obtenerIngredientes());
        } else {
            ingredientesObs.setAll(
                    invController.obtenerIngredientes().stream()
                            .filter(i -> i.getNombre().toLowerCase().contains(texto))
                            .toList()
            );
        }
    }

    @FXML
    private void handleSeleccionarIngrediente() {

    }

    @FXML
    private void handleNuevoIngrediente() {
        limpiarFormulario();
        modoEdicion = false;
        lblTituloFormulario.setText("Registrar Ingrediente");
        tablaIngredientes.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleGuardarIngrediente() {
        try {
            String nombre     = txtNombre.getText().trim();
            String cantStr    = txtCantidad.getText().trim();
            String minStr     = txtStockMinimo.getText().trim();
            String unidad     = cmbUnidad.getValue();

            if (nombre.isEmpty() || cantStr.isEmpty() || minStr.isEmpty() || unidad == null) {
                mostrarMensajeForm("⚠ Completa todos los campos obligatorios.", true);
                return;
            }

            double cantidad   = Double.parseDouble(cantStr);
            double stockMin   = Double.parseDouble(minStr);

            if (modoEdicion && ingredienteSeleccionado != null) {
                ingredienteSeleccionado.setNombre(nombre);
                ingredienteSeleccionado.setCantidad(cantidad);
                ingredienteSeleccionado.setStockMinimo(stockMin);
                ingredienteSeleccionado.setUnidad(unidad);
                invController.actualizarIngrediente(ingredienteSeleccionado);
                mostrarMensajeForm("✓ Ingrediente actualizado.", false);
            } else {
                invController.registrarIngrediente(nombre, cantidad, stockMin, unidad);
                mostrarMensajeForm("✓ Ingrediente registrado.", false);
            }

            limpiarFormulario();
            actualizarTodo();

        } catch (NumberFormatException e) {
            mostrarMensajeForm("⚠ Cantidad y stock mínimo deben ser números.", true);
        } catch (Exception e) {
            mostrarMensajeForm("⚠ " + e.getMessage(), true);
        }
    }

    @FXML
    private void handleRegistrarEntrada() {
        if (ingredienteSeleccionado == null) return;
        try {
            String cantStr = txtEntradaStock.getText().trim();
            if (cantStr.isEmpty()) {
                mostrarMensajeForm("⚠ Ingresa la cantidad a agregar.", true);
                return;
            }
            double cantidad = Double.parseDouble(cantStr);
            invController.registrarEntradaStock(ingredienteSeleccionado.getId(), cantidad);
            txtEntradaStock.clear();
            mostrarMensajeForm("✓ Stock actualizado correctamente.", false);
            actualizarTodo();
        } catch (NumberFormatException e) {
            mostrarMensajeForm("⚠ Ingresa un número válido.", true);
        } catch (Exception e) {
            mostrarMensajeForm("⚠ " + e.getMessage(), true);
        }
    }

    @FXML
    private void handleEliminarIngrediente() {
        if (ingredienteSeleccionado == null) return;
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Eliminar el ingrediente '" + ingredienteSeleccionado.getNombre() + "'?",
                ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Confirmar eliminación");
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                // Se delega al controller; aquí simplemente removemos de la lista del inventario
                Inventario.getInstance().getListaIngredientes().remove(ingredienteSeleccionado);
                invController.guardarDatos();
                limpiarFormulario();
                actualizarTodo();
                mostrarMensajeStatus("Ingrediente eliminado.", false);
            }
        });
    }

    @FXML
    private void handleResolverAlerta() {
        AlertaStock alerta = tablaAlertas.getSelectionModel().getSelectedItem();
        if (alerta == null) return;
        alerta.resolver();
        invController.guardarDatos();
        actualizarAlertas();
        mostrarMensajeStatus("Alerta resuelta.", false);
    }

    @FXML
    private void handleLimpiarFormulario() {
        limpiarFormulario();
    }

    // Actulización de Vista

    private void actualizarTodo() {
        actualizarIngredientes();
        actualizarAlertas();
        actualizarMovimientos();
        actualizarStatusBar();
    }

    private void actualizarIngredientes() {
        ingredientesObs.setAll(invController.obtenerIngredientes());
    }

    private void actualizarAlertas() {
        List<AlertaStock> activas = invController.obtenerAlertasActivas();
        alertasObs.setAll(activas);
        lblTotalAlertas.setText(activas.size() + " alerta" + (activas.size() == 1 ? "" : "s"));
        lblTotalAlertas.setStyle(activas.isEmpty()
                ? "-fx-text-fill: #16a34a;"
                : "-fx-text-fill: #dc2626; -fx-font-weight: bold;");
    }

    private void actualizarMovimientos() {
        movimientosObs.setAll(invController.obtenerMovimientos());
    }

    private void actualizarStatusBar() {
        int total      = invController.obtenerIngredientes().size();
        int bajoStock  = invController.obtenerIngredientesBajoStock().size();
        lblTotalIngredientes.setText("Total ingredientes: " + total);
        lblBajoStock.setText("Bajo stock: " + bajoStock);
        lblBajoStock.setStyle(bajoStock > 0
                ? "-fx-text-fill: #dc2626; -fx-font-weight: bold;"
                : "-fx-text-fill: #64748b;");
    }

    private void cargarIngredienteEnFormulario(Ingrediente ing) {
        modoEdicion = true;
        lblTituloFormulario.setText("Editar Ingrediente");
        txtNombre.setText(ing.getNombre());
        txtCantidad.setText(String.valueOf(ing.getCantidad()));
        txtStockMinimo.setText(String.valueOf(ing.getStockMinimo()));
        cmbUnidad.setValue(ing.getUnidad());
        txtEntradaStock.clear();
        lblMensajeForm.setText("");
    }

    private void limpiarFormulario() {
        modoEdicion = false;
        ingredienteSeleccionado = null;
        lblTituloFormulario.setText("Registrar Ingrediente");
        txtNombre.clear();
        txtCantidad.clear();
        txtStockMinimo.clear();
        cmbUnidad.setValue(null);
        txtEntradaStock.clear();
        lblMensajeForm.setText("");
        btnEliminar.setDisable(true);
        btnAgregarStock.setDisable(true);
    }

    private void mostrarMensajeForm(String msg, boolean esError) {
        lblMensajeForm.setText(msg);
        lblMensajeForm.setStyle(esError ? "-fx-text-fill: #dc2626;" : "-fx-text-fill: #16a34a;");
    }

    private void mostrarMensajeStatus(String msg, boolean esError) {
        lblStatusMsg.setText(msg);
        lblStatusMsg.setStyle(esError ? "-fx-text-fill: #dc2626;" : "-fx-text-fill: #16a34a;");
    }
}