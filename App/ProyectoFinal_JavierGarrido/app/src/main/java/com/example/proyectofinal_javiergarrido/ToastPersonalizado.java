package com.example.proyectofinal_javiergarrido;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
public class ToastPersonalizado {

    public static void show(Context context, String message) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_personalizado, null);

        TextView text = layout.findViewById(R.id.toastMessage);
        ImageView icon = layout.findViewById(R.id.toastIcon);

        text.setText(message);
        icon.setImageResource(R.drawable.logomejoradomodified);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
