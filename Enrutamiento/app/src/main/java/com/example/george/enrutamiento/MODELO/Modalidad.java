package com.example.george.enrutamiento.MODELO;

public class Modalidad
{
    private  int id_modalidad;
    private  String nombre;

    public Modalidad(int id , String n)
    {
        this.id_modalidad= id;
        this.nombre=n;
    }


    public int getId_modalidad() {
        return id_modalidad;
    }

    public String getNombre() {
        return nombre;
    }
}
