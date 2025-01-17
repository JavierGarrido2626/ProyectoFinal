package com.example.proyectofinal_javiergarrido.ui.ubi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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

public class UbiFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText nombreUbicacionInput;
    private Button btnGuardarUbicacion, btnListaUbi;
    private LatLng posicionSeleccionada;
    private FusedLocationProviderClient fusedLocationClient;
    private ArrayList<Ubicacion> ubicacionesGuardadas;
    private Marker marcador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ubi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ubicacionesGuardadas = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }

        nombreUbicacionInput = view.findViewById(R.id.nombreUbicacionInput);
        btnGuardarUbicacion = view.findViewById(R.id.btnGuardarUbicacion);
        btnListaUbi = view.findViewById(R.id.btnListaUbi);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        btnGuardarUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarUbicacion();
            }
        });

        btnListaUbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListaUbiActivity.class);
                intent.putExtra("ubicaciones", ubicacionesGuardadas);
                startActivity(intent);
            }
        });

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

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (marcador != null) {
                    marcador.remove();
                }
                marcador = mMap.addMarker(new MarkerOptions().position(latLng).title("Ubicación seleccionada"));
                posicionSeleccionada = latLng;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marcador != null) {
                    marcador.remove();
                    marcador = null;
                }
            }
        });
    }

    private void obtenerUbicacionActual() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new com.google.android.gms.tasks.OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                            mMap.addMarker(new MarkerOptions().position(latLng).title("Tu ubicación"));
                        } else {
                            Toast.makeText(getContext(), "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show();
                        }
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
            nombre = "Ubicación " + (ubicacionesGuardadas.size() + 1);
        }

        Ubicacion ubicacion = new Ubicacion(posicionSeleccionada.latitude, posicionSeleccionada.longitude, nombre);
        ubicacionesGuardadas.add(ubicacion);

        nombreUbicacionInput.setText("");

        Toast.makeText(getContext(), "Ubicación guardada correctamente", Toast.LENGTH_SHORT).show();
    }
}
