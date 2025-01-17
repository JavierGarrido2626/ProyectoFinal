package com.example.proyectofinal_javiergarrido.ui.diario;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal_javiergarrido.CrearNotaActivity;
import com.example.proyectofinal_javiergarrido.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotaAdapter extends RecyclerView.Adapter<NotaAdapter.NotaViewHolder> {

    private List<Nota> listaNotas;
    private BaseDatosDiarioSQL baseDeDatos;
    private Context context;

    private static final Map<String, String> coloresMapa;

    static {
        coloresMapa = new HashMap<>();
        coloresMapa.put("rojo", "#FF6B6B");
        coloresMapa.put("verde", "#A5D6A7");
        coloresMapa.put("azul", "#90CAF9");
        coloresMapa.put("amarillo", "#FFF59D");
        coloresMapa.put("rosa", "#F48FB1");
        coloresMapa.put("cian", "#B2EBF2");
        coloresMapa.put("blanco", "#FFFFFF");
    }

    public NotaAdapter(List<Nota> listaNotas, Context context) {
        this.listaNotas = listaNotas;
        this.context = context;
        this.baseDeDatos = new BaseDatosDiarioSQL(context);
    }

    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nota, parent, false);
        return new NotaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, int position) {
        final Nota nota = listaNotas.get(position);
        holder.tvTitulo.setText(nota.getTitulo());
        holder.tvContenido.setText(nota.getContenido());
        holder.tvFecha.setText(nota.getFecha());

        // Aplicar color de fondo según el color seleccionado
        if (nota.getColor() != null && !nota.getColor().isEmpty()) {
            String colorEnHex = coloresMapa.get(nota.getColor().toLowerCase());
            holder.itemView.setBackgroundColor(
                    colorEnHex != null ? Color.parseColor(colorEnHex) : Color.TRANSPARENT
            );
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.botonEliminar.setOnClickListener(v -> {
            baseDeDatos.eliminarNota(nota);
            listaNotas.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, listaNotas.size());
        });

        holder.itemView.setOnLongClickListener(v -> {
            mostrarDialogoColor(nota, position);
            return true;
        });

        holder.itemView.setOnClickListener(v -> {
            abrirNotaEditar(nota);
        });
    }

    private void mostrarDialogoColor(Nota nota, int posicion) {
        String[] colores = {"rojo", "verde", "azul", "amarillo", "rosa", "cian", "blanco"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Seleccionar color");
        builder.setItems(colores, (dialog, which) -> {
            String colorSeleccionado = colores[which];
            nota.setColor(colorSeleccionado);

            // Actualizar la base de datos y la lista
            if (baseDeDatos.actualizarNota(nota)) {
                listaNotas.set(posicion, nota);
                notifyDataSetChanged(); // Actualiza la lista completa
            }
        });
        builder.show();
    }

    private void abrirNotaEditar(Nota nota) {
        Intent intent = new Intent(context, CrearNotaActivity.class);
        intent.putExtra("id", nota.getId());
        intent.putExtra("titulo", nota.getTitulo());
        intent.putExtra("contenido", nota.getContenido());
        intent.putExtra("color", nota.getColor());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return listaNotas.size();
    }

    static class NotaViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo;
        TextView tvContenido;
        TextView tvFecha;
        Button botonEliminar;

        NotaViewHolder(View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.ed_TituloNota);
            tvContenido = itemView.findViewById(R.id.ed_ContenidoNota);
            tvFecha = itemView.findViewById(R.id.ed_FechaNota);
            botonEliminar = itemView.findViewById(R.id.boton_eliminar);
        }
    }

    public void actualizarLista(List<Nota> nuevasNotas) {
        this.listaNotas = nuevasNotas;
        notifyDataSetChanged();
    }
}
