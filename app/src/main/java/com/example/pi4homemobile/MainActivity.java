package com.example.pi4homemobile;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pi4homemobile.fragments.FragmentBlinds;
import com.example.pi4homemobile.fragments.FragmentDoors;
import com.example.pi4homemobile.fragments.FragmentLights;
import com.example.pi4homemobile.fragments.FragmentSensors;
import com.example.pi4homemobile.fragments.FragmentYeelight;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    ImageView imageView;
    TextView textView;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animation = AnimationUtils.loadAnimation(this, R.anim.logoanim);

        imageView = findViewById(R.id.appBarLayoutImageId);
        textView = findViewById(R.id.appbarTextId);
        tabLayout = findViewById(R.id.tablayout_id);
        appBarLayout = findViewById(R.id.appBarId);
        viewPager = findViewById(R.id.viewerpager_id);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Context applicationContext = getApplicationContext();

        adapter.addFragment(new FragmentBlinds(), "Rolety");
        adapter.addFragment(new FragmentLights(), "Światło");
        adapter.addFragment(new FragmentYeelight(), "Yeelight");
        adapter.addFragment(new FragmentSensors(), "Czujniki");
        adapter.addFragment(new FragmentDoors(), "Drzwi");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void updateAppBarByProgressBar(int progress) {

        imageView.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView.setText(progress + "%");
    }

    public void updateAppBarByImage() {
        imageView.animate().alpha(1).start();
        imageView.startAnimation(animation);
        textView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
    }
}
