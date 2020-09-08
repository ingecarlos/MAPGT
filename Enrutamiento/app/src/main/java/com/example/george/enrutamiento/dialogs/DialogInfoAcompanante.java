package com.example.george.enrutamiento.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.george.enrutamiento.R;

import org.json.JSONException;
import org.json.JSONObject;


public class DialogInfoAcompanante extends AppCompatActivity {
    public static final int REQUEST_DIALOG = 1;
    public static final int TIPO_SUCCESS = 1001;
    public static final int TIPO_FAILED = 1002;

    private String acomDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_info_acompanante);
        this.setFinishOnTouchOutside(true);

        Intent from = getIntent();
        int tipo = from.getIntExtra("tipo", -1);
        acomDatos = from.getStringExtra("acompanante");

        if (tipo == TIPO_SUCCESS) {
            setContents();
        } else if (tipo == TIPO_FAILED) {
            setFailedContents();
        }
    }

    protected void setFailedContents() {
        ((TextView) findViewById(R.id.nombre_acompanante)).setText("Atención");
        ((TextView) findViewById(R.id.tipo_acompanante)).setText("Los datos proporcionados en el inicio de sesión son erroneos.\nPor favor, verificar información.");

        findViewById(R.id.departamento_acompanante).setVisibility(View.GONE);
        findViewById(R.id.especialidad_acompanante).setVisibility(View.GONE);
        findViewById(R.id.sede_acompanante).setVisibility(View.GONE);
        findViewById(R.id.dialog_warning_ok).setVisibility(View.INVISIBLE);
        findViewById(R.id.dialog_warning_cancelar).setVisibility(View.INVISIBLE);

    }

    protected void setContents() {
        setDatosAcompanante();

        final TextView subscribe = (TextView) findViewById(R.id.dialog_warning_ok);

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("identidad", 1);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });


        final TextView cancelar = (TextView) findViewById(R.id.dialog_warning_cancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    protected void setDatosAcompanante() {
        try {
            JSONObject acompanante = new JSONObject(acomDatos);
            ((TextView) findViewById(R.id.nombre_acompanante)).setText(acompanante.getString("nombre"));
            ((TextView) findViewById(R.id.tipo_acompanante)).setText(acompanante.getString("tipo"));
            ((TextView ) findViewById(R.id.departamento_acompanante)).setText("Departamento: " + acompanante.getString("departamento"));
            ((TextView ) findViewById(R.id.sede_acompanante)).setText("Sede: " + acompanante.getString("sede"));
            ((TextView ) findViewById(R.id.especialidad_acompanante)).setText("Especialidad: " + acompanante.getString("especialidad"));
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
    }

}
