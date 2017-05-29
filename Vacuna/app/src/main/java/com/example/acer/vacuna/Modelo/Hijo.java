package com.example.acer.vacuna.Modelo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Clase Hijo
 * @author marcelo
 */
public class Hijo {
    private int ci;
    private String nombre;
    private String apellido;
    private String email;
    private Date fecha_nac;

    public Hijo(int ci, String nombre, String apellido, String email, String fecha_nac){
        this.ci = ci;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.fecha_nac = fechaDate(fecha_nac);
    }

    public Date getFecha_nac() {
        return fecha_nac;
    }

    public void setFecha_nac(Date fecha_nac) {
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

    protected Date fechaDate(String fecha){
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.ROOT);
            date = sdf.parse(fecha);

        }catch(ParseException e){

        }
        return date;
    }

    @Override
    public String toString() {

        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy",Locale.ROOT);
        String fecha = sdf2.format(fecha_nac);

        return "" + nombre + " " + apellido + " - " + fecha ;

    }
}
