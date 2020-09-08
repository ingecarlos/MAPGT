package com.example.george.enrutamiento.Enrutador;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.george.enrutamiento.ODK.OpenBlankFormActivity;
import com.example.george.enrutamiento.R;
import com.example.george.enrutamiento.adapter.LeftMenuAdapter;
import com.example.george.enrutamiento.informeGraficos.informeMenu;

public class reporteIncidentes extends AppCompatActivity implements
        View.OnClickListener{
    private FloatingActionButton back;//flotate de atras
    private FloatingActionButton launch_odk;//flotate de atras


    private Button btnInformeObservacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte__incidentes);

        launch_odk=findViewById(R.id.launch_odk);
        launch_odk.setOnClickListener(this);

        ((TextView) (findViewById(R.id.toolbar_title))).setText("Reportar incidente");

        setListOptions();
    }

    String[] itemname = {
            "REPORTAR INCIDENTE DE INFRAESTRUCTURA",
            "REPORTAR COORDENADAS ERRONEAS"
    };

    Integer[] imgid = {
            R.drawable.ic_lm_cards,
            R.drawable.ic_lm_cards
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
                //incidente de infraestructura
                intent = new Intent(this,OpenBlankFormActivity.class);
                intent.putExtra("nombre","incidenteInfraestructura");
                startActivity(intent);
                break;
            case 2:
                //coordenadas erroneas
                intent = new Intent(this,OpenBlankFormActivity.class);
                intent.putExtra("nombre","CoordenadasErroneas");
                startActivity(intent);
                break;
        }

        //((TextView)(findViewById(R.id.toolbar_title))).setText(Integer.toString(opcion));
    }


    @Override
    public void onClick(View v) {

        Intent intent;
        int i=v.getId();
        switch (i)
        {
            case R.id.back:
                finish();
                break;
            case R.id.launch_odk:
                PackageManager manager = getPackageManager();
                intent  = manager.getLaunchIntentForPackage("org.odk.collect.android");
                intent .addCategory(Intent.CATEGORY_LAUNCHER);
                startActivity(intent);
                break;

        }
    }
}
