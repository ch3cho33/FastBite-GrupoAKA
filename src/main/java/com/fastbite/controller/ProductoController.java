package com.fastbite.fastbite.controller;
import com.fastbite.fastbite.model.Categoria;
import com.fastbite.fastbite.model.Producto;
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
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
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
            }
    }
}
