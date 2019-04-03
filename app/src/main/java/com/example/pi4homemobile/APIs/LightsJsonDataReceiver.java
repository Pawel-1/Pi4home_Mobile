package com.example.pi4homemobile.APIs;

import com.example.pi4homemobile.GlobalApplication;
import com.example.pi4homemobile.model.Light;

import java.util.List;
import java.util.Properties;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LightsJsonDataReceiver implements JsonDataReceiver {

    private Properties properties;
    private List<Light> lightList;

    public LightsJsonDataReceiver(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void getDataFromRestService() {
//        GlobalApplication.getAppContext();
//        String pi4HomeServerUser = properties.getProperty("spring.security.user.name");
//        String pi4homeServerPassword = properties.getProperty("spring.security.user.password");
//        String serverUrl = properties.getProperty("server.url");
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(serverUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        LightApiService apiService = retrofit.create(LightApiService.class);
//        Call<List<Light>> lightStatusData = apiService.getLightStatusData();
//
//        lightStatusData.enqueue(new Callback<List<Light>>() {
//            @Override
//            public void onResponse(Call<List<Light>> call, Response<List<Light>> response) {
//                if (!response.isSuccessful()) {
//                    System.out.println("Code: " + response.code());
//                    return;
//                }
//                lightList = response.body();
//                for (Light light : lightList) {
//                    System.out.println(light);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Light>> call, Throwable t) {
//                System.out.println(t.getMessage());
//            }
//        });
    }

    public List<Light> getLightList() {
        return lightList;
    }

}
