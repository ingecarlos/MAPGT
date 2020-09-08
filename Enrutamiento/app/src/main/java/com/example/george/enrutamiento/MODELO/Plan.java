package com.example.george.enrutamiento.MODELO;

public class Plan
{
    private  int id_plan;
    private  String nombre;

    public Plan(int id, String n )
    {
        this.id_plan=id;
        this.nombre=n;
    }

    public String getNombre() {
        return nombre;
    }

    public int getId_plan() {
        return id_plan;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setId_plan(int id_plan) {
        this.id_plan = id_plan;
    }
}
