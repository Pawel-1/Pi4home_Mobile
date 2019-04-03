package com.example.pi4homemobile;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.pi4homemobile.fragments.FragmentBlinds;
import com.example.pi4homemobile.fragments.FragmentDoors;
import com.example.pi4homemobile.fragments.FragmentLights;
import com.example.pi4homemobile.fragments.FragmentSensors;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tablayout_id);
        appBarLayout = findViewById(R.id.appbarid);
        viewPager = findViewById(R.id.viewerpager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentBlinds(), "Rolety");
        FragmentLights fragmentLights = new FragmentLights();
        adapter.addFragment(fragmentLights, "Światło");
        adapter.addFragment(new FragmentSensors(), "Czujniki");
        adapter.addFragment(new FragmentDoors(), "Drzwi");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
