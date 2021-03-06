package com.example.pi4homemobile.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pi4homemobile.APIs.LightApiService;
import com.example.pi4homemobile.R;
import com.example.pi4homemobile.model.Light;
import com.example.pi4homemobile.properties.PropertyReader;
import com.rey.material.widget.Switch;

import java.util.List;
import java.util.Properties;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.pi4homemobile.APIs.ApiServiceCreator.createApiService;
import static com.example.pi4homemobile.APIs.AuthenticationCreator.createAuthenticationHeader;

public class FragmentLights extends Fragment {

    private boolean entranceLightOn;
    private boolean sidewalkLightOn;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.light_fragment, container, false);
        final Switch entranceLightSwitch = view.findViewById(R.id.entranceLightSwitch);
        final Switch sidewalkLightSwtich = view.findViewById(R.id.sidewalkLightSwitch);

        Properties myProperties = readProperties();
        LightApiService apiService = createApiService(myProperties, LightApiService.class);
        final String authHeader = createAuthenticationHeader(myProperties);

        Call<List<Light>> lightStatusData = apiService.getLightStatusData(authHeader);

        lightStatusData.enqueue(new Callback<List<Light>>() {
            @Override
            public void onResponse(Call<List<Light>> call, Response<List<Light>> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code());
                    return;
                }
                List<Light> body = response.body();
                for (Light light : body) {
                    System.out.println(light);
                    String lightName = light.getName();
                    if (lightName.equals("entranceLight")) {
                        entranceLightOn = light.isTurnedOn();
                    }
                    if (lightName.equals("sidewalkLight")) {
                        sidewalkLightOn = light.isTurnedOn();
                    }
                    entranceLightSwitch.setChecked(entranceLightOn);
                    sidewalkLightSwtich.setChecked(sidewalkLightOn);
                }
            }

            @Override
            public void onFailure(Call<List<Light>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });


        entranceLightSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, final boolean checked) {
                Light updatedLight = new Light();
                updatedLight.setName("entranceLight");
                updatedLight.setTurnedOn(checked);

                Call<Light> lightCall = apiService.updateLightStatusData("entranceLight", authHeader, updatedLight);
                lightCall.enqueue(new Callback<Light>() {
                    @Override
                    public void onResponse(Call<Light> call, Response<Light> response) {
                        if (checked) {
                            Toast.makeText(context, "entranceLight is ON", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "entranceLight is OFF", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Light> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        sidewalkLightSwtich.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, final boolean checked) {
                Light updatedLight = new Light();
                updatedLight.setName("sidewalkLight");
                updatedLight.setTurnedOn(checked);

                Call<Light> lightCall = apiService.updateLightStatusData("sidewalkLight", authHeader, updatedLight);
                lightCall.enqueue(new Callback<Light>() {
                    @Override
                    public void onResponse(Call<Light> call, Response<Light> response) {
                        if (checked) {
                            Toast.makeText(context, "sidewalkLight is ON", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "sidewalkLight is OFF", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Light> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }

    private Properties readProperties() {
        context = getActivity().getApplicationContext();
        PropertyReader propertyReader = new PropertyReader(context);
        return propertyReader.getMyProperties("config.properties");
    }
}
