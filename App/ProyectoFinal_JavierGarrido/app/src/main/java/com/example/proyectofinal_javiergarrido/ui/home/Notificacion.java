package com.example.proyectofinal_javiergarrido.ui.home;

import java.util.List;

public class Notificacion {
    private int idNotificacion;
    private String hora;
    private int idUsuario;
    private String nombreNotificacion;
    private List<String> diasRepeticion;

    public Notificacion(int idNotificacion, String hora, int idUsuario, String nombreNotificacion, List<String> diasRepeticion) {
        this.idNotificacion = idNotificacion;
        this.hora = hora;
        this.idUsuario = idUsuario;
        this.nombreNotificacion = nombreNotificacion;
        this.diasRepeticion = diasRepeticion;
    }

    public int getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreNotificacion() {
        return nombreNotificacion;
    }

    public void setNombreNotificacion(String nombreNotificacion) {
        this.nombreNotificacion = nombreNotificacion;
    }

    public List<String> getDiasRepeticion() {
        return diasRepeticion;
    }

    public void setDiasRepeticion(List<String> diasRepeticion) {
        this.diasRepeticion = diasRepeticion;
    }
}
