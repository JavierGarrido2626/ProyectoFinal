package com.example.proyectofinal_javiergarrido.ui.estadisticas.Trabalenguas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinal_javiergarrido.R;
import com.example.proyectofinal_javiergarrido.ui.ServiciosServer.ClienteApi;
import com.example.proyectofinal_javiergarrido.ui.ServiciosServer.ServicioApi;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstadisticasJuegoTrabalenguas extends AppCompatActivity {

    private RecyclerView recyclerViewFacil, recyclerViewMedio, recyclerViewDificil;
    private String contenido = "";
    private static final int REQUEST_CODE_CREATE_FILE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas_trabalenguas);

        recyclerViewFacil = findViewById(R.id.recyclerViewFacil);
        recyclerViewMedio = findViewById(R.id.recyclerViewMedio);
        recyclerViewDificil = findViewById(R.id.recyclerViewDificil);

        recyclerViewFacil.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMedio.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDificil.setLayoutManager(new LinearLayoutManager(this));

        Button btnExportar = findViewById(R.id.btnExportar);
        btnExportar.setOnClickListener(v -> exportarEstadisticas());

        SharedPreferences preferencias = getSharedPreferences("SesionUsuario", MODE_PRIVATE);
        int idUsuario = preferencias.getInt("idUsuario", -1);

        if (idUsuario == -1) {
            Toast.makeText(this, "Error: No se encontró el ID de usuario.", Toast.LENGTH_SHORT).show();
        } else {
            cargarEstadisticas(idUsuario, "Facil", recyclerViewFacil);
            cargarEstadisticas(idUsuario, "Medio", recyclerViewMedio);
            cargarEstadisticas(idUsuario, "Dificil", recyclerViewDificil);
        }
    }

    private void cargarEstadisticas(int idUsuario, String nivel, RecyclerView recyclerView) {
        ServicioApi servicioApi = ClienteApi.getClient().create(ServicioApi.class);
        Call<List<EstadisticasJuegoTrabalenguasObjeto>> call = servicioApi.obtenerEstadisticasTrabalenguas(idUsuario, nivel);

        call.enqueue(new Callback<List<EstadisticasJuegoTrabalenguasObjeto>>() {
            @Override
            public void onResponse(Call<List<EstadisticasJuegoTrabalenguasObjeto>> call, Response<List<EstadisticasJuegoTrabalenguasObjeto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<EstadisticasJuegoTrabalenguasObjeto> estadisticas = response.body();

                    if (estadisticas.isEmpty()) {
                        for (int i = 0; i < 5; i++) {
                            estadisticas.add(new EstadisticasJuegoTrabalenguasObjeto());
                        }
                    }

                    EstadisticasJuegoTrabalenguasAdapter adaptador = new EstadisticasJuegoTrabalenguasAdapter(estadisticas);
                    recyclerView.setAdapter(adaptador);

                    agregarEstadisticas(nivel, estadisticas);
                } else {
                    Toast.makeText(EstadisticasJuegoTrabalenguas.this, "No se pudo cargar datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<EstadisticasJuegoTrabalenguasObjeto>> call, Throwable t) {
                Log.e("API Error", "Error al obtener estadísticas: " + t.getMessage());
                Toast.makeText(EstadisticasJuegoTrabalenguas.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void agregarEstadisticas(String nivel, List<EstadisticasJuegoTrabalenguasObjeto> estadisticas) {
        contenido += "\n\nNivel: " + nivel + "\n";
        for (EstadisticasJuegoTrabalenguasObjeto estadistica : estadisticas) {
            contenido += "Tiempo: " + estadistica.getTiempo_total() + " segundos\n";
        }
    }

    private void exportarEstadisticas() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "EstadisticasJuego_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(System.currentTimeMillis()) + ".txt");
        startActivityForResult(intent, REQUEST_CODE_CREATE_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CREATE_FILE && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            try (OutputStream outputStream = getContentResolver().openOutputStream(uri);
                 OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {

                writer.write(contenido);
                Toast.makeText(this, "Estadísticas exportadas correctamente.", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                Toast.makeText(this, "Error al guardar el archivo.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}
