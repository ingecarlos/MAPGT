package com.example.george.enrutamiento.Enrutador;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.george.enrutamiento.R;

import java.util.List;

public class EstablecimientoAdapter_ruta extends RecyclerView.Adapter<EstablecimientoAdapter_ruta.EstablecimientoViewHolder>
{
private List<Establecimiento> establecimientosList;

public EstablecimientoAdapter_ruta(List<Establecimiento> establecimientosList)
{
    this.establecimientosList=establecimientosList;
}

public void clearEstablecimientosList()
{
    this.establecimientosList.clear();

}


    public void setEstablecimientosList(List<Establecimiento> establecimientosList)
    {
        this.establecimientosList.clear();
        this.establecimientosList.addAll(establecimientosList);
    }

    @NonNull
    @Override
    public EstablecimientoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.cardview_ruta,viewGroup,false);
        return new EstablecimientoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EstablecimientoViewHolder establecimientoViewHolder, int i)
    {
        Establecimiento est=establecimientosList.get(i);

        establecimientoViewHolder.vTitle.setText("Establecimiento a visitar No."+(i+1));
        establecimientoViewHolder.vNombre.setText(est.getNombre()+"");
        establecimientoViewHolder.vDireccion.setText(est.getDireccion()+"");
        establecimientoViewHolder.vCodigo=est.getCodigo();
        establecimientoViewHolder.vindice=i;

        if (i==0)establecimientoViewHolder.vsubir.setVisibility(View.INVISIBLE);
        else establecimientoViewHolder.vsubir.setVisibility(View.VISIBLE);
        if(i==Filtros.ruta.size()-1)establecimientoViewHolder.vbajar.setVisibility(View.INVISIBLE);
        else establecimientoViewHolder.vbajar.setVisibility(View.VISIBLE);


    }

    @Override
    public int getItemCount() {
        return establecimientosList.size();
    }

    public static  class EstablecimientoViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView vTitle;
        protected TextView vNombre;
        protected TextView vDireccion;
        protected String vCodigo;
        protected ToggleButton vsubir;
        protected ToggleButton vbajar;
        protected ToggleButton veliminar;

        protected int vindice;

        public EstablecimientoViewHolder(final View itemView)
        {
            super(itemView);
            itemView.setClickable(true);
            vTitle=itemView.findViewById(R.id.title_cw_ruta);
            vNombre= itemView.findViewById(R.id.nombre_cw_ruta);
            vDireccion=itemView.findViewById(R.id.direccion_cw_ruta);
            vsubir=itemView.findViewById(R.id.toggleButton4);
            vbajar=itemView.findViewById(R.id.toggleButton5);
            veliminar=itemView.findViewById(R.id.toggleButton6);

            vsubir.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        Filtros.subir_ruta(vindice);
                        Ruta.refresh();
                        vsubir.setChecked(false);
                    }
                }
            });
            vbajar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        vbajar.setChecked(false);
                        Filtros.bajar_ruta(vindice);
                        Ruta.refresh();
                    }
                }
            });
            veliminar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        veliminar.setChecked(false);
                        Filtros.eliminar(vindice);
                        Ruta.refresh();
                    }
                }
            });

        }
    }
}
