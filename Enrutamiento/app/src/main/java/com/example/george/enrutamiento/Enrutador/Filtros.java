package com.example.george.enrutamiento.Enrutador;

import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.george.enrutamiento.MODELO.Modalidad;
import com.example.george.enrutamiento.MODELO.Plan;
import com.example.george.enrutamiento.MODELO.Sector;
import com.example.george.enrutamiento.R;
import com.example.george.enrutamiento.MODELO.Departamento;
import com.example.george.enrutamiento.MODELO.Municipio;
import com.example.george.enrutamiento.MODELO.Nivel_Escolar;
import com.example.george.enrutamiento.dialogs.DialogInfoAcompanante;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Filtros {
    static Manejador_BD BD;
    //lista de objectos existentes para cada busqueda
    static List<Departamento> Departamentos = new ArrayList<>();
    static List<Municipio> Municipos = new ArrayList<>();
    static List<Nivel_Escolar> NivelesEscolares =  new ArrayList<>();
    static List<Sector> Sectores =  new ArrayList<>();
    static List<Plan> Planes = null;
    static List<Modalidad> Modalidades = null;
    static List<Establecimiento> Establecimientos = new ArrayList<>();

    //listas de nombre que se despliegan en los spinner
    static List<String> listaDepartamento = new ArrayList<>();
    static List<String> listaMunicipio = new ArrayList<>();
    static List<String> listaNivelEscolar = new ArrayList<>();
    static List<String> listaSector = new ArrayList<>();
    static List<String> listaPlan = new ArrayList<>();
    static List<String> listaModalidad = new ArrayList<>();


    public static Context context;
    public static List<Establecimiento> ruta = new ArrayList<>();//lista de establecimientos

    //valores seleccionados en los spinners
    private static int departamento = 0;
    private static int municipio = 0;
    private static int nivelescolar = 0;
    private static int sector = 0;
    private static int plan = 0;
    private static int modalidad = 0;


    public static void init_() {
        //llenado de los spinner
        Departamento();//solo carga departamento, para crecimientos de filtros incrementales
    }


    public static void actualizar(int grupo, int item) {
        switch (grupo) {
            case 0://departamento
                //refresca los criterios
                departamento = 0;
                municipio = 0;
                nivelescolar = 0;
                sector = 0;

                departamento = Departamentos.get(item).getId_departamento();//codigo del departamento
                Municipio(departamento);
                break;
            case 1://municipio
                //refresca criterios
                municipio = 0;
                nivelescolar = 0;
                sector = 0;

                municipio = Municipos.get(item).getId_municipio();//codigo del municipio
                //filtrar por nivel de los municipios seleccionados
                Nivel_Escolar(departamento,municipio);
                break;
            case 2://nivel escolar
                nivelescolar = 0;
                sector = 0;

                nivelescolar = NivelesEscolares.get(item).getId_nivel();//codigo del sector
                //filtrar por sector los niveles seleccionados
                Sector(departamento,municipio,nivelescolar);
                break;
            case 3://sector
                sector = 0;

                sector = Sectores.get(item).getId_Sector();//codigo del sector
                break;
        }
    }

    public static List<String> getDepartamentos() {
        return listaDepartamento;
    }

    public static List<String> getMunicipios() {
        return listaMunicipio;
    }

    public static List<String> getNivelEscolar() {
        return listaNivelEscolar;
    }

    public static List<String> getSector() {
        return listaSector;
    }

    //public static List<String> getPlan() {
    //    return listaPlan;
    //}

    //public static List<String> getModalidad() {
    //    return listaModalidad;
    //}


    public static void Departamento() {
        //limpiar listas de spinner y de obj
        listaDepartamento.clear();
        odkapi_getDepartamentos();
    }

    public static void Municipio(int departamento) {
        listaMunicipio.clear();
        if (Municipos != null)
            Municipos.clear();

        odkapi_getMunicipios(departamento);
    }

    public static void Nivel_Escolar(int departamento, int municipio) {   //limpiar listas de spinner y de obj
        listaNivelEscolar.clear();
        if (NivelesEscolares != null)
            NivelesEscolares.clear();
        odkapi_getNiveles(departamento,municipio);
    }

    public static void Sector(int departamento, int municipio, int nivel) {   //limpiar listas de spinner y de obj
        listaSector.clear();
        if (Sectores!= null)
            Sectores.clear();
        odkapi_getSectores(departamento,municipio,nivel);
    }



    public static void Establecimiento() {

        if (departamento == 0 || municipio == 0 || nivelescolar == 0) {//solicitar otro criterio por la cantidad de establecimiento que apareceran
            //solicitar al menos estos tres criterios
            return;
        } else {
            odkapi_getCentros();
        }

    }

    private static void odkapi_getDepartamentos() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://odk.exitoescolar.org:5000/getDepartamentos";
        JSONObject jsonBody = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String JSONString = response.toString();
                        try {
                            JSONObject deptos = new JSONObject(JSONString);
                            if (!deptos.isNull("departamentos")) {
                                //Llenar departamentos
                                JSONArray deptosArray = deptos.getJSONArray("departamentos");
                                int len = deptosArray.length();
                                for (int i = 0; i < len; i++) {
                                    JSONObject JSONDepto = (JSONObject) deptosArray.get(i);
                                    listaDepartamento.add(JSONDepto.getString("departamento"));
                                    int id = Integer.parseInt(JSONDepto.getString("id"));
                                    String departamento = JSONDepto.getString("departamento");
                                    Departamentos.add(new Departamento(id, departamento));
                                }
                            }
                        } catch (Throwable tx) {
                            Log.e("DEBUG", "Error en JSON del response");
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

    private static void odkapi_getMunicipios(int departamento) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://odk.exitoescolar.org:5000/getMunicipios";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("departamento", departamento);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String JSONString = response.toString();
                        try {
                            JSONObject munis = new JSONObject(JSONString);
                            if (!munis.isNull("municipios")) {
                                //Llenar departamentos
                                JSONArray munisArray = munis.getJSONArray("municipios");
                                int len = munisArray.length();
                                for (int i = 0; i < len; i++) {
                                    JSONObject JSONMuni = (JSONObject) munisArray.get(i);
                                    listaMunicipio.add(JSONMuni.getString("municipio"));
                                    int id_depto = Integer.parseInt(JSONMuni.getString("id_depto"));
                                    int id_muni = Integer.parseInt(JSONMuni.getString("id_municipio"));
                                    String municipio = JSONMuni.getString("municipio");
                                    Municipos.add(new Municipio(id_muni, id_depto, municipio));
                                }
                            }
                        } catch (Throwable tx) {
                            tx.printStackTrace();
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

    private static void odkapi_getNiveles(int departamento,int municipio) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://odk.exitoescolar.org:5000/getNiveles";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("departamento", departamento);
            jsonBody.put("municipio",municipio);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String JSONString = response.toString();
                        try {
                            JSONObject munis = new JSONObject(JSONString);
                            if (!munis.isNull("niveles")) {
                                //Llenar departamentos
                                JSONArray munisArray = munis.getJSONArray("niveles");
                                int len = munisArray.length();
                                for (int i = 0; i < len; i++) {
                                    JSONObject JSONMuni = (JSONObject) munisArray.get(i);
                                    listaNivelEscolar.add(JSONMuni.getString("nivel"));
                                    int id_nivel= Integer.parseInt(JSONMuni.getString("id_nivel"));
                                    String nivel= JSONMuni.getString("nivel");
                                    NivelesEscolares.add(new Nivel_Escolar(id_nivel,nivel));
                                }
                            }
                        } catch (Throwable tx) {
                            tx.printStackTrace();
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

    private static void odkapi_getSectores(int departamento,int municipio, int nivel) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://odk.exitoescolar.org:5000/getSectores";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("departamento", departamento);
            jsonBody.put("municipio",municipio);
            jsonBody.put("nivel",nivel);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String JSONString = response.toString();
                        try {
                            JSONObject munis = new JSONObject(JSONString);
                            if (!munis.isNull("sectores")) {
                                //Llenar departamentos
                                JSONArray munisArray = munis.getJSONArray("sectores");
                                int len = munisArray.length();
                                for (int i = 0; i < len; i++) {
                                    JSONObject JSONMuni = (JSONObject) munisArray.get(i);
                                    listaSector.add(JSONMuni.getString("sector"));
                                    int id_sector= Integer.parseInt(JSONMuni.getString("id_sector"));
                                    String sector= JSONMuni.getString("sector");
                                    Sectores.add(new Sector(id_sector,sector));
                                }
                            }
                            Establecimiento();
                        } catch (Throwable tx) {
                            tx.printStackTrace();
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


    private static void odkapi_getCentros() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://odk.exitoescolar.org:5000/getCentros";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("departamento", departamento);
            jsonBody.put("municipio", municipio);
            jsonBody.put("nivel", nivelescolar);

            if (sector <= 0)
                jsonBody.put("sector", -1);
            else
                jsonBody.put("sector", sector);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        String JSONString = response.toString();
                        try {
                            JSONObject centros = new JSONObject(JSONString);
                            if (!centros.isNull("centros")) {
                                //Llenar departamentos
                                JSONArray centrosArray = centros.getJSONArray("centros");
                                int len = centrosArray.length();
                                Busqueda.ca.clearEstablecimientosList();
                                Establecimientos.clear();
                                for (int i = 0; i < len; i++) {
                                    JSONObject JSONCentro = (JSONObject) centrosArray.get(i);
                                    String codigo = JSONCentro.getString("codigo");
                                    String nombre = JSONCentro.getString("nombre");
                                    String dir = JSONCentro.getString("direccion");
                                    Double latitud = 0.0;
                                    if (!JSONCentro.isNull("latitud"))
                                        latitud = JSONCentro.getDouble("latitud");
                                    Double longitud = 0.0;
                                    if (!JSONCentro.isNull("longitud"))
                                        longitud = JSONCentro.getDouble("longitud");
                                    Establecimientos.add(new Establecimiento(codigo, nombre, dir, latitud, longitud));
                                }
                                Busqueda.ca.setEstablecimientosList(Establecimientos);
                                Busqueda.ca.notifyDataSetChanged();
                            }
                        } catch (Throwable tx) {
                            tx.printStackTrace();
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


    public static void setContext(Context context) {
        Filtros.context = context;
    }

    public static void setRuta(Establecimiento e) {
        Filtros.ruta.add(e);
    }

    public static void subir_ruta(int i) {
        if (i > 0) {
            Establecimiento e = ruta.get(i);
            ruta.remove(i);
            ruta.add(i - 1, e);
        }
    }

    public static void mover_ruta(int from, int to) {
        Establecimiento e = ruta.get(from);
        ruta.remove(from);
        ruta.add(to, e);
        Filtros.storeList(context);
    }

    public static void bajar_ruta(int i) {
        if (i < ruta.size() - 1) {
            subir_ruta(i + 1);
        }
    }

    public static void eliminar(int i) {
        ruta.remove(i);
    }

    public static void setBD(Manejador_BD a) {
        BD = a;
    }

    public static void storeList(Context context) {

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Filtros.ruta);
        //Log.d("JSON", json);
        prefsEditor.putString("ruta", json);
        prefsEditor.commit();
    }

    public static void restoreList(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("ruta", "");
        Type listType = new TypeToken<ArrayList<Establecimiento>>() {
        }.getType();
        List<Establecimiento> establecimientos = new Gson().fromJson(json, listType);
        if(establecimientos==null) establecimientos = new ArrayList<Establecimiento>();
        Filtros.ruta = establecimientos;
    }
}
