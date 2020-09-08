package com.example.george.enrutamiento.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.george.enrutamiento.Enrutador.Busqueda;
import com.example.george.enrutamiento.Enrutador.Establecimiento;
import com.example.george.enrutamiento.Enrutador.Filtros;
import com.example.george.enrutamiento.Enrutador.Manejador_BD;
import com.example.george.enrutamiento.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


public class DialogInfoCentro extends AppCompatActivity {
    public static final int REQUEST_DIALOG = 1;


    public Manejador_BD BD;//manejador de la base de datos

    protected int from;

    String codigo;
    String municipio;
    String nombre;
    String direccion;
    Double latitud = 0.0;
    Double longitud = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_info_centro);
        this.setFinishOnTouchOutside(true);

        setContents();
    }

    protected void setContents() {
        setDatosCentro();
        final TextView subscribe = (TextView) findViewById(R.id.dialog_warning_ok);

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                añadirCentro();
                Toast.makeText(getApplicationContext(), "Centro añadido a la ruta", Toast.LENGTH_LONG).show();

                Intent returnIntent = new Intent();
                returnIntent.putExtra("refrescar", 1);
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

    protected void setDatosCentro() {
        Intent intent = getIntent();
        codigo = intent.getStringExtra("codigo");


        if (codigo != null) {
            TextView dialogtext = (TextView) findViewById(R.id.codigo_centro);
            dialogtext.setText(codigo);
            odkapiGetCentro();
        }


    }

    private void odkapiGetCentro() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://odk.exitoescolar.org:5000/getCentro";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("codigo", codigo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String JSONString = response.toString();
                        try {
                            JSONObject centro = new JSONObject(JSONString);
                            Log.d("DEBUG", "onResponse: " + JSONString);
                            if (centro.isNull("codigo")) {
                                //MOSTRAR ERROR BUSQUEDA DE CENTRO
                                ((TextView) findViewById(R.id.centro_datos)).setText("Codigo de centro no encontrado");
                                ((TextView) findViewById(R.id.nivel)).setText("Intente realizando una búsqueda avanzada");
                                ((TextView) findViewById(R.id.plan)).setText("");

                                ((TextView) findViewById(R.id.dialog_warning_ok)).setVisibility(View.INVISIBLE);
                                ((TextView) findViewById(R.id.dialog_warning_cancelar)).setVisibility(View.INVISIBLE);

                            } else {
                                //AÑADIR CENTRO
                                municipio = centro.getString("municipio");
                                nombre = centro.getString("nombre");
                                direccion = centro.getString("direccion");
                                //nivel = centro.getString("nivel");
                                //plan = centro.getString("plan");
                                //if (!Busqueda.estado) {
                                if (!centro.isNull("latitud")) {
                                    latitud = centro.getDouble("latitud");
                                }if (!centro.isNull("longitud")) {
                                    longitud = centro.getDouble("longitud");
                                }
                                //}


                                ((TextView) findViewById(R.id.centro_datos)).setText(nombre + ", " + municipio + ", " + direccion);
                                //((TextView) findViewById(R.id.nivel)).setText("Nivel: " + nivel);
                                //((TextView) findViewById(R.id.plan)).setText("Plan: " + plan);

                            }
                        } catch (Throwable tx) {
                            Log.e("LOGIN", "Error en JSON del response");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO error en response

                    }
                });
        queue.add(request);
    }

    protected void añadirCentro() {
        Filtros.setRuta
                (
                        new Establecimiento(
                                codigo,
                                nombre,
                                direccion,
                                latitud, longitud)
                );
    }
}
