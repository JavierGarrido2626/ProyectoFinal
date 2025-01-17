package com.example.proyectofinal_javiergarrido.ui.ubi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinal_javiergarrido.R;
import java.util.List;

public class UbiAdapter extends RecyclerView.Adapter<UbiAdapter.UbicacionViewHolder> {

    private List<Ubicacion> ubicaciones;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Ubicacion ubicacion);
    }

    public UbiAdapter(List<Ubicacion> ubicaciones, OnItemClickListener listener) {
        this.ubicaciones = ubicaciones;
        this.listener = listener;
    }

    @Override
    public UbicacionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_ubiadapter, parent, false);
        return new UbicacionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UbicacionViewHolder holder, int position) {
        Ubicacion ubicacion = ubicaciones.get(position);
        holder.bind(ubicacion, position);
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

        public void bind(final Ubicacion ubicacion, final int position) {
            textView.setText(ubicacion.getNombre());

            btnBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ubicaciones.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, ubicaciones.size());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(ubicacion);
                }
            });
        }
    }
}
