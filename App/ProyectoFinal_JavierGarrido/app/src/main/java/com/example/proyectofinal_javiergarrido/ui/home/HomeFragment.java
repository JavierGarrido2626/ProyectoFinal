package com.example.proyectofinal_javiergarrido.ui.home;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import com.example.proyectofinal_javiergarrido.R;

public class HomeFragment extends Fragment {

    private static final int SOLICITUD_PERMISO_LLAMADA = 1;
    private static final int SOLICITUD_NUMERO = 2;
    private String numeroTelefonoGuardado;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Button btnLlamada = root.findViewById(R.id.btn_llamadas);
        Button btnAlarma = root.findViewById(R.id.btn_alarmas);



        btnAlarma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
                    startActivity(intent);
                } catch (Exception e) {
                    // Log y mensaje en caso de error
                    Log.e("ErrorAlarma", "Error al abrir la aplicación de reloj", e);
                    Toast.makeText(getContext(), "No se pudo abrir la aplicación del reloj: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });





        btnLlamada.setOnLongClickListener(v -> {
            Intent intent = new Intent(getContext(), IngresarNumeroActivity.class);
            startActivityForResult(intent, SOLICITUD_NUMERO);
            return true;
        });

        btnLlamada.setOnClickListener(v -> {
            if (numeroTelefonoGuardado != null && !numeroTelefonoGuardado.isEmpty()) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE}, SOLICITUD_PERMISO_LLAMADA);
                } else {
                    realizarLlamada();
                }
            } else {
                Toast.makeText(getContext(), "Por favor, ingresa un número primero.", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private String formatearNumero(String numero) {
        return numero.replaceAll("[^0-9]", "");
    }

    private void realizarLlamada() {
        String numeroTelefono = "tel:" + numeroTelefonoGuardado;
        Intent intentoLlamada = new Intent(Intent.ACTION_CALL);
        intentoLlamada.setData(Uri.parse(numeroTelefono));
        startActivity(intentoLlamada);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SOLICITUD_NUMERO && resultCode == RESULT_OK) {
            if (data != null) {
                numeroTelefonoGuardado = data.getStringExtra("numeroTelefono");

                numeroTelefonoGuardado = formatearNumero(numeroTelefonoGuardado);

                Toast.makeText(getContext(), "Número guardado: " + numeroTelefonoGuardado, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int codigoSolicitud, String[] permisos, int[] resultadosPermiso) {
        super.onRequestPermissionsResult(codigoSolicitud, permisos, resultadosPermiso);

        if (codigoSolicitud == SOLICITUD_PERMISO_LLAMADA) {
            if (resultadosPermiso.length > 0 && resultadosPermiso[0] == PackageManager.PERMISSION_GRANTED) {
                realizarLlamada();
            } else {
                Toast.makeText(getContext(), "Permiso denegado para hacer llamadas", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
