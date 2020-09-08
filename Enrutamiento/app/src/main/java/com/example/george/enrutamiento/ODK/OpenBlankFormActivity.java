package com.example.george.enrutamiento.ODK;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.george.enrutamiento.Enrutador.Establecimiento;
import com.example.george.enrutamiento.Enrutador.EstablecimientoAdapter_seleccionar;
import com.example.george.enrutamiento.Enrutador.Filtros;
import com.example.george.enrutamiento.R;
import com.example.george.enrutamiento.dialogs.DialogInfoAcompanante;
import com.example.george.enrutamiento.utilities.cache;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.george.enrutamiento.utilities.cache.deleteDir;


public class OpenBlankFormActivity extends ODK {
    public static final String FORMS_URI = "content://org.odk.collect.android.provider.odk.forms/forms";
    String nombre;
    int ODKCALL = 12345;

    public static List<Establecimiento> Establecimientos = new ArrayList<>();
    public static EstablecimientoAdapter_seleccionar ca;
    RecyclerView recList;
    public static Context me;
    public static codigoVariable codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        me = this;
        super.onCreate(null);
        ejecutar();
        /*setContentView(R.layout.activity_ejecutar_odk);

        Filtros.context = this;
        ((TextView) (findViewById(R.id.toolbar_title))).setText("Llenar instrumento");

        Filtros.restoreList(this);
        setMenu();
        setCentros();

        codigo = new codigoVariable();
        codigo.setListener(new codigoVariable.ChangeListener() {
            @Override
            public void onChange() {

                setUsuarioEstablecimientoMetadata(codigo.codigo());
                ejecutar();

            }
        });*/

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ODKCALL && data != null) {
            //Toast.makeText(this,getApplicationContext().getCacheDir().toString(),Toast.LENGTH_LONG).show();
            //ejecutar();
        }
    }

    private void ejecutar() {
        nombre = getIntent().getExtras().getString("nombre");
        openBlankForm();
        finish();
    }


    private void setCentros() {

        recList = findViewById(R.id.Lista);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        registerForContextMenu(recList);
        ca = new EstablecimientoAdapter_seleccionar(new ArrayList<Establecimiento>());
        recList.setAdapter(ca);
        ca.setEstablecimientosList(Filtros.ruta);
        ca.notifyDataSetChanged();
    }

    public void openBlankForm() {
        Map<String, Integer> forms = getFormList(getFormsCursor());
        String expectedJrFormId = nombre;

        if (forms.containsKey(expectedJrFormId)) {
            int id = forms.get(expectedJrFormId);
            Intent i = new Intent(Intent.ACTION_EDIT, Uri.parse(FORMS_URI + "/" + id));
            startActivityIfAvailable(i);
        } else {
            Toast.makeText(this, "No existe formulario. Por favor obtenga el formulario en ODk", Toast.LENGTH_LONG)
                    .show();
        }
    }


    private Cursor getFormsCursor() {
        return getContentResolver().query(Uri.parse(FORMS_URI), null, null, null, null);
    }

    private Map<String, Integer> getFormList(Cursor cursor) {
        Map<String, Integer> forms = new HashMap<>();

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
                    String jrFormId = cursor.getString(cursor.getColumnIndex("jrFormId"));
                    forms.put(jrFormId, id);
                    Log.d("listadoodk", "" + jrFormId);
                }
            } finally {
                cursor.close();
            }
        }
        return forms;
    }


    protected void setMenu() {
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private Context getContextoODKCollect() {
        try {
            return createPackageContext("org.odk.collect.android", CONTEXT_IGNORE_SECURITY);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setUsuarioEstablecimientoMetadata(String codigo) {
        String usuario = getUsuario();
        Context mContext = null;

        mContext = getContextoODKCollect();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", usuario + "|" + codigo);
        editor.putString("metadata_username", usuario + "|" + codigo);

        editor.apply();

    }

    private String getUsuario() {
        Context mContext = null;
        try {
            mContext = OpenBlankFormActivity.me;
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            String JSONString = sp.getString("acompananteJSON", "");
            JSONObject acomp = new JSONObject(JSONString);
            return acomp.getString("codigo");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error usuario";
    }


}

