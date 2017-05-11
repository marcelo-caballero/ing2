package com.example.acer.myapplication;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Hashtable;


public class RegistroVacunacion extends AppCompatActivity {

    public class Estado{
        String columna;
        String orden;

        public Estado(String columna, String orden) {
            this.columna = columna;
            this.orden = orden;
        }
    }

    Hashtable<String,String> campo_tabla;


    ArrayList<TextView[]> tv = new ArrayList<>();
    ArrayList<Historial> lista_vacuna;
    BDatos db;

    TableLayout tablelayout ;
    TableRow row;
    TableRow.LayoutParams lp;
    TextView c_vacuna;
    TextView c_fecha;
    TextView c_aplicada;
    String ci ;
    Estado e;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_vacunacion);

        db = new BDatos(this, "DBUsuarios", null, 1);


        //Orden de las columnas de la tabla
        campo_tabla = new Hashtable<String,String>();
        campo_tabla.put("vacuna","1");
        campo_tabla.put("fecha","2");
        campo_tabla.put("aplicada","3");

        //Estado del ordenado de la tabla
         e = new Estado("1","asc");


        inicializar_lista_vacunas();
        crear_tabla();
        crear_filas(lista_vacuna.size());
        cargar_tabla(lista_vacuna.size());

        c_aplicada.setTextColor(Color.RED);
        c_vacuna.setTextColor(Color.RED);
        c_fecha.setTextColor(Color.RED);


    }

    private void inicializar_lista_vacunas() {

        ci = Integer.toString(getIntent().getExtras().getInt("ci"));
        lista_vacuna = db.historial_paciente(ci,campo_tabla.get("vacuna"));

    }

    private void cargar_tabla(int tamano) {

        for(int i = 0; i<tamano; i++){
            tv.get(i)[0].setText(lista_vacuna.get(i).getVacuna());
            tv.get(i)[1].setText(lista_vacuna.get(i).getFecha());
            tv.get(i)[2].setText(lista_vacuna.get(i).getAplicada());

        }

    }

    private void crear_filas(int tamano) {


        for (int i = 0; i < lista_vacuna.size(); i++) {

            row = new TableRow(this);
            lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            TextView vacuna = new TextView(this);
            TextView fecha = new TextView(this);
            TextView aplicada = new TextView(this);

            //c_vacuna.setText(lista_vacuna.get(i).getVacuna());
            vacuna.setPadding(3, 3, 3, 3);
            //c_fecha.setText(lista_vacuna.get(i).getFecha());
            fecha.setPadding(3, 3, 3, 3);
           // c_aplicada.setText(lista_vacuna.get(i).getAplicada());
            aplicada.setPadding(3, 3, 3, 3);
            row.addView(vacuna);
            row.addView(fecha);
            row.addView(aplicada);

            TextView[] listTextV = {vacuna,fecha,aplicada};
            tv.add(listTextV);

            tablelayout.addView(row, i+1);


        }
    }

    private void crear_tabla() {


        tablelayout = (TableLayout) findViewById(R.id.table_layout);
        row = new TableRow(this);
        lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        c_vacuna = new TextView(this);
        c_fecha = new TextView(this);
        c_aplicada = new TextView(this);



        //Establece la cabecera de la tabla
        c_vacuna.setText("↑VACUNA");
        c_vacuna.setPadding(10, 10, 10, 10);

        c_fecha.setText("FECHA");
        c_fecha.setPadding(3, 3, 3, 3);
        c_aplicada.setText("APLICADA");
        c_aplicada.setPadding(3, 3, 3, 3);
        row.addView(c_vacuna);
        row.addView(c_fecha);
        row.addView(c_aplicada);
        tablelayout.addView(row, 0);

        establecer_onclick_cabeceras();
    }

    private void establecer_onclick_cabeceras() {

        c_vacuna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inst;
                if(e.columna.equals(campo_tabla.get("vacuna"))){
                    if(e.orden.equals("asc")){
                        e.orden = "desc";
                        inst = campo_tabla.get("vacuna")+" desc";
                        c_vacuna.setText("↓VACUNA");
                    }else{
                        e.orden = "asc";
                        inst = campo_tabla.get("vacuna")+" asc";
                        c_vacuna.setText("↑VACUNA");
                    }
                }else{
                     inst = campo_tabla.get("vacuna")+" asc";
                    e.columna = campo_tabla.get("vacuna");
                    e.orden = "asc";
                    c_vacuna.setText("↑VACUNA");
                }
                c_fecha.setText("FECHA");
                c_aplicada.setText("APLICADA");
                lista_vacuna = db.historial_paciente(ci,inst);
                cargar_tabla(lista_vacuna.size());



            }
        });

        c_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inst;
                if(e.columna.equals(campo_tabla.get("fecha"))){
                    if(e.orden.equals("asc")){
                        e.orden = "desc";
                        inst = campo_tabla.get("fecha")+" desc";
                        c_fecha.setText("↓FECHA");
                    }else{
                        e.orden = "asc";
                        inst = campo_tabla.get("fecha")+" asc";
                        c_fecha.setText("↑FECHA");
                    }
                }else{
                    inst = campo_tabla.get("fecha")+" asc";
                    e.columna = campo_tabla.get("fecha");
                    e.orden = "asc";
                    c_fecha.setText("↑FECHA");
                }
                c_vacuna.setText("VACUNA");
                c_aplicada.setText("APLICADA");
                lista_vacuna = db.historial_paciente(ci,inst);
                cargar_tabla(lista_vacuna.size());



            }
        });

        c_aplicada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String inst;
                if(e.columna.equals(campo_tabla.get("aplicada"))){
                    if(e.orden.equals("asc")){
                        e.orden = "desc";
                        inst = campo_tabla.get("aplicada")+" desc";
                        c_aplicada.setText("↓APLICADA");
                    }else{
                        e.orden = "asc";
                        inst = campo_tabla.get("aplicada")+" asc";
                        c_aplicada.setText("↑APLICADA");
                    }
                }else{
                    inst = campo_tabla.get("aplicada")+" asc";
                    e.columna = campo_tabla.get("aplicada");
                    e.orden = "asc";
                    c_aplicada.setText("↑APLICADA");
                }

                lista_vacuna = db.historial_paciente(ci,inst);
                c_fecha.setText("FECHA");
                c_vacuna.setText("VACUNA");
                cargar_tabla(lista_vacuna.size());



            }
        });
    }
}
