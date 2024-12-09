/*
package com.example.proyectofinal_javiergarrido.ui.ubi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.maplibre.gl.maps.MapView;
import com.maplibre.gl.maps.MapboxMap;
import com.maplibre.gl.maps.OnMapReadyCallback;
import com.maplibre.gl.maps.model.LatLng;
import com.maplibre.gl.maps.model.MarkerOptions;
import com.maplibre.gl.maps.CameraUpdateFactory;

import java.util.ArrayList;
import java.util.List;

public class UbiFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private RecyclerView recyclerViewUbicaciones;
    private UbicacionAdapter ubicacionAdapter;
    private List<Ubi> listaUbicaciones;

    private Button btnMostrarUbicaciones;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ubi, container, false);

        // Inicializar MapView
        mapView = root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Inicializar RecyclerView
        recyclerViewUbicaciones = root.findViewById(R.id.recyclerViewUbicaciones);
        recyclerViewUbicaciones.setLayoutManager(new LinearLayoutManager(getContext()));

        // Crear lista de ubicaciones
        listaUbicaciones = new ArrayList<>();
        listaUbicaciones.add(new Ubi("Casa", 40.4168, -3.7038));
        listaUbicaciones.add(new Ubi("Hospital", 40.4517, -3.6949));

        // Crear y configurar el adaptador para el RecyclerView
        ubicacionAdapter = new UbicacionAdapter(listaUbicaciones, this::mostrarUbicacionEnMapa);
        recyclerViewUbicaciones.setAdapter(ubicacionAdapter);

        // Configurar botón para mostrar las ubicaciones
        btnMostrarUbicaciones = root.findViewById(R.id.btnMostrarUbicaciones);
        btnMostrarUbicaciones.setOnClickListener(v -> recyclerViewUbicaciones.setVisibility(View.VISIBLE));

        return root;
    }

    private void mostrarUbicacionEnMapa(Ubi ubi) {
        if (mapboxMap != null) {
            LatLng ubicacion = new LatLng(ubi.getLat(), ubi.getLng());
            mapboxMap.addMarker(new MarkerOptions().position(ubicacion).title(ubi.getNombre()));
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 12));
        }
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(com.maplibre.gl.maps.Style.MAPBOX_STREETS);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    // Clase interna para las ubicaciones
    public static class Ubi {
        private String nombre;
        private double lat;
        private double lng;

        public Ubi(String nombre, double lat, double lng) {
            this.nombre = nombre;
            this.lat = lat;
            this.lng = lng;
        }

        public String getNombre() {
            return nombre;
        }

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }
    }
}
*/