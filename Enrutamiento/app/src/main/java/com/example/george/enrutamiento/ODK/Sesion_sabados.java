package com.example.george.enrutamiento.ODK;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.george.enrutamiento.R;
import com.example.george.enrutamiento.adapter.LeftMenuAdapter;
import com.example.george.enrutamiento.dialogs.DialogWarning;

public class Sesion_sabados extends AppCompatActivity implements
        View.OnClickListener {



    private FloatingActionButton launch_odk;//flotate de atras


    private Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion_sabados);
        mcontext = this;


        launch_odk = findViewById(R.id.launch_odk);
        launch_odk.setOnClickListener(this);


        //Sesión en sedes Sábados

        ((TextView) (findViewById(R.id.toolbar_title))).setText("Sesión en sedes Sábados");
        setListOptions();
    }


    String[] itemname = {
            "INICIO",
            "REPORTAR ASIGNATURA",
            "FIN"
    };

    Integer[] imgid = {
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
                //INICIO
                //este intent espera un resultado del cuadro de dialogo mostrado
                //las acciones se muestran en la funcion OnActivityResult de esta clase
                intent = new Intent(this, DialogWarning.class);
                intent.putExtra("mensaje", getString(R.string.inicio_sabado_mensaje)); //Optional parameters
                intent.putExtra("from", DialogWarning.INICIO_PROFESORADO);
                startActivityForResult(intent, DialogWarning.REQUEST_DIALOG);
                break;
            case 2:
                //INTERMEDIO
                intent = new Intent(this, OpenBlankFormActivity.class);
                intent.putExtra("nombre", "REPORTAR_ACOMPANAMIENTO_SEDE_SABADO");
                startActivity(intent);
                break;
            case 3:
                //FIN
                //este intent espera un resultado del cuadro de dialogo mostrado
                //las acciones se muestran en la funcion OnActivityResult de esta clase
                intent = new Intent(this, DialogWarning.class);
                intent.putExtra("mensaje", getString(R.string.fin_sabado_mensaje)); //Optional parameters
                intent.putExtra("from", DialogWarning.FIN_PROFESORADO);
                startActivityForResult(intent, DialogWarning.REQUEST_DIALOG);
                break;
        }

        //((TextView)(findViewById(R.id.toolbar_title))).setText(Integer.toString(opcion));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data==null)return;
        if (requestCode == DialogWarning.REQUEST_DIALOG) {
            int from = data.getIntExtra("from", -1);
            String buttonPressed = data.getStringExtra("buttonPressed");
            if (buttonPressed.equals("CANCEL")) return;
            Intent intent;
            switch (from) {
                case DialogWarning.INICIO_PROFESORADO:
                    intent = new Intent(mcontext, OpenBlankFormActivity.class);
                    intent.putExtra("nombre", "INICIO_ACOMPANAMIENTO_SEDE_SABADO");
                    startActivity(intent);
                    break;
                case DialogWarning.FIN_PROFESORADO:
                    intent = new Intent(mcontext, OpenBlankFormActivity.class);
                    intent.putExtra("nombre", "FIN_ACOMPANAMIENTO_SEDE_SABADO");
                    startActivity(intent);
                    break;
            }

        }
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Importante");

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
}
