package com.example.proyectofinal_javiergarrido.ui.ServiciosServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClienteApi {
   // private static final String BASE_URL = "http://192.168.1.133:5000";  // Wifi
    private static final String BASE_URL = "http://192.168.115.103:5000";  // Movil


    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL + "/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
