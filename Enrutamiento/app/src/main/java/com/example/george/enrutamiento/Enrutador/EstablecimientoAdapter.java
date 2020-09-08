package com.example.george.enrutamiento.Enrutador;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.george.enrutamiento.R;
import com.example.george.enrutamiento.dialogs.DialogInfoCentro;

import java.util.List;

public class EstablecimientoAdapter extends RecyclerView.Adapter<EstablecimientoAdapter.EstablecimientoViewHolder>
{
private List<Establecimiento> establecimientosList;

public EstablecimientoAdapter(List<Establecimiento> establecimientosList)
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
                    inflate(R.layout.cardview,viewGroup,false);
        return new EstablecimientoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EstablecimientoViewHolder establecimientoViewHolder, int i)
    {
        Establecimiento est=establecimientosList.get(i);
        establecimientoViewHolder.vNombre.setText(est.getNombre());
        establecimientoViewHolder.vDireccion.setText(est.getDireccion());
        establecimientoViewHolder.vCodigo=est.getCodigo();
        establecimientoViewHolder.vTitle.setText(est.getCodigo());
    }

    @Override
    public int getItemCount() {
        return establecimientosList.size();
    }



    public static  class EstablecimientoViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView vNombre;
        protected TextView vDireccion;
        protected TextView vTitle;
        protected String vCodigo;


        public EstablecimientoViewHolder(final View itemView)
        {
            super(itemView);
            itemView.setClickable(true);
            vNombre= itemView.findViewById(R.id.nombre);
            vDireccion=itemView.findViewById(R.id.direccion);
            vTitle= itemView.findViewById(R.id.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    //verificar si ya fue incluido en los centros a visitar

                    if(exist())
                    {
                        Toast.makeText(itemView.getContext(), "Centro ya seleccionado", Toast.LENGTH_LONG).show();
                    }
                    else
                        {
                            //*
                            Intent intent = new Intent(Busqueda.activity, DialogInfoCentro.class);
                            intent.putExtra("codigo",""+ vCodigo);
                            Busqueda.activity.startActivityForResult(intent,DialogInfoCentro.REQUEST_DIALOG);
                            //Busqueda.context.startActivity(intent);
                            //*/
                            /*
                            Intent intent = new Intent(Busqueda.context, DetalleEstablecimiento.class);
                            intent.putExtra("codigo",""+ vCodigo);
                            Busqueda.context.startActivity(intent);
                            //*/
                        }
                }
            });
        }

        private boolean exist()
        {//recorrer en todos los elemetnos incluidos en la lista de establecimientos
            // de la clase Busqueda.java
            if (Filtros.ruta.isEmpty() )
            {
                return false;
            }
            else
                {
                 for (int i=0;i<Filtros.ruta.size();i++)
                 {
                     if(vCodigo.equals(Filtros.ruta.get(i).getCodigo()))
                     {
                         return true;
                     }
                 }
                }
            return false;
        }
    }
}
