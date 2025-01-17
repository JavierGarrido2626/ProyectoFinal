package com.example.proyectofinal_javiergarrido.ui.estadisticas.Colores;

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
import com.example.proyectofinal_javiergarrido.ui.estadisticas.ClienteApi;
import com.example.proyectofinal_javiergarrido.ui.estadisticas.ServicioApi;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstadisticasJuegoColor extends AppCompatActivity {

    private TextView rondaMasAlta;
    private TextView tiempoRondaMasAlta;
    private TextView rondaMenor;
    private TextView tiempoRondaMenor;
    private TextView rondaMedia;
    private TextView tiempoMedioPorRonda;
    private TextView nombreUsu;
    private ServicioApi servicioApi;
    private int idUsuario;

    private static final int REQUEST_CODE_CREATE_FILE = 1;
    private String contenido;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas_juegocolor);

        rondaMasAlta = findViewById(R.id.rondaMasAlta);
        tiempoRondaMasAlta = findViewById(R.id.tiempoRondaMasAlta);
        rondaMenor = findViewById(R.id.rondaMenor);
        tiempoRondaMenor = findViewById(R.id.tiempoRondaMenor);
        rondaMedia = findViewById(R.id.rondaMedia);
        tiempoMedioPorRonda = findViewById(R.id.tiempoMedioPorRonda);
        nombreUsu = findViewById(R.id.txtNombreUsu);

        if (rondaMasAlta == null || tiempoRondaMasAlta == null || rondaMenor == null ||
                tiempoRondaMenor == null || rondaMedia == null || tiempoMedioPorRonda == null || nombreUsu == null) {
            Toast.makeText(this, "Error: No se pudieron inicializar algunas vistas.", Toast.LENGTH_SHORT).show();
            return;
        }

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

        btnFacil.setOnClickListener(v -> obtenerEstadisticasPorNivel("facil"));
        btnMedio.setOnClickListener(v -> obtenerEstadisticasPorNivel("medio"));
        btnDificil.setOnClickListener(v -> obtenerEstadisticasPorNivel("dificil"));

        Button btnExportar = findViewById(R.id.btnExportar);
        btnExportar.setOnClickListener(v -> exportarEstadisticas());
    }

    private void obtenerEstadisticasPorNivel(String nivel) {
        if (idUsuario == -1) {
            Toast.makeText(this, "Error: No se encontró el ID de usuario.", Toast.LENGTH_SHORT).show();
            return;
        }

        System.out.println("Solicitando estadísticas para idUsuario: " + idUsuario + " y nivel: " + nivel);

        servicioApi.obtenerEstadisticasJuegoColor(idUsuario, nivel).enqueue(new Callback<EstadisticasJuegoColorObjeto>() {
            @Override
            public void onResponse(Call<EstadisticasJuegoColorObjeto> call, Response<EstadisticasJuegoColorObjeto> response) {
                System.out.println("Respuesta de la API - Código de respuesta: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    EstadisticasJuegoColorObjeto estadisticas = response.body();

                    System.out.println("Estadísticas obtenidas: " + estadisticas);

                    mostrarEstadisticas(estadisticas, nivel);
                } else {
                    System.out.println("Error en la respuesta del servidor. Código de error: " + response.code());
                    Toast.makeText(EstadisticasJuegoColor.this, "Error en la respuesta del servidor. Intenta más tarde.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EstadisticasJuegoColorObjeto> call, Throwable t) {
                System.out.println("Error de red al hacer la solicitud: " + t.getMessage());
                Toast.makeText(EstadisticasJuegoColor.this, "Error de red al cargar estadísticas.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarEstadisticas(EstadisticasJuegoColorObjeto estadisticas, String nivel) {
        int rondaAlta = 0;
        int tiempoRondaAlta = 0;
        int rondaBaja = 0;
        int tiempoRondaBaja = 0;
        double rondaMediaValor = 0.0;
        double tiempoMediaRonda = 0.0;

        if (nivel.equals("facil")) {
            rondaAlta = estadisticas.getRonda_mas_alta();
            tiempoRondaAlta = estadisticas.getTiempo_ronda_mas_alta();
            rondaBaja = estadisticas.getRonda_menor();
            tiempoRondaBaja = estadisticas.getTiempo_ronda_menor();
            rondaMediaValor = estadisticas.getRonda_media();
            tiempoMediaRonda = estadisticas.getTiempo_medio_por_ronda();


        } else if (nivel.equals("medio")) {
            rondaAlta = estadisticas.getRonda_mas_alta();
            tiempoRondaAlta = estadisticas.getTiempo_ronda_mas_alta();
            rondaBaja = estadisticas.getRonda_menor();
            tiempoRondaBaja = estadisticas.getTiempo_ronda_menor();
            rondaMediaValor = estadisticas.getRonda_media();
            tiempoMediaRonda = estadisticas.getTiempo_medio_por_ronda();


        } else if (nivel.equals("dificil")) {
            rondaAlta = estadisticas.getRonda_mas_alta();
            tiempoRondaAlta = estadisticas.getTiempo_ronda_mas_alta();
            rondaBaja = estadisticas.getRonda_menor();
            tiempoRondaBaja = estadisticas.getTiempo_ronda_menor();
            rondaMediaValor = estadisticas.getRonda_media();
            tiempoMediaRonda = estadisticas.getTiempo_medio_por_ronda();
        }

        rondaMasAlta.setText(String.format("Ronda Más Alta: %d", rondaAlta));
        tiempoRondaMasAlta.setText(String.format("Tiempo Ronda Más Alta: %d", tiempoRondaAlta));
        rondaMenor.setText(String.format("Ronda Menor: %d", rondaBaja));
        tiempoRondaMenor.setText(String.format("Tiempo Ronda Menor: %d", tiempoRondaBaja));
        rondaMedia.setText(String.format("Ronda Media: %.2f", rondaMediaValor));
        tiempoMedioPorRonda.setText(String.format("Tiempo Medio por Ronda: %.2f", tiempoMediaRonda));
    }

    private void exportarEstadisticas() {
        if (rondaMasAlta.getText().toString().isEmpty() ||
                tiempoRondaMasAlta.getText().toString().isEmpty() ||
                rondaMenor.getText().toString().isEmpty() ||
                tiempoRondaMenor.getText().toString().isEmpty() ||
                rondaMedia.getText().toString().isEmpty() ||
                tiempoMedioPorRonda.getText().toString().isEmpty()) {

            Toast.makeText(this, "No se han obtenido las estadísticas. Por favor, comprueba primero.", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombreJuego = "Juego de Colores";
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        contenido = String.format("Juego: %s\nFecha: %s\nRonda Más Alta: %s\nTiempo Ronda Más Alta: %s\nRonda Menor: %s\nTiempo Ronda Menor: %s\nRonda Media: %s\nTiempo Medio por Ronda: %s",
                nombreJuego,
                fecha,
                rondaMasAlta.getText().toString(),
                tiempoRondaMasAlta.getText().toString(),
                rondaMenor.getText().toString(),
                tiempoRondaMenor.getText().toString(),
                rondaMedia.getText().toString(),
                tiempoMedioPorRonda.getText().toString());

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "Juego_de_Colores_" + fecha + ".txt"); // Nombre por defecto del archivo

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
