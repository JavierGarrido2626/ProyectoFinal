package com.example.proyectofinal_javiergarrido.ui.ServiciosServer;

import com.example.proyectofinal_javiergarrido.ui.diario.Nota;
import com.example.proyectofinal_javiergarrido.ui.diario.RespuestaNotas;
import com.example.proyectofinal_javiergarrido.ui.estadisticas.Cartas.EstadisticasCartasObjeto;
import com.example.proyectofinal_javiergarrido.ui.estadisticas.Colores.EstadisticasJuegoColorObjeto;
import com.example.proyectofinal_javiergarrido.ui.estadisticas.Contar.EstadisticasJuegoContarObjeto;
import com.example.proyectofinal_javiergarrido.ui.estadisticas.Trabalenguas.EstadisticasJuegoTrabalenguasObjeto;
import com.example.proyectofinal_javiergarrido.ui.home.Notificacion;
import com.example.proyectofinal_javiergarrido.ui.home.ObtenerTelefonoObjeto;
import com.example.proyectofinal_javiergarrido.ui.home.SolicitudNumeroTelefono;
import com.example.proyectofinal_javiergarrido.ui.ubi.RespuestaUbicaciones;
import com.example.proyectofinal_javiergarrido.ui.ubi.Ubicacion;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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




    @GET("obtener_numero_telefono")
    Call<ObtenerTelefonoObjeto> obtenerNumeroTelefono(@Query("id_usuario") int idUsuario);



    @POST("insertar_numero_telefono")
    Call<Void> guardarTelefono(@Body SolicitudNumeroTelefono solicitudNumeroTelefono);


    @PUT("actualizar_numero_telefono")
    Call<Void> actualizarNumeroTelefono(@Body SolicitudNumeroTelefono solicitudNumeroTelefono);







    @POST("insertar_notificacion")
    Call<Void> guardarNotificacion(@Body Notificacion notificacion);

    @GET("obtener_notificaciones")
    Call<List<Notificacion>> obtenerNotificaciones(@Query("idUsuario") int idUsuario);

    @DELETE("eliminar_notificacion/{id}")
    Call<Void> eliminarNotificacion(@Path("id") int idNotificacion);





    @POST("insertar_ubicacion")
    Call<Void> guardarUbicacion(@Body Ubicacion ubicacion);

    @POST("insertar_ubicacion_seleccionada")
    Call<Void> guardarUbicacionSeleccionado(@Body Ubicacion ubicacion);






    @GET("obtener_ubicaciones_por_usuario")
    Call<RespuestaUbicaciones> obtenerUbicacionesPorUsuario(@Query("id_usuario") int idUsuario);

    @DELETE("eliminar_ubicacion/{id_ubicacion}")
    Call<Void> eliminarUbicacion(@Path("id_ubicacion") int idUbicacion);



    @POST("insertar_nota")
    Call<Nota> guardarNota(@Body Nota nota);


    @GET("obtener_notas_por_usuario")
    Call<RespuestaNotas> obtenerNotasPorUsuario(@Query("id_usuario") int idUsuario);
    @DELETE("eliminar_nota/{id_nota}")
    Call<Void> eliminarNota(@Path("id_nota") int idNota);







}
