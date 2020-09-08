package com.example.george.enrutamiento.Enrutador;

public class Establecimiento
{
    private String Codigo;
    private String Nombre;
    private String Direccion;
    private double Latitud;
    private double Longitud;

    public Establecimiento(String Codigo,String nombre,String Direccion)
    {
        this.Codigo=Codigo;
        this.Nombre= nombre;
        this.Direccion=Direccion;
    }

    public Establecimiento(String Codigo,String nombre,String Direccion,double a,double b)
    {
        this.Codigo=Codigo;
        this.Nombre= nombre;
        this.Direccion=Direccion;
        this.Latitud=a;
        this.Longitud=b;
    }


    public String getDireccion() {
        return Direccion;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setCodigo(String codigo) { Codigo = codigo; }

    public String getCodigo() { return Codigo; }

    public void setLatitud(double latitud) { Latitud = latitud; }

    public void setLongitud(double longitud) { Longitud = longitud; }

    public double getLatitud() { return Latitud; }

    public double getLongitud() { return Longitud; }
}
