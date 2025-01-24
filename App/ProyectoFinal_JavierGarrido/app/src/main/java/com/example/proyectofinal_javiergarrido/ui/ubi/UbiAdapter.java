package com.example.proyectofinal_javiergarrido.ui.ubi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinal_javiergarrido.R;
import com.example.proyectofinal_javiergarrido.ui.ServiciosServer.ServicioApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.Toast;

import java.util.List;

public class UbiAdapter extends RecyclerView.Adapter<UbiAdapter.UbicacionViewHolder> {

    private List<Ubicacion> ubicaciones;
    private OnItemClickListener listener;
    private ServicioApi servicioApi;

    public interface OnItemClickListener {
        void onItemClick(Ubicacion ubicacion);
    }

    public UbiAdapter(List<Ubicacion> ubicaciones, OnItemClickListener listener, ServicioApi servicioApi) {
        this.ubicaciones = ubicaciones;
        this.listener = listener;
        this.servicioApi = servicioApi;
    }

    @Override
    public UbicacionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_ubiadapter, parent, false);
        return new UbicacionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UbicacionViewHolder holder, int position) {
        Ubicacion ubicacion = ubicaciones.get(position);
        holder.bind(ubicacion);
    }

    @Override
    public int getItemCount() {
        return ubicaciones.size();
    }

    class UbicacionViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private Button btnBorrar;

        public UbicacionViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.ubicacionText);
            btnBorrar = itemView.findViewById(R.id.btnBorrar);
        }

        public void bind(final Ubicacion ubicacion) {
            textView.setText(ubicacion.getNombre());
            btnBorrar.setOnClickListener(v -> {
                eliminarUbicacion(ubicacion);
            });

            itemView.setOnClickListener(v -> listener.onItemClick(ubicacion));
        }

        private void eliminarUbicacion(Ubicacion ubicacion) {
            Call<Void> call = servicioApi.eliminarUbicacion(ubicacion.getId_ubicacion());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            ubicaciones.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, ubicaciones.size());
                            Toast.makeText(itemView.getContext(), "Ubicación eliminada correctamente", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(itemView.getContext(), "Error al eliminar la ubicación", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(itemView.getContext(), "Error de red", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
