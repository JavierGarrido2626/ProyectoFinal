package com.example.proyectofinal_javiergarrido.ui.home;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal_javiergarrido.MainActivity;
import com.example.proyectofinal_javiergarrido.R;
import com.example.proyectofinal_javiergarrido.ui.ServiciosServer.ClienteApi;
import com.example.proyectofinal_javiergarrido.ui.ServiciosServer.ServicioApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListaNotificacionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificacionAdapter alarmaAdapter;
    private List<Notificacion> listaAlarmas;
    private Button btnVolver;
    private static final String CHANNEL_ID = "alarma_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alarmas);

        btnVolver = findViewById(R.id.btnVolverNotificacion);

        crearCanalDeNotificacion();
        verificarPermisoExactAlarm();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        recyclerView = findViewById(R.id.recyclerAlarmas);
        listaAlarmas = new ArrayList<>();

        Retrofit retrofit = ClienteApi.getClient();
        ServicioApi servicioApi = retrofit.create(ServicioApi.class);

        alarmaAdapter = new NotificacionAdapter(listaAlarmas, this, servicioApi);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(alarmaAdapter);

        ImageButton btnCrearAlarma = findViewById(R.id.btnImg_crearAlarmas);
        btnCrearAlarma.setOnClickListener(v -> abrirDialogoCrearAlarma());

        obtenerAlarmasDelServidor();

        btnVolver.setOnClickListener(v -> {
            Intent i = new Intent(ListaNotificacionActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void obtenerAlarmasDelServidor() {
        int idUsuario = obtenerIdUsuario();

        if (idUsuario != -1) {
            Retrofit retrofit = ClienteApi.getClient();
            ServicioApi servicioApi = retrofit.create(ServicioApi.class);

            Call<List<Notificacion>> call = servicioApi.obtenerNotificaciones(idUsuario);
            call.enqueue(new Callback<List<Notificacion>>() {
                @Override
                public void onResponse(Call<List<Notificacion>> call, Response<List<Notificacion>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        listaAlarmas.clear();
                        listaAlarmas.addAll(response.body());
                        alarmaAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ListaNotificacionActivity.this, "Error al cargar las notificaciones", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Notificacion>> call, Throwable t) {
                    Toast.makeText(ListaNotificacionActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(ListaNotificacionActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void crearCanalDeNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Alarmas";
            String description = "Canal para alarmas";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void verificarPermisoExactAlarm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }
    }

    private void abrirDialogoCrearAlarma() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_crear_alarma, null);

        TimePicker timePicker = dialogView.findViewById(R.id.timePicker);
        CheckBox checkLunes = dialogView.findViewById(R.id.checkLunes);
        CheckBox checkMartes = dialogView.findViewById(R.id.checkMartes);
        CheckBox checkMiercoles = dialogView.findViewById(R.id.checkMiercoles);
        CheckBox checkJueves = dialogView.findViewById(R.id.checkJueves);
        CheckBox checkViernes = dialogView.findViewById(R.id.checkViernes);
        CheckBox checkSabado = dialogView.findViewById(R.id.checkSabado);
        CheckBox checkDomingo = dialogView.findViewById(R.id.checkDomingo);
        Button btnGuardar = dialogView.findViewById(R.id.btnGuardarAlarma);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        btnGuardar.setOnClickListener(v -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            String hora = String.format("%02d:%02d", hour, minute);

            List<String> diasRepeticion = new ArrayList<>();
            if (checkLunes.isChecked()) diasRepeticion.add("Lunes");
            if (checkMartes.isChecked()) diasRepeticion.add("Martes");
            if (checkMiercoles.isChecked()) diasRepeticion.add("Miércoles");
            if (checkJueves.isChecked()) diasRepeticion.add("Jueves");
            if (checkViernes.isChecked()) diasRepeticion.add("Viernes");
            if (checkSabado.isChecked()) diasRepeticion.add("Sábado");
            if (checkDomingo.isChecked()) diasRepeticion.add("Domingo");

            String tituloNotificacion = ((EditText) dialogView.findViewById(R.id.editTextTituloNotificacion)).getText().toString();

            if (tituloNotificacion.isEmpty()) {
                Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            if (diasRepeticion.isEmpty()) {
                Toast.makeText(this, "Debe seleccionar al menos un día", Toast.LENGTH_SHORT).show();
                return;
            }

            int idUsuario = obtenerIdUsuario();

            Notificacion nuevaAlarma = new Notificacion(0, hora, idUsuario, tituloNotificacion, diasRepeticion);

            listaAlarmas.add(nuevaAlarma);
            alarmaAdapter.notifyItemInserted(listaAlarmas.size() - 1);
            programarAlarma(hour, minute, diasRepeticion, tituloNotificacion);
            guardarNotificacionEnBackend(nuevaAlarma);
            Toast.makeText(this, "Alarma guardada", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }

    private void programarAlarma(int hora, int minuto, List<String> diasRepeticion, String tituloNotificacion) {
        for (String dia : diasRepeticion) {
            Calendar calendario = Calendar.getInstance();
            calendario.set(Calendar.HOUR_OF_DAY, hora);
            calendario.set(Calendar.MINUTE, minuto);
            calendario.set(Calendar.SECOND, 0);

            switch (dia) {
                case "Lunes":
                    calendario.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    break;
                case "Martes":
                    calendario.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                    break;
                case "Miércoles":
                    calendario.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                    break;
                case "Jueves":
                    calendario.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                    break;
                case "Viernes":
                    calendario.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                    break;
                case "Sábado":
                    calendario.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                    break;
                case "Domingo":
                    calendario.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    break;
            }

            if (calendario.getTimeInMillis() < System.currentTimeMillis()) {
                calendario.add(Calendar.WEEK_OF_YEAR, 1);
            }

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, NotificacionReceiver.class);
            intent.putExtra("titulo", tituloNotificacion);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, hora * 100 + minuto, intent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendario.getTimeInMillis(), pendingIntent);
        }
    }

    private void guardarNotificacionEnBackend(Notificacion notificacion) {
        Retrofit retrofit = ClienteApi.getClient();
        ServicioApi servicioApi = retrofit.create(ServicioApi.class);

        Call<Void> call = servicioApi.guardarNotificacion(notificacion);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ListaNotificacionActivity.this, "Notificación guardada en el servidor", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ListaNotificacionActivity.this, "Error al guardar la notificación", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ListaNotificacionActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int obtenerIdUsuario() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("idUsuario", -1);
    }
}
