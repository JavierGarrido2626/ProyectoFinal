package com.example.proyectofinal_javiergarrido.ui.ubi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.proyectofinal_javiergarrido.R;
import com.example.proyectofinal_javiergarrido.ui.ServiciosServer.ClienteApi;
import com.example.proyectofinal_javiergarrido.ui.ServiciosServer.ServicioApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbiFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText nombreUbicacionInput;
    private Button btnGuardarUbicacion, btnListaUbi;
    private LatLng posicionSeleccionada;
    private FusedLocationProviderClient fusedLocationClient;
    private Marker marcador;
    private int idUsuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ubi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nombreUbicacionInput = view.findViewById(R.id.nombreUbicacionInput);
        btnGuardarUbicacion = view.findViewById(R.id.btnGuardarUbicacion);
        btnListaUbi = view.findViewById(R.id.btnListaUbi);

        idUsuario = obtenerIdUsuario();
        if (idUsuario == -1) {
            Log.e("UbiFragment", "No se ha recibido el id_usuario de SharedPreferences");
            Toast.makeText(getContext(), "No se ha recibido el id_usuario", Toast.LENGTH_SHORT).show();
        }

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        btnGuardarUbicacion.setOnClickListener(v -> guardarUbicacion());
        btnListaUbi.setOnClickListener(v -> mostrarListaUbicaciones());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("UbiFragment", "Mapa cargado correctamente");

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            obtenerUbicacionActual();
        }

        mMap.setOnMapLongClickListener(latLng -> {
            if (marcador != null) {
                marcador.remove();
            }
            marcador = mMap.addMarker(new MarkerOptions().position(latLng).title("Ubicación seleccionada"));
            posicionSeleccionada = latLng;
        });

        mMap.setOnMapClickListener(latLng -> {
            if (marcador != null) {
                marcador.remove();
                marcador = null;
            }
        });
    }

    private void obtenerUbicacionActual() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), location -> {
                    if (location != null) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        if (marcador != null) {
                            marcador.remove();
                        }
                        marcador = mMap.addMarker(new MarkerOptions().position(latLng).title("Tu ubicación"));
                    } else {
                        Toast.makeText(getContext(), "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void guardarUbicacion() {
        if (posicionSeleccionada == null) {
            Toast.makeText(getContext(), "Primero selecciona una ubicación en el mapa", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombre = nombreUbicacionInput.getText().toString().trim();

        if (nombre.isEmpty()) {
            Toast.makeText(getContext(), "Por favor ingresa un nombre para la ubicación", Toast.LENGTH_SHORT).show();
            return;
        }

        Ubicacion ubicacion = new Ubicacion(posicionSeleccionada.latitude, posicionSeleccionada.longitude, nombre, idUsuario);

        ServicioApi servicioApi = ClienteApi.getClient().create(ServicioApi.class);
        Call<Void> call = servicioApi.guardarUbicacion(ubicacion);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Ubicación guardada correctamente", Toast.LENGTH_SHORT).show();
                    nombreUbicacionInput.setText("");
                    posicionSeleccionada = null;
                } else {
                    Toast.makeText(getContext(), "Error al guardar la ubicación", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarListaUbicaciones() {
        ServicioApi servicioApi = ClienteApi.getClient().create(ServicioApi.class);
        Call<RespuestaUbicaciones> call = servicioApi.obtenerUbicacionesPorUsuario(idUsuario);
        call.enqueue(new Callback<RespuestaUbicaciones>() {
            @Override
            public void onResponse(Call<RespuestaUbicaciones> call, Response<RespuestaUbicaciones> response) {
                if (response.isSuccessful()) {
                    List<Ubicacion> ubicaciones = response.body().getUbicaciones();
                    Intent intent = new Intent(getActivity(), ListaUbiActivity.class);
                    intent.putExtra("ubicaciones", (ArrayList<Ubicacion>) ubicaciones);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Error al obtener las ubicaciones", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaUbicaciones> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int obtenerIdUsuario() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("idUsuario", -1);
    }
}
