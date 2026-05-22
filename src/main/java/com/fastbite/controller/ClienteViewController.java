package com.fastbite.controller;

import com.fastbite.model.Cliente;
import com.fastbite.model.MovimientoPuntos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

// Esta clase conecta la pantalla (FXML) con la lógica (ClienteController)
public class ClienteViewController {

    // Controlador principal
    private ClienteController controlador = new ClienteController();

    // ── Campos REGISTRO ────────────────────────────────────────────
    @FXML private TextField campoNombre;
    @FXML private TextField campoApellido;
    @FXML private TextField campoEmail;
    @FXML private TextField campoTelefono;
    @FXML private Label etiquetaMensajeRegistro;

    // ── Campos PUNTOS ──────────────────────────────────────────────
    @FXML private TextField campoIdPuntos;
    @FXML private TextField campoCantidadPuntos;
    @FXML private TextField campoDescripcionPuntos;
    @FXML private Label etiquetaMensajePuntos;

    // ── Tabla clientes ─────────────────────────────────────────────
    @FXML private TableView<Cliente> tablaClientes;

    @FXML private TableColumn<Cliente, Integer> columnaId;
    @FXML private TableColumn<Cliente, String> columnaNombre;
    @FXML private TableColumn<Cliente, String> columnaEmail;
    @FXML private TableColumn<Cliente, String> columnaTelefono;
    @FXML private TableColumn<Cliente, Integer> columnaPuntos;
    @FXML private TableColumn<Cliente, String> columnaCategoria;

    // ── Tabla historial ────────────────────────────────────────────
    @FXML private TableView<MovimientoPuntos> tablaHistorial;

    @FXML private TableColumn<MovimientoPuntos, String> columnaHTipo;
    @FXML private TableColumn<MovimientoPuntos, Integer> columnaHCantidad;
    @FXML private TableColumn<MovimientoPuntos, String> columnaHDescripcion;
    @FXML private TableColumn<MovimientoPuntos, String> columnaHFecha;

    // ── Consulta ───────────────────────────────────────────────────
    @FXML private TextField campoIdConsulta;
    @FXML private Label etiquetaResultadoConsulta;

    // ── Inicializar ────────────────────────────────────────────────
    @FXML
    public void initialize() {

        // Tabla clientes
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        columnaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnaTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        columnaPuntos.setCellValueFactory(new PropertyValueFactory<>("puntos"));
        columnaCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        // Tabla historial
        columnaHTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        columnaHCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        columnaHDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaHFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        actualizarTablaClientes();
        actualizarTablaHistorial();
    }

    // ── Registrar cliente ──────────────────────────────────────────
    @FXML
    public void onClickRegistrar(ActionEvent evento) {

        String nombre = campoNombre.getText().trim();
        String apellido = campoApellido.getText().trim();
        String email = campoEmail.getText().trim();
        String telefono = campoTelefono.getText().trim();

        String resultado =
                controlador.registrarCliente(
                        nombre,
                        apellido,
                        email,
                        telefono
                );

        etiquetaMensajeRegistro.setText(resultado);

        if (resultado.startsWith("OK")) {

            campoNombre.clear();
            campoApellido.clear();
            campoEmail.clear();
            campoTelefono.clear();

            actualizarTablaClientes();

            etiquetaMensajeRegistro.setStyle("-fx-text-fill: #52B788;");
        } else {
            etiquetaMensajeRegistro.setStyle("-fx-text-fill: #E63946;");
        }
    }

    // ── Agregar puntos ─────────────────────────────────────────────
    @FXML
    public void onClickAgregarPuntos(ActionEvent evento) {

        String textoId = campoIdPuntos.getText().trim();
        String textoCantidad = campoCantidadPuntos.getText().trim();
        String descripcion = campoDescripcionPuntos.getText().trim();

        if (textoId.isEmpty() || textoCantidad.isEmpty()) {

            etiquetaMensajePuntos.setText(
                    "ERROR: Completa el ID y la cantidad"
            );

            etiquetaMensajePuntos.setStyle(
                    "-fx-text-fill: #E63946;"
            );

            return;
        }

        int idCliente;
        int cantidad;

        try {

            idCliente = Integer.parseInt(textoId);
            cantidad = Integer.parseInt(textoCantidad);

        } catch (NumberFormatException e) {

            etiquetaMensajePuntos.setText(
                    "ERROR: El ID y la cantidad deben ser números"
            );

            etiquetaMensajePuntos.setStyle(
                    "-fx-text-fill: #E63946;"
            );

            return;
        }

        String resultado =
                controlador.agregarPuntos(
                        idCliente,
                        cantidad,
                        descripcion
                );

        etiquetaMensajePuntos.setText(resultado);

        if (resultado.startsWith("OK")) {

            etiquetaMensajePuntos.setStyle(
                    "-fx-text-fill: #52B788;"
            );

            campoIdPuntos.clear();
            campoCantidadPuntos.clear();
            campoDescripcionPuntos.clear();

            actualizarTablaClientes();
            actualizarTablaHistorial();

        } else {

            etiquetaMensajePuntos.setStyle(
                    "-fx-text-fill: #E63946;"
            );
        }
    }

    // ── Canjear puntos ─────────────────────────────────────────────
    @FXML
    public void onClickCanjearPuntos(ActionEvent evento) {

        String textoId = campoIdPuntos.getText().trim();
        String textoCantidad = campoCantidadPuntos.getText().trim();
        String descripcion = campoDescripcionPuntos.getText().trim();

        if (textoId.isEmpty() || textoCantidad.isEmpty()) {

            etiquetaMensajePuntos.setText(
                    "ERROR: Completa el ID y la cantidad"
            );

            etiquetaMensajePuntos.setStyle(
                    "-fx-text-fill: #E63946;"
            );

            return;
        }

        int idCliente;
        int cantidad;

        try {

            idCliente = Integer.parseInt(textoId);
            cantidad = Integer.parseInt(textoCantidad);

        } catch (NumberFormatException e) {

            etiquetaMensajePuntos.setText(
                    "ERROR: El ID y la cantidad deben ser números"
            );

            etiquetaMensajePuntos.setStyle(
                    "-fx-text-fill: #E63946;"
            );

            return;
        }

        String resultado =
                controlador.canjearPuntos(
                        idCliente,
                        cantidad,
                        descripcion
                );

        etiquetaMensajePuntos.setText(resultado);

        if (resultado.startsWith("OK")) {

            etiquetaMensajePuntos.setStyle(
                    "-fx-text-fill: #52B788;"
            );

            campoIdPuntos.clear();
            campoCantidadPuntos.clear();
            campoDescripcionPuntos.clear();

            actualizarTablaClientes();
            actualizarTablaHistorial();

        } else {

            etiquetaMensajePuntos.setStyle(
                    "-fx-text-fill: #E63946;"
            );
        }
    }

    // ── Consultar ──────────────────────────────────────────────────
    @FXML
    public void onClickConsultar(ActionEvent evento) {

        String textoId = campoIdConsulta.getText().trim();

        if (textoId.isEmpty()) {

            etiquetaResultadoConsulta.setText(
                    "ERROR: Ingresa un ID"
            );

            etiquetaResultadoConsulta.setStyle(
                    "-fx-text-fill: #E63946;"
            );

            return;
        }

        int idCliente;

        try {

            idCliente = Integer.parseInt(textoId);

        } catch (NumberFormatException e) {

            etiquetaResultadoConsulta.setText(
                    "ERROR: El ID debe ser un número"
            );

            etiquetaResultadoConsulta.setStyle(
                    "-fx-text-fill: #E63946;"
            );

            return;
        }

        String resultado =
                controlador.consultarPuntos(idCliente);

        etiquetaResultadoConsulta.setText(resultado);

        if (resultado.startsWith("OK")
                || resultado.startsWith("El cliente")) {

            etiquetaResultadoConsulta.setStyle(
                    "-fx-text-fill: #52B788;"
            );

        } else {

            etiquetaResultadoConsulta.setStyle(
                    "-fx-text-fill: #E63946;"
            );
        }
    }

    // ── Actualizar tabla clientes ─────────────────────────────────
    private void actualizarTablaClientes() {

        ArrayList<Cliente> lista =
                controlador.getListaClientes();

        ObservableList<Cliente> datos =
                FXCollections.observableArrayList(lista);

        tablaClientes.setItems(datos);
    }

    // ── Actualizar historial ──────────────────────────────────────
    private void actualizarTablaHistorial() {

        ArrayList<MovimientoPuntos> lista =
                controlador.getHistorial();

        ObservableList<MovimientoPuntos> datos =
                FXCollections.observableArrayList(lista);

        tablaHistorial.setItems(datos);
    }
}
