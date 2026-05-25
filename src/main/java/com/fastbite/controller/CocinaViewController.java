package com.fastbite.controller;

import com.fastbite.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

  // Controlador JavaFX de la vista de Cocina (fxml)
  // Solo interactúa con CocinaController, nunca con entidades directamente.
  // GRASP Low Coupling: la vista no modifica Pedido, pide acción al controller.

public class CocinaViewController implements Initializable {

    // Controller del negocio
    private final CocinaController cocinaController = CocinaController.getInstance();

    // FXMl Paneles
    @FXML private ListView<Pedido> listaPedidos;
    @FXML private ComboBox<String> cmbFiltroEstado;
    @FXML private Button btnIniciarPreparacion;
    @FXML private Button btnMarcarListo;

    @FXML private GridPane gridDetalle;
    @FXML private Label lblNumeroPedido;
    @FXML private Label lblTipoPedido;
    @FXML private Label lblEstadoPedido;
    @FXML private Label lblHoraPedido;
    @FXML private Label lblTotalPedido;
    @FXML private Label lblObservaciones;

    @FXML private TableView<ItemPedido> tablaItems;
    @FXML private TableColumn<ItemPedido, String> colProducto;
    @FXML private TableColumn<ItemPedido, Integer> colCantidad;
    @FXML private TableColumn<ItemPedido, Double> colSubtotal;
    @FXML private TableColumn<ItemPedido, EstadoPedido> colEstadoItem;
    @FXML private Button btnMarcarItemListo;

    // Barra de estado
    @FXML private Label lblPendientes;
    @FXML private Label lblEnPreparacion;
    @FXML private Label lblMensaje;
    @FXML private Label lblFecha;

    // Estado Interno
    private Pedido pedidoSeleccionado;
    private final ObservableList<Pedido> pedidosObservable = FXCollections.observableArrayList();
    private final ObservableList<ItemPedido> itemsObservable = FXCollections.observableArrayList();

    // Inicializar
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        configurarFiltro();
        configurarListeners();
        actualizarVista();
        actualizarFecha();
    }

    // Configuración Inicial
    private void configurarColumnas() {
        colProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        colEstadoItem.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Formato de moneda en subtotal
        colSubtotal.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double val, boolean empty) {
                super.updateItem(val, empty);
                setText(empty || val == null ? null : "$" + String.format("%.0f", val));
            }
        });

        // Color según estado del ítem
        colEstadoItem.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(EstadoPedido estado, boolean empty) {
                super.updateItem(estado, empty);
                if (empty || estado == null) { setText(null); setStyle(""); return; }
                setText(estado.getDescripcion());
                setStyle(switch (estado) {
                    case LISTO          -> "-fx-text-fill: #16a34a; -fx-font-weight: bold;";
                    case EN_PREPARACION -> "-fx-text-fill: #d97706; -fx-font-weight: bold;";
                    default             -> "-fx-text-fill: #64748b;";
                });
            }
        });

        tablaItems.setItems(itemsObservable);
        listaPedidos.setItems(pedidosObservable);

        // Renderer de celdas en la lista de pedidos
        listaPedidos.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Pedido p, boolean empty) {
                super.updateItem(p, empty);
                if (empty || p == null) { setText(null); setStyle(""); return; }
                setText(p.getNumeroPedido() + "  •  " + p.getTipoPedido()
                        + "  •  " + p.getEstado().getDescripcion()
                        + "  •  " + p.getFechaCreacionFormateada());
                setStyle(switch (p.getEstado()) {
                    case EN_PREPARACION -> "-fx-background-color: #fef3c7;";
                    case LISTO          -> "-fx-background-color: #dcfce7;";
                    default             -> "";
                });
            }
        });
    }

    private void configurarFiltro() {
        cmbFiltroEstado.setItems(FXCollections.observableArrayList(
                "Todos los activos", "Pendiente", "En preparación", "Listo"));
        cmbFiltroEstado.getSelectionModel().selectFirst();
    }

    private void configurarListeners() {
        listaPedidos.getSelectionModel().selectedItemProperty().addListener(
                (obs, ant, nuevo) -> mostrarDetallePedido(nuevo));

        tablaItems.getSelectionModel().selectedItemProperty().addListener(
                (obs, ant, nuevo) -> btnMarcarItemListo.setDisable(nuevo == null));
    }

    // Handlers FXMl

    @FXML
    private void handleActualizar() {
        actualizarVista();
        mostrarMensaje("Lista actualizada.");
    }

    @FXML
    private void handleFiltrarEstado() {
        actualizarVista();
    }

    @FXML
    private void handleSeleccionarPedido() {
        pedidoSeleccionado = listaPedidos.getSelectionModel().getSelectedItem();
        mostrarDetallePedido(pedidoSeleccionado);
    }

    @FXML
    private void handleIniciarPreparacion() {
        if (pedidoSeleccionado == null) return;
        try {
            cocinaController.iniciarPreparacion(pedidoSeleccionado.getId());
            mostrarMensaje("✓ Pedido " + pedidoSeleccionado.getNumeroPedido() + " en preparación.");
            actualizarVista();
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void handleMarcarListo() {
        if (pedidoSeleccionado == null) return;
        try {
            cocinaController.marcarPedidoListo(pedidoSeleccionado.getId());
            mostrarMensaje("🎉 Pedido " + pedidoSeleccionado.getNumeroPedido() + " LISTO para entregar.");
            actualizarVista();
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void handleMarcarItemListo() {
        if (pedidoSeleccionado == null) return;
        ItemPedido item = tablaItems.getSelectionModel().getSelectedItem();
        if (item == null) return;
        try {
            cocinaController.marcarItemListo(pedidoSeleccionado.getId(), item.getId());
            mostrarDetallePedido(pedidoSeleccionado);
            mostrarMensaje("Ítem '" + item.getNombreProducto() + "' marcado como listo.");
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    // Actualización Vista

    private void actualizarVista() {
        String filtro = cmbFiltroEstado.getValue();
        var pedidos = switch (filtro == null ? "Todos los activos" : filtro) {
            case "Pendiente"       -> cocinaController.obtenerPedidosPendientes();
            case "En preparación"  -> cocinaController.obtenerPedidosEnPreparacion();
            default                -> cocinaController.obtenerPedidosActivos();
        };

        pedidosObservable.setAll(pedidos);

        long pendientes    = pedidos.stream().filter(p -> p.getEstado() == EstadoPedido.PENDIENTE).count();
        long enPreparacion = pedidos.stream().filter(p -> p.getEstado() == EstadoPedido.EN_PREPARACION).count();
        lblPendientes.setText("Pendientes: " + pendientes);
        lblEnPreparacion.setText("En preparación: " + enPreparacion);
    }

    private void mostrarDetallePedido(Pedido pedido) {
        pedidoSeleccionado = pedido;
        if (pedido == null) {
            gridDetalle.setVisible(false);
            itemsObservable.clear();
            btnIniciarPreparacion.setDisable(true);
            btnMarcarListo.setDisable(true);
            return;
        }

        gridDetalle.setVisible(true);
        lblNumeroPedido.setText(pedido.getNumeroPedido());
        lblTipoPedido.setText(pedido.getTipoPedido() != null ? pedido.getTipoPedido() : "-");
        lblEstadoPedido.setText(pedido.getEstado().getDescripcion());
        lblHoraPedido.setText(pedido.getFechaCreacionFormateada());
        lblTotalPedido.setText("$" + String.format("%.0f", pedido.getTotal()));
        lblObservaciones.setText(pedido.getObservaciones() != null ? pedido.getObservaciones() : "—");

        itemsObservable.setAll(pedido.getItems());

        // Habilitar botones según estado
        btnIniciarPreparacion.setDisable(pedido.getEstado() != EstadoPedido.PENDIENTE);
        btnMarcarListo.setDisable(pedido.getEstado() != EstadoPedido.EN_PREPARACION);
    }

    private void actualizarFecha() {
        String fecha = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        lblFecha.setText(fecha);
    }

    private void mostrarMensaje(String msg) {
        lblMensaje.setStyle("-fx-text-fill: #16a34a;");
        lblMensaje.setText(msg);
    }

    private void mostrarError(String msg) {
        lblMensaje.setStyle("-fx-text-fill: #dc2626;");
        lblMensaje.setText("⚠ " + msg);
    }
}