package com.fastbite.controller;

import com.fastbite.model.Cliente;
import com.fastbite.model.MovimientoPuntos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

// Esta clase maneja TODA la lógica del programa
// Aquí se guardan los clientes y se hacen las operaciones
public class ClienteController {

    // Lista donde guardamos todos los clientes
    // ArrayList es como un arreglo que crece solo
    private ArrayList<Cliente> listaClientes;

    // Lista donde guardamos el historial de movimientos de puntos
    private ArrayList<MovimientoPuntos> historial;

    // Contador para generar IDs únicos (1, 2, 3, 4...)
    private int contadorId;

    // ── Constructor ────────────────────────────────────────────────
    public ClienteController() {
        listaClientes = new ArrayList<>();
        historial     = new ArrayList<>();
        contadorId    = 1;

    }

    // ── 1. REGISTRAR un nuevo cliente ──────────────────────────────
    public String registrarCliente(String nombre, String apellido,
                                   String email, String telefono) {

        // Validar que no estén vacíos los campos obligatorios
        if (nombre.isEmpty()) {
            return "ERROR: El nombre no puede estar vacío";
        }
        if (apellido.isEmpty()) {
            return "ERROR: El apellido no puede estar vacío";
        }
        if (email.isEmpty()) {
            return "ERROR: El email no puede estar vacío";
        }

        // Verificar que el email no esté ya registrado
        for (int i = 0; i < listaClientes.size(); i++) {
            Cliente c = listaClientes.get(i);
            if (c.getEmail().equals(email)) {
                return "ERROR: Ya existe un cliente con ese email";
            }
        }

        // Crear el nuevo cliente
        Cliente nuevo = new Cliente(contadorId, nombre, apellido, email, telefono);
        contadorId = contadorId + 1; // aumentar el contador para el próximo

        // Agregar a la lista
        listaClientes.add(nuevo);

        return "OK: Cliente registrado con ID " + nuevo.getId();
    }

    // ── 2. BUSCAR un cliente por su ID ─────────────────────────────
    public Cliente buscarClientePorId(int id) {
        // Recorremos toda la lista buscando el ID
        for (int i = 0; i < listaClientes.size(); i++) {
            Cliente c = listaClientes.get(i);
            if (c.getId() == id) {
                return c; // lo encontramos, lo devolvemos
            }
        }
        return null; // no lo encontramos
    }

    // ── 3. AGREGAR puntos a un cliente ─────────────────────────────
    public String agregarPuntos(int idCliente, int cantidad, String descripcion) {

        // Buscar el cliente
        Cliente cliente = buscarClientePorId(idCliente);

        // Si no existe, retornar error
        if (cliente == null) {
            return "ERROR: No existe un cliente con ese ID";
        }

        // Validar que la cantidad sea positiva
        if (cantidad <= 0) {
            return "ERROR: La cantidad debe ser mayor a 0";
        }

        // Sumar los puntos al cliente
        int puntosNuevos = cliente.getPuntos() + cantidad;
        cliente.setPuntos(puntosNuevos);

        // Guardar en el historial
        String fecha = obtenerFechaHoy();
        MovimientoPuntos mov = new MovimientoPuntos(MovimientoPuntos.TIPO_GANAR, cantidad, descripcion, fecha);
        historial.add(mov);

        return "OK: Se agregaron " + cantidad + " puntos a " + cliente.getNombreCompleto();
    }

    // ── 4. CANJEAR puntos de un cliente ────────────────────────────
    public String canjearPuntos(int idCliente, int cantidad, String descripcion) {

        // Buscar el cliente
        Cliente cliente = buscarClientePorId(idCliente);

        // Si no existe, retornar error
        if (cliente == null) {
            return "ERROR: No existe un cliente con ese ID";
        }

        // Validar que la cantidad sea positiva
        if (cantidad <= 0) {
            return "ERROR: La cantidad debe ser mayor a 0";
        }

        // Validar que tenga suficientes puntos
        if (cliente.getPuntos() < cantidad) {
            return "ERROR: El cliente solo tiene " + cliente.getPuntos() + " puntos";
        }

        // Restar los puntos al cliente
        int puntosRestantes = cliente.getPuntos() - cantidad;
        cliente.setPuntos(puntosRestantes);

        // Guardar en el historial
        String fecha = obtenerFechaHoy();
        MovimientoPuntos mov = new MovimientoPuntos(MovimientoPuntos.TIPO_CANJEAR, cantidad, descripcion, fecha);
        historial.add(mov);

        return "OK: Se canjearon " + cantidad + " puntos de " + cliente.getNombreCompleto();
    }

    // ── 5. CONSULTAR puntos de un cliente ──────────────────────────
    public String consultarPuntos(int idCliente) {
        Cliente cliente = buscarClientePorId(idCliente);

        if (cliente == null) {
            return "ERROR: No existe un cliente con ese ID";
        }

        return "El cliente " + cliente.getNombreCompleto()
                + " tiene " + cliente.getPuntos() + " puntos"
                + " | Categoría: " + cliente.getCategoria();
    }

    // ── Métodos de apoyo ───────────────────────────────────────────

    // Devuelve la lista completa de clientes
    public ArrayList<Cliente> getListaClientes() {
        return listaClientes;
    }

    // Devuelve la lista completa del historial
    public ArrayList<MovimientoPuntos> getHistorial() {
        return historial;
    }

    // Devuelve la fecha de hoy en formato dd/MM/yyyy
    private String obtenerFechaHoy() {
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return hoy.format(formato);
    }


}