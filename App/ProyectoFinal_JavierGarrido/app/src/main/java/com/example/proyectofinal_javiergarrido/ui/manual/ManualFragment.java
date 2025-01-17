package com.example.proyectofinal_javiergarrido.ui.manual;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectofinal_javiergarrido.MainActivity;
import com.example.proyectofinal_javiergarrido.R;
import com.example.proyectofinal_javiergarrido.databinding.FragmentManualBinding;
import com.example.proyectofinal_javiergarrido.ui.diario.DiarioFragment;
import com.example.proyectofinal_javiergarrido.ui.estadisticas.EstadisticasJuegosInicio;
import com.example.proyectofinal_javiergarrido.ui.ubi.Ubicacion;

public class ManualFragment extends Fragment {

    private FragmentManualBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ManualViewModel manualViewModel =
                new ViewModelProvider(this).get(ManualViewModel.class);

        binding = FragmentManualBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // Botón para ir a Home
        binding.btnImgHome.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        // Botón para ir a Ubicaciones
        binding.btnImgUbicaciones.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Ubicacion.class);
            startActivity(intent);
        });

        // Botón para ir a Diario
        binding.btnImgDiario.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DiarioFragment.class);
            startActivity(intent);
        });

        // Botón para ir a Estadisticas

        binding.btnImgEstadisticas.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EstadisticasJuegosInicio.class); // Cambiar por la clase real
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
