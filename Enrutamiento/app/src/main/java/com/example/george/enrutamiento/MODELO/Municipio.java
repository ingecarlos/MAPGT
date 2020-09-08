package com.example.george.enrutamiento.MODELO;

public class Municipio
{
    private  int id_municipio;
    private  int id_departamento;
    private  String nombre;

    public Municipio(int id_municipio,int id_departamento,String nombre)
    {
        this.id_departamento=id_departamento;
        this.id_municipio=id_municipio;
        this.nombre=nombre;
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

    public String getNombre() {
        return nombre;
    }

    public int getId_municipio() {
        return id_municipio;
    }

    public void setId_municipio(int id_municipio) {
        this.id_municipio = id_municipio;
    }
}
