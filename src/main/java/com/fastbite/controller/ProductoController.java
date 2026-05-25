package com.fastbite.controller;

import com.fastbite.model.Categoria;
import com.fastbite.model.Producto;
import com.fastbite.persistence.ProductoRepository;
import com.fastbite.util.AlertaUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;


 // Boundary: Controlador JavaFX del módulo de Productos.
 // Corregido para persistir en JSON usando ProductoRepository.

public class ProductoController {

    // Repositorio de Persistencia
    private final ProductoRepository repository = new ProductoRepository();

    // FXML
    @FXML private TextField txtNombre;
    @FXML private TextArea txtDescripcion;
    @FXML private TextField txtPrecio;
    @FXML private ComboBox<Categoria> cbCategoria;
    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, Boolean> colEstado;

    private ObservableList<Producto> listaProductos;

    // Inicialización
    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoriaId"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("activo"));

        // Formato booleano en columna Activo
        colEstado.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean val, boolean empty) {
                super.updateItem(val, empty);
                if (empty || val == null) { setText(null); return; }
                setText(val ? "Activo" : "Inactivo");
                setStyle(val ? "-fx-text-fill: #16a34a;" : "-fx-text-fill: #dc2626;");
            }
        });

        cargarCategorias();

        // Cargar productos desde JSON al iniciar
        List<Producto> guardados = repository.cargarTodos();
        listaProductos = FXCollections.observableArrayList(guardados);
        tablaProductos.setItems(listaProductos);
    }

    private void cargarCategorias() {
        cbCategoria.getItems().addAll(
                new Categoria(1, "Hamburguesas"),
                new Categoria(2, "Bebidas"),
                new Categoria(3, "Postres"),
                new Categoria(4, "Acompañamientos")
        );
    }

    // Guardar Producto
    @FXML
    public void guardarProducto() {
        try {
            String nombre      = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            String precioStr   = txtPrecio.getText().trim();
            Categoria categoria = cbCategoria.getValue();

            // Validaciones
            if (nombre.isEmpty()) {
                AlertaUtil.error("Campo requerido", "El nombre del producto es obligatorio.");
                return;
            }
            if (precioStr.isEmpty()) {
                AlertaUtil.error("Campo requerido", "El precio es obligatorio.");
                return;
            }
            if (categoria == null) {
                AlertaUtil.error("Campo requerido", "Selecciona una categoría.");
                return;
            }

            double precio = Double.parseDouble(precioStr);
            if (precio <= 0) {
                AlertaUtil.error("Precio inválido", "El precio debe ser mayor a 0.");
                return;
            }

            Producto producto = new Producto(nombre, descripcion, precio, categoria.getNombre());

            // Agregar a la lista observable y guardar en JSON
            listaProductos.add(producto);
            repository.guardar(listaProductos.stream().toList());

            limpiarCampos();
            AlertaUtil.exito("Producto '" + nombre + "' guardado correctamente.");

        } catch (NumberFormatException e) {
            AlertaUtil.error("Precio inválido", "Ingresa un número válido (ej: 15000).");
        } catch (Exception e) {
            AlertaUtil.errorGenerico(e);
        }
    }

    // Eliminar Producto
    @FXML
    public void eliminarProducto() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            AlertaUtil.advertencia("Sin selección", "Selecciona un producto de la tabla.");
            return;
        }
        boolean confirmar = AlertaUtil.confirmar("Eliminar producto",
                "¿Eliminar '" + seleccionado.getNombre() + "'? Esta acción no se puede deshacer.");
        if (confirmar) {
            listaProductos.remove(seleccionado);
            repository.guardar(listaProductos.stream().toList());
            AlertaUtil.exito("Producto eliminado.");
        }
    }

    // Desactivar Producto
    @FXML
    public void desactivarProducto() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            AlertaUtil.advertencia("Sin selección", "Selecciona un producto de la tabla.");
            return;
        }
        seleccionado.setActivo(false);
        tablaProductos.refresh();
        repository.guardar(listaProductos.stream().toList());
    }

    // Activar producto
    @FXML
    public void activarProducto() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            AlertaUtil.advertencia("Sin selección", "Selecciona un producto de la tabla.");
            return;
        }
        seleccionado.setActivo(true);
        tablaProductos.refresh();
        repository.guardar(listaProductos.stream().toList());
    }

    // útil
    private void limpiarCampos() {
        txtNombre.clear();
        txtDescripcion.clear();
        txtPrecio.clear();
        cbCategoria.setValue(null);
    }
}