package com.example.proyectofinal_javiergarrido.ui.diario;

import java.io.Serializable;

public class Nota implements Serializable {
    private int id;
    private String titulo;
    private String contenido;
    private String fecha;
    private String color;

    public Nota(String titulo, String contenido, String fecha, String color) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.fecha = fecha;
        this.color = color;
    }

    public Nota() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
