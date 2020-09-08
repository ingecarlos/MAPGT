package com.example.george.enrutamiento.Enrutador;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.george.enrutamiento.Login;
import com.example.george.enrutamiento.ODK.OpenBlankFormActivity;
import com.example.george.enrutamiento.R;
import com.example.george.enrutamiento.dialogs.DialogInfoCentro;

import org.json.JSONObject;

import java.util.List;

public class EstablecimientoAdapter_seleccionar extends RecyclerView.Adapter<EstablecimientoAdapter_seleccionar.EstablecimientoViewHolder>
{
private List<Establecimiento> establecimientosList;

public EstablecimientoAdapter_seleccionar(List<Establecimiento> establecimientosList)
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
                    OpenBlankFormActivity.codigo.setCodigo(vCodigo);
                }
            });
        }


    }


}
