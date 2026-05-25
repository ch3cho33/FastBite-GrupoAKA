package com.fastbite.controller;

import com.fastbite.model.Cliente;
import com.fastbite.model.MovimientoPuntos;
import com.fastbite.persistence.ClienteRepository;
import com.fastbite.persistence.MovimientoPuntosRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

//Control: Gestiona la lógica de clientes y puntos.

public class ClienteController {

    private ArrayList<Cliente>         listaClientes;
    private ArrayList<MovimientoPuntos> historial;
    private int                         contadorId;

    private final ClienteRepository          clienteRepo;
    private final MovimientoPuntosRepository puntosRepo;

    public ClienteController() {
        this.clienteRepo = new ClienteRepository();
        this.puntosRepo  = new MovimientoPuntosRepository();

        // Cargar datos persistidos al iniciar
        this.listaClientes = clienteRepo.cargarTodos();
        this.historial     = puntosRepo.cargarTodos();

        // El contador continúa desde el último ID guardado
        this.contadorId = clienteRepo.obtenerMaxId() + 1;
    }

    // 1. Registrar Cliente

    public String registrarCliente(String nombre, String apellido,
                                   String email, String telefono) {
        if (nombre.isEmpty())   return "ERROR: El nombre no puede estar vacío";
        if (apellido.isEmpty()) return "ERROR: El apellido no puede estar vacío";
        if (email.isEmpty())    return "ERROR: El email no puede estar vacío";

        for (Cliente c : listaClientes) {
            if (c.getEmail().equalsIgnoreCase(email)) {
                return "ERROR: Ya existe un cliente con ese email";
            }
        }

        Cliente nuevo = new Cliente(contadorId, nombre, apellido, email, telefono);
        contadorId++;
        listaClientes.add(nuevo);

        // Guardar en JSON inmediatamente
        clienteRepo.guardar(listaClientes);

        return "OK: Cliente registrado con ID " + nuevo.getId();
    }

    // 2. Buscar por ID

    public Cliente buscarClientePorId(int id) {
        for (Cliente c : listaClientes) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    // 3. Agregar Puntos

    public String agregarPuntos(int idCliente, int cantidad, String descripcion) {
        Cliente cliente = buscarClientePorId(idCliente);
        if (cliente == null) return "ERROR: No existe un cliente con ese ID";
        if (cantidad <= 0)   return "ERROR: La cantidad debe ser mayor a 0";

        cliente.setPuntos(cliente.getPuntos() + cantidad);

        MovimientoPuntos mov = new MovimientoPuntos(
                MovimientoPuntos.TIPO_GANAR, cantidad, descripcion, obtenerFechaHoy());
        historial.add(mov);

        // Guardar ambas listas en JSON
        clienteRepo.guardar(listaClientes);
        puntosRepo.guardar(historial);

        return "OK: Se agregaron " + cantidad + " puntos a "
                + cliente.getNombreCompleto();
    }

    // Canjear Puntos

    public String canjearPuntos(int idCliente, int cantidad, String descripcion) {
        Cliente cliente = buscarClientePorId(idCliente);
        if (cliente == null) return "ERROR: No existe un cliente con ese ID";
        if (cantidad <= 0)   return "ERROR: La cantidad debe ser mayor a 0";
        if (cliente.getPuntos() < cantidad)
            return "ERROR: El cliente solo tiene " + cliente.getPuntos() + " puntos";

        cliente.setPuntos(cliente.getPuntos() - cantidad);

        MovimientoPuntos mov = new MovimientoPuntos(
                MovimientoPuntos.TIPO_CANJEAR, cantidad, descripcion, obtenerFechaHoy());
        historial.add(mov);

        // Guardar ambas listas en JSON
        clienteRepo.guardar(listaClientes);
        puntosRepo.guardar(historial);

        return "OK: Se canjearon " + cantidad + " puntos de "
                + cliente.getNombreCompleto();
    }

    // 5. Consultar Puntos

    public String consultarPuntos(int idCliente) {
        Cliente cliente = buscarClientePorId(idCliente);
        if (cliente == null) return "ERROR: No existe un cliente con ese ID";
        return "El cliente " + cliente.getNombreCompleto()
                + " tiene " + cliente.getPuntos() + " puntos"
                + " | Categoría: " + cliente.getCategoria();
    }

    // Getters

    public ArrayList<Cliente> getListaClientes() {
        return listaClientes;
    }

    public ArrayList<MovimientoPuntos> getHistorial() {
        return historial;
    }

    // Privados

    private String obtenerFechaHoy() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}