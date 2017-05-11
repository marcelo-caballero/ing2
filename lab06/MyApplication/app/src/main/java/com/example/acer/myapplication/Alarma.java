package com.example.acer.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


import java.util.Calendar;

/**
 * Esta clase Alarma establece funciones que inician o cancelan la ejecucion de un codigo,definido en otra clase,
 * cada intervalo de tiempo, sin importar que la aplicacion este abierta o cerrada.
 *
 * Por defecto, el intervalo de repeticion de la alarma es por dia
 *
 * Esta clase implementa la clase AlarmReceiver
 */

public class Alarma {

    private Context context;
    private PendingIntent pendingIntent;
    private int interval = 1000*60;// intervalo de repeticion  minuto


    /**
     *Constructor de la clase Alarma
     * @param context de tipo Context
     * @param cls de la clase que extiende de BroadcastReceiver, que contiene codigo se se ejecutará cada intervalo de tiempo
     *
     */
    public Alarma(Context context, Class<?> cls){
        //cls.asSubclass(BroadcastReceiver.class);
        Intent alarmIntent = new Intent(context, cls);
        pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        this.context = context;
    }

    /**
     * Inicia de forma explicita la ejecucion de la Alarma, cada cierto tiempo establecido.
     */

    public void start() {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval , pendingIntent);
        Toast.makeText(context, "Alarma iniciada", Toast.LENGTH_SHORT).show();
    }

    /**
     * Finaliza la ejecucion de la Alarma
     */

    public void cancel() {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(context, "Alarma finalizada", Toast.LENGTH_SHORT).show();
    }



}
