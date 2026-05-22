package com.fastbite.model;

public class Cliente {


    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private int puntos;


    public Cliente(int id, String nombre, String apellido, String email, String telefono) {
        this.id       = id;
        this.nombre   = nombre;
        this.apellido = apellido;
        this.email    = email;
        this.telefono = telefono;
        this.puntos   = 0; // todo cliente empieza con 0 puntos
    }



    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public int getPuntos() {
        return puntos;
    }



    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }


    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public String getCategoria() {
        if (puntos >= 2000) {
            return "Oro";
        } else if (puntos >= 500) {
            return "Plata";
        } else {
            return "Bronce";
        }
    }
}
