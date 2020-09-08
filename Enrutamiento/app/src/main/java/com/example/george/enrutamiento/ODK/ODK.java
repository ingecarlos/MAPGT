package com.example.george.enrutamiento.ODK;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.george.enrutamiento.R;
import com.example.george.enrutamiento.adapter.LeftMenuAdapter;

public class ODK extends AppCompatActivity implements
        View.OnClickListener {

    //private FloatingActionButton back;//flotate de atras
    private FloatingActionButton launch_odk;//flotate de atras


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odk);
        //back = findViewById(R.id.back);
        //back.setOnClickListener(this);
        launch_odk = findViewById(R.id.launch_odk);
        launch_odk.setOnClickListener(this);


        ((TextView) (findViewById(R.id.toolbar_title))).setText("Formularios para el registro de actividades de acompañamiento");

        setListOptions();

        //setHeight();
    }

    private void setHeight(){
        ListView lv = findViewById(R.id.menuPrincipalList);

        ViewGroup.LayoutParams params = lv.getLayoutParams();

        Display display = getWindowManager(). getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;


        params.height = (int)(height/1.35);
        lv.setLayoutParams(params);
        lv.requestLayout();
    }


    String[] itemname = {
            "VISITA A CENTRO EDUCATIVO",
            "CAPACITACIÓN",
            "ACOMPAÑAMIENTO A COMUNIDAD DE APRENDIZAJE",
            "ASISTENCIA EN LOS PROFESORADOS LOS DÍAS SÁBADOS",
            "REUNIONES MINEDUC",
            "SESIONES DE TRABAJO FHI360",
            "ACOMPAÑAMIENTO A REDES"
    };

    Integer[] imgid = {
            R.drawable.ic_lm_cards,
            R.drawable.ic_lm_cards,
            R.drawable.ic_lm_cards,
            R.drawable.ic_lm_cards,
            R.drawable.ic_lm_cards,
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
                //VISITA A CENTRO EDUCATIVO
                intent = new Intent(this, OpenBlankFormActivity.class);
                intent.putExtra("nombre", "Bitácora");
                startActivity(intent);
                break;
            case 2:
                //CAPACITACIÓN
                intent = new Intent(this, OpenBlankFormActivity.class);
                intent.putExtra("nombre", "Instrumento_Capacitacion");
                startActivity(intent);
                break;
            case 3:
                //ACOMPAÑAMIENTO A COMUNIDAD DE APRENDIZAJE
                intent = new Intent(this, OpenBlankFormActivity.class);
                intent.putExtra("nombre", "Instrumento_A_Tecnica");
                startActivity(intent);
                break;
            case 4:
                //ASISTENCIA EN LOS PROFESORADOS LOS DÍAS SABADOS
                intent = new Intent(this, com.example.george.enrutamiento.ODK.Sesion_sabados.class);
                this.startActivity(intent);
                break;
            case 5:
                //REUNIONES MINEDUC
                intent = new Intent(this, OpenBlankFormActivity.class);
                intent.putExtra("nombre", "Instrumento_Reunion_Mineduc");
                startActivity(intent);
                break;
            case 6:
                //SESIONES DE TRABAJO FHI360
                intent = new Intent(this, OpenBlankFormActivity.class);
                intent.putExtra("nombre", "sesiones_trabajo_fhi");
                startActivity(intent);
                break;
            case 7:
                //ACOMPAÑAMIENTO A REDES
                intent = new Intent(this, OpenBlankFormActivity.class);
                intent.putExtra("nombre", "Instrumento_A_Tecnica_redes");
                startActivity(intent);
                break;
        }
    }

    protected void launchODK(){
        Intent intent;
        PackageManager manager = getPackageManager();
        intent = manager.getLaunchIntentForPackage("org.odk.collect.android");
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        int i = v.getId();
        switch (i) {
            case R.id.launch_odk:
                try {
                    PackageManager manager = getPackageManager();
                    intent = manager.getLaunchIntentForPackage("org.odk.collect.android");
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    startActivity(intent);
                    break;
                }catch(Exception e){
                    Toast.makeText(this,"El dispositivo no cuenta con la aplicación ODK Collect",Toast.LENGTH_LONG).show();
                }

        }
    }


    public void startActivityIfAvailable(Intent i) {
        startActivity(i);
    }

}
