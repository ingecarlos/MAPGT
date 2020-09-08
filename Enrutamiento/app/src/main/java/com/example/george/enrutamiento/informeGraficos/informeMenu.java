package com.example.george.enrutamiento.informeGraficos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.george.enrutamiento.ODKData.ODKData;
import com.example.george.enrutamiento.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class informeMenu extends AppCompatActivity {

    private static ListView formulariosList;
    private static List<Map<String, Object>> formulariosTodos;
    private static List<Map<String, Object>> formulariosFiltro=new ArrayList<>();
    private static SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe_menu);

        //Titulo
        ((TextView) (findViewById(R.id.toolbar_title))).setText("Informe de tiempo de clase");

        //WRITE EXTERNAL STORAGE
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Conceda los permisos para esta funcionalidad", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
            finish();
            return;
        }
        //formulariosList.setDividerHeight(0);
        formulariosList = findViewById(R.id.formulariosList);
        addFormularios();
    }

    private void addFormularios() {
        formulariosTodos = new ArrayList<Map<String, Object>>();

        List<Map<String, Object>> formularios = ODKData.getFormulariosRecientes();
        Collections.reverse(formularios);
        for (Map<String, Object> form : formularios) {
            formulariosTodos.add(form);
        }

        String[] from = {"centro", "fecha", "grado", "asignatura","codigo","datos"};
        int[] to = {R.id.formulariosCentro, R.id.formulariosListaFecha, R.id.formulariosListaGrado, R.id.formulariosListaAsignatura};

        resetForms();

        simpleAdapter = new SimpleAdapter(this, formulariosFiltro, R.layout.formularios_lista_item,
                from, to);
        formulariosList.setAdapter(simpleAdapter);

        formulariosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                Map<String, Object> clickItemObj = (HashMap<String, Object>) adapterView.getAdapter().getItem(index);

                Intent intent = new Intent(getApplicationContext(), informeGraficos.class);
                intent.putExtra("formulario", (File) clickItemObj.get("archivo"));
                intent.putExtra("codigo",(String) clickItemObj.get("codigo"));
                intent.putExtra("fecha",(String) clickItemObj.get("fecha"));
                intent.putExtra("grado",(String) clickItemObj.get("grado"));
                intent.putExtra("seccion",(String) clickItemObj.get("seccion"));
                intent.putExtra("asignatura",(String) clickItemObj.get("asignatura"));
                intent.putExtra("centro",(String) clickItemObj.get("centro"));

                startActivity(intent);

                //Toast.makeText(getApplicationContext(), "You clicked " + clickItemObj.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void resetForms() {
        int cantFormularios = formulariosTodos.size();
        List<Map<String,Object>> nueva = new ArrayList<>();
        for (Map<String,Object> form : formulariosTodos) nueva.add(form);

        formulariosFiltro.clear();

        if (cantFormularios > 5) {
            List<Map<String,Object>> sub = nueva.subList(0, 5);
            for(Map<String,Object> item: sub) formulariosFiltro.add(item);
        } else {
            List<Map<String,Object>> sub = nueva.subList(0, cantFormularios);
            for(Map<String,Object> item: sub) formulariosFiltro.add(item);
        }
    }

    public void buscar(View v) {
        String text =((TextView) findViewById(R.id.buscarTxt)).getText().toString();
        text = text.toLowerCase();
        if (text.equals("")) {
            resetForms();
            simpleAdapter.notifyDataSetChanged();
            return;
        }
        formulariosFiltro.clear();

        List<Map<String,Object>> nueva = new ArrayList<>();
        for (Map<String,Object> form : formulariosTodos) nueva.add(form);

        for (Map<String, Object> form : nueva) {
            String formText=form.toString().toLowerCase();
            if (formText.contains(text)) {
                formulariosFiltro.add(form);
            }
        }


        simpleAdapter.notifyDataSetChanged();
    }

}
