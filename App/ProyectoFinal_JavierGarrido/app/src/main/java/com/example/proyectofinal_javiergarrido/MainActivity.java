package com.example.proyectofinal_javiergarrido;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.proyectofinal_javiergarrido.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String PREFS_NOMBRE = "SesionUsuario";
    private static final String CLAVE_SESION_INICIADA = "sesionIniciada";
    private static final String CLAVE_NOMBRE_USUARIO = "nombreUsuario";
    private static final String CLAVE_ID_USUARIO = "idUsuario";

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

        SharedPreferences preferencias = getSharedPreferences(PREFS_NOMBRE, MODE_PRIVATE);
        String nombreUsuario = preferencias.getString(CLAVE_NOMBRE_USUARIO, "Usuario no disponible");

        NavigationView navigationView = enlaceBinding.navView;
        View headerView = navigationView.getHeaderView(0);
        TextView txtNombreUsuario = headerView.findViewById(R.id.txtNombreUsuario);

        if (txtNombreUsuario != null) {
            txtNombreUsuario.setText(nombreUsuario);
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

        return super.onOptionsItemSelected(item);
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
}
