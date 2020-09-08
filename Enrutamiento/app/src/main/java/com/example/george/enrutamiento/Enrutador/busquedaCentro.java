package com.example.george.enrutamiento.Enrutador;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.george.enrutamiento.R;
import com.example.george.enrutamiento.adapter.DraggableDataProvider;
import com.example.george.enrutamiento.adapter.ListsDraggableAdapter;
import com.example.george.enrutamiento.dialogs.DialogInfoCentro;
import com.example.george.enrutamiento.dialogs.DialogNuevaRuta;
import com.google.gson.Gson;
import com.h6ah4i.android.widget.advrecyclerview.animator.DraggableItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class busquedaCentro extends AppCompatActivity {
    static EstablecimientoAdapter_ruta adaptador_ruta;
    public static Activity self;

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_centro);
        self = this;
        Filtros.context = this;
        ((TextView) (findViewById(R.id.toolbar_title))).setText("Búsqueda de centro educativo");

        Filtros.restoreList(this);
        setMenu();
        setDraggableList();
        setSearchBox();



        mantenerRuta();


        turnOnGps();
    }

    protected void mantenerRuta() {
        if(Filtros.ruta.size()>0) {
            Intent intent = new Intent(this, DialogNuevaRuta.class);
            intent.putExtra("mensaje", getString(R.string.nueva_ruta_mensaje)); //Optional parameters
            startActivityForResult(intent, DialogNuevaRuta.REQUEST_DIALOG);
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        setDraggableList();
        //BAJAR LISTA A MEMORIA PERSISTENTE
        Filtros.storeList(this);

    }

    private void setSearchBox() {
        //EditText
        final EditText searchBox = findViewById(R.id.textSearch);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = searchBox.getText().toString();
                int textlength = searchBox.getText().length();

                if (text.endsWith(" ")/*si agregan espacios*/ || count == 0/*es backspace*/)
                    return;

                if (textlength == 2 || textlength == 5 || textlength == 10) {
                    searchBox.setText(text + "-"/*new StringBuilder(text).insert(text.length(), "-").toString()*/);
                    searchBox.setSelection(searchBox.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchBox.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    addCentro(searchBox.getText().toString());
                    searchBox.setText("");
                    return true;
                }
                return false;
            }
        });

        //Boton Ver ruta
        Button btnVerRuta = (Button) findViewById(R.id.btn_ver_ruta);
        btnVerRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Enrutador.desplegar_mapa(self);
            }
        });

        //Boton busqueda avanzada
        Button busquedaAvanzada = (Button) findViewById(R.id.busqueda_avanzada);
        busquedaAvanzada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Busqueda.set_estado(false);//vista normal
                Intent intent = new Intent(self, Busqueda.class);
                self.startActivity(intent);
            }
        });


    }


    private void addCentro(String codigo) {
        Intent intent = new Intent(this, DialogInfoCentro.class);
        intent.putExtra("codigo", "" + codigo);
        startActivityForResult(intent, DialogInfoCentro.REQUEST_DIALOG);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DialogInfoCentro.REQUEST_DIALOG) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getIntExtra("refrescar", 0) == 1) {
                    setDraggableList();
                    //BAJAR LISTA A MEMORIA PERSISTENTE
                    Filtros.storeList(this);

                }
            }
        } else if (requestCode == DialogNuevaRuta.REQUEST_DIALOG) {
            String buttonPressed = data.getStringExtra("buttonPressed");
            if (buttonPressed.equals("mantener")) return;
            Filtros.ruta = new ArrayList<Establecimiento>();
            Filtros.storeList(this);
            setDraggableList();
        }
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;

    public void setDraggableList() {
//noinspection ConstantConditions
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // drag & drop manager
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        mRecyclerViewDragDropManager.setDraggingItemShadowDrawable(
                (NinePatchDrawable) ContextCompat.getDrawable(this, R.drawable.material_shadow_z3));

        //adapter


        final ListsDraggableAdapter myItemAdapter = new ListsDraggableAdapter(new DraggableDataProvider(this));


        mAdapter = myItemAdapter;

        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(myItemAdapter);      // wrap for dragging

        final GeneralItemAnimator animator = new DraggableItemAnimator();

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerView.setItemAnimator(animator);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        /*on item touch*/

        mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(this, R.drawable.list_divider_h), true));


        mRecyclerViewDragDropManager.attachRecyclerView(mRecyclerView);
    }


    protected void setMenu() {
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public static void refresh() {
        adaptador_ruta.notifyDataSetChanged();//realiza el cambio en la lista de establecimientos
        if (Filtros.ruta.size() == 0)//finaliza la actividad cuando no hay elementos seleccionados
        {
            //Busqueda._show();
            self.finish();
        }
        createList();

    }

    private static void createList() {
        List<Establecimiento> result = Filtros.ruta;
        if (result == null) return;
        adaptador_ruta.setEstablecimientosList(result);

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rutas_, menu);
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
        int id = item.getItemId();
        switch (id) {
            case R.id.ver_ruta:
                Enrutador.desplegar_mapa(this);
                //startService(new Intent(this, BubbleHeadService.class));
                break;
            case R.id.borrar_ruta:
                //Filtros.clear();
                Filtros.ruta.clear();
                Toast.makeText(self, "se ha borrado la ruta", Toast.LENGTH_LONG);
                setDraggableList();
                //BAJAR LISTA A MEMORIA PERSISTENTE
                Filtros.storeList(this);
                //finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
