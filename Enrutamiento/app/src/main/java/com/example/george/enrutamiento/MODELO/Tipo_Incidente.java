package com.example.george.enrutamiento.MODELO;

public class Tipo_Incidente
{
    private int id ;
    private  String nombre;

    public Tipo_Incidente(int id, String nombre)
    {
        this.id=id;
        this.nombre= nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public int getId() {
        return id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setId(int id) {
        this.id = id;
    }
}
