package com.example.proyectofinal_javiergarrido.ui.estadisticas;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectofinal_javiergarrido.MainActivity;
import com.example.proyectofinal_javiergarrido.R;
import com.example.proyectofinal_javiergarrido.ui.estadisticas.Colores.EstadisticasJuegoColor;
import com.example.proyectofinal_javiergarrido.ui.estadisticas.Contar.EstadisticasJuegoContar;
import com.example.proyectofinal_javiergarrido.ui.estadisticas.Trabalenguas.EstadisticasJuegoTrabalenguas;

public class EstadisticasJuegosInicio extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_estadisticas_juegos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton imgBtnJuegoContarObjetos = view.findViewById(R.id.imgbtnJuegoContarObjetos);
        ImageButton imgBtnJuegoMemoria = view.findViewById(R.id.imgbtnJuegoMemoria);
        ImageButton imgBtnJuegoTrabalenguas = view.findViewById(R.id.imgbtnJuegoTrabalenguas);
        ImageButton imgBtnJuegoColor = view.findViewById(R.id.imgbtnJuegoColor);
        Button btnVolverHome = view.findViewById(R.id.btnEstadisticasJuegos);

        imgBtnJuegoMemoria.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), EstadisticasJuegoCartas.class);
            startActivity(intent);
        });

        imgBtnJuegoContarObjetos.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), EstadisticasJuegoContar.class);
            startActivity(intent);
        });

        imgBtnJuegoTrabalenguas.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), EstadisticasJuegoTrabalenguas.class);
            startActivity(intent);
        });

        imgBtnJuegoColor.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), EstadisticasJuegoColor.class);
            startActivity(intent);
        });

        btnVolverHome.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }
}
