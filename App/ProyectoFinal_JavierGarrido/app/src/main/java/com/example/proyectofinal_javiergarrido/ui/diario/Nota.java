package com.example.proyectofinal_javiergarrido.ui.diario;

import java.io.Serializable;

public class Nota implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id_nota;
    private String titulo;
    private String contenido;
    private String fecha;
    private String color;
    private int id_usuario;

    public Nota() { }

    public Nota(String titulo, String contenido, String fecha, String color) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.fecha = fecha;
        this.color = color;
    }

    public int getIdNota() {
        return id_nota;
    }

    public void setIdNota(int id_nota) {
        this.id_nota = id_nota;
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

    public int getIdUsuario() {
        return id_usuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.id_usuario = idUsuario;
    }
}
