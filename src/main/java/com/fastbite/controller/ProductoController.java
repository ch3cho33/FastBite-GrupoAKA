package com.fastbite.controller;
import com.fastbite.model.Categoria;
import com.fastbite.model.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProductoController {
    @FXML
    private TextField txtNombre;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private TextField txtPrecio;
    @FXML
    private ComboBox<Categoria> cbCategoria;
    @FXML
    private TableView<Producto> tablaProductos;
    @FXML
    private TableColumn<Producto, String> colNombre;
    @FXML
    private TableColumn<Producto, Double> colPrecio;
    @FXML
    private TableColumn<Producto, String> colCategoria;
    @FXML
    private TableColumn<Producto, Boolean> colEstado;
    private ObservableList<Producto> listaProductos;
    @FXML
    
    public void initialize() {
        listaProductos = FXCollections.observableArrayList();
        tablaProductos.setItems(listaProductos);
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoriaId"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("activo"));
        cargarCategorias();
    }

    private void cargarCategorias() {
        cbCategoria.getItems().addAll(
                new Categoria(1, "Hamburguesas"),
                new Categoria(2, "Bebidas"),
                new Categoria(3, "Postres"),
                new Categoria(4, "Acompañamientos")
        );
    }

    @FXML
    public void guardarProducto() {
        try {
            String nombre = txtNombre.getText();
            String descripcion = txtDescripcion.getText();
            double precio = Double.parseDouble(txtPrecio.getText());
            Categoria categoria = cbCategoria.getValue();
            if (nombre.isEmpty() || categoria == null) {
                mostrarError("Complete todos los campos");
                return;
            }
            Producto producto = new Producto();
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(precio);
            producto.setCategoriaId(categoria.getNombre());
            producto.setActivo(true);
            listaProductos.add(producto);
            limpiarCampos();
        } catch (NumberFormatException e) {
            mostrarError("Precio inválido");
        }
    }
    
    @FXML
    public void eliminarProducto() {
        Producto productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            listaProductos.remove(productoSeleccionado);
        } else {
            mostrarError("Seleccione un producto");
        }
    }

    @FXML
    public void desactivarProducto() {
        Producto productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            productoSeleccionado.setActivo(false);
            tablaProductos.refresh();
        } else {
            mostrarError("Seleccione un producto");
        }
    }

    @FXML
    public void activarProducto() {
        Producto productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            productoSeleccionado.setActivo(true);
            tablaProductos.refresh();
        } else {
            mostrarError("Seleccione un producto");
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtDescripcion.clear();
        txtPrecio.clear();
        cbCategoria.setValue(null);
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
