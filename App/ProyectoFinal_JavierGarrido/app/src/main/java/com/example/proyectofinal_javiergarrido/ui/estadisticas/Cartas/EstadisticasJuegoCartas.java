package com.example.proyectofinal_javiergarrido.ui.estadisticas.Cartas;

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

public class EstadisticasJuegoCartas extends AppCompatActivity {

    private TextView textVictorias;
    private TextView textDerrotas;
    private TextView textIntentos;
    private TextView nombreUsu;
    private ServicioApi servicioApi;
    private int idUsuario;

    private static final int REQUEST_CODE_CREATE_FILE = 1;

    private String contenido;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_estadisticas_juegocartas);

        textVictorias = findViewById(R.id.txtVictorias);
        textDerrotas = findViewById(R.id.txtDerrotas);
        textIntentos = findViewById(R.id.txtIntentos);
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

        Button btnFacil = findViewById(R.id.btnFacil);
        Button btnMedio = findViewById(R.id.btnMedio);
        Button btnDificil = findViewById(R.id.btnDificil);
        Button btnExportar = findViewById(R.id.btnExportar);

        btnFacil.setOnClickListener(v -> obtenerEstadisticasPorNivel("facil"));
        btnMedio.setOnClickListener(v -> obtenerEstadisticasPorNivel("medio"));
        btnDificil.setOnClickListener(v -> obtenerEstadisticasPorNivel("dificil"));

        btnExportar.setOnClickListener(v -> exportarDatos());
    }

    private void obtenerEstadisticasPorNivel(String nivel) {
        if (idUsuario == -1) {
            Toast.makeText(this, "Error: No se encontró el ID de usuario.", Toast.LENGTH_SHORT).show();
            return;
        }

        servicioApi.obtenerEstadisticasVoltearcartas(idUsuario, nivel).enqueue(new Callback<EstadisticasCartasObjeto>() {
            @Override
            public void onResponse(Call<EstadisticasCartasObjeto> call, Response<EstadisticasCartasObjeto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    EstadisticasCartasObjeto estadisticas = response.body();
                    mostrarEstadisticas(estadisticas);
                } else {
                    Toast.makeText(EstadisticasJuegoCartas.this, "Error en la respuesta del servidor.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EstadisticasCartasObjeto> call, Throwable t) {
                Toast.makeText(EstadisticasJuegoCartas.this, "Error de red al cargar estadísticas.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarEstadisticas(EstadisticasCartasObjeto estadisticas) {
        int victorias = estadisticas.getVictorias();
        int derrotas = estadisticas.getDerrotas();
        int intentos = estadisticas.getTotalIntentos();

        textVictorias.setText(String.format("Victorias: %d", victorias));
        textDerrotas.setText(String.format("Derrotas: %d", derrotas));
        textIntentos.setText(String.format("Intentos: %d", intentos));
    }

    private void exportarDatos() {
        if (textVictorias.getText().toString().isEmpty() ||
                textDerrotas.getText().toString().isEmpty() ||
                textIntentos.getText().toString().isEmpty()) {
            Toast.makeText(this, "No se han obtenido las estadísticas. Por favor, comprueba primero.", Toast.LENGTH_SHORT).show();
            return;
        }

        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        contenido = String.format("Estadísticas del Juego de Cartas\nFecha: %s\nVictorias: %s\nDerrotas: %s\nIntentos: %s",
                fecha,
                textVictorias.getText().toString(),
                textDerrotas.getText().toString(),
                textIntentos.getText().toString());

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "Estadisticas_Juego_Cartas_" + fecha + ".txt");

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
