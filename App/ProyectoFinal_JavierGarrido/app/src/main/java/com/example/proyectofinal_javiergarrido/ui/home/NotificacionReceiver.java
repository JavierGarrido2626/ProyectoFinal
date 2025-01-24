package com.example.proyectofinal_javiergarrido.ui.home;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import com.example.proyectofinal_javiergarrido.R;

public class NotificacionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String titulo = intent.getStringExtra("titulo");
        int idNotificacion = intent.getIntExtra("idNotificacion", 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alarma_channel")
                .setSmallIcon(R.drawable.logomejoradomodified)
                .setContentTitle("Notificación programada")
                .setContentText(titulo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        Intent intentAbrirApp = new Intent(context, ListaNotificacionActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                idNotificacion,
                intentAbrirApp,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(idNotificacion, builder.build());
        }
    }
}
