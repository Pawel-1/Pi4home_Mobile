package com.example.pi4homemobile.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pi4homemobile.R;
import com.spark.submitbutton.SubmitButton;

public class FragmentDoors extends Fragment {

    private CountDownTimer timer;
    private long timeLeft = 30000;

    public FragmentDoors() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doors_fragment, container, false);

        SubmitButton doorsButton = view.findViewById(R.id.doors_button);
        final TextView doorsTextView = view.findViewById(R.id.doors_textView1);

        doorsButton.setOnClickListener(triggerTimer(doorsTextView));

        return view;
    }

    private View.OnClickListener triggerTimer(final TextView doorsTextView) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timer = new CountDownTimer(timeLeft, 1000) {
                    @Override
                    public void onTick(long l) {
                        timeLeft = l;
                        int secondsLeft = (int) timeLeft % 60000 / 1000;

                        String timeLeftString = "";

                        if (timeLeft < 10) {
                            timeLeftString += "0";
                        }
                        timeLeftString += secondsLeft;
                        doorsTextView.setText(timeLeftString);
                    }

                    @Override
                    public void onFinish() {
                        timer.cancel();
                    }
                }.start();
            }
        };
    }
}


