package com.example.proyectofinal_javiergarrido.ui.diario;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal_javiergarrido.R;
import com.example.proyectofinal_javiergarrido.ui.ServiciosServer.ClienteApi;
import com.example.proyectofinal_javiergarrido.ui.ServiciosServer.ServicioApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotaAdapter extends RecyclerView.Adapter<NotaAdapter.NotaViewHolder> {

    private List<Nota> listaNotas;
    private Context context;

    public NotaAdapter(List<Nota> listaNotas, Context context) {
        this.listaNotas = listaNotas;
        this.context = context;
    }

    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_nota, parent, false);
        return new NotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, int position) {
        Nota nota = listaNotas.get(position);

        holder.titulo.setText(nota.getTitulo());
        holder.fecha.setText(formatFecha(nota.getFecha()));
        holder.colorCard.setCardBackgroundColor(Color.parseColor(nota.getColor()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalleNotaActivity.class);
            intent.putExtra("titulo", nota.getTitulo());
            intent.putExtra("contenido", nota.getContenido());
            intent.putExtra("fecha", formatFecha(nota.getFecha()));
            context.startActivity(intent);
        });

        holder.botonEliminar.setOnClickListener(v -> {
            int idNota = nota.getIdNota();
            if (idNota != 0) {
                eliminarNota(idNota, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaNotas.size();
    }

    public void actualizarLista(List<Nota> listaNotas) {
        this.listaNotas = listaNotas;
        notifyDataSetChanged();
    }

    private void eliminarNota(int idNota, int position) {
        Log.d("NotaAdapter", "Enviando ID de la nota para eliminar: " + idNota);

        ServicioApi servicioApi = ClienteApi.getClient().create(ServicioApi.class);
        Call<Void> call = servicioApi.eliminarNota(idNota);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("NotaAdapter", "Nota eliminada con éxito");
                    listaNotas.remove(position);
                    notifyItemRemoved(position);
                } else {
                    Log.e("NotaAdapter", "Error al eliminar nota: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("NotaAdapter", "Error al eliminar nota: " + t.getMessage());
            }
        });
    }

    private String formatFecha(String fecha) {
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        SimpleDateFormat formatoSalida = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));

        try {
            Date date = formatoEntrada.parse(fecha);
            return formatoSalida.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return fecha;
        }
    }

    public static class NotaViewHolder extends RecyclerView.ViewHolder {

        TextView titulo, fecha;
        Button botonEliminar;
        CardView colorCard;

        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.ed_TituloNota);
            fecha = itemView.findViewById(R.id.ed_FechaNota);
            botonEliminar = itemView.findViewById(R.id.boton_eliminar);
            colorCard = itemView.findViewById(R.id.cardViewNota);
        }
    }
}
