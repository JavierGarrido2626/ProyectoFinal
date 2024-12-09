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
        coloresMapa.put("rojo", "#FF0000");
        coloresMapa.put("verde", "#00FF00");
        coloresMapa.put("azul", "#0000FF");
        coloresMapa.put("amarillo", "#FFFF00");
        coloresMapa.put("magenta", "#FF00FF");
        coloresMapa.put("cian", "#00FFFF");
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

        if (nota.getColor() != null && !nota.getColor().isEmpty()) {
            String colorEnHex = coloresMapa.get(nota.getColor().toLowerCase());
            if (colorEnHex != null) {
                holder.itemView.setBackgroundColor(Color.parseColor(colorEnHex));
            } else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }
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
        String[] colores = {"rojo", "verde", "azul", "amarillo", "magenta", "cian"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Seleccionar color");
        builder.setItems(colores, (dialog, which) -> {
            String colorSeleccionado = colores[which];
            nota.setColor(colorSeleccionado);

            boolean resultado = baseDeDatos.actualizarNota(nota);

            if (resultado) {
                listaNotas.set(posicion, nota);
                notifyItemChanged(posicion);
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
