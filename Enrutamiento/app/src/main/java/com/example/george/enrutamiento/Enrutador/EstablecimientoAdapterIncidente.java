package com.example.george.enrutamiento.Enrutador;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.george.enrutamiento.R;

import java.util.List;

public class EstablecimientoAdapterIncidente extends RecyclerView.Adapter<EstablecimientoAdapterIncidente.EstablecimientoViewHolder> {
    private List<Establecimiento> establecimientosList;

    public EstablecimientoAdapterIncidente(List<Establecimiento> establecimientosList) {
        this.establecimientosList = establecimientosList;
    }

    public void clearEstablecimientosList() {
        this.establecimientosList.clear();

    }


    public void setEstablecimientosList(List<Establecimiento> establecimientosList) {
        this.establecimientosList.clear();
        this.establecimientosList.addAll(establecimientosList);
    }

    @NonNull
    @Override
    public EstablecimientoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.cardview_incidente, viewGroup, false);
        return new EstablecimientoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EstablecimientoViewHolder establecimientoViewHolder, int i) {
        Establecimiento est = establecimientosList.get(i);
        establecimientoViewHolder.vNombre.setText(est.getNombre());
        establecimientoViewHolder.vDireccion.setText(est.getDireccion());
        establecimientoViewHolder.vCodigo = est.getCodigo();
        establecimientoViewHolder.vTitle.setText(est.getCodigo());
        establecimientoViewHolder.foto.setBackgroundResource(R.drawable.ic_action_home);
        //establecimientoViewHolder.foto.setImageIcon(@drawable/ic_action_home);
    }

    @Override
    public int getItemCount() {
        return establecimientosList.size();
    }


    public static class EstablecimientoViewHolder extends RecyclerView.ViewHolder {
        protected TextView vNombre;
        protected TextView vDireccion;
        protected TextView vTitle;
        protected String vCodigo;
        protected ImageView foto;

        public EstablecimientoViewHolder(final View itemView) {
            super(itemView);
            itemView.setClickable(true);
            vNombre = itemView.findViewById(R.id.nombre);
            vDireccion = itemView.findViewById(R.id.direccion);
            vTitle = itemView.findViewById(R.id.codigo);
            foto = itemView.findViewById(R.id.imageAvatar_draggable);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), ((TextView)itemView.findViewById(R.id.codigo)).getText(), Toast.LENGTH_LONG).show();
                }
            });
        }

        private boolean exist() {//recorrer en todos los elemetnos incluidos en la lista de establecimientos
            // de la clase Busqueda.java
            if (Filtros.ruta.isEmpty()) {
                return false;
            } else {
                for (int i = 0; i < Filtros.ruta.size(); i++) {
                    if (vCodigo.equals(Filtros.ruta.get(i).getCodigo())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
