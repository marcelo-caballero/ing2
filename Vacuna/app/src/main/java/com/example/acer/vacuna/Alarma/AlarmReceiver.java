package com.example.acer.vacuna.Alarma;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.acer.vacuna.MainActivity;
import com.example.acer.vacuna.Modelo.Hijo;
import com.example.acer.vacuna.Modelo.Historial;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


/**
 * Clase AlarmReceiver que extiende de BroadcastReceiver, contiene codigo que será ejecutado por Alarma
 */

public class AlarmReceiver extends BroadcastReceiver {

    Datos usdbh ;
    int NOTIF_ALERTA_ID = 1;


    String direccion = MainActivity.direccion_ip;

    ArrayList<Historial> lista_historial = new ArrayList<Historial>();
    Hijo hijo = null;



    /**
     * Codigo que se ejecutará cada intervalo de tiempo, aun cuando el dispositivo sea reiniciado o encendido
     * @param context de tipo Context
     * @param intent de tipo Intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        usdbh = new Datos(context, "DBUsuarios", null, 1);


        //Se ejecuta despues de que el dispositivo es reinicializado y la alarma programada
        if (intent.getAction()== null) {
            NOTIF_ALERTA_ID = 1;

            String email = usdbh.get_usuario();


            ObtenerHistorial servicio = new ObtenerHistorial();
            try{

                servicio.execute(email).get(4500, TimeUnit.MILLISECONDS);
                if(!lista_historial.isEmpty()){
                    for(int i=0;i<lista_historial.size();i++){
                        ObtenerHijo serv = new ObtenerHijo();
                        serv.execute(lista_historial.get(i).getCiHijo()).get(4500, TimeUnit.MILLISECONDS);
                        notificar(context,lista_historial.get(i),hijo);

                    }
                }
            }catch(Exception e){

            }






            //Se emite una vez cuando se termina de encender el dispositivo, se ejecuta despues de que el usuario ingresó su
            //pin o patron por primera vez
        }else if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            if(usdbh.getAlarma() == 1) {
                Alarma alarma = new Alarma(context, AlarmReceiver.class);
                alarma.start();
            }


        }


    }

    private void notificar(Context context, Historial h,Hijo hijo){


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(android.R.drawable.stat_sys_warning)

                        .setContentTitle(hijo.getNombre())
                        .setContentText("necesita aplicar " + h.getVacuna())
                        .setTicker("Vacuna!")
                        .setAutoCancel(true);



        Intent notIntent =
                new Intent(context, MainActivity.class);

        PendingIntent contIntent = PendingIntent.getActivity(
                context, 0, notIntent, 0);

        mBuilder.setContentIntent(contIntent);

        mBuilder.setVibrate(new long[] { 1000, 1000});
        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());

        NOTIF_ALERTA_ID = NOTIF_ALERTA_ID +1;
    }


    /**
     * Consulta el registro de hijos a vacunar
     * @return
     */
    private class ObtenerHistorial extends AsyncTask<String,Void,Void> {
        Historial historial = null;
        @Override
        protected Void doInBackground(String... params) {

            String email = params[0];

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost postAplicacion = new HttpPost(direccion+"/rest1/servicioWeb/vacunas/aplicacionVacunas?dias=2");
            postAplicacion.setHeader("content-type", "application/json");

            try
            {
                JSONObject dato = new JSONObject();
                dato.put("correo",email);
                StringEntity entity = new StringEntity(dato.toString());
                postAplicacion.setEntity(entity);

                HttpResponse resp = httpClient.execute(postAplicacion);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);



                for(int i=0; i<respJSON.length();i++){

                    JSONObject obj = respJSON.getJSONObject(i);
                    int idHijo = obj.getInt("idHijo");
                    String aplicada = obj.getString("aplicada");
                    String fecha = obj.getString("fechaAplicacion");
                    String nombreVacuna = obj.getString("nombre");

                    historial = new Historial(idHijo,nombreVacuna,fecha,aplicada);
                    lista_historial.add(historial);

                }



            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
            }
            return null;
        }
    }

    private class ObtenerHijo extends AsyncTask<Integer,Void,Void> {


        @Override
        protected Void doInBackground(Integer... params) {

            int ci = params[0];

            HttpClient httpClient = new DefaultHttpClient();


            HttpPost postHijo = new HttpPost(direccion+"/rest1/servicioWeb/hijo/obtenerHijo");
            postHijo.setHeader("content-type", "application/json");

            try
            {
                JSONObject dato = new JSONObject();
                dato.put("id",ci);
                StringEntity entity = new StringEntity(dato.toString());
                postHijo.setEntity(entity);

                HttpResponse resp = httpClient.execute(postHijo);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject obj = new JSONObject(respStr);


                String sexo = obj.getString("sexo");
                int id = obj.getInt("id");

                int idPadre = obj.getInt("idPadre");
                String fecha_nac = obj.getString("fechaNac");
                String nombre = obj.getString("nombre");

                hijo = new Hijo(id,nombre,sexo,idPadre,fecha_nac);
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
            }
            return null;
        }
    }
}