package com.example.proyectofinal_javiergarrido.ui.diario;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinal_javiergarrido.R;

public class DetalleNotaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_nota);

        TextView tvTitulo = findViewById(R.id.tv_titulo_detalle);
        TextView tvContenido = findViewById(R.id.tv_contenido_detalle);
        TextView tvFecha = findViewById(R.id.tv_fecha_detalle);

        String titulo = getIntent().getStringExtra("titulo");
        String contenido = getIntent().getStringExtra("contenido");
        String fecha = getIntent().getStringExtra("fecha");

        tvTitulo.setText(titulo);
        tvContenido.setText(contenido);
        tvFecha.setText(fecha);
    }
}
