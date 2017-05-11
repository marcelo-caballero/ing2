package com.example.acer.myapplication;

/**
 * Created by Acer on 16/03/2017.
 */

public class Hijo {
    private int ci;
    private String nombre;
    private String apellido;
    private String email;
    private String fecha_nac;

    public Hijo(int ci, String nombre, String apellido, String email, String fecha_nac){
        this.ci = ci;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.fecha_nac = fecha_nac;
    }

    public String getFecha_nac() {
        return fecha_nac;
    }

    public void setFecha_nac(String fecha_nac) {
        this.fecha_nac = fecha_nac;
    }

    public int getCi() {
        return ci;
    }

    public void setCi(int ci) {
        this.ci = ci;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "" + nombre + " " + apellido + " - " + fecha_nac +"\n";
    }
}
