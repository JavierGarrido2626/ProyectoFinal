package com.example.proyectofinal_javiergarrido.ui.estadisticas.Contar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinal_javiergarrido.R;
import com.example.proyectofinal_javiergarrido.ui.ServiciosServer.ClienteApi;
import com.example.proyectofinal_javiergarrido.ui.ServiciosServer.ServicioApi;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstadisticasJuegoContar extends AppCompatActivity {

    private TextView contadorMayor;
    private TextView contadorMenor;
    private TextView contadorMedio;
    private TextView nombreUsu;
    private ServicioApi servicioApi;
    private int idUsuario;

    private static final int REQUEST_CODE_CREATE_FILE = 1;

    private String contenido;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas_juegocontar);

        contadorMayor = findViewById(R.id.contadorMayor);
        contadorMenor = findViewById(R.id.contadorMenor);
        contadorMedio = findViewById(R.id.contadorMedio);
        nombreUsu = findViewById(R.id.txtNombreUsu);

        SharedPreferences preferencias = getSharedPreferences("SesionUsuario", MODE_PRIVATE);
        idUsuario = preferencias.getInt("idUsuario", -1);

        if (idUsuario == -1) {
            Toast.makeText(this, "Error: No se encontró el ID de usuario.", Toast.LENGTH_SHORT).show();
        } else {
            String nombreUsuario = preferencias.getString("nombreUsuario", "Usuario");
            nombreUsu.setText(String.format("Bienvenido, %s", nombreUsuario));
        }

        servicioApi = ClienteApi.getClient().create(ServicioApi.class);

        Button btnComprobar = findViewById(R.id.btnComprobar);
        btnComprobar.setOnClickListener(v -> obtenerEstadisticasJuegoContar());

        Button btnExportar = findViewById(R.id.btnExportar);
        btnExportar.setOnClickListener(v -> exportarEstadisticas());
    }

    private void obtenerEstadisticasJuegoContar() {
        if (idUsuario == -1) {
            Toast.makeText(this, "Error: No se encontró el ID de usuario.", Toast.LENGTH_SHORT).show();
            return;
        }

        servicioApi.obtenerEstadisticasJuegoContar(idUsuario).enqueue(new Callback<EstadisticasJuegoContarObjeto>() {
            @Override
            public void onResponse(Call<EstadisticasJuegoContarObjeto> call, Response<EstadisticasJuegoContarObjeto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    EstadisticasJuegoContarObjeto estadisticas = response.body();
                    mostrarEstadisticas(estadisticas);
                } else {
                    Toast.makeText(EstadisticasJuegoContar.this, "Error en la respuesta del servidor.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EstadisticasJuegoContarObjeto> call, Throwable t) {
                Toast.makeText(EstadisticasJuegoContar.this, "Error de red.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarEstadisticas(EstadisticasJuegoContarObjeto estadisticas) {
        contadorMayor.setText(String.format("Contador Mayor: %d", estadisticas.getContadorMayor()));
        contadorMenor.setText(String.format("Contador Menor: %d", estadisticas.getContadorMenor()));
        contadorMedio.setText(String.format("Contador Medio: %.2f", estadisticas.getContadorMedio()));
    }

    private void exportarEstadisticas() {
        if (contadorMayor.getText().toString().isEmpty() ||
                contadorMenor.getText().toString().isEmpty() ||
                contadorMedio.getText().toString().isEmpty()) {

            Toast.makeText(this, "No se han obtenido las estadísticas. Por favor, comprueba primero.", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombreJuego = "Juego de Contar Objetos";
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        contenido = String.format("Juego: %s\nFecha: %s\nContador Mayor: %s\nContador Menor: %s\nContador Medio: %s",
                nombreJuego,
                fecha,
                contadorMayor.getText().toString(),
                contadorMenor.getText().toString(),
                contadorMedio.getText().toString());

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "Juego_de_Contar_" + fecha + ".txt");

        startActivityForResult(intent, REQUEST_CODE_CREATE_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CREATE_FILE && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
                if (outputStream != null) {
                    OutputStreamWriter writer = new OutputStreamWriter(outputStream);
                    writer.write(contenido);
                    writer.close();

                    Toast.makeText(this, "Estadísticas exportadas exitosamente.", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al exportar las estadísticas.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
