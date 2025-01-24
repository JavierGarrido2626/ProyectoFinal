package com.example.proyectofinal_javiergarrido.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinal_javiergarrido.R;
import com.example.proyectofinal_javiergarrido.ui.ServiciosServer.ServicioApi;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificacionAdapter extends RecyclerView.Adapter<NotificacionAdapter.AlarmaViewHolder> {
    private List<Notificacion> listaAlarmas;
    private Context context;
    private ServicioApi servicioApi;

    public NotificacionAdapter(List<Notificacion> listaAlarmas, Context context, ServicioApi servicioApi) {
        this.listaAlarmas = listaAlarmas;
        this.context = context;
        this.servicioApi = servicioApi;
    }

    @Override
    public AlarmaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarma, parent, false);
        return new AlarmaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlarmaViewHolder holder, int position) {
        Notificacion alarma = listaAlarmas.get(position);
        holder.textHora.setText(alarma.getHora());
        holder.textNombreAlarma.setText(alarma.getNombreNotificacion());
        holder.btnEliminarAlarma.setOnClickListener(v -> eliminarNotificacionDeBaseDeDatos(position));
    }

    @Override
    public int getItemCount() {
        return listaAlarmas.size();
    }

    public class AlarmaViewHolder extends RecyclerView.ViewHolder {
        TextView textHora, textNombreAlarma;
        ImageButton btnEliminarAlarma;

        public AlarmaViewHolder(View itemView) {
            super(itemView);
            textHora = itemView.findViewById(R.id.textHora);
            textNombreAlarma = itemView.findViewById(R.id.txtTituloAlarma);
            btnEliminarAlarma = itemView.findViewById(R.id.btnEliminarAlarma);
        }
    }

    private void eliminarNotificacionDeBaseDeDatos(int position) {
        servicioApi.eliminarNotificacion(listaAlarmas.get(position).getIdNotificacion()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listaAlarmas.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, listaAlarmas.size());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }
}
