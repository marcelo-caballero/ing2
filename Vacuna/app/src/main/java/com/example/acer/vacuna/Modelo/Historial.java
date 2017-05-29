package com.example.acer.vacuna.Modelo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Acer on 28/05/2017.
 */

public class Historial {

    private String vacuna;
    private String fecha;
    private String aplicada;
    private Date date;

    public Historial(String vacuna, String fecha, String aplicada) {
        this.vacuna = vacuna;
        this.fecha = fechaDate(fecha);
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    protected String fechaDate(String fecha){


        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.ROOT);
            this.date = sdf.parse(fecha);

            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy",Locale.ROOT);
            fecha = sdf2.format(this.date);

        }catch(ParseException e){

        }
        return fecha;
    }

}
