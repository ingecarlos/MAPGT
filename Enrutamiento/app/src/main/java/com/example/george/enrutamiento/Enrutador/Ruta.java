package com.example.george.enrutamiento.Enrutador;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.george.enrutamiento.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.example.george.enrutamiento.adapter.DraggableDataProvider;
import com.example.george.enrutamiento.adapter.ListsDraggableAdapter;
import com.h6ah4i.android.widget.advrecyclerview.animator.DraggableItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;

public class Ruta extends AppCompatActivity {
    static EstablecimientoAdapter_ruta adaptador_ruta;
    public static Activity self;

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta);
        self = this;
        ((TextView) (findViewById(R.id.toolbar_title))).setText("Ruta");


        setMenu();
        setDraggableList();

    }


    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;

    protected void setDraggableList() {
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
                Toast.makeText(getApplicationContext(), "se ha borrado la ruta", Toast.LENGTH_LONG);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
