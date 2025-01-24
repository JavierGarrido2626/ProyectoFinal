package com.example.proyectofinal_javiergarrido.ui.diario;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal_javiergarrido.CrearNotaActivity;
import com.example.proyectofinal_javiergarrido.R;
import com.example.proyectofinal_javiergarrido.ui.ServiciosServer.ClienteApi;
import com.example.proyectofinal_javiergarrido.ui.ServiciosServer.ServicioApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiarioFragment extends Fragment {

    private List<Nota> listaNotas;
    private NotaAdapter notaAdapter;
    private EditText etBuscar;
    private Button botonCrearNota;

    private static final String TAG = "DiarioFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_diario, container, false);

        listaNotas = new ArrayList<>();
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view_notas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notaAdapter = new NotaAdapter(listaNotas, getContext());
        recyclerView.setAdapter(notaAdapter);

        etBuscar = root.findViewById(R.id.et_buscar);
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarNotas(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        botonCrearNota = root.findViewById(R.id.boton_crear_nota);
        botonCrearNota.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), CrearNotaActivity.class);
            startActivityForResult(intent, 1);
        });

        cargarNotasDesdeAPI();

        return root;
    }

    private void cargarNotasDesdeAPI() {
        int idUsuario = obtenerIdUsuario();

        if (idUsuario == -1) {
            return;
        }

        ServicioApi servicioApi = ClienteApi.getClient().create(ServicioApi.class);
        Call<RespuestaNotas> call = servicioApi.obtenerNotasPorUsuario(idUsuario);
        call.enqueue(new Callback<RespuestaNotas>() {
            @Override
            public void onResponse(Call<RespuestaNotas> call, Response<RespuestaNotas> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaNotas.clear();
                    listaNotas.addAll(response.body().getNotas());
                    notaAdapter.actualizarLista(listaNotas);
                }
            }

            @Override
            public void onFailure(Call<RespuestaNotas> call, Throwable t) {
                Log.e(TAG, "Error en la conexión con la API: " + t.getMessage());
            }
        });
    }

    private void filtrarNotas(String query) {
        List<Nota> listaFiltrada = new ArrayList<>();
        for (Nota nota : listaNotas) {
            if (nota.getTitulo().toLowerCase().contains(query.toLowerCase())) {
                listaFiltrada.add(nota);
            }
        }
        notaAdapter.actualizarLista(listaFiltrada);
    }

    private int obtenerIdUsuario() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("idUsuario", -1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            cargarNotasDesdeAPI();
        }
    }
}
