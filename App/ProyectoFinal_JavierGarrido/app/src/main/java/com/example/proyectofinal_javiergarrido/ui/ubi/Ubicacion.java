package com.example.proyectofinal_javiergarrido.ui.ubi;

import java.io.Serializable;

public class Ubicacion implements Serializable {
    private double latitud;
    private double longitud;
    private String nombre;

    public Ubicacion(double latitud, double longitud, String nombre) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
