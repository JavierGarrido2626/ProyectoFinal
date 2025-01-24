package com.example.proyectofinal_javiergarrido.ui.home;

public class SolicitudNumeroTelefono {
    private int id_usuario;
    private String nuevo_telefono;

    public SolicitudNumeroTelefono(int id_usuario, String nuevo_telefono) {
        this.id_usuario = id_usuario;
        this.nuevo_telefono = nuevo_telefono;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNuevo_telefono() {
        return nuevo_telefono;
    }

    public void setNuevo_telefono(String nuevo_telefono) {
        this.nuevo_telefono = nuevo_telefono;
    }
}
