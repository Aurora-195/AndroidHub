package com.example.aurorahub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        // Get the data passed from MainActivity
        String userId = getIntent().getStringExtra("userId");
        String userLogin = getIntent().getStringExtra("userLogin");
        String activities = getIntent().getStringExtra("activities");
        Button settings = findViewById(R.id.settings_btn);
        Button signOut = findViewById(R.id.sign_out_btn);
        Button start = findViewById(R.id.start_btn);
        ImageButton activity1 = findViewById(R.id.activity1);
        ImageButton activity2 = findViewById(R.id.activity2);
        ImageButton activity3 = findViewById(R.id.activity3);
        ImageButton activity4 = findViewById(R.id.activity4);
        final int[] activity = {0};

        activity1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lights up the corresponding activity icon while dimming the others
                if (activity1.getImageTintList() == ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.gray))) {
                    activity1.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.light_gray)));
                } else {
                        activity1.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.gray)));
                        activity2.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.light_gray)));
                        activity3.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.light_gray)));
                        activity4.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.light_gray)));
                        activity[0] = 1;
                }
            }
        });

        activity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lights up the corresponding activity icon while dimming the others
                if(activity2.getImageTintList() == ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.gray))) {
                    activity2.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.light_gray)));
                } else {
                    activity2.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.gray)));
                    activity1.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.light_gray)));
                    activity3.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.light_gray)));
                    activity4.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.light_gray)));
                    activity[0] = 2;
                }
            }
        });

        activity3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lights up the corresponding activity icon while dimming the others
                if(activity3.getImageTintList() == ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.gray))) {
                    activity3.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.light_gray)));
                } else {
                    activity3.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.gray)));
                    activity1.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.light_gray)));
                    activity2.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.light_gray)));
                    activity4.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.light_gray)));
                    activity[0] = 3;
                }
            }
        });

        activity4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lights up the corresponding activity icon while dimming the others
                if (activity4.getImageTintList() == ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.gray))) {
                    activity4.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.light_gray)));
            } else {
                    activity4.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.gray)));
                    activity1.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.light_gray)));
                    activity2.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.light_gray)));
                    activity3.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.light_gray)));
                    activity[0] = 4;
                }
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to another activity and pass user data
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Arrays.asList(activity1.getImageTintList(), activity2.getImageTintList(), activity3.getImageTintList(), activity4.getImageTintList())
                        .contains(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.gray)))) {
                    Snackbar.make(findViewById(R.id.start_btn), "Please select an activity", Snackbar.LENGTH_SHORT).show();
                } else {
                    if(start.getText().toString().equals("Start Activity")) {
                        start.setText("Stop Activity");
                        Animation fadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
                        switch(activity[0]) {
                            case 1:
                                Animation a1move = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down_left);
                                activity1.startAnimation(a1move);
                                activity2.startAnimation(fadeout);
                                activity3.startAnimation(fadeout);
                                activity4.startAnimation(fadeout);
                                activity1.setClickable(false);
                                activity2.setClickable(false);
                                activity3.setClickable(false);
                                activity4.setClickable(false);
                                break;
                            case 2:
                                Animation a2move = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down_right);
                                activity2.startAnimation(a2move);
                                activity1.startAnimation(fadeout);
                                activity3.startAnimation(fadeout);
                                activity4.startAnimation(fadeout);
                                activity1.setClickable(false);
                                activity2.setClickable(false);
                                activity3.setClickable(false);
                                activity4.setClickable(false);
                                break;
                            case 3:
                                Animation a3move = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up_left);
                                activity3.startAnimation(a3move);
                                activity1.startAnimation(fadeout);
                                activity2.startAnimation(fadeout);
                                activity4.startAnimation(fadeout);
                                activity1.setClickable(false);
                                activity2.setClickable(false);
                                activity3.setClickable(false);
                                activity4.setClickable(false);
                                break;
                            case 4:
                                Animation a4move = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up_right);
                                activity4.startAnimation(a4move);
                                activity1.startAnimation(fadeout);
                                activity2.startAnimation(fadeout);
                                activity3.startAnimation(fadeout);
                                activity1.setClickable(false);
                                activity2.setClickable(false);
                                activity3.setClickable(false);
                                activity4.setClickable(false);
                                break;
                            }
                        } else if(start.getText().toString().equals("Stop Activity")) {
                            Animation fadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
                            Animation fadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                            start.setText("Start Activity");
                            switch(activity[0]) {
                                case 1:
                                    Animation a1move = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up_right);
                                    activity1.startAnimation(a1move);
                                    activity2.startAnimation(fadein);
                                    activity3.startAnimation(fadein);
                                    activity4.startAnimation(fadein);
                                    activity1.setClickable(true);
                                    activity2.setClickable(true);
                                    activity3.setClickable(true);
                                    activity4.setClickable(true);
                                    break;
                                case 2:
                                    Animation a2move = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up_left);
                                    activity2.startAnimation(a2move);
                                    activity1.startAnimation(fadein);
                                    activity3.startAnimation(fadein);
                                    activity4.startAnimation(fadein);
                                    activity1.setClickable(true);
                                    activity2.setClickable(true);
                                    activity3.setClickable(true);
                                    activity4.setClickable(true);
                                    break;
                                case 3:
                                    Animation a3move = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down_right);
                                    activity3.startAnimation(a3move);
                                    activity1.startAnimation(fadein);
                                    activity2.startAnimation(fadein);
                                    activity4.startAnimation(fadein);
                                    activity1.setClickable(true);
                                    activity2.setClickable(true);
                                    activity3.setClickable(true);
                                    activity4.setClickable(true);
                                    break;
                                case 4:
                                    Animation a4move = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down_left);
                                    activity4.startAnimation(a4move);
                                    activity1.startAnimation(fadein);
                                    activity2.startAnimation(fadein);
                                    activity3.startAnimation(fadein);
                                    activity1.setClickable(true);
                                    activity2.setClickable(true);
                                    activity3.setClickable(true);
                                    activity4.setClickable(true);
                                    break;
                            }
                        }

                    }
                }
        });

        TextView message = findViewById(R.id.id_message);
        message.setText(userId);
        // Use the retrieved data (e.g., display it or use it in API calls)
    }

}