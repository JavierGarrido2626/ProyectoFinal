package com.example.proyectofinal_javiergarrido.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinal_javiergarrido.R;

public class IngresarNumeroActivity extends AppCompatActivity {

    private EditText etNumeroTelefono;
    private Button btnGuardarNumero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_numero);

        etNumeroTelefono = findViewById(R.id.et_numero_telefono);
        btnGuardarNumero = findViewById(R.id.btn_guardar_numero);

        btnGuardarNumero.setOnClickListener(v -> {
            String numeroTelefono = etNumeroTelefono.getText().toString().trim();

            if (!numeroTelefono.isEmpty() && esNumeroValido(numeroTelefono)) {
                Intent intent = new Intent();
                intent.putExtra("numeroTelefono", numeroTelefono);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(IngresarNumeroActivity.this, "Por favor ingresa un número de teléfono válido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean esNumeroValido(String numero) {
        return numero.matches("[+0-9\\-()\\s]*");
    }
}
