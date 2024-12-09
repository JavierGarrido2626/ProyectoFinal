/*package com.example.proyectofinal_javiergarrido.ui.ubi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UbiAdapter extends RecyclerView.Adapter<UbiAdapter.UbicacionViewHolder> {
/*
    private List<Ubi> listaUbicaciones;
    private OnUbicacionClickListener listener;

    public interface OnUbicacionClickListener {
        void onUbicacionClick(Ubi ubi);
    }

    public UbiAdapter(List<Ubi> listaUbicaciones, OnUbicacionClickListener listener) {
        this.listaUbicaciones = listaUbicaciones;
        this.listener = listener;
    }

    @Override
    public UbicacionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new UbicacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UbicacionViewHolder holder, int position) {
        Ubi ubi = listaUbicaciones.get(position);
        holder.textView.setText(ubi.getNombre());
        holder.itemView.setOnClickListener(v -> listener.onUbicacionClick(ubi));
    }

    @Override
    public int getItemCount() {
        return listaUbicaciones.size();
    }

    public static class UbicacionViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public UbicacionViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
*/