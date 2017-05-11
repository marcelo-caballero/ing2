package com.example.acer.myapplication;

/**
 * Created by Acer on 10/05/2017.
 */

public class Usuario {
    private int id;
    private String correo;
    private String nombre;

    public Usuario() {
    }

    public Usuario(int id, String correo, String nombre) {
        this.id = id;
        this.correo = correo;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}