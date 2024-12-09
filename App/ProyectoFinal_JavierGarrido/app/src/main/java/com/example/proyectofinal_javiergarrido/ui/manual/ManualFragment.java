package com.example.proyectofinal_javiergarrido.ui.manual;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.proyectofinal_javiergarrido.MainActivity;
import com.example.proyectofinal_javiergarrido.R;
import com.example.proyectofinal_javiergarrido.databinding.FragmentManualBinding;


public class ManualFragment extends Fragment {

    private FragmentManualBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ManualViewModel manualViewModel =
                new ViewModelProvider(this).get(ManualViewModel.class);

        binding = FragmentManualBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.botonSalidaHome.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
