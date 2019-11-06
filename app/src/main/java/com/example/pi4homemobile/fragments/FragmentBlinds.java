package com.example.pi4homemobile.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.pi4homemobile.APIs.BlindApiService;
import com.example.pi4homemobile.MainActivity;
import com.example.pi4homemobile.R;
import com.example.pi4homemobile.model.Blind;
import com.example.pi4homemobile.properties.PropertyReader;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentBlinds extends Fragment {

    SeekBar largeWindowLeft;
    SeekBar largeWindowRight;
    SeekBar smallWindowLeft;
    SeekBar smallWindowMiddle;
    SeekBar smallWindowRight;
    private boolean initialSetUp = false;
    private MainActivity mainActivity;
    private Context context;
    private final List<SeekBar> seekBarArrayList = new ArrayList<>();
    List<Blind> initialBlindList;

    public FragmentBlinds() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        View view = inflater.inflate(R.layout.blinds_fragment, container, false);
        mainActivity = (MainActivity) getActivity();

        largeWindowLeft = view.findViewById(R.id.largeWindowLeft);
        largeWindowRight = view.findViewById(R.id.largeWindowRight);
        smallWindowLeft = view.findViewById(R.id.smallWindowLeft);
        smallWindowMiddle = view.findViewById(R.id.smallWindowMiddle);
        smallWindowRight = view.findViewById(R.id.smallWindowRight);

        seekBarArrayList.add(largeWindowLeft);
        seekBarArrayList.add(largeWindowRight);
        seekBarArrayList.add(smallWindowLeft);
        seekBarArrayList.add(smallWindowMiddle);
        seekBarArrayList.add(smallWindowRight);

        Properties myProperties = readProperties();
        BlindApiService blindApiService = createBlindApiService(myProperties);

        final String authHeader = createAuthenticationHeader(myProperties);

        Call<List<Blind>> blindStatusData = blindApiService.getBlindStatusData(authHeader);


        setUptInitialBlindState(blindStatusData);


        try {
            addListener(blindApiService, authHeader, initialBlindList, seekBarArrayList);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return view;
    }


    //ToDo: debug - jak to zadziala w momencie ruszenia suwakiem? bedzie iterowac po wszystkim?
    private void addListener(BlindApiService blindApiService, String authHeader, List<Blind> initialBlindList, List<SeekBar> seekBarArrayList) throws NoSuchFieldException, IllegalAccessException {

        for (Blind blind : initialBlindList) {
            String name = blind.getName();
            Field field = this.getClass().getField(blind.getName());
            SeekBar slider = (SeekBar) field.get(this);

            slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                Blind updatedBlind = new Blind();

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    updatedBlind.setName(name);
                    double percentageMaskingState = new Double(progress);
                    updatedBlind.setPercentageMaskingState(percentageMaskingState);

                    if (initialSetUp) {
                        mainActivity.updateAppBarByProgressBar(progress);
                    }
                    initialSetUp = true;

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    Call<Blind> updateBlindStatusData = blindApiService.updateBlindStatusData(authHeader, updatedBlind);

                    updateBlindStatusData.enqueue(new Callback<Blind>() {
                        @Override
                        public void onResponse(Call<Blind> call, Response<Blind> response) {
                            Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onFailure(Call<Blind> call, Throwable t) {
                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                        }
                    });
                    mainActivity.updateAppBarByImage();
                }
            });
        }
    }

    private void setUptInitialBlindState(Call<List<Blind>> blindStatusData) {

        try {
            Response<List<Blind>> response = blindStatusData.execute();
            initialBlindList = response.body();
            initialBlindList.forEach(blind -> setUpBlindState(blind));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Properties readProperties() {
        context = getActivity().getApplicationContext();
        PropertyReader propertyReader = new PropertyReader(context);
        return propertyReader.getMyProperties("config.properties");
    }

    private String createAuthenticationHeader(Properties myProperties) {
        String serverUser = myProperties.getProperty("spring.security.user.name");
        String serverPass = myProperties.getProperty("spring.security.user.password");

        return "Basic " + Base64.encodeToString((serverUser + ":" + serverPass).getBytes(), Base64.NO_WRAP);
    }

    private BlindApiService createBlindApiService(Properties myProperties) {
        String serverUrl = myProperties.getProperty("server.url");
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(15, TimeUnit.SECONDS);
        client.readTimeout(15, TimeUnit.SECONDS);
        client.writeTimeout(15, TimeUnit.SECONDS);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(BlindApiService.class);
    }

    private void setUpBlindState(Blind blind) {
        double percentageMaskingState = blind.getPercentageMaskingState();
        int percentageMaskingStateInFloat = (int) percentageMaskingState;
        try {
            Field field = this.getClass().getField(blind.getName());
            try {
                SeekBar slider = (SeekBar) field.get(this);
                slider.setProgress(percentageMaskingStateInFloat);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
