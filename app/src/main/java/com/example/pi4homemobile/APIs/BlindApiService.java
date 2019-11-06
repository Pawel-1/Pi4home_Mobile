package com.example.pi4homemobile.APIs;

import com.example.pi4homemobile.model.Blind;
import com.example.pi4homemobile.model.Light;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BlindApiService
{
    @GET("blindStatus")
    Call<List<Blind>> getBlindStatusData(@Header("Authorization") String authHeader);

    @PUT("blind/")
    Call<Blind> updateBlindStatusData(@Header("Authorization") String authHeader, @Body Blind blind);
}
