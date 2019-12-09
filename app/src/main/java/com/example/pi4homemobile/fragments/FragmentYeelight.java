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

import com.example.pi4homemobile.APIs.YeelightApiService;
import com.example.pi4homemobile.R;
import com.example.pi4homemobile.model.yeelight.Yeelight;
import com.example.pi4homemobile.model.yeelight.YeelightSwitchWrapper;
import com.example.pi4homemobile.properties.PropertyReader;
import com.rey.material.widget.Switch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.pi4homemobile.APIs.ApiServiceCreator.createApiService;
import static com.example.pi4homemobile.APIs.AuthenticationCreator.createAuthenticationHeader;

public class FragmentYeelight extends Fragment {

    private Context context;

    YeelightSwitchWrapper deskYeelight = new YeelightSwitchWrapper();
    YeelightSwitchWrapper topLeftYeelight = new YeelightSwitchWrapper();
    YeelightSwitchWrapper topRightYeelight = new YeelightSwitchWrapper();

    List<YeelightSwitchWrapper> yeelightList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yeelight_fragment, container, false);

        deskYeelight.setaSwitch(view.findViewById(R.id.yeelightDeskSwitch));
        deskYeelight.setYeelightName("deskYeelight");

        topLeftYeelight.setaSwitch(view.findViewById(R.id.yeelightTopLeftSwitch));
        topLeftYeelight.setYeelightName("leftYeelight");

        topRightYeelight.setaSwitch(view.findViewById(R.id.yeelightTopRightSwitch));
        topRightYeelight.setYeelightName("rightYeelight");

        yeelightList.addAll(Arrays.asList(deskYeelight, topLeftYeelight, topRightYeelight));

        Properties myProperties = readProperties();
        YeelightApiService apiService = createApiService(myProperties, YeelightApiService.class);
        final String authHeader = createAuthenticationHeader(myProperties);

        addListeners(yeelightList, apiService, authHeader);

        return view;
    }

    private void addListeners(List<YeelightSwitchWrapper> yeelightSwitchWrappers, YeelightApiService apiService, String authHeader) {

        for (YeelightSwitchWrapper yeelightSwitchWrapper : yeelightSwitchWrappers) {

            Switch yeelightSwitch = yeelightSwitchWrapper.getaSwitch();
            String yeelightName = yeelightSwitchWrapper.getYeelightName();

            yeelightSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(Switch view, boolean checked) {

                    Yeelight updatedYeelight = new Yeelight();
                    updatedYeelight.setName(yeelightName);
                    updatedYeelight.setTurnedOn(checked);

                    Call<Yeelight> lightCall = apiService.updateYeelightStatusData(authHeader, updatedYeelight);
                    lightCall.enqueue(new Callback<Yeelight>() {
                        @Override
                        public void onResponse(Call<Yeelight> call, Response<Yeelight> response) {
                            if (checked) {
                                Toast.makeText(context, "desk Yeelight is ON", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "desk Yeelight is OFF", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Yeelight> call, Throwable t) {
                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    private Properties readProperties() {
        context = getActivity().getApplicationContext();
        PropertyReader propertyReader = new PropertyReader(context);
        return propertyReader.getMyProperties("config.properties");
    }
}
