package com.example.proyectofinal_javiergarrido.ui.diario;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal_javiergarrido.R;
import com.example.proyectofinal_javiergarrido.CrearNotaActivity;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class DiarioFragment extends Fragment {

    private static final int REQUEST_CODE_CREAR_NOTA = 1;
    private List<Nota> listaNotas;
    private NotaAdapter notaAdapter;
    private EditText etBuscar;
    private Button botonCrearNota;
    private BaseDatosDiarioSQL baseDeDatos;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_diario, container, false);

        baseDeDatos = new BaseDatosDiarioSQL(getContext());
        listaNotas = new ArrayList<>();
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view_notas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notaAdapter = new NotaAdapter(listaNotas, getContext());
        recyclerView.setAdapter(notaAdapter);

        etBuscar = root.findViewById(R.id.et_buscar);
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filtrarNotas(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        botonCrearNota = root.findViewById(R.id.boton_crear_nota);
        botonCrearNota.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), CrearNotaActivity.class);
            startActivityForResult(intent, REQUEST_CODE_CREAR_NOTA);
        });

        cargarNotasDesdeBD();

        return root;
    }

    private void cargarNotasDesdeBD() {
        listaNotas.clear();
        listaNotas.addAll(baseDeDatos.obtenerNotas());
        notaAdapter.actualizarLista(listaNotas);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CREAR_NOTA && resultCode == RESULT_OK && data != null) {
            cargarNotasDesdeBD();
        }
    }
}
