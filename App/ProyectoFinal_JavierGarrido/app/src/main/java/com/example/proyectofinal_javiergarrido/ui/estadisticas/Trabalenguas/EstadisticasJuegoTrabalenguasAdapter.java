package com.example.proyectofinal_javiergarrido.ui.estadisticas.Trabalenguas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal_javiergarrido.R;

import java.util.List;

public class EstadisticasJuegoTrabalenguasAdapter extends RecyclerView.Adapter<EstadisticasJuegoTrabalenguasAdapter.ViewHolder> {

    private List<EstadisticasJuegoTrabalenguasObjeto> estadisticas;

    public EstadisticasJuegoTrabalenguasAdapter(List<EstadisticasJuegoTrabalenguasObjeto> estadisticas) {
        this.estadisticas = estadisticas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_estadisticas_trabalenguas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EstadisticasJuegoTrabalenguasObjeto estadistica = estadisticas.get(position);

        holder.textViewIdIntento.setText(String.valueOf(position + 1));
        holder.textViewTiempoTotal.setText(estadistica.getTiempo_total() + " segundos");
    }

    @Override
    public int getItemCount() {
        return estadisticas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewIdIntento, textViewTiempoTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewIdIntento = itemView.findViewById(R.id.text_view_id_intento);
            textViewTiempoTotal = itemView.findViewById(R.id.text_view_tiempo_total);
        }
    }
}
