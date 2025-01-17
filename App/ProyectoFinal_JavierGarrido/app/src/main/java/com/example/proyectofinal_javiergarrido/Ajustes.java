package com.example.proyectofinal_javiergarrido;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONObject;

public class Ajustes extends AppCompatActivity {

    private static final String PREFERENCIAS_NOMBRE = "SesionUsuario";
    private static final String CLAVE_ID_USUARIO = "idUsuario";
 //   private static final String URL_BASE = "http://192.168.1.133:5000"; // WIFI
    private static final String URL_BASE = "http://192.168.115.103:5000"; // MOVIL



    private Button btnCambiarNombreUsuario;
    private Button btnCambiarContrasena;
    private Button btnEliminarCuenta;
    private Button btnVolverInicio;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        btnCambiarNombreUsuario = findViewById(R.id.btnCambiarNombreUsuario);
        btnCambiarContrasena = findViewById(R.id.btnCambiarContrasena);
        btnEliminarCuenta = findViewById(R.id.btnEliminarCuenta);
        btnVolverInicio = findViewById(R.id.btnInicioAjustes);  // Actualizado al ID correcto

        btnVolverInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ajustes.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnCambiarNombreUsuario.setOnClickListener(v -> cambiarNombreUsuario());

        btnCambiarContrasena.setOnClickListener(v -> cambiarContrasena());

        btnEliminarCuenta.setOnClickListener(v -> eliminarCuenta());
    }
    private void cambiarNombreUsuario() {
        SharedPreferences preferencias = getSharedPreferences(PREFERENCIAS_NOMBRE, MODE_PRIVATE);
        int idUsuario = preferencias.getInt(CLAVE_ID_USUARIO, -1);

        if (idUsuario != -1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Cambiar Nombre de Usuario");

            final EditText entradaTexto = new EditText(this);
            entradaTexto.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(entradaTexto);

            builder.setPositiveButton("Confirmar", (dialog, which) -> {
                String nuevoNombre = entradaTexto.getText().toString().trim();
                if (!nuevoNombre.isEmpty()) {
                    actualizarNombreUsuario(idUsuario, nuevoNombre);
                } else {
                    Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            builder.show();
        } else {
            Toast.makeText(this, "Usuario no encontrado en la sesión", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarNombreUsuario(int idUsuario, String nuevoNombre) {
        new Thread(() -> {
            OkHttpClient cliente = new OkHttpClient();
            try {
                JSONObject json = new JSONObject();
                json.put("id_usuario", idUsuario);
                json.put("nuevo_nombre", nuevoNombre);

                RequestBody cuerpoSolicitud = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
                Request solicitud = new Request.Builder()
                        .url(URL_BASE + "/cambiar_nombre_usuario")
                        .post(cuerpoSolicitud)
                        .build();

                Response respuesta = cliente.newCall(solicitud).execute();
                String datosRespuesta = respuesta.body() != null ? respuesta.body().string() : "";

                runOnUiThread(() -> {
                    if (respuesta.isSuccessful()) {
                        Toast.makeText(this, "Nombre de usuario actualizado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error al actualizar el nombre: " + datosRespuesta, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void cambiarContrasena() {
        SharedPreferences preferencias = getSharedPreferences(PREFERENCIAS_NOMBRE, MODE_PRIVATE);
        int idUsuario = preferencias.getInt(CLAVE_ID_USUARIO, -1);

        if (idUsuario != -1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Cambiar Contraseña");

            final EditText entradaTexto = new EditText(this);
            entradaTexto.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(entradaTexto);

            builder.setPositiveButton("Confirmar", (dialog, which) -> {
                String nuevaContrasena = entradaTexto.getText().toString().trim();
                if (!nuevaContrasena.isEmpty()) {
                    actualizarContrasena(idUsuario, nuevaContrasena);
                } else {
                    Toast.makeText(this, "La contraseña no puede estar vacía", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            builder.show();
        } else {
            Toast.makeText(this, "Usuario no encontrado en la sesión", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarContrasena(int idUsuario, String nuevaContrasena) {
        new Thread(() -> {
            OkHttpClient cliente = new OkHttpClient();
            try {
                JSONObject json = new JSONObject();
                json.put("id_usuario", idUsuario);
                json.put("nueva_contrasena", nuevaContrasena);

                RequestBody cuerpoSolicitud = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
                Request solicitud = new Request.Builder()
                        .url(URL_BASE + "/cambiar_contrasena")
                        .post(cuerpoSolicitud)
                        .build();

                Response respuesta = cliente.newCall(solicitud).execute();
                String datosRespuesta = respuesta.body() != null ? respuesta.body().string() : "";

                runOnUiThread(() -> {
                    if (respuesta.isSuccessful()) {
                        Toast.makeText(this, "Contraseña actualizada correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error al actualizar la contraseña: " + datosRespuesta, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void eliminarCuenta() {
        SharedPreferences preferencias = getSharedPreferences(PREFERENCIAS_NOMBRE, MODE_PRIVATE);
        int idUsuario = preferencias.getInt(CLAVE_ID_USUARIO, -1);

        if (idUsuario != -1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("¿Estás seguro de que deseas eliminar tu cuenta?");
            builder.setMessage("¡Esta acción no se puede deshacer!");

            builder.setPositiveButton("Sí", (dialog, which) -> {
                eliminarCuentaConConfirmacion(idUsuario);
            });

            builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());

            builder.show();
        } else {
            Toast.makeText(this, "No se encontró el usuario", Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarCuentaConConfirmacion(int idUsuario) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Estás seguro?");
        builder.setMessage("Se eliminará permanentemente tu cuenta. ¿Deseas continuar?");

        builder.setPositiveButton("Confirmar", (dialog, which) -> {
            eliminarCuentaDelServidor(idUsuario);
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void eliminarCuentaDelServidor(int idUsuario) {
        new Thread(() -> {
            OkHttpClient cliente = new OkHttpClient();
            try {
                JSONObject json = new JSONObject();
                json.put("id_usuario", idUsuario);

                RequestBody cuerpoSolicitud = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
                Request solicitud = new Request.Builder()
                        .url(URL_BASE + "/eliminar_cuenta")
                        .delete(cuerpoSolicitud)
                        .build();

                Response respuesta = cliente.newCall(solicitud).execute();
                String datosRespuesta = respuesta.body() != null ? respuesta.body().string() : "";

                runOnUiThread(() -> {
                    if (respuesta.isSuccessful()) {
                        Toast.makeText(this, "Cuenta eliminada exitosamente", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Ajustes.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(this, "Error al eliminar la cuenta: " + datosRespuesta, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

}
