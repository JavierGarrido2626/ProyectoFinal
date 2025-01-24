package com.example.proyectofinal_javiergarrido.ui.manual;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.proyectofinal_javiergarrido.CrearNotaActivity;
import com.example.proyectofinal_javiergarrido.MainActivity;
import com.example.proyectofinal_javiergarrido.databinding.FragmentManualBinding;
import com.example.proyectofinal_javiergarrido.ui.estadisticas.Cartas.EstadisticasJuegoCartas;
import com.example.proyectofinal_javiergarrido.ui.ubi.ListaUbiActivity;


public class ManualFragment extends Fragment {

    private FragmentManualBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentManualBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.btnImgHome.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
/*
        binding.btnImgUbicaciones.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ListaUbiActivity.class);
            startActivity(intent);
        });
*/
        binding.btnImgDiario.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CrearNotaActivity.class);
            startActivity(intent);
        });


        binding.btnImgEstadisticas.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EstadisticasJuegoCartas.class);
            startActivity(intent);
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
