package com.example.acer.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Acer on 13/03/2017.
 */

public class BDatos extends SQLiteOpenHelper {

    String sqlCreate = "CREATE TABLE HIJO (" +
            "ci INTEGER,"+
            "nombre TEXT,"+
            "apellido TEXT,"+
            "email TEXT,"+
            "fecha_nac TEXT)";

    public BDatos (Context contexto, String nombre, SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
        /*Datos precargados*/
        db.execSQL("INSERT INTO HIJO(ci,nombre,apellido,email,fecha_nac)"+
                " VALUES(5555,'juan','perez','marcecaballero91@gmail.com','15/05/2015')");

        db.execSQL("INSERT INTO HIJO(ci,nombre,apellido,email,fecha_nac)"+
                " VALUES(4444,'jose','perez','marcecaballero@gmail.com','15/05/2015')");

        db.execSQL("INSERT INTO HIJO(ci,nombre,apellido,email,fecha_nac)"+
                " VALUES(4444,'jose','fulano','marcecaballero91@gmail.com','15/05/2015')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS HIJO");
        db.execSQL(sqlCreate);
    }

    public ArrayList<String> obtener_lista_hijos(String email) {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT nombre, apellido, fecha_nac FROM HIJO WHERE email=?", new String[]{email});
        if (c.moveToFirst()) {
            do {
                String nombre = c.getString(0);
                String apellido = c.getString(1);
                String fecha_nac = c.getString(2);

                lista.add("" + nombre + " " + apellido + " - " + fecha_nac +"\n");
            } while(c.moveToNext());

        }

        return lista;
    }
}
