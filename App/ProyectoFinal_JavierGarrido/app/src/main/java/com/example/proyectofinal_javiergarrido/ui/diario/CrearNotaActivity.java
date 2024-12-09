package com.example.proyectofinal_javiergarrido;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinal_javiergarrido.ui.diario.Nota;
import com.example.proyectofinal_javiergarrido.ui.diario.BaseDatosDiarioSQL;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CrearNotaActivity extends AppCompatActivity {

    private EditText etTituloNota;
    private EditText etContenidoNota;
    private BaseDatosDiarioSQL baseDatos;
    private int notaId;
    private String colorSeleccionado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_nota);

        etTituloNota = findViewById(R.id.et_titulo_nota);
        etContenidoNota = findViewById(R.id.et_contenido_nota);
        baseDatos = new BaseDatosDiarioSQL(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            notaId = intent.getIntExtra("id", -1);
            String titulo = intent.getStringExtra("titulo");
            String contenido = intent.getStringExtra("contenido");
            colorSeleccionado = intent.getStringExtra("color");

            etTituloNota.setText(titulo);
            etContenidoNota.setText(contenido);
        } else {
            notaId = -1;
        }
    }

    public void guardarNota(View view) {
        String titulo = etTituloNota.getText().toString().trim();
        String contenido = etContenidoNota.getText().toString().trim();

        if (titulo.isEmpty() || contenido.isEmpty()) {
            Toast.makeText(this, "Por favor, completa el título y el contenido", Toast.LENGTH_SHORT).show();
            return;
        }

        String fecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        if (notaId != -1) {
            Nota notaExistente = new Nota();
            notaExistente.setId(notaId);
            notaExistente.setTitulo(titulo);
            notaExistente.setContenido(contenido);
            notaExistente.setFecha(fecha);
            notaExistente.setColor(colorSeleccionado);

            boolean resultado = baseDatos.actualizarNota(notaExistente);

            if (resultado) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("notaActualizada", notaExistente);
                setResult(RESULT_OK, resultIntent);
                Toast.makeText(this, "Nota actualizada con éxito", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al actualizar la nota", Toast.LENGTH_SHORT).show();
            }
        } else {
            Nota nuevaNota = new Nota(titulo, contenido, fecha, colorSeleccionado);
            long idNota = baseDatos.agregarNota(nuevaNota);
            if (idNota != -1) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("nuevaNota", nuevaNota);
                setResult(RESULT_OK, resultIntent);
                Toast.makeText(this, "Nota creada con éxito", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al guardar la nota", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
