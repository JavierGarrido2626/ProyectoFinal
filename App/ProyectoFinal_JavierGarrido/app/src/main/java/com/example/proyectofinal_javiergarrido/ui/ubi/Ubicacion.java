package com.example.proyectofinal_javiergarrido.ui.ubi;

import java.io.Serializable;

public class Ubicacion implements Serializable {
    private double latitud;
    private double longitud;
    private String nombre;
    private int id_usuario;
    private int id_ubicacion;

    public Ubicacion(double latitud, double longitud, String nombre) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
    }


    public Ubicacion(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Ubicacion(double latitud, double longitud, String nombre, int id_usuario) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
        this.id_usuario = id_usuario;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_ubicacion() {
        return id_ubicacion;
    }

    public void setId_ubicacion(int id_ubicacion) {
        this.id_ubicacion = id_ubicacion;
    }
}
