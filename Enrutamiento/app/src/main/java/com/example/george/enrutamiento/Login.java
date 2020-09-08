package com.example.george.enrutamiento;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.george.enrutamiento.dialogs.DialogInfoAcompanante;
import com.github.mikephil.charting.utils.EntryXComparator;

import org.json.JSONException;
import org.json.JSONObject;


public class Login extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setMetadataServer();
        if (isLogged()) proceedAndFinish();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView ingresarButton = (TextView) (findViewById(R.id.ingresar_button));
        ingresarButton.setOnClickListener(ingresarPressed);

        context = this;
    }

    private boolean validateInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Si hay conexión a Internet en este momento
        } else {
            Toast.makeText(this, "Necesita de acceso a internet para validar sus credenciales", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    protected String JSONString;
    protected String codigoAcomp;

    private void validateLogin(int codigo, String password) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://odk.exitoescolar.org:5000/login";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("codigo", codigo);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONString = response.toString();
                        try {
                            JSONObject acomp = new JSONObject(JSONString);
                            if (acomp.isNull("codigo")) {
                                //MOSTRAR ERROR EN LOGIN
                                Intent intent = new Intent(getApplicationContext(), DialogInfoAcompanante.class);
                                intent.putExtra("tipo", DialogInfoAcompanante.TIPO_FAILED);
                                startActivityForResult(intent, DialogInfoAcompanante.REQUEST_DIALOG);
                            } else {
                                //CONFIRMAR IDENTIDAD
                                codigoAcomp = acomp.getString("codigo");
                                Intent intent = new Intent(getApplicationContext(), DialogInfoAcompanante.class);
                                intent.putExtra("tipo", DialogInfoAcompanante.TIPO_SUCCESS);
                                intent.putExtra("acompanante", JSONString);
                                startActivityForResult(intent, DialogInfoAcompanante.REQUEST_DIALOG);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DialogInfoAcompanante.REQUEST_DIALOG && data!=null) {
            if (data.getIntExtra("identidad", -1) == 1) {
                loginSuccess();
            }
        }
    }

    protected void loginSuccess() {
        changeMetadataUsername(codigoAcomp);
        setMetadataAcompanante(JSONString);
        proceedAndFinish();
    }

    private View.OnClickListener ingresarPressed = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //CONFIRMAR IDENTIDAD
            try {
                if (!validateInternetConnection()) return;
                //acá se deben hacer las validaciones para dar acceso a la pantalla principal de FHI360-MAPGT
                int codigo = Integer.parseInt(((TextView) findViewById(R.id.edit_codigo)).getText().toString());
                String pass = ((TextView) findViewById(R.id.edit_password)).getText().toString();
                validateLogin(codigo, pass);
                //en este momento dentro de validateLogin se hace un request a odkapi
                //se responde un objeto JSON con informacion del acompañante, si las credenciales son correctas
                //y se responde un objeto JSON vació si las credenciales fuesen incorrectas
                //esto se maneja dentro del metodo validateLogin
            }catch(Exception e){
                Toast.makeText(context,"Error en los campos ingresados",Toast.LENGTH_LONG).show();
            }
        }
    };

    private void proceedAndFinish() {
        Intent i = new Intent(getApplicationContext(), Menu_Principal.class);
        startActivity(i);
        finish();
    }

    private Boolean isLogged() {
        Context mContext = null;
        try {
            mContext = createPackageContext("org.odk.collect.android", CONTEXT_IGNORE_SECURITY);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            String strName = sp.getString("username", "");
            Log.d("DEBUG", "isLogged: " + strName);
            if (!strName.equals("")) return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setMetadataServer() {
        Context mContext = null;
        try {
            mContext = createPackageContext("org.odk.collect.android", CONTEXT_IGNORE_SECURITY);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            //Map preferencias =  sp.getAll();
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("server_url", getString(R.string.odkAgreggateServer));
            editor.apply();

            validateMeta();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setMetadataAcompanante(String JSONString) {
        Context mContext = null;
        mContext = getApplicationContext();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("acompananteJSON", JSONString);
        editor.apply();
    }

    public void changeMetadataUsername(String username) {
        Context mContext = null;
        try {

            mContext = createPackageContext("org.odk.collect.android", CONTEXT_IGNORE_SECURITY);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("username", username);
            editor.putString("metadata_username",username);
            editor.apply();

            validateMeta();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void validateMeta() {
        Context mContext = null;
        try {
            mContext = createPackageContext("org.odk.collect.android", CONTEXT_IGNORE_SECURITY);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            String strName = sp.getString("username", "");
            Log.d("DEBUG", "validateMeta-Login: " + strName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


}