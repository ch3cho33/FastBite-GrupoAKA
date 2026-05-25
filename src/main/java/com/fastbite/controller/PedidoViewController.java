package com.fastbite.controller;

import com.fastbite.model.*;
import com.fastbite.model.Pago.MetodoPago;
import com.fastbite.persistence.ProductoRepository;
import com.fastbite.util.AlertaUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;


 // Boundary: Controlador JavaFX del módulo de Toma de Pedidos.


public class PedidoViewController implements Initializable {

    // Controllers y Repositorios
    private final PedidoController pedidoCtrl = PedidoController.getInstance();
    private final PagoController pagoCtrl = PagoController.getInstance();
    private final ProductoRepository productoRepo = new ProductoRepository();

    // Estado Interno
    private Pedido pedidoActual;
    private final ObservableList<Producto> menuObs = FXCollections.observableArrayList();
    private final ObservableList<ItemPedido> carritoObs = FXCollections.observableArrayList();

    // FXML - Menú
    @FXML private TableView<Producto> tablaMenu;
    @FXML private TableColumn<Producto, String> colMenuNombre;
    @FXML private TableColumn<Producto, String> colMenuCategoria;
    @FXML private TableColumn<Producto, Double> colMenuPrecio;
    @FXML private TableColumn<Producto, Boolean> colMenuEstado;
    @FXML private ComboBox<String> cmbCategoria;
    @FXML private TextField txtBuscarProducto;
    @FXML private TextField txtCantidad;
    @FXML private Label lblMsgMenu;

    // FXMl - Carrito
    @FXML private TableView<ItemPedido> tablaCarrito;
    @FXML private TableColumn<ItemPedido, String> colItemNombre;
    @FXML private TableColumn<ItemPedido, Integer> colItemCantidad;
    @FXML private TableColumn<ItemPedido, Double> colItemPrecio;
    @FXML private TableColumn<ItemPedido, Double> colItemSubtotal;
    @FXML private ComboBox<String> cmbTipoPedido;
    @FXML private TextField txtObservaciones;
    @FXML private Label lblNumeroPedido;

    // FXML - Totales
    @FXML private Label lblSubtotal;
    @FXML private Label lblIva;
    @FXML private Label lblTotal;

    // FXMl Pago
    @FXML private ComboBox<MetodoPago> cmbMetodoPago;
    @FXML private TextField txtMontoPagado;
    @FXML private Label lblCambio;
    @FXML private Label lblMsgPago;

    // FXML Estado
    @FXML private Label lblHora;
    @FXML private Label lblTotalProductos;
    @FXML private Label lblItemsCarrito;
    @FXML private Label lblEstadoPedido;

    // inicialización
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablaMenu();
        configurarTablaCarrito();
        configurarComboBoxes();
        cargarMenuDesdeRepositorio();
        actualizarHora();
        iniciarNuevoPedido();
    }

    // Configuración

    private void configurarTablaMenu() {
        colMenuNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colMenuCategoria.setCellValueFactory(new PropertyValueFactory<>("categoriaId"));
        colMenuPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colMenuEstado.setCellValueFactory(new PropertyValueFactory<>("activo"));

        colMenuPrecio.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(Double v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : "$" + String.format("%,.0f", v));
                setStyle(empty ? "" : "-fx-text-fill: #f97316; -fx-font-weight: bold;");
            }
        });
        colMenuEstado.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(Boolean v, boolean empty) {
                super.updateItem(v, empty);
                if (empty || v == null) { setText(null); setStyle(""); return; }
                setText(v ? "✓" : "✗");
                setStyle(v ? "-fx-text-fill:#22c55e; -fx-font-weight:bold;"
                        : "-fx-text-fill:#ef4444;");
            }
        });
        tablaMenu.setItems(menuObs);
    }

    private void configurarTablaCarrito() {
        colItemNombre.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colItemCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colItemPrecio.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colItemSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        for (TableColumn<ItemPedido, Double> col : List.of(colItemPrecio, colItemSubtotal)) {
            col.setCellFactory(c -> new TableCell<>() {
                @Override protected void updateItem(Double v, boolean empty) {
                    super.updateItem(v, empty);
                    setText(empty || v == null ? null : "$" + String.format("%,.0f", v));
                }
            });
        }
        tablaCarrito.setItems(carritoObs);
    }

    private void configurarComboBoxes() {
        cmbTipoPedido.setItems(FXCollections.observableArrayList(
                "Mesa", "Llevar", "Domicilio"));
        cmbTipoPedido.getSelectionModel().selectFirst();

        cmbMetodoPago.setItems(FXCollections.observableArrayList(MetodoPago.values()));
        cmbMetodoPago.getSelectionModel().selectFirst();

        cmbMetodoPago.setOnAction(e -> {
            MetodoPago m = cmbMetodoPago.getValue();
            boolean esEfectivo = m == MetodoPago.EFECTIVO;
            txtMontoPagado.setDisable(!esEfectivo);
            if (!esEfectivo && pedidoActual != null)
                txtMontoPagado.setText(String.format("%.0f", pedidoActual.getTotal()));
        });
    }

    private void cargarMenuDesdeRepositorio() {
        List<Producto> productos = productoRepo.cargarActivos();

        // Si no hay productos guardados, carga ejemplos
        if (productos.isEmpty()) {
            productos = List.of(
                    new Producto("Hamburguesa Clásica",  "Carne, queso y vegetales", 18000, "Hamburguesas"),
                    new Producto("Hamburguesa Doble",    "Doble carne y queso",      24000, "Hamburguesas"),
                    new Producto("Papas Fritas",         "Porción grande crujiente",  8000, "Acompañamientos"),
                    new Producto("Gaseosa 350ml",        "Coca-Cola o Pepsi",         5000, "Bebidas"),
                    new Producto("Agua Mineral",         "500ml",                     3000, "Bebidas"),
                    new Producto("Pollo a la Plancha",   "Con ensalada",             15000, "Platos"),
                    new Producto("Combo Familiar",       "2 hamburguesas + papas",   42000, "Combos"),
                    new Producto("Helado de Vainilla",   "Dos bolas",                 7000, "Postres")
            );
            productoRepo.guardar((List<Producto>) productos);
        }

        menuObs.setAll(productos);

        // Poblar categorías
        List<String> cats = menuObs.stream()
                .map(Producto::getCategoriaId)
                .distinct().sorted().toList();
        cmbCategoria.getItems().clear();
        cmbCategoria.getItems().add("Todas");
        cmbCategoria.getItems().addAll(cats);
        cmbCategoria.getSelectionModel().selectFirst();

        lblTotalProductos.setText("Productos en menú: " + menuObs.size());
    }

    // Handlers FXMl

    @FXML
    private void handleNuevoPedido() {
        if (pedidoActual != null && !carritoObs.isEmpty()) {
            boolean ok = AlertaUtil.confirmar("Nuevo pedido",
                    "Hay un pedido en curso con " + carritoObs.size()
                            + " item(s). ¿Descartarlo y empezar uno nuevo?");
            if (!ok) return;
        }
        iniciarNuevoPedido();
    }

    @FXML
    private void handleFiltrarCategoria() {
        filtrarProductos();
    }

    @FXML
    private void handleBuscarProducto() {
        filtrarProductos();
    }

    @FXML
    private void handleSeleccionarProducto() {

    }

    @FXML
    private void handleAgregarItem() {
        Producto producto = tablaMenu.getSelectionModel().getSelectedItem();
        if (producto == null) {
            mostrarMsgMenu("Selecciona un producto de la tabla.", true);
            return;
        }
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidad <= 0) {
                mostrarMsgMenu("La cantidad debe ser mayor a 0.", true);
                return;
            }

            // Verificar si ya existe el producto en el carrito
            for (ItemPedido item : carritoObs) {
                if (item.getProductoId().equals(producto.getId())) {
                    item.setCantidad(item.getCantidad() + cantidad);
                    pedidoActual.calcularTotal();
                    tablaCarrito.refresh();
                    actualizarTotales();
                    mostrarMsgMenu("Cantidad actualizada.", false);
                    return;
                }
            }

            ItemPedido item = new ItemPedido(producto, cantidad);
            pedidoActual.agregarItem(item);
            carritoObs.add(item);
            actualizarTotales();
            txtCantidad.setText("1");
            mostrarMsgMenu("✓ " + producto.getNombre() + " agregado.", false);

        } catch (NumberFormatException e) {
            mostrarMsgMenu("Ingresa una cantidad válida.", true);
        }
    }

    @FXML
    private void handleQuitarItem() {
        ItemPedido item = tablaCarrito.getSelectionModel().getSelectedItem();
        if (item == null) {
            AlertaUtil.advertencia("Sin selección", "Selecciona un ítem del carrito.");
            return;
        }
        pedidoActual.removerItem(item);
        carritoObs.remove(item);
        actualizarTotales();
    }

    @FXML
    private void handleLimpiarPedido() {
        if (carritoObs.isEmpty()) return;
        boolean ok = AlertaUtil.confirmar("Limpiar pedido",
                "¿Quitar todos los productos del pedido actual?");
        if (ok) {
            carritoObs.clear();
            pedidoActual.getItems().clear();
            pedidoActual.calcularTotal();
            actualizarTotales();
        }
    }

    @FXML
    private void handleProcesarPago() {
        if (carritoObs.isEmpty()) {
            AlertaUtil.error("Pedido vacío", "Agrega productos antes de procesar el pago.");
            return;
        }

        MetodoPago metodo = cmbMetodoPago.getValue();
        if (metodo == null) {
            AlertaUtil.error("Método requerido", "Selecciona un método de pago.");
            return;
        }

        try {
            double monto;
            if (metodo == MetodoPago.EFECTIVO) {
                String montoStr = txtMontoPagado.getText().trim();
                if (montoStr.isEmpty()) {
                    AlertaUtil.error("Monto requerido", "Ingresa el monto recibido.");
                    return;
                }
                monto = Double.parseDouble(montoStr);
            } else {
                monto = pedidoActual.getTotal();
            }

            // Actualizar tipo y observaciones
            pedidoActual.setTipoPedido(cmbTipoPedido.getValue());
            pedidoActual.setObservaciones(txtObservaciones.getText().trim());

            Pago pago = pagoCtrl.procesarPago(pedidoActual, metodo, monto);

            // Guardar pedido y enviar a cocina
            pedidoCtrl.crearPedido(pedidoActual);

            // Mostrar cambio y resumen
            lblCambio.setText("$" + String.format("%,.0f", pago.getCambio()));
            String msg = String.format(
                    "✓ Pago procesado | %s | Total: $%,.0f | Cambio: $%,.0f",
                    metodo, pago.getTotalPedido(), pago.getCambio());
            mostrarMsgPago(msg, false);

            AlertaUtil.exito("Pedido " + pedidoActual.getNumeroPedido()
                    + " confirmado y enviado a cocina.");

            iniciarNuevoPedido();

        } catch (NumberFormatException e) {
            mostrarMsgPago("Monto inválido. Ingresa solo números.", true);
        } catch (Exception e) {
            mostrarMsgPago("⚠ " + e.getMessage(), true);
        }
    }

    // útil

    private void iniciarNuevoPedido() {
        pedidoActual = new Pedido("Mesa");
        carritoObs.clear();
        txtObservaciones.clear();
        txtMontoPagado.clear();
        lblCambio.setText("$0");
        lblMsgPago.setText("");
        lblMsgMenu.setText("");
        cmbTipoPedido.getSelectionModel().selectFirst();
        cmbMetodoPago.getSelectionModel().selectFirst();
        lblNumeroPedido.setText(pedidoActual.getNumeroPedido());
        lblEstadoPedido.setText("Pedido en curso");
        actualizarTotales();
    }

    private void filtrarProductos() {
        String categoria = cmbCategoria.getValue();
        String busqueda  = txtBuscarProducto.getText().toLowerCase().trim();
        List<Producto> todos = productoRepo.cargarActivos();
        if (todos.isEmpty()) todos = menuObs; // fallback

        List<Producto> filtrados = todos.stream()
                .filter(p -> (categoria == null || categoria.equals("Todas")
                        || categoria.equals(p.getCategoriaId())))
                .filter(p -> busqueda.isEmpty()
                        || p.getNombre().toLowerCase().contains(busqueda)
                        || (p.getCategoriaId() != null
                        && p.getCategoriaId().toLowerCase().contains(busqueda)))
                .toList();

        menuObs.setAll(filtrados);
    }

    private void actualizarTotales() {
        pedidoActual.calcularTotal();
        lblSubtotal.setText("$" + String.format("%,.0f", pedidoActual.getSubtotal()));
        lblIva.setText("$"       + String.format("%,.0f", pedidoActual.getIva()));
        lblTotal.setText("$"     + String.format("%,.0f", pedidoActual.getTotal()));
        lblItemsCarrito.setText("Items en pedido: " + carritoObs.size());

        // Actualizar monto si no es efectivo
        MetodoPago m = cmbMetodoPago.getValue();
        if (m != null && m != MetodoPago.EFECTIVO)
            txtMontoPagado.setText(String.format("%.0f", pedidoActual.getTotal()));
    }

    private void actualizarHora() {
        lblHora.setText(LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm")));
    }

    private void mostrarMsgMenu(String msg, boolean esError) {
        lblMsgMenu.setText(msg);
        lblMsgMenu.setStyle(esError ? "-fx-text-fill:#ef4444;" : "-fx-text-fill:#22c55e;");
    }

    private void mostrarMsgPago(String msg, boolean esError) {
        lblMsgPago.setText(msg);
        lblMsgPago.setStyle(esError ? "-fx-text-fill:#ef4444;" : "-fx-text-fill:#22c55e;");
    }
}