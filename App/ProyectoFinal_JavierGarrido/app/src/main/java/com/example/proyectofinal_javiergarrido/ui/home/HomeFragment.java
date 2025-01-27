package com.example.proyectofinal_javiergarrido.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.proyectofinal_javiergarrido.R;
import com.example.proyectofinal_javiergarrido.ui.ServiciosServer.ClienteApi;
import com.example.proyectofinal_javiergarrido.ui.ServiciosServer.ServicioApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private static final int SOLICITUD_NUMERO = 2;
    private String numeroTelefonoGuardado;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Button btnLlamada = root.findViewById(R.id.btn_llamadas);
        Button btnAlarma = root.findViewById(R.id.btn_alarmas);
        Button btnNumero = root.findViewById(R.id.btn_ingresar_numero);

        int idUsuario = obtenerIdUsuario();
        Log.d("HomeFragment", "ID de usuario: " + idUsuario);

        obtenerNumeroTelefono(idUsuario);

        btnAlarma.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(getContext(), ListaNotificacionActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Log.e("ErrorAlarma", "Error al abrir la actividad de la lista de alarmas", e);
                Toast.makeText(getContext(), "No se pudo abrir la lista de alarmas: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnNumero.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), IngresarNumeroActivity.class);
            startActivityForResult(intent, SOLICITUD_NUMERO);
        });

        btnLlamada.setOnClickListener(v -> {
            Log.d("HomeFragment", "Botón de llamada clicado");
            if (numeroTelefonoGuardado != null && !numeroTelefonoGuardado.isEmpty()) {
                realizarLlamada();
            } else {
                Toast.makeText(getContext(), "No hay número guardado. Ingresa uno primero.", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private void obtenerNumeroTelefono(int idUsuario) {
        ServicioApi servicioApi = ClienteApi.getClient().create(ServicioApi.class);
        Call<ObtenerTelefonoObjeto> call = servicioApi.obtenerNumeroTelefono(idUsuario);
        call.enqueue(new Callback<ObtenerTelefonoObjeto>() {
            @Override
            public void onResponse(Call<ObtenerTelefonoObjeto> call, Response<ObtenerTelefonoObjeto> response) {
                if (response.isSuccessful()) {
                    ObtenerTelefonoObjeto respuesta = response.body();
                    if (respuesta != null && respuesta.getNumero_telefono() != null && !respuesta.getNumero_telefono().isEmpty()) {
                        numeroTelefonoGuardado = respuesta.getNumero_telefono();
                    } else {
                        numeroTelefonoGuardado = null;
                    }
                } else {
                    Log.e("ErrorObtenerNumero", "Error al obtener el número guardado.");
                    numeroTelefonoGuardado = null;
                }
            }

            @Override
            public void onFailure(Call<ObtenerTelefonoObjeto> call, Throwable t) {
                Log.e("ErrorObtenerNumero", "Fallo en la conexión con el servidor", t);
                numeroTelefonoGuardado = null;
            }
        });
    }

    private void realizarLlamada() {
        String numeroTelefono = "tel:" + numeroTelefonoGuardado;
        Intent intentoLlamada = new Intent(Intent.ACTION_CALL);
        intentoLlamada.setData(Uri.parse(numeroTelefono));
        try {
            startActivity(intentoLlamada);
        } catch (Exception e) {
            Log.e("ErrorLlamada", "Error al intentar realizar la llamada", e);
            Toast.makeText(getContext(), "Error al intentar realizar la llamada: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private int obtenerIdUsuario() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("idUsuario", -1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SOLICITUD_NUMERO && resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                numeroTelefonoGuardado = data.getStringExtra("numeroTelefono");
                Toast.makeText(getContext(), "Número guardado: " + numeroTelefonoGuardado, Toast.LENGTH_SHORT).show();
            }
        }
    }
}