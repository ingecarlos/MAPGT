package com.example.george.enrutamiento;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.george.enrutamiento.Enrutador.Busqueda;
import com.example.george.enrutamiento.Enrutador.busquedaCentro;
import com.example.george.enrutamiento.Enrutador.reporteIncidentes;
import com.example.george.enrutamiento.ODK.ODK;
import com.example.george.enrutamiento.ODK.ejecucion_ODK;
import com.example.george.enrutamiento.adapter.LeftMenuAdapter;

public class Menu_Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__principal);
        permisos();

        ((TextView) (findViewById(R.id.toolbar_title))).setText("Menú Principal");
        setListOptions();

        Context mContext = null;
    }


    String[] itemname = {
            "1. ESTABLECER RUTA",
            "2. REGISTRAR ACTIVIDADES DE ACOMPAÑAMIENTO",
            "3. EJECUTAR ACOMPAÑAMIENTO",
            "4. REPORTAR INCIDENTE",
            "5. CERRAR SESIÓN"
    };

    Integer[] imgid = {
            R.drawable.ic_lm_search,
            R.drawable.ic_lm_cards,
            R.drawable.ic_lm_cards,
            R.drawable.ic_lm_lists,
            R.drawable.ic_friends
    };


    public void setListOptions() {
        LeftMenuAdapter adapter = new LeftMenuAdapter(this, itemname, imgid);
        ListView list = (ListView) findViewById(R.id.menuPrincipalList);
        LayoutInflater myinflater = getLayoutInflater();
        //ViewGroup myHeader = (ViewGroup) myinflater.inflate(R.layout.nav_header_main, list, false);
        //list.addHeaderView(myHeader, null, false);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            selectOption(position);
                                        }
                                    }
        );
    }

    protected void selectOption(int opcion) {
        opcion += 1;
        Intent intent;
        switch (opcion) {
            case 1:
                //1. ESTABLECER RUTA
                Busqueda.set_estado(false);//vista normal
                intent = new Intent(this, busquedaCentro.class);
                Menu_Principal.this.startActivity(intent);
                break;
            case 2:
                //2. REGISTRO DE ACTIVIDADES DE ACOMPAÑAMIENTO
                intent = new Intent(this, ODK.class);
                Menu_Principal.this.startActivity(intent);
                break;
            case 3:
                //2. EJECUCIÓN DE ACOMPAÑAMIENTO
                intent = new Intent(this, ejecucion_ODK.class);
                Menu_Principal.this.startActivity(intent);
                break;
            case 4:
                //4. REPORTAR INCIDENTE
                intent = new Intent(this, reporteIncidentes.class);
                Menu_Principal.this.startActivity(intent);
                break;
            case 5:
                //5. CERRAR SESIÓN
                clearMetadataUsername();
                intent = new Intent(this, Login.class);
                startActivity(intent);
                finish();
                break;
        }

        //((TextView)(findViewById(R.id.toolbar_title))).setText(Integer.toString(opcion));
    }

    protected void validateMeta() {
        Context mContext = null;
        try {
            mContext = createPackageContext("org.odk.collect.android", CONTEXT_IGNORE_SECURITY);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            String strName = sp.getString("username", "");
            Log.d("DEBUG", "validateMeta-Menu_principal: " + strName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void clearMetadataUsername() {
        Context mContext = null;
        try {

            mContext = createPackageContext("org.odk.collect.android", CONTEXT_IGNORE_SECURITY);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = sp.edit();
            editor.remove("username");
            editor.apply();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void permisos() {
        //CONSEGUIR LOS PERMISOS PARA LA APP
        //ACCES FINE LOCATION
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        //ACCES COARSE LOCATION
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
        //WRITE EXTERNAL STORAGE
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }


        //permisos para la burbuja
        //reunir todos los permisos de la aplicacion en este metodo!!!!!!!!!!!!!!
        //Debido a cambios en la aplicación ya no se hace uso de la burbuja
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            Intent intent = new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())
            );
            startActivityForResult(intent, TYPE_APPLICATION_OVERLAY);

        } else {
            //  initializeView();
        }
        */
    }


}