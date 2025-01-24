package com.example.proyectofinal_javiergarrido;

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

import com.example.proyectofinal_javiergarrido.LoginRegistro.LoginActivity;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONObject;

public class Ajustes extends AppCompatActivity {

    private static final String PREFERENCIAS_NOMBRE = "SesionUsuario";
    private static final String CLAVE_ID_USUARIO = "idUsuario";
    private static final String URL_BASE = "http://192.168.115.103:5000";

    private Button btnCambiarNombreUsuario;
    private Button btnCambiarContrasena;
    private Button btnCambiarCorreo;
    private Button btnEliminarCuenta;
    private Button btnVolverInicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        btnCambiarNombreUsuario = findViewById(R.id.btnCambiarNombreUsuario);
        btnCambiarContrasena = findViewById(R.id.btnCambiarContrasena);
        btnCambiarCorreo = findViewById(R.id.btnCambiarCorreo);
        btnEliminarCuenta = findViewById(R.id.btnEliminarCuenta);
        btnVolverInicio = findViewById(R.id.btnInicioAjustes);

        btnVolverInicio.setOnClickListener(v -> {
            Intent intent = new Intent(Ajustes.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btnCambiarNombreUsuario.setOnClickListener(v -> cambiarNombreUsuario());
        btnCambiarContrasena.setOnClickListener(v -> cambiarContrasena());
        btnCambiarCorreo.setOnClickListener(v -> cambiarCorreo());
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
                if (!nuevoNombre.isEmpty() && esNombreUsuarioValido(nuevoNombre)) {
                    actualizarNombreUsuario(idUsuario, nuevoNombre);
                } else {
                    Toast.makeText(this, "El nombre no puede estar vacío y solo puede contener letras, números y los caracteres ._-", Toast.LENGTH_SHORT).show();
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

            final EditText entradaTexto1 = new EditText(this);
            entradaTexto1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(entradaTexto1);

            builder.setMessage("Introduce tu nueva contraseña:");

            builder.setPositiveButton("Confirmar", (dialog, which) -> {
                String nuevaContrasena1 = entradaTexto1.getText().toString().trim();

                final EditText entradaTexto2 = new EditText(this);
                entradaTexto2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(entradaTexto2);
                builder.setMessage("Confirma tu nueva contraseña:");

                builder.setPositiveButton("Confirmar", (dialog1, which1) -> {
                    String nuevaContrasena2 = entradaTexto2.getText().toString().trim();
                    if (nuevaContrasena1.equals(nuevaContrasena2) && !nuevaContrasena1.isEmpty()) {
                        actualizarContrasena(idUsuario, nuevaContrasena1);
                    } else {
                        Toast.makeText(this, "Las contraseñas no coinciden, por favor inténtelo de nuevo", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Cancelar", (dialog1, which1) -> dialog1.cancel());
                builder.show();
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            builder.show();
        } else {
            Toast.makeText(this, "Usuario no encontrado en la sesión", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarContrasena(int idUsuario, String nuevaContrasena) {
        if (!esContrasenaValida(nuevaContrasena)) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres, incluir una mayúscula y un número", Toast.LENGTH_SHORT).show();
            return;
        }

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


    private void cambiarCorreo() {
        SharedPreferences preferencias = getSharedPreferences(PREFERENCIAS_NOMBRE, MODE_PRIVATE);
        int idUsuario = preferencias.getInt(CLAVE_ID_USUARIO, -1);

        if (idUsuario != -1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Cambiar Correo Electrónico");

            final EditText entradaTexto = new EditText(this);
            entradaTexto.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            builder.setView(entradaTexto);

            builder.setPositiveButton("Confirmar", (dialog, which) -> {
                String nuevoCorreo = entradaTexto.getText().toString().trim();
                if (!nuevoCorreo.isEmpty() && esCorreoValido(nuevoCorreo)) {
                    actualizarCorreo(idUsuario, nuevoCorreo);
                } else {
                    Toast.makeText(this, "Por favor, ingrese un correo electrónico válido. Asegúrese de que contenga '@' y un dominio como '.com'.", Toast.LENGTH_LONG).show();
                }
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            builder.show();
        } else {
            Toast.makeText(this, "Usuario no encontrado en la sesión", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarCorreo(int idUsuario, String nuevoCorreo) {
        new Thread(() -> {
            OkHttpClient cliente = new OkHttpClient();
            try {
                JSONObject json = new JSONObject();
                json.put("id_usuario", idUsuario);
                json.put("nuevo_correo", nuevoCorreo);

                RequestBody cuerpoSolicitud = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
                Request solicitud = new Request.Builder()
                        .url(URL_BASE + "/cambiar_correo")
                        .post(cuerpoSolicitud)
                        .build();

                Response respuesta = cliente.newCall(solicitud).execute();
                String datosRespuesta = respuesta.body() != null ? respuesta.body().string() : "";

                runOnUiThread(() -> {
                    if (respuesta.isSuccessful()) {
                        Toast.makeText(this, "Correo actualizado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error al actualizar el correo: " + datosRespuesta, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private boolean esCorreoValido(String correo) {
        String patronCorreo = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return correo.matches(patronCorreo);
    }


    private boolean esNombreUsuarioValido(String nombreUsuario) {
        String patronNombreUsuario = "^[a-zA-Z0-9._%+-]+$";
        return nombreUsuario.matches(patronNombreUsuario) && !nombreUsuario.startsWith(".") && !nombreUsuario.endsWith(".") && !nombreUsuario.contains("..");
    }
    private boolean esContrasenaValida(String contrasena) {
        return contrasena.length() >= 6 && contrasena.matches(".*[A-Z].*") && contrasena.matches(".*[0-9].*");
    }


    private void eliminarCuenta() {
        SharedPreferences preferencias = getSharedPreferences(PREFERENCIAS_NOMBRE, MODE_PRIVATE);
        int idUsuario = preferencias.getInt(CLAVE_ID_USUARIO, -1);

        if (idUsuario != -1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("¿Estás seguro de que deseas eliminar tu cuenta?");
            builder.setMessage("¡Esta acción no se puede deshacer!");

            builder.setPositiveButton("Sí", (dialog, which) -> eliminarCuentaDelServidor(idUsuario));
            builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
            builder.show();
        } else {
            Toast.makeText(this, "No se encontró el usuario", Toast.LENGTH_SHORT).show();
        }
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
