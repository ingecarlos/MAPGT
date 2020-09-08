package com.example.george.enrutamiento.MODELO;

public class Sector
{
    private int id_Sector;
    private String nombre;

    public Sector (int id, String n)
    {
        this.id_Sector=id;
        this.nombre=n;
    }


    public void setId_Sector(int id_Sector) {
        this.id_Sector = id_Sector;
    }

    public int getId_Sector() {
        return id_Sector;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
