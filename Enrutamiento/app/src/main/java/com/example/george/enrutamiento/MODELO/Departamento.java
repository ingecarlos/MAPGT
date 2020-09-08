package com.example.george.enrutamiento.MODELO;

public class Departamento
{
    private int id_departamento;
    private String nombre;


    public Departamento(int id_departamento, String nombre)
    {
        this.id_departamento=id_departamento;
        this.nombre=nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setId_departamento(int id_departamento) {
        this.id_departamento = id_departamento;
    }

    public int getId_departamento() {
        return id_departamento;
    }
}
