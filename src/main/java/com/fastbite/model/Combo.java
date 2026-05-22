package com.fastbite.model;
import java.util.ArrayList;
import java.util.List;
public class Combo {
    private int id;
    private String nombre;
    private List<Producto> productos;
    private double precioEspecial;
    
    public Combo() {
        productos = new ArrayList<>();
    }

    public Combo(int id, String nombre, double precioEspecial) {
        this.id = id;
        this.nombre = nombre;
        this.precioEspecial = precioEspecial;
        this.productos = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public double getPrecioEspecial() {
        return precioEspecial;
    }

    public void setPrecioEspecial(double precioEspecial) {
        this.precioEspecial = precioEspecial;
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public void eliminarProducto(Producto producto) {
        productos.remove(producto);
    }

}
