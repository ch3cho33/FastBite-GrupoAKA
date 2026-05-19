package com.fastbite.fastbite.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Pedido {

    // --- Constante de IVA ---
    private static final double TASA_IVA = 0.19; // 19% según normativa colombiana

    // --- Atributos ---
    private int numeroPedido;
    private String nombreCliente;
    private List<ItemPedido> items;
    private EstadoPedido estado;
    private LocalDateTime fechaHora;

    // --- Constructor ---
    public Pedido(int numeroPedido, String nombreCliente) {
        this.numeroPedido  = numeroPedido;
        this.nombreCliente = nombreCliente;
        this.items         = new ArrayList<>();
        this.estado        = EstadoPedido.PENDIENTE;
        this.fechaHora     = LocalDateTime.now();
    }



    public void agregarItem(ItemPedido item) {
        items.add(item);
    }

    public void eliminarItem(int indice) {
        if (indice >= 0 && indice < items.size()) {
            items.remove(indice);
        }
    }

    public double calcularSubtotal() {
        double subtotal = 0;
        for (ItemPedido item : items) {
            subtotal += item.calcularSubtotal();
        }
        return subtotal;
    }

    public double calcularIVA() {
        return calcularSubtotal() * TASA_IVA;
    }

    public double calcularTotal() {
        return calcularSubtotal() + calcularIVA();
    }


    public void cambiarEstado(EstadoPedido nuevoEstado) {
        this.estado = nuevoEstado;
    }


    public int getNumeroPedido()        { return numeroPedido; }
    public String getNombreCliente()    { return nombreCliente; }
    public List<ItemPedido> getItems()  { return items; }
    public EstadoPedido getEstado()     { return estado; }
    public LocalDateTime getFechaHora() { return fechaHora; }

    public void setNumeroPedido(int numeroPedido)      { this.numeroPedido = numeroPedido; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public void setEstado(EstadoPedido estado)         { this.estado = estado; }


    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append("========== PEDIDO #").append(numeroPedido).append(" ==========\n");
        sb.append("Cliente  : ").append(nombreCliente).append("\n");
        sb.append("Fecha    : ").append(fechaHora.format(formato)).append("\n");
        sb.append("Estado   : ").append(estado).append("\n");
        sb.append("-----------------------------------\n");
        for (ItemPedido item : items) {
            sb.append("  ").append(item).append("\n");
        }
        sb.append("-----------------------------------\n");
        sb.append(String.format("Subtotal : $%,.2f%n", calcularSubtotal()));
        sb.append(String.format("IVA (19%%): $%,.2f%n", calcularIVA()));
        sb.append(String.format("TOTAL    : $%,.2f%n", calcularTotal()));
        sb.append("===================================");
        return sb.toString();
    }
}