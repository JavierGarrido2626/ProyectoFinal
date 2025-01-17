package com.example.proyectofinal_javiergarrido.ui.estadisticas.Contar;

public class EstadisticasJuegoContarObjeto {

    private int contador_mayor;
    private int contador_menor;
    private double contador_medio;

    public int getContadorMayor() {
        return contador_mayor;
    }

    public void setContadorMayor(int contadorMayor) {
        this.contador_mayor = contadorMayor;
    }

    public int getContadorMenor() {
        return contador_menor;
    }

    public void setContadorMenor(int contadorMenor) {
        this.contador_menor = contadorMenor;
    }

    public double getContadorMedio() {
        return contador_medio;
    }

    public void setContadorMedio(double contadorMedio) {
        this.contador_medio = contadorMedio;
    }

    @Override
    public String toString() {
        return "EstadisticasJuegoContarObjeto{" +
                "contadorMayor=" + contador_mayor +
                ", contadorMenor=" + contador_menor +
                ", contadorMedio=" + contador_medio +
                '}';
    }
}
