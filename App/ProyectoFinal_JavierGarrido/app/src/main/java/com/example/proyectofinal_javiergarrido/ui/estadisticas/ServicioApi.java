package com.example.proyectofinal_javiergarrido.ui.estadisticas;

import com.example.proyectofinal_javiergarrido.ui.estadisticas.Colores.EstadisticasJuegoColorObjeto;
import com.example.proyectofinal_javiergarrido.ui.estadisticas.Contar.EstadisticasJuegoContarObjeto;
import com.example.proyectofinal_javiergarrido.ui.estadisticas.Trabalenguas.EstadisticasJuegoTrabalenguasObjeto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServicioApi {

    @GET("estadisticasVoltearcartas")
    Call<EstadisticasCartasObjeto> obtenerEstadisticasVoltearcartas(@Query("id_usuario") int idUsuario, @Query("nivel") String nivel);


    @GET("estadisticasTrabalenguas")
    Call<List<EstadisticasJuegoTrabalenguasObjeto>> obtenerEstadisticasTrabalenguas(
            @Query("id_usuario") int idUsuario,
            @Query("nivel") String nivel);

    @GET("estadisticasJuegoColor")
    Call<EstadisticasJuegoColorObjeto> obtenerEstadisticasJuegoColor(
            @Query("id_usuario") int idUsuario,
            @Query("nivel") String nivel);




    @GET("estadisticasJuegoContar")
    Call<EstadisticasJuegoContarObjeto> obtenerEstadisticasJuegoContar(
            @Query("id_usuario") int idUsuario);




}


