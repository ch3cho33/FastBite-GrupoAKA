package com.fastbite.fastbite.model;

public class ItemPedido {

    //atrobutos

    private Producto producto;
    private int cantidad;
    private String notasEspeciales;

    //constructor
    public ItemPedido(Producto producto, int cantidad, String notasEspeciales){
        this.producto = producto;
        this.cantidad = cantidad;
        this.notasEspeciales = notasEspeciales;
    }
    //constructor sin notas especiales
    public ItemPedido(Producto producto, int cantidad){
        this(producto, cantidad, "");
    }

    //modelo de negocio
    public double calcularSubtotal(){
        return producto.getPrecio() * cantidad;
    }
    //gettsers
    public Producto getProducto() {return producto;}
    public int getCantidad() {return cantidad;}
    public String getNotasEspeciales() {return notasEspeciales;}

    //setters
    public void setProducto(Producto producto) {this.producto = producto;}
    public void setCantidad(int cantidad) {this.cantidad = cantidad;}

    public void setNotasEspeciales(String notasEspeciales) {this.notasEspeciales = notasEspeciales;}

    @Override
    public String toString(){
        return String.format("ItemPedido{producto='%s', cantidad=%d, subtotal=$%.2f}", producto.getNombre(), cantidad,
                calcularSubtotal());
    }
}
