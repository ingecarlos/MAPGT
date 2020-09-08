package com.example.george.enrutamiento.sincronizacion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;



import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class sincronizacion {
    static RequestQueue queue;
    static String url;
    static String respuesta;
    static Activity contexto;

    public static void sincronizar(Activity activity){
        contexto = activity;
        setUpSincronizacion();
        send_request();
    }


    public static void setUpSincronizacion(){
        queue =  Volley.newRequestQueue(contexto);
        url="http://18.211.67.102:8080/ServerMapGT/Servicios";
    }

    public static void send_request()  {
        Map<String, String> params = new HashMap<String, String>();
        params.put("date_time", "2019-05-20");

        //JsonRequest
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,url,new JSONObject(params),new ResponseListener(),new ErrorListener());
        Log.d("DEBUG/REQUEST", request.getBodyContentType() );
        Log.d("DEBUG/REQUEST",new String(request.getBody()));
        Log.d("DEBUG/REQUEST", request.toString());
        queue.add(request);
    }
}

//response listener
class ResponseListener implements Response.Listener{

    @Override
    public void onResponse(Object response) {
        Log.d("DEBUG/RESPONSE", response.toString());
    }
}

//error listener
class ErrorListener implements Response.ErrorListener{

    @Override
    public void onErrorResponse(VolleyError error) {

    }
}