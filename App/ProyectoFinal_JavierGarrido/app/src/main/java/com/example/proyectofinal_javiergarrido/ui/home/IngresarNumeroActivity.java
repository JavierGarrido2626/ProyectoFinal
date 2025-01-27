package com.example.proyectofinal_javiergarrido.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinal_javiergarrido.R;
import com.example.proyectofinal_javiergarrido.ToastPersonalizado;
import com.example.proyectofinal_javiergarrido.ui.ServiciosServer.ClienteApi;
import com.example.proyectofinal_javiergarrido.ui.ServiciosServer.ServicioApi;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngresarNumeroActivity extends AppCompatActivity {

    private EditText etNumeroTelefono;
    private EditText etNumeroModificar;
    private CountryCodePicker ccp;
    private Button btnGuardarNumero;
    private Button btnSalirTelefono;
    private TextView txtNumeroGuardado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_numero);

        etNumeroTelefono = findViewById(R.id.ed_numero_telefono);
        etNumeroModificar = findViewById(R.id.ed_numero_modificar);
        ccp = findViewById(R.id.ccp);
        btnGuardarNumero = findViewById(R.id.btn_guardar_numero);
        btnSalirTelefono = findViewById(R.id.btn_SalirTelefono);
        txtNumeroGuardado = findViewById(R.id.txtNumeroGuardado);

        int idUsuario = obtenerIdUsuario();
        Log.d("ID_USUARIO", "ID del usuario: " + idUsuario);

        if (idUsuario == -1) {
            ToastPersonalizado.show(IngresarNumeroActivity.this, "Error: No se pudo obtener el ID de usuario.");
            finish();
            return;
        }

        obtenerNumeroGuardado(idUsuario);

        btnGuardarNumero.setOnClickListener(v -> {
            String numeroTelefono = etNumeroModificar.getText().toString().trim();
            String numeroTelefonoInsertado = etNumeroTelefono.getText().toString().trim();
            String codigoPais = "+" + ccp.getSelectedCountryCode();

            if (!numeroTelefono.isEmpty() && !numeroTelefonoInsertado.isEmpty()) {
                ToastPersonalizado.show(IngresarNumeroActivity.this, "No puedes usar ambos campos a la vez.");
                return;
            }

            if (!numeroTelefono.isEmpty()) {
                String numeroCompleto = codigoPais + numeroTelefono;

                if (esNumeroValido(numeroCompleto)) {
                    String numeroFormateado = formatearNumeroCompleto(codigoPais, numeroTelefono);
                    actualizarNumeroTelefono(idUsuario, numeroFormateado);
                } else {
                    ToastPersonalizado.show(IngresarNumeroActivity.this, "Número inválido.");
                }
            }
            else if (!numeroTelefonoInsertado.isEmpty()) {
                String numeroCompleto = codigoPais + numeroTelefonoInsertado;

                if (esNumeroValido(numeroCompleto)) {
                    String numeroFormateado = formatearNumeroCompleto(codigoPais, numeroTelefonoInsertado);
                    insertarNumeroTelefono(idUsuario, numeroFormateado);
                } else {
                    ToastPersonalizado.show(IngresarNumeroActivity.this, "Número inválido.");
                }
            } else {
                ToastPersonalizado.show(IngresarNumeroActivity.this, "Por favor, ingresa un número.");
            }
        });

        btnSalirTelefono.setOnClickListener(v -> {
            Intent i = new Intent(IngresarNumeroActivity.this, HomeFragment.class);
            startActivity(i);
            finish();
        });
    }

    private boolean esNumeroValido(String numero) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            String numeroLimpiado = numero.replaceAll("[^0-9]", "");
            Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(numero, null);
            return phoneUtil.isValidNumber(phoneNumber);
        } catch (Exception e) {
            Log.e("NumeroInvalido", "Error al validar el número", e);
            return false;
        }
    }

    private String formatearNumeroCompleto(String codigoPais, String numero) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            String numeroLimpiado = numero.replaceAll("[^0-9]", "");
            Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(codigoPais + numeroLimpiado, null);
            return phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (Exception e) {
            Log.e("FormatearError", "Error al formatear el número", e);
            return codigoPais + numero;
        }
    }

    private int obtenerIdUsuario() {
        SharedPreferences sharedPreferences = getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("idUsuario", -1);
    }

    private void obtenerNumeroGuardado(int idUsuario) {
        ServicioApi servicioApi = ClienteApi.getClient().create(ServicioApi.class);
        Call<ObtenerTelefonoObjeto> call = servicioApi.obtenerNumeroTelefono(idUsuario);
        call.enqueue(new Callback<ObtenerTelefonoObjeto>() {
            @Override
            public void onResponse(Call<ObtenerTelefonoObjeto> call, Response<ObtenerTelefonoObjeto> response) {
                if (response.isSuccessful()) {
                    ObtenerTelefonoObjeto respuesta = response.body();
                    if (respuesta != null && respuesta.getNumero_telefono() != null && !respuesta.getNumero_telefono().isEmpty()) {
                        txtNumeroGuardado.setText("Número guardado: " + respuesta.getNumero_telefono());
                    } else {
                        txtNumeroGuardado.setText("No tiene teléfono.");
                    }
                } else {
                    Log.e("ErrorObtenerNumero", "Error al obtener el número guardado.");
                    txtNumeroGuardado.setText("Error al obtener el número");
                }
            }

            @Override
            public void onFailure(Call<ObtenerTelefonoObjeto> call, Throwable t) {
                Log.e("ErrorObtenerNumero", "Fallo en la conexión con el servidor", t);
                txtNumeroGuardado.setText("Error en la conexión");
            }
        });
    }

    private void insertarNumeroTelefono(int idUsuario, String numero) {
        ServicioApi servicioApi = ClienteApi.getClient().create(ServicioApi.class);
        SolicitudNumeroTelefono solicitud = new SolicitudNumeroTelefono(idUsuario, numero);

        Call<Void> call = servicioApi.guardarTelefono(solicitud);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("InsertarNumero", "Código de respuesta: " + response.code());
                if (response.isSuccessful()) {
                    Log.d("TelefonoGuardado", "Número guardado correctamente.");
                    ToastPersonalizado.show(IngresarNumeroActivity.this, "Número guardado correctamente.");
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("numeroTelefono", numero);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    String errorMessage = "Error desconocido";
                    if (response.code() == 400) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("ErrorGuardarTelefono", "Respuesta del error: " + errorBody);
                            JSONObject jsonError = new JSONObject(errorBody);
                            String errorDetail = jsonError.getString("error");
                            errorMessage = errorDetail != null ? errorDetail : "Error desconocido";
                        } catch (IOException | JSONException e) {
                            Log.e("ErrorGuardarTelefono", "Error al leer el cuerpo del error", e);
                        }
                    } else if (response.code() == 500) {
                        errorMessage = "Ya hay un número registrado, si quieres poner un número nuevo, modifícalo en el texto de abajo.";
                    }
                    Log.e("ErrorGuardarTelefono", "Error al guardar el teléfono. Código de respuesta: " + response.code());
                    ToastPersonalizado.show(IngresarNumeroActivity.this, errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ErrorGuardarTelefono", "Fallo en la conexión con el servidor", t);
                ToastPersonalizado.show(IngresarNumeroActivity.this, "Fallo en la conexión con el servidor.");
            }
        });
    }

    private void actualizarNumeroTelefono(int idUsuario, String numero) {
        ServicioApi servicioApi = ClienteApi.getClient().create(ServicioApi.class);
        SolicitudNumeroTelefono solicitud = new SolicitudNumeroTelefono(idUsuario, numero);

        Call<Void> call = servicioApi.actualizarNumeroTelefono(solicitud);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("TelefonoActualizado", "Número actualizado correctamente.");
                    ToastPersonalizado.show(IngresarNumeroActivity.this, "Número actualizado correctamente.");
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("numeroTelefono", numero);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    String errorMessage = response.message();
                    Log.e("ErrorActualizarTelefono", errorMessage);
                    ToastPersonalizado.show(IngresarNumeroActivity.this, errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ErrorActualizarTelefono", "Fallo en la conexión con el servidor", t);
                ToastPersonalizado.show(IngresarNumeroActivity.this, "Fallo en la conexión con el servidor.");
            }
        });
    }
}