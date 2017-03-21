package com.example.acer.myapplication;

/**
 * Created by Acer on 20/03/2017.
 */

public class Historial {

    private String vacuna;
    private String fecha;
    private String aplicada;

    public Historial(String vacuna, String fecha, String aplicada) {
        this.vacuna = vacuna;
        this.fecha = fecha;
        this.aplicada = aplicada;
    }

    public String getVacuna() {
        return vacuna;
    }

    public void setVacuna(String vacuna) {
        this.vacuna = vacuna;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getAplicada() {
        return aplicada;
    }

    public void setAplicada(String aplicada) {
        this.aplicada = aplicada;
    }
}
