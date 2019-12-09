package com.example.pi4homemobile.APIs;

import com.example.pi4homemobile.model.yeelight.Yeelight;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;

public interface YeelightApiService {

    @GET("yeelightStatus")
    Call<List<Yeelight>> getYeelightStatusData(@Header("Authorization") String authHeader);

    @PUT("yeelight/")
    Call<Yeelight> updateYeelightStatusData(@Header("Authorization") String authHeader, @Body Yeelight light);
}
