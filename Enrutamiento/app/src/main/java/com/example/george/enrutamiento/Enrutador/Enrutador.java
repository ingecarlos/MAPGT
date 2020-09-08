package com.example.george.enrutamiento.Enrutador;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.example.george.enrutamiento.R;

public  class Enrutador implements
        LocationListener
{
    private static final int LOCATION_REQUEST = 101;
    public  static String ruta(Activity a)
    {
        String texto ="";
        for (int i =0;i<Filtros.ruta.size();i++)
        {
            texto =texto +"/'"+Filtros.ruta.get(i).getLatitud()+","+Filtros.ruta.get(i).getLongitud()+"'";
        }
        texto = "https://www.google.com/maps/dir/"+get_coordenadas(a)+texto;
        return texto;
    }

    public static String get_coordenadas(Activity activity)
    {
        //retorna la posicion actual en el formato 'latitud,longitud'
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setIcon(R.mipmap.ic_launcher);

        progressDialog.setMessage("Obteniendo coordenadas ...");
        progressDialog.show();
            /*RUTA*/
            LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //Permisos para obtener localizacion actual
                Toast.makeText(activity, R.string.error_permisos_gps, Toast.LENGTH_LONG).show();
                return "";
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
            Location myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if(myLocation == null){
                (Toast.makeText(activity,
                        R.string.error_gps , Toast.LENGTH_SHORT)).show();
                progressDialog.dismiss();
                return "";
            }
            double longitude = myLocation.getLongitude();
            double latitude = myLocation.getLatitude();

        progressDialog.dismiss();
        return  myLocation.getLatitude()+","+myLocation.getLongitude();
    }

    /*Variables necesarias para extraccion de coordenadas GPS */
    static double longitude,latitude;
    private static final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    public static void desplegar_mapa(Activity a)
    {
        String ruta=Enrutador.ruta(a);
        //if(ruta=="") return;

        Uri rutaUri = Uri.parse(ruta);
        Intent intent = new Intent( Intent.ACTION_VIEW, rutaUri);
        a.startActivity( intent );

    }

    @Override
    public void onLocationChanged(Location location) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}
