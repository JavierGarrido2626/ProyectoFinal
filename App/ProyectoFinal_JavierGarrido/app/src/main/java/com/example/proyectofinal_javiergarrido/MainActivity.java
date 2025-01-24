package com.example.proyectofinal_javiergarrido;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.proyectofinal_javiergarrido.LoginRegistro.LoginActivity;
import com.example.proyectofinal_javiergarrido.databinding.ActivityMainBinding;
import com.example.proyectofinal_javiergarrido.ui.ServiciosServer.ClienteApi;
import com.example.proyectofinal_javiergarrido.ui.ServiciosServer.ServicioApi;
import com.example.proyectofinal_javiergarrido.ui.ubi.Ubicacion;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String PREFS_NOMBRE = "SesionUsuario";
    private static final String CLAVE_SESION_INICIADA = "sesionIniciada";
    private static final String CLAVE_NOMBRE_USUARIO = "nombreUsuario";
    private static final String CLAVE_ID_USUARIO = "idUsuario";
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private FusedLocationProviderClient fusedLocationClient;
    private SharedPreferences sharedPreferences;
    private Handler handler;
    private boolean enviandoUbicaciones = false;
    private int intervaloSeleccionado = 0;

    private AppBarConfiguration configuracionBarraApp;
    private ActivityMainBinding enlaceBinding;
    private NavController controladorNavegacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!sesionIniciada()) {
            volverALogin();
            return;
        }

        enlaceBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(enlaceBinding.getRoot());

        setSupportActionBar(enlaceBinding.appBarMain.toolbar);

        sharedPreferences = getSharedPreferences(PREFS_NOMBRE, MODE_PRIVATE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        handler = new Handler();

        NavigationView navigationView = enlaceBinding.navView;
        View headerView = navigationView.getHeaderView(0);
        TextView txtNombreUsuario = headerView.findViewById(R.id.txtNombreUsuario);

        if (txtNombreUsuario != null) {
            txtNombreUsuario.setText(sharedPreferences.getString(CLAVE_NOMBRE_USUARIO, "Usuario no disponible"));
        }

        DrawerLayout menuLateral = enlaceBinding.drawerLayout;
        NavigationView vistaNavegacion = enlaceBinding.navView;

        configuracionBarraApp = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_ubi, R.id.nav_manual, R.id.diarioFragment)
                .setOpenableLayout(menuLateral)
                .build();

        controladorNavegacion = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, controladorNavegacion, configuracionBarraApp);
        NavigationUI.setupWithNavController(vistaNavegacion, controladorNavegacion);

        vistaNavegacion.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Ajustes.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.btnCerrarSesion) {
            cerrarSesion();
            return true;
        }

        if (id == R.id.btnInformacionMenu) {
            Intent intent = new Intent(this, Informacion.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.btnGenerarUbicacion) {
            mostrarDialogoTiempo();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void mostrarDialogoTiempo() {
        final String[] opcionesTiempo = {
                "1 minuto", "5 minutos", "10 minutos", "30 minutos", "1 hora",
                "5 horas", "10 horas", "24 horas", "PARAR"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona el intervalo de tiempo")
                .setItems(opcionesTiempo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 8) {
                            detenerEnvioUbicaciones();
                        } else {
                            String tiempoSeleccionado = opcionesTiempo[which];
                            switch (which) {
                                case 0: intervaloSeleccionado = 60000; break;  // 1 minuto
                                case 1: intervaloSeleccionado = 300000; break; // 5 minutos
                                case 2: intervaloSeleccionado = 600000; break; // 10 minutos
                                case 3: intervaloSeleccionado = 1800000; break; // 30 minutos
                                case 4: intervaloSeleccionado = 3600000; break; // 1 hora
                                case 5: intervaloSeleccionado = 18000000; break; // 5 horas
                                case 6: intervaloSeleccionado = 36000000; break; // 10 horas
                                case 7: intervaloSeleccionado = 86400000; break; // 24 horas
                            }

                            if (intervaloSeleccionado > 0) {
                                iniciarEnvioUbicaciones();
                            }
                            Toast.makeText(MainActivity.this, "Intervalo seleccionado: " + tiempoSeleccionado, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar", null);

        builder.create().show();
    }

    private void iniciarEnvioUbicaciones() {
        if (enviandoUbicaciones) return;

        enviandoUbicaciones = true;
        obtenerUbicacionYEnviar();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (enviandoUbicaciones) {
                    obtenerUbicacionYEnviar();
                    handler.postDelayed(this, intervaloSeleccionado);
                }
            }
        }, intervaloSeleccionado);
    }

    private void obtenerUbicacionYEnviar() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                double latitud = location.getLatitude();
                                double longitud = location.getLongitude();

                                int idUsuario = sharedPreferences.getInt(CLAVE_ID_USUARIO, -1);

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                String nombre = sdf.format(new Date());

                                Ubicacion ubicacion = new Ubicacion(latitud, longitud, nombre, idUsuario);
                                enviarUbicacionAlServidor(ubicacion);
                            }
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
    }

    private void detenerEnvioUbicaciones() {
        enviandoUbicaciones = false;
        handler.removeCallbacksAndMessages(null);
        Toast.makeText(this, "Envío de ubicaciones detenido", Toast.LENGTH_SHORT).show();
    }

    private void enviarUbicacionAlServidor(Ubicacion ubicacion) {
        ServicioApi servicioApi = ClienteApi.getClient().create(ServicioApi.class);
        Call<Void> call = servicioApi.guardarUbicacion(ubicacion);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Ubicación enviada correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Error al enviar la ubicación", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean sesionIniciada() {
        SharedPreferences preferencias = getSharedPreferences(PREFS_NOMBRE, MODE_PRIVATE);
        return preferencias.getBoolean(CLAVE_SESION_INICIADA, false);
    }

    private void volverALogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(controladorNavegacion, configuracionBarraApp) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_diario) {
            controladorNavegacion.navigate(R.id.diarioFragment);
        } else {
            NavigationUI.onNavDestinationSelected(item, controladorNavegacion);
        }

        DrawerLayout menuLateral = enlaceBinding.drawerLayout;
        menuLateral.closeDrawers();
        return true;
    }

    private void cerrarSesion() {
        SharedPreferences preferencias = getSharedPreferences(PREFS_NOMBRE, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putBoolean(CLAVE_SESION_INICIADA, false);
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacionYEnviar();
            } else {
                Toast.makeText(this, "El permiso de ubicación es necesario", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
