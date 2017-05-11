package com.example.acer.myapplication;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;


/**
 * Clase AlarmReceiver que extiende de BroadcastReceiver, contiene codigo que será ejecutado por Alarma
 */

public class AlarmReceiver extends BroadcastReceiver {

    BDatos usdbh ;
    int NOTIF_ALERTA_ID = 1;
    /**
     * Codigo que se ejecutará cada intervalo de tiempo, aun cuando el dispositivo sea reiniciado o encendido
     * @param context de tipo Context
     * @param intent de tipo Intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        usdbh = new BDatos(context, "DBUsuarios", null, 1);

        //Se ejecuta cuando se lo pida en la aplicacion
        if (intent.getAction()== null) {
            NOTIF_ALERTA_ID = 1;




            String cuenta = usdbh.getUsuarioLogueado();
            ArrayList<Hijo> hijos = usdbh.obtener_lista_hijos(cuenta);
            ArrayList<Historial> vacuna_no_aplicada;
            if(!hijos.isEmpty()){
                for(int i=0;i<hijos.size();i++){
                    String ci = Integer.toString(hijos.get(i).getCi());
                    vacuna_no_aplicada =  usdbh.historial_vacunas_no_aplicadas(ci);


                    if(!vacuna_no_aplicada.isEmpty()){
                        for(int j=0;j<vacuna_no_aplicada.size();j++){

                            Historial h = vacuna_no_aplicada.get(j);
                            notificar(context, h,hijos.get(i));
                        }

                    }
                }
            }




            //Se emite una vez, cuando se termina de encender el dispositivo, se ejecuta despues de que el usuario ingresó su
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
                        //.setContentInfo(usdbh.getUsuarioLogueado())

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


}