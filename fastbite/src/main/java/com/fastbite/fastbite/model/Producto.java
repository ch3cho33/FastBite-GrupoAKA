package com.fastbite.fastbite.model;

public class Producto{

    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String categoria;

    public Producto(int id, String nombre, String descripcion, double precio, String categoria){

        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
    }

    public Producto(int id, String nombre, double precio){
        this(id, nombre, "", precio, "general");
    }

    //getters
    public int getId()  { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
    public String getCategoria() { return categoria; }

    //setters

    public void setId(int id) {this.id = id; }
    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    public void setPrecio(double precio) {this.precio = precio;}
    public void setCategoria(String categoria) {this.categoria = categoria;}

    @Override
    public String toString(){
        return String.format("Producto{id=%d, nombre='%s', precio=$&.2f", id, nombre, precio);
    }
}