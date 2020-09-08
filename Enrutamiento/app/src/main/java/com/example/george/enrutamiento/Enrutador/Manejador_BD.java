package com.example.george.enrutamiento.Enrutador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.example.george.enrutamiento.MODELO.Departamento;
import com.example.george.enrutamiento.MODELO.Modalidad;
import com.example.george.enrutamiento.MODELO.Municipio;
import com.example.george.enrutamiento.MODELO.Nivel_Escolar;
import com.example.george.enrutamiento.MODELO.Plan;
import com.example.george.enrutamiento.MODELO.Sector;
import com.example.george.enrutamiento.MODELO.Tipo_Incidente;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Manejador_BD extends SQLiteOpenHelper
{
    private static final String DB_NAME = "DB_AP_MOVIL.db";
    private static  String DB_PATH="";
    private static SQLiteDatabase mDataBase;
    private static final int DB_VERSION = 3;
    private Context mContext=null;

    public Manejador_BD(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);

        if(Build.VERSION.SDK_INT>16)
            DB_PATH=context.getApplicationInfo().dataDir+"/databases/";
        else
            DB_PATH="/data/data/"+context.getPackageName()+"/databases/";
 
        mContext=context;
    }

    @Override
    public synchronized  void close()
    {
        if(mDataBase!=null)
            mDataBase.close();
        super.close();
    }


    public void openDataBase() throws SQLException {

        // Open the database
        String myPath = DB_PATH + DB_NAME;
        mDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

   public void CrearDB()  {
       boolean dbExist= checkDB();

       if(dbExist)
       {
           //la base de datos ya existe
       }
       else
           {
               this.getReadableDatabase();
               try
               {
                   copyDB();
               }
               catch(Exception e)
               {
                throw new Error("Error copiando DB");
               }
           }
   }

public boolean checkDB()
{
    SQLiteDatabase tempdb=null;
    try
    {
        String path =DB_PATH+DB_NAME;
        tempdb= SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
    }
    catch(Exception e){}

    if(tempdb!=null)
        tempdb.close();
    return tempdb!=null?true:false;
}
   private void copyDB  ()
   {
        try
        {
            InputStream myInput = mContext.getAssets().open(DB_NAME);
            String outFileName=DB_PATH+DB_NAME;
            OutputStream myOutput=new FileOutputStream(outFileName);
            byte[] buffer =new byte[1024];
            int length;
            while((length = myInput.read(buffer))>0)
            {
                myOutput.write(buffer,0,length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
        catch (Exception e) { }

    }

    public Cursor select(String query) throws SQLException
    {
        return mDataBase.rawQuery(query,null);
    }


    public void exec(String codigo, String a ,String b)
    {

        ContentValues cv = new ContentValues();
        cv.put("latitud",a); //These Fields should be your String values of actual column names
        cv.put("longitud",b);
        mDataBase.update("Establecimiento", cv, "codigo='"+codigo+"'", null);
    }

    public ArrayList<Departamento> Load_Departamento  (String query)
    {   //CREAR UNA LISTA CON LOS DEPARTAMENTO
        ArrayList arreglo = new ArrayList<Departamento>();
        Cursor cursor= select (query);
        cursor.moveToFirst();

        do {
            arreglo.add(new Departamento(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_departamento"))),cursor.getString(cursor.getColumnIndex("nombre"))));
        }
        while(cursor.moveToNext());
        cursor.close();

        return arreglo;
    }



    public ArrayList<Municipio> Load_Municipio(String query)
    {
        ArrayList arreglo = new ArrayList<Municipio>();
        Cursor cursor=select(query);
        cursor.moveToFirst();
        do
            {
                arreglo.add(
                        new Municipio(
                                Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_municipio"))),
                                Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_departamento"))),
                                cursor.getString(cursor.getColumnIndex("nombre"))
                        )
                );
            }
            while(cursor.moveToNext());
        cursor.close();
        return arreglo;
    }


    public ArrayList<Establecimiento> Load_Establecimiento(String query)
    {
        ArrayList arreglo = new ArrayList<Establecimiento>();
        Cursor cursor=select(query);
        cursor.moveToFirst();
        do
        {
            arreglo.add(
                    new Establecimiento(
                            cursor.getString(cursor.getColumnIndex("codigo")),
                            cursor.getString(cursor.getColumnIndex("nombre")),
                            cursor.getString(cursor.getColumnIndex("direccion"))
                    )
                         );
        }
        while(cursor.moveToNext());
        cursor.close();
        return arreglo;
    }

public ArrayList<Nivel_Escolar> Load_NivelEscolar(String query)
{
    ArrayList arreglo = new ArrayList<Establecimiento>();
    Cursor cursor=select(query);
    cursor.moveToFirst();
    do
    {
        arreglo.add(
                new Nivel_Escolar(
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_nivel_escolar"))),
                        cursor.getString(cursor.getColumnIndex("nombre"))
                )
        );
    }
    while(cursor.moveToNext());
    cursor.close();
    return arreglo;
}

    public ArrayList<Sector> Load_Sector(String query)
    {
        ArrayList arreglo = new ArrayList<Sector>();
        Cursor cursor=select(query);
        cursor.moveToFirst();
        do
        {
            arreglo.add(
                    new Sector(
                            Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_sector"))),
                            cursor.getString(cursor.getColumnIndex("nombre"))
                    )
            );
        }
        while(cursor.moveToNext());
        cursor.close();
        return arreglo;
    }
    public ArrayList<Plan> Load_Plan(String query)
    {
        ArrayList arreglo = new ArrayList<Plan>();
        Cursor cursor=select(query);
        cursor.moveToFirst();
        do
        {
            arreglo.add(
                    new Plan(
                            Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_plan"))),
                            cursor.getString(cursor.getColumnIndex("nombre"))
                    )
            );
        }
        while(cursor.moveToNext());
        cursor.close();
        return arreglo;
    }
    public ArrayList<Modalidad> Load_Modalidad(String query)
    {
        ArrayList arreglo = new ArrayList<Modalidad>();
        Cursor cursor=select(query);
        cursor.moveToFirst();
        do
        {
            arreglo.add(
                    new Modalidad(
                            Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_modalidad"))),
                            cursor.getString(cursor.getColumnIndex("nombre"))
                    )
            );
        }
        while(cursor.moveToNext());
        cursor.close();
        return arreglo;
    }
    public ArrayList<Tipo_Incidente> Load_Tipo_Incidente(String query)
    {
        ArrayList arreglo = new ArrayList<Tipo_Incidente>();
        Cursor cursor=select(query);
        cursor.moveToFirst();
        do
        {
            arreglo.add(
                    new Tipo_Incidente
                            (Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_tipo_incidente"))),
                             cursor.getString(cursor.getColumnIndex("nombre"))
                    )
            );
        }
        while(cursor.moveToNext());
        cursor.close();
        return arreglo;
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {}
}