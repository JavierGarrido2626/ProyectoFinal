package com.example.proyectofinal_javiergarrido;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinal_javiergarrido.ui.ServiciosServer.ClienteApi;
import com.example.proyectofinal_javiergarrido.ui.ServiciosServer.ServicioApi;
import com.example.proyectofinal_javiergarrido.ui.diario.Nota;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearNotaActivity extends AppCompatActivity {

    private EditText etTituloNota;
    private EditText etContenidoNota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_nota);

        etTituloNota = findViewById(R.id.et_titulo_nota);
        etContenidoNota = findViewById(R.id.et_contenido_nota);
    }

    public void guardarNota(View view) {

        String titulo = etTituloNota.getText().toString().trim();
        String contenido = etContenidoNota.getText().toString().trim();
        int idUsuario = obtenerIdUsuario();

        if (titulo.isEmpty() || contenido.isEmpty()) {
            Toast.makeText(this, "Por favor, completa el título y el contenido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (idUsuario == -1) {
            Toast.makeText(this, "Error al obtener el ID de usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm", new Locale("es", "ES"));
        String fecha = sdfFecha.format(new Date());
        String hora = sdfHora.format(new Date());

        String fechaHora = fecha + " " + hora;

        Nota nuevaNota = new Nota();
        nuevaNota.setTitulo(titulo);
        nuevaNota.setContenido(contenido);
        nuevaNota.setFecha(fechaHora);
        nuevaNota.setIdUsuario(idUsuario);

        ServicioApi servicioApi = ClienteApi.getClient().create(ServicioApi.class);
        Call<Nota> call = servicioApi.guardarNota(nuevaNota);

        call.enqueue(new Callback<Nota>() {
            @Override
            public void onResponse(Call<Nota> call, Response<Nota> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CrearNotaActivity.this, "Nota creada con éxito", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(CrearNotaActivity.this, "Error al guardar la nota", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Nota> call, Throwable t) {
                Toast.makeText(CrearNotaActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int obtenerIdUsuario() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("idUsuario", -1);
    }
}
