package com.example.acer.vacuna;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acer.vacuna.Modelo.Historial;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class RegistroVacunacion extends AppCompatActivity {
    // numero de columna relacionada con los datos
    static final  private int VACUNA = 1;
    static final private int FECHA = 2;
    static final private int APLICADA = 3;

    //orden de listas
    static final private int ASC = 1;
    static final private int DESC = 2;



        //Indica el estado del ordenado de la tabla
        public class Estado{
            int columna;
            int orden;

            public Estado(int columna, int orden) {
                this.columna = columna;
                this.orden = orden;
            }
        }


        ArrayList<TextView[]> tv = new ArrayList<>();
        ArrayList<Historial> lista = new ArrayList<Historial>();


        TableLayout tablelayout ;
        TableRow row;
        TableRow.LayoutParams lp;
        //cabeceras de la tabla
        TextView c_vacuna;
        TextView c_fecha;
        TextView c_aplicada;

        String ci ;// cedula del hijo
        Estado e; //el estado del orden que se encuentra la tabla



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_registro_vacunacion);

            ci = Integer.toString(getIntent().getExtras().getInt("ci"));

            //la tabla ordenada por vacuna en forma asc
            e = new Estado(VACUNA ,ASC);


            obtener_lista();


            crear_tabla();
            crear_filas();
            cargar_tabla(lista.size());

            c_aplicada.setTextColor(Color.RED);
            c_vacuna.setTextColor(Color.RED);
            c_fecha.setTextColor(Color.RED);


        }

    /**
     * Obtiene la lista del registro de vacunacion, utilizando el servicio web
     */
    private void obtener_lista() {
            /*llama al servicio web*/
            obtenerVacuna servicio = new obtenerVacuna();
            try{

                servicio.execute(ci).get(4500, TimeUnit.MILLISECONDS);
            }catch(Exception e){
                Toast.makeText(this, "Error en la comunicacion", Toast.LENGTH_SHORT).show();

            }

    }

    /**
     * Carga la tabla con los valores obtenidos de la lista
     * @param tamano
     */

        private void cargar_tabla(int tamano) {

            for(int i = 0; i<tamano; i++){
                tv.get(i)[0].setText(lista.get(i).getVacuna());
                tv.get(i)[1].setText(lista.get(i).getFecha());
                tv.get(i)[2].setText(lista.get(i).getAplicada());

            }

        }

    /**
     * Crea las filas de la tabla con 3 columnas
     *
     */
        private void crear_filas() {


            for (int i = 0; i < lista.size(); i++) {

                row = new TableRow(this);
                lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);
                TextView vacuna = new TextView(this);
                TextView fecha = new TextView(this);
                TextView aplicada = new TextView(this);

                //c_vacuna.setText(lista.get(i).getVacuna());
                vacuna.setPadding(3, 3, 3, 3);
                //c_fecha.setText(lista.get(i).getFecha());
                fecha.setPadding(3, 3, 3, 3);
                // c_aplicada.setText(lista.get(i).getAplicada());
                aplicada.setPadding(3, 3, 3, 3);
                row.addView(vacuna);
                row.addView(fecha);
                row.addView(aplicada);

                TextView[] listTextV = {vacuna,fecha,aplicada};
                tv.add(listTextV);

                tablelayout.addView(row, i+1);


            }
        }

        /** Crea la cabecera de la tabla
         *
         */
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

    /**
     * Establece las acciones que se ejecutaran al momento de dar click
     */
        private void establecer_onclick_cabeceras() {

            c_vacuna.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(e.columna == VACUNA){
                        if(e.orden == ASC){
                            e.orden = DESC;
                            c_vacuna.setText("↓VACUNA");
                        }else{
                            e.orden = ASC;
                            c_vacuna.setText("↑VACUNA");
                        }
                    }else{

                        e.columna = VACUNA;
                        e.orden = ASC;
                        c_vacuna.setText("↑VACUNA");
                    }
                    c_fecha.setText("FECHA");
                    c_aplicada.setText("APLICADA");
                    obtener_lista();
                    cargar_tabla(lista.size());



                }
            });

            c_fecha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(e.columna == FECHA){
                        if(e.orden == ASC){
                            e.orden = DESC;
                            c_fecha.setText("↓FECHA");
                        }else{
                            e.orden = ASC;
                            c_fecha.setText("↑FECHA");
                        }
                    }else{

                        e.columna = FECHA;
                        e.orden = ASC;
                        c_fecha.setText("↑FECHA");
                    }
                    c_vacuna.setText("VACUNA");
                    c_aplicada.setText("APLICADA");
                    obtener_lista();
                    cargar_tabla(lista.size());



                }
            });

            c_aplicada.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String inst;
                    if(e.columna == APLICADA){
                        if(e.orden == ASC){
                            e.orden = DESC;

                            c_aplicada.setText("↓APLICADA");
                        }else{
                            e.orden = ASC;
                            c_aplicada.setText("↑APLICADA");
                        }
                    }else{
                        e.columna = APLICADA;
                        e.orden = ASC;
                        c_aplicada.setText("↑APLICADA");
                    }


                    c_fecha.setText("FECHA");
                    c_vacuna.setText("VACUNA");
                    obtener_lista();
                    cargar_tabla(lista.size());


                }
            });
        }

    private class obtenerVacuna extends AsyncTask<String,Void,Void> {
        Historial historial;
        String direccion = getIntent().getExtras().getString("direccion_ip") + "/rest/webresources/paquete.registrovacunas";
        protected Void doInBackground(String... params) {

            boolean resul = true;

            String ci = params[0];

            HttpClient httpClient = new DefaultHttpClient();


            HttpGet del = new HttpGet(direccion+"/vacuna/asc?ciHijo=" + ci);

            if(e.columna == VACUNA) {
                if(e.orden == ASC){
                    del = new HttpGet(direccion+"/vacuna/asc?ciHijo=" + ci);
                }else{
                    del = new HttpGet(direccion+"/vacuna/desc?ciHijo=" + ci);
                }
            }
            if(e.columna == FECHA) {
                if(e.orden == ASC){
                    del = new HttpGet(direccion+"/fecha/asc?ciHijo=" + ci);
                }else{
                    del = new HttpGet(direccion+"/fecha/desc?ciHijo=" + ci);
                }
            }
            if(e.columna == APLICADA) {
                if(e.orden == ASC){
                    del = new HttpGet(direccion+"/aplicada/asc?ciHijo=" + ci);
                }else{
                    del = new HttpGet(direccion+"/aplicada/desc?ciHijo=" + ci);
                }
            }

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                lista = new ArrayList<Historial>();

                for(int i=0; i<respJSON.length();i++){

                    JSONObject obj = respJSON.getJSONObject(i);
                    int ciHijo = obj.getInt("ciHijo");
                    String aplicada = obj.getString("aplicada");
                    String fecha = obj.getString("fechaAplicacion");
                    String nombreVacuna = obj.getString("nombreVacuna");

                    historial = new Historial(ciHijo,nombreVacuna,fecha,aplicada);
                    lista.add(historial);

                }



            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);

                resul = false;
            }

            return null;
        }
    }

}
