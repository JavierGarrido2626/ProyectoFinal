package com.example.proyectofinal_javiergarrido.LoginRegistro;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinal_javiergarrido.R;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistroEstadisticas extends AppCompatActivity {

    private static final String BASE_URL = "http://192.168.115.103:5000";
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
        ImageView imgIconoContra = findViewById(R.id.imgIconoContraRegistro);

        imgIconoContra.setOnClickListener(v -> {
            if (edContrasena.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                edContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                imgIconoContra.setImageResource(R.drawable.ojoabierto);
            } else {
                edContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                imgIconoContra.setImageResource(R.drawable.ojocerrado);
            }
            edContrasena.setSelection(edContrasena.getText().length());
        });

        btnRegistrar.setOnClickListener(v -> {
            String nombre = edNombre.getText().toString().trim();
            String apellidos = edApellidos.getText().toString().trim();
            String usuario = edUsuario.getText().toString().trim();
            String contrasena = edContrasena.getText().toString().trim();
            String correo = edCorreo.getText().toString().trim();

            if (nombre.isEmpty() || apellidos.isEmpty() || usuario.isEmpty() || contrasena.isEmpty() || correo.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            } else if (!esCorreoValido(correo)) {
                Toast.makeText(this, "Por favor ingresa un correo válido", Toast.LENGTH_SHORT).show();
            } else if (!esUsuarioValido(usuario)) {
                Toast.makeText(this, "El nombre de usuario debe tener al menos 3 caracteres y contener solo letras, números o tildes", Toast.LENGTH_SHORT).show();
            } else if (!esApellidoValido(apellidos)) {
                Toast.makeText(this, "El apellido debe tener al menos 3 caracteres y contener solo letras o tildes", Toast.LENGTH_SHORT).show();
            } else if (!esNombreValido(nombre)) {
                Toast.makeText(this, "El nombre debe tener al menos 3 caracteres y contener solo letras o tildes", Toast.LENGTH_SHORT).show();
            } else if (!esContrasenaValida(contrasena)) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres, incluir una mayúscula y un número", Toast.LENGTH_LONG).show();
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

    private boolean esCorreoValido(String correo) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return correo.matches(regex);
    }

    private boolean esUsuarioValido(String usuario) {
        return usuario.length() >= 3 && usuario.matches("[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ]+");
    }

    private boolean esApellidoValido(String apellido) {
        return apellido.length() >= 3 && apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ]+");
    }

    private boolean esNombreValido(String nombre) {
        return nombre.length() >= 3 && nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ]+");
    }

    private boolean esContrasenaValida(String contrasena) {
        return contrasena.length() >= 6 && contrasena.matches(".*[A-Z].*") && contrasena.matches(".*[0-9].*");
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
