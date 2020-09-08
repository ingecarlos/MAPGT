package com.example.george.enrutamiento.ODK;

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
import android.widget.Toast;

import com.example.george.enrutamiento.R;
import com.example.george.enrutamiento.adapter.LeftMenuAdapter;
import com.example.george.enrutamiento.informeGraficos.informeMenu;

public class ejecucion_ODK extends AppCompatActivity implements
        View.OnClickListener{
    private FloatingActionButton back;//flotate de atras
    private FloatingActionButton launch_odk;//flotate de atras


    private Button btnInformeObservacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejecucion__odk);

        launch_odk=findViewById(R.id.launch_odk);
        launch_odk.setOnClickListener(this);

        ((TextView) (findViewById(R.id.toolbar_title))).setText("Formularios para la ejecución de acompañamiento");

        setListOptions();


    }

    String[] itemname = {
            "CONFORMACIÓN DE COMUNIDADES DE APRENDIZAJE",
            "OBSERVACIÓN DE APLICACIÓN DEL CNB",
            "ACOMPAÑAMIENTO DEL USO DE TIEMPO DE CLASE",
            "OBSERVACIÓN EN EL AULA",
            "SEGUIMIENTO A REDES EDUCATIVAS",
            "PLAN DE MEJORA",
            "SEGUIMIENTO A INSCRIPCIÓN AUTOMÁTICA 2018",
            "INFORME DE TIEMPO DE CLASE"
    };

    Integer[] imgid = {
            R.drawable.ic_lm_cards,
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
                //conformación de comunidades de aprendizaje
                intent = new Intent(this,OpenBlankFormActivity.class);
                intent.putExtra("nombre","conformacion_comunidades");
                startActivity(intent);
                break;
            case 2:
                //observación de aplicación del CNB
                intent = new Intent(this,OpenBlankFormActivity.class);
                intent.putExtra("nombre",getResources().getString(R.string.IDAplicacionCNB));
                startActivity(intent);
                break;
            case 3:
                //acompañamiento del uso de tiempo de clase
                intent = new Intent(this,OpenBlankFormActivity.class);
                intent.putExtra("nombre",getResources().getString(R.string.IDStallings));
                startActivity(intent);
                break;
            case 4:
                //observación en el aula
                intent = new Intent(this,OpenBlankFormActivity.class);
                intent.putExtra("nombre","Observacion_Aula");
                startActivity(intent);
                break;
            case 5:
                //seguimiento a redes educativas
                intent = new Intent(this,OpenBlankFormActivity.class);
                intent.putExtra("nombre","plan_red_educativa");
                startActivity(intent);
                break;
            case 6:
                //plan de mejora
                intent = new Intent(this,OpenBlankFormActivity.class);
                intent.putExtra("nombre","plan_mejora_anual");
                startActivity(intent);
                break;
            case 7:
                //seguimiento a inscripción automática 2018
                intent = new Intent(this,OpenBlankFormActivity.class);
                intent.putExtra("nombre","seguimiento_inscripcion_automatica");
                startActivity(intent);
                break;
            case 8:
                //informe de tiempo de clase
                Intent intentInforme = new Intent(this, informeMenu.class);
                ejecucion_ODK.this.startActivity(intentInforme);
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
}
