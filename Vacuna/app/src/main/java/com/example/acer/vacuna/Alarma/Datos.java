package com.example.acer.vacuna.Alarma;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



/**
 * Guarda informacion con el funcionamiento de la alarma
 * Guarda datos sobre el email del usuario y si la alarma esta programada o no.
 * Estos datos son utilizados por la aplicacion, cuando el dispositivo es reinicializado
 * o encendido.
 *
 */

public class Datos extends SQLiteOpenHelper {


    //Guarda informacion relevante para el funcionamiento de la alarma

    String sqlCreateUsuario = "CREATE TABLE usuario (correo TEXT)";
    String sqlCreateAlarma = "CREATE TABLE alarma (programado INTEGER)";


    public Datos (Context contexto, String nombre, SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreateUsuario);
        db.execSQL(sqlCreateAlarma);

        //datos de la aplicacion
        db.execSQL("INSERT INTO Usuario(correo)"+ " VALUES(null)");
        db.execSQL("INSERT INTO alarma(programado)"+ " VALUES(0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /**
     * Actualiza el email del usuario logueado en la base de datos
     * @param correo direccion de email
     */
    public void set_usuario(String correo){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE usuario SET correo='"+correo+"'");
    }

    /**
     * Retorna el email del usuario logueado en la aplicacion
     * @return String
     */
    public String get_usuario(){
        SQLiteDatabase db = this.getWritableDatabase();
        String correo = null;
        Cursor c = db.rawQuery("select correo from usuario",null);
        if(c.moveToFirst()){
            correo = c.getString(0);
        }
        return correo;
    }

    /**
     * Indica que la alarma esta activa
     *
     */
    public void programarAlarma(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE alarma SET programado = 1");
    }

    /**
     * Indica que la alarma no esta activa
     */
    public void desprogramarAlarma(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE alarma SET programado = 0");
    }

    /**
     * Retornará informacion sobre la alarma
     * @return int si retorna 1 la alarma esta activa, 0 si no esta activa
     */
    public int getAlarma(){
        SQLiteDatabase db = this.getWritableDatabase();
        int programado = 0;
        Cursor c = db.rawQuery("select programado from alarma",null);
        if(c.moveToFirst()){
            programado = c.getInt(0);
        }
        return programado;
    }
}
