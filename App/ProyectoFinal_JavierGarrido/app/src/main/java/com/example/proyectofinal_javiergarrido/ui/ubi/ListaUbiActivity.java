package com.example.proyectofinal_javiergarrido.ui.ubi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinal_javiergarrido.R;
import java.util.ArrayList;

public class ListaUbiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listaubi);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewLista);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Ubicacion> ubicaciones = (ArrayList<Ubicacion>) getIntent().getSerializableExtra("ubicaciones");

        UbiAdapter adapter = new UbiAdapter(ubicaciones, new UbiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Ubicacion ubicacion) {
                mostrarRuta(ubicacion);
            }
        });
        recyclerView.setAdapter(adapter);

        Button btnSalir = findViewById(R.id.btnSalidaUbi);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void mostrarRuta(Ubicacion ubicacion) {
        String uri = "google.navigation:q=" + ubicacion.getLatitud() + "," + ubicacion.getLongitud();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }
}
