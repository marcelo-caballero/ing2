package com.example.acer.vacuna.Modelo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Calendar;

/**
 * Clase Hijo
 * @author marcelo
 */
public class Hijo {
    private int id;
    private String nombre;
    private String sexo;
    private int idPadre;
    private Date fechaNac;


    public Hijo(int id, String nombre, String sexo, int idPadre, String fechaNac){
        this.id = id;
        this.nombre = nombre;
        this.sexo = sexo;
        this.idPadre = idPadre;
        this.fechaNac = fechaDate(fechaNac);
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getIdPadre() {
        return idPadre;
    }

    public void setIdPadre(int idPadre) {
        this.idPadre = idPadre;
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

    public int edad(){
        int edad = -1;
        if(fechaNac != null) {
            Calendar nacimiento = Calendar.getInstance();
            nacimiento.setTime(fechaNac);
            Calendar hoy = Calendar.getInstance();
            hoy.setTime(new Date());
            edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR);
            if (nacimiento.get(Calendar.MONTH) > hoy.get(Calendar.MONTH) ||
                    (nacimiento.get(Calendar.MONTH) == hoy.get(Calendar.MONTH) && nacimiento.get(Calendar.DATE) > hoy.get(Calendar.DATE))) {
                edad--;
            }

        }
        return edad;
    }

    @Override
    public String toString() {

        /*SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy",Locale.ROOT);
        String fecha = sdf2.format(fechaNac);*/

        return "" + nombre + ", " + edad() + ", " + sexo ;

    }
}
