package com.example.proyectofinal_javiergarrido;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistroEstadisticas extends AppCompatActivity {

    // private static final String BASE_URL = "http://192.168.1.133:5000"; // WIFI
    private static final String BASE_URL = "http://192.168.115.103:5000"; //Movil

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_estadisticas);

        EditText edNombre = findViewById(R.id.edNombre);
        EditText edApellidos = findViewById(R.id.edApellidos);
        EditText edUsuario = findViewById(R.id.edUsuarioRegistro);
        EditText edContrasena = findViewById(R.id.edContrasenaRegistro);
        EditText edCorreo = findViewById(R.id.edCorreo);
        Button btnRegistrar = findViewById(R.id.btnRegistrar);
        Button btnVolverLogin = findViewById(R.id.btnVolverLoginRegistro);

        btnRegistrar.setOnClickListener(v -> {
            String nombre = edNombre.getText().toString().trim();
            String apellidos = edApellidos.getText().toString().trim();
            String usuario = edUsuario.getText().toString().trim();
            String contrasena = edContrasena.getText().toString().trim();
            String correo = edCorreo.getText().toString().trim();

            if (nombre.isEmpty() || apellidos.isEmpty() || usuario.isEmpty() || contrasena.isEmpty() || correo.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            } else if (!correo.contains("@")) {
                Toast.makeText(this, "Por favor ingresa un correo válido que contenga '@'", Toast.LENGTH_SHORT).show();
            } else {
                registrarUsuario(nombre, apellidos, usuario, contrasena, correo);
            }
        });

        btnVolverLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegistroEstadisticas.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registrarUsuario(String nombre, String apellidos, String usuario, String contrasena, String correo) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            try {
                JSONObject json = new JSONObject();
                json.put("nombre", nombre);
                json.put("apellidos", apellidos);
                json.put("usuario", usuario);
                json.put("contrasena", contrasena);
                json.put("correo", correo);

                RequestBody body = RequestBody.create(json.toString(), JSON);
                Request request = new Request.Builder()
                        .url(BASE_URL + "/registrar_usuario")
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                String responseData = response.body() != null ? response.body().string() : "No response body";

                Log.d("RegistroUsuario", "Request: " + json.toString());
                Log.d("RegistroUsuario", "Response Code: " + response.code());
                Log.d("RegistroUsuario", "Response Data: " + responseData);

                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        if (responseData.contains("El nombre de usuario ya está en uso")) {
                            Toast.makeText(this, "El nombre de usuario ya está en uso. Intenta con otro.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Error al registrar usuario: " + responseData, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show());
                Log.e("RegistroUsuario", "Error: " + e.getMessage(), e);
            }
        }).start();
    }
}