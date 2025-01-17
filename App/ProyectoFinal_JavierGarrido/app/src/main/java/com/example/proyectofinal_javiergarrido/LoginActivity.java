package com.example.proyectofinal_javiergarrido;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {

    //private static final String URL_BASE = "http://192.168.1.133:5000"; //WIFI

    private static final String URL_BASE = "http://192.168.115.103:5000"; // MOVIL

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final String PREFS_NOMBRE = "SesionUsuario";
    private static final String CLAVE_SESION_INICIADA = "sesionIniciada";
    private static final String CLAVE_NOMBRE_USUARIO = "nombreUsuario";
    private static final String CLAVE_ID_USUARIO = "idUsuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText edUsuario = findViewById(R.id.edUsuario);
        EditText edContrasena = findViewById(R.id.edContrasena);
        Button btnInicio = findViewById(R.id.btnInicio);
        Button btnRegistro = findViewById(R.id.btnRegistro);

        btnInicio.setOnClickListener(v -> {
            String usuario = edUsuario.getText().toString().trim();
            String contrasena = edContrasena.getText().toString().trim();

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa usuario y contraseña", Toast.LENGTH_SHORT).show();
            } else {
                iniciarSesion(usuario, contrasena);
            }
        });

        btnRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistroEstadisticas.class);
            startActivity(intent);
        });
    }

    private void iniciarSesion(String usuario, String contrasena) {
        new Thread(() -> {
            OkHttpClient cliente = new OkHttpClient();
            try {
                JSONObject json = new JSONObject();
                json.put("usuario", usuario);
                json.put("contrasena", contrasena);

                RequestBody cuerpoSolicitud = RequestBody.create(json.toString(), JSON);
                Request solicitud = new Request.Builder()
                        .url(URL_BASE + "/login")
                        .post(cuerpoSolicitud)
                        .build();

                Response respuesta = cliente.newCall(solicitud).execute();
                String datosRespuesta = respuesta.body() != null ? respuesta.body().string() : "Sin cuerpo en la respuesta";

                Log.d("LoginActivity", "Respuesta del servidor: " + datosRespuesta);

                runOnUiThread(() -> {
                    if (respuesta.isSuccessful()) {
                        try {
                            JSONObject jsonResponse = new JSONObject(datosRespuesta);

                            if (jsonResponse.has("id_usuario") && jsonResponse.has("nombre")) {
                                int idUsuario = jsonResponse.getInt("id_usuario");
                                String nombreUsuario = jsonResponse.getString("nombre");
                                String mensaje = jsonResponse.optString("message", "Login exitoso");

                                SharedPreferences preferencias = getSharedPreferences(PREFS_NOMBRE, MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferencias.edit();
                                editor.putBoolean(CLAVE_SESION_INICIADA, true);
                                editor.putInt(CLAVE_ID_USUARIO, idUsuario);
                                editor.putString(CLAVE_NOMBRE_USUARIO, nombreUsuario);
                                editor.apply();

                                Log.d("LoginActivity", "ID de usuario guardado: " + idUsuario);
                                Log.d("LoginActivity", "Nombre de usuario guardado: " + nombreUsuario);

                                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.e("LoginActivity", "El campo 'id_usuario' o 'nombre' no está presente en la respuesta del servidor.");
                                Toast.makeText(this, "Error: No se encontró el ID o nombre del usuario.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(this, "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show();
                            Log.e("Login", "Error al procesar la respuesta: " + e.getMessage(), e);
                        }
                    } else {
                        Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show());
                Log.e("Login", "Error: " + e.getMessage(), e);
            }
        }).start();
    }
}