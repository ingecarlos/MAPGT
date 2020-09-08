package com.example.george.enrutamiento.Enrutador;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.george.enrutamiento.ODK.OpenBlankFormActivity;
import com.example.george.enrutamiento.R;

import static android.view.View.INVISIBLE;

public class DetalleEstablecimiento extends AppCompatActivity

    implements LocationListener,View.OnClickListener
{   //datos a mostrar
    private TextView codigo;
    private TextView departamento;
    private TextView municipio;
    private TextView nombre;
    private TextView direccion;
    private TextView plan;
    private TextView nivel;
    private double longitud;
    private double latitud;
    private String Codigo;
    public Manejador_BD BD;//manejador de la base de datos
    private FloatingActionButton add_rut;//agrega el establecimiento a lista a recorrer
    private FloatingActionButton geoposicionar;//obtiene coordenadas actuales para geoposicionar
    private FloatingActionButton back;//flotate de atras
    private static final int LOCATION_REQUEST = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_establecimiento);
        BD=Busqueda.BD;
        Codigo=getIntent().getExtras().getString("codigo");
        add_rut= findViewById(R.id.add);
        geoposicionar=findViewById(R.id.geoposicionamiento);
        codigo = findViewById(R.id.t1);
        departamento = findViewById(R.id.t2);
        municipio = findViewById(R.id.t3);
        nombre = findViewById(R.id.t4);
        direccion = findViewById(R.id.t5);
        nivel = findViewById(R.id.t6);
        plan = findViewById(R.id.t7);
        back=findViewById(R.id.back);

        //consulta que retorna los datos del centro educativo
        Cursor cursor=BD.select(
                "select B.nombre as a, C.nombre as b,A.nombre as c,A.direccion as d,D.nombre as e,E.nombre as f, " +
                        "A.latitud as g, A.longitud as h"+
                        " from establecimiento as A " +
                        "inner join Municipio        as C On C.id_municipio=A.id_municipio " +
                        "inner join Departamento     as B On B.id_departamento=C.id_departamento and C.id_departamento= A.id_departamento " +
                        "inner join Nivel_escolar    as D On D.id_nivel_escolar=A.id_nivel_escolar " +
                        "inner join plan             as E On E.id_plan=A.id_plan " +
                        "where A.codigo='"+Codigo+"'");

        cursor.moveToFirst();
        //obtencion de la informacion de la bd
        codigo.setText(Codigo);
        departamento.setText(cursor.getString(cursor.getColumnIndex("a")));
        municipio.setText(cursor.getString(cursor.getColumnIndex("b")));
        nombre.setText(cursor.getString(cursor.getColumnIndex("c")));
        direccion.setText(cursor.getString(cursor.getColumnIndex("d")));
        nivel.setText(cursor.getString(cursor.getColumnIndex("e")));
        plan.setText(cursor.getString(cursor.getColumnIndex("f")));
        if (!Busqueda.estado)
        {
            latitud = Double.parseDouble(cursor.getString(cursor.getColumnIndex("g")));
            longitud = Double.parseDouble(cursor.getString(cursor.getColumnIndex("h")));

        }
        cursor.close();
        back.setOnClickListener(this);
        add_rut.setOnClickListener(this);
        geoposicionar.setOnClickListener(this);

        if(Busqueda.estado)
            {
                add_rut.hide();
                geoposicionar.show();
            }
        else
            {
                add_rut.show();
                geoposicionar.hide();
            }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onClick(View v)
    {
        int i=v.getId();
        switch (i)
        {
            case R.id.back:
                finish();
                break;
            case R.id.add:
                    Filtros.setRuta
                            (
                                    new Establecimiento(
                                            codigo.getText().toString(),
                                            nombre.getText().toString(),
                                            direccion.getText().toString(),
                                            latitud,longitud)
                            );
                    //Busqueda._show();
                    finish();
                break;
            case R.id.geoposicionamiento:

                  String coordenadas =Enrutador.get_coordenadas(this);
                  //creacion de un dialogo de alerta de aviso de recepcion de coordenadas

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
                dialogo1.setTitle("Importante !!!");
                dialogo1.setMessage(
                        "Se hizo la lectura de las coordenas de la ubicion actual ("+coordenadas+
                                "), por lo que usted debería estar en el centro educativo."

                );
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Me encuentro en \n el centro educativo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id)
                    {

                        // eliminacion de dos actividades
                        Busqueda.activity.finish();//finalizacion de actividad padre
                        finish();//finalizacion de actual actividad

                        //realizar el reporte del incidente
                        //formato sugerido
                                /*
                                { codigo='01-02-1234-43',
                                 latitud ='14.34',
                                 longitud = '-90.45'
                                 }
                                 */
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dialogo1.show();
                break;
        }
    }
}