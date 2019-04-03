package com.example.pi4homemobile.APIs;

import com.example.pi4homemobile.model.Light;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface LightApiService {

    @GET("lightStatus")
    Call<List<Light>> getLightStatusData(@Header("Authorization") String authHeader);

    @PUT("light/{name}")
    Call<Light> updateLightStatusData(@Path("name") String name, @Header("Authorization") String authHeader, @Body Light light);
}
