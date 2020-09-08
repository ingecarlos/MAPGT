package com.example.george.enrutamiento.MODELO;

public class Nivel_Escolar
{
    private int id_nivel;
    private String nombre;

    public Nivel_Escolar(int i, String n)
    {
        this.id_nivel=i;
        this.nombre=n;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setId_nivel(int id_nivel) {
        this.id_nivel = id_nivel;
    }

    public String getNombre() {
        return nombre;
    }

    public int getId_nivel() {
        return id_nivel;
    }
}
