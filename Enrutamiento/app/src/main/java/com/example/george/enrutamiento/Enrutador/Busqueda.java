package com.example.george.enrutamiento.Enrutador;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.george.enrutamiento.R;
import com.example.george.enrutamiento.design.ExpandableListAdapter;
import com.example.george.enrutamiento.dialogs.DialogInfoCentro;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Busqueda extends AppCompatActivity {


    public static String departamento = "";//0
    public static String municipio = "";//1
    public static String nivel = "";//2
    public static String sector = "";//3
    //public static String plan = "";//4
    //public static String modalidad = "";//5


    public static Manejador_BD BD;


    public static Context context;// contexto d ela aplicacion
    public static Activity activity;//referencia a la actual activity, util en la funcion de geoposicion
    public static EstablecimientoAdapter ca;
    RecyclerView recList;

    public static boolean estado;//permite reciclar la vista, para la funcionalidad de geoposicionamiento
    //verdadero:geoposicion, falso:enrutamiento de centro
    static TextView cantidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        turnOnGps();

        ((TextView) (findViewById(R.id.toolbar_title))).setText("Búsqueda de centro educativo");

        context = this;
        activity = this;

        BD = new Manejador_BD(getApplicationContext());

        //apertura de base de datos
        try {
            BD.CrearDB();
            BD.openDataBase();
        } catch (Exception e) {
        }

        //configuracion de elementos de filtro ,{revisar !!!}
        Filtros.setContext(this);
        Filtros.setBD(BD);


        recList = findViewById(R.id.Lista);
        //recList.setOnClickListener(this);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        registerForContextMenu(recList);
        ca = new EstablecimientoAdapter(new ArrayList<Establecimiento>());
        recList.setAdapter(ca);



        iniciarLista();
        setMenu();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DialogInfoCentro.REQUEST_DIALOG) {
            if(resultCode == Activity.RESULT_OK){
                int result = data.getIntExtra("refrescar",0);
                if(result==1){
                    limpiarFiltros();
                    finish();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        limpiarFiltros();
    }

    protected void limpiarFiltros(){
        departamento = "";//0
        municipio = "";//1
        nivel = "";//2
        sector = "";//3
        //plan = "";//4
        //modalidad = "";//5
    }

    protected void setMenu() {
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHash;

    protected void iniciarLista() {
        Filtros.init_();
        listView = (ExpandableListView) findViewById(R.id.lvExp);
        fillData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listHash);
        listView.setAdapter(listAdapter);

        listView.setOnChildClickListener(itemClicked);
    }

    ExpandableListView.OnChildClickListener itemClicked = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            clearHeaders(groupPosition + 1);
            switch (groupPosition) {
                case 0:
                    departamento = (String) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
                    break;
                case 1:
                    municipio = (String) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
                    break;
                case 2:
                    nivel = (String) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
                    break;
                case 3:
                    sector = (String) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
                    break;
                //case 4:
                //    plan = (String) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
                //    break;
                //case 5:
                //    modalidad = (String) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
                //    break;
            }
            updateFiltros(groupPosition, childPosition);
            ActualizarLista();
            return true;
        }
    };

    protected void ActualizarLista() {
        listView = (ExpandableListView) findViewById(R.id.lvExp);
        fillData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listHash);
        listView.setAdapter(listAdapter);

        listView.setOnChildClickListener(itemClicked);
    }


    private void fillData() {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listDataHeader.add("Departamento");
        listDataHeader.add("Municipio");
        listDataHeader.add("Nivel Escolar");
        listDataHeader.add("Sector");
        //listDataHeader.add("Plan");
        //listDataHeader.add("Modalidad");


        List<String> deptoList = new ArrayList<>();
        deptoList = Filtros.getDepartamentos();

        List<String> municipioList = new ArrayList<>();
        municipioList = Filtros.getMunicipios();

        List<String> nivelList = new ArrayList<>();
        nivelList = Filtros.getNivelEscolar();

        List<String> sectorList = new ArrayList<>();
        sectorList = Filtros.getSector();

        //List<String> planList = new ArrayList<>();
        //planList = Filtros.getPlan();

        //List<String> modalidadList = new ArrayList<>();
        //modalidadList = Filtros.getModalidad();

        listHash.put(listDataHeader.get(0), deptoList);
        listHash.put(listDataHeader.get(1), municipioList);
        listHash.put(listDataHeader.get(2), nivelList);
        listHash.put(listDataHeader.get(3), sectorList);
        //listHash.put(listDataHeader.get(4), planList);
        //listHash.put(listDataHeader.get(5), modalidadList);
    }


    public static void set_estado(boolean e) {
        Busqueda.estado = e;
    }


    private void createList() {
        if (ca == null) return;

        Filtros.Establecimiento(); //acá se rellena el establecimientos adapter "ca"

    }

    private void turnOnGps() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            (Toast.makeText(this,
                    R.string.encender_gps, Toast.LENGTH_LONG)).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

/*
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > -1) {
            ca.clearEstablecimientosList();
            recList.removeAllViews();
            Filtros.actualizar(parent.getId(), position);
            //createList();
        }
    }*/

    protected void updateFiltros(int grupo, int item) {
        ca.clearEstablecimientosList();
        recList.removeAllViews();
        Filtros.actualizar(grupo, item);
        if (grupo==3/*si se seleccióno un sector*/)
            createList();
    }

    protected void clearHeaders(int pos) {
        switch (pos) {
            case 0:
                departamento = "";
            case 1:
                municipio = "";
            case 2:
                nivel = "";
            case 3:
                sector = "";
                //case 4:
                //plan = "";
                //case 5:
                //modalidad = "";
        }
    }




    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rutas, menu);
        return true;
    }

    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    // Log.e(Constants.DEBUG_LOG, "onMenuOpened", e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.modificar_ruta:
                Intent intent = new Intent(this, Ruta.class);
                this.startActivity(intent);
                break;
            case R.id.ver_ruta:
                Enrutador.desplegar_mapa(this);
                //startService(new Intent(this, BubbleHeadService.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
