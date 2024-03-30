package com.example.aurorahub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

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

        activity1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity1.getImageTintList() == ColorStateList.valueOf(getResources().getColor(R.color.gray)))
                    activity1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray)));
                else {
                    activity1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
                    activity2.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray)));
                    activity3.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray)));
                    activity4.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray)));
                }
            }
        });

        activity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity2.getImageTintList() == ColorStateList.valueOf(getResources().getColor(R.color.gray)))
                    activity2.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray)));
                else {
                    activity2.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
                    activity1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray)));
                    activity3.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray)));
                    activity4.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray)));
                }
            }
        });

        activity3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity3.getImageTintList() == ColorStateList.valueOf(getResources().getColor(R.color.gray)))
                    activity3.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray)));
                else {
                    activity3.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
                    activity1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray)));
                    activity2.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray)));
                    activity4.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray)));
                }
            }
        });

        activity4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity4.getImageTintList() == ColorStateList.valueOf(getResources().getColor(R.color.gray)))
                    activity4.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray)));
                else {
                    activity4.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
                    activity1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray)));
                    activity2.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray)));
                    activity3.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray)));
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
                        .contains(ColorStateList.valueOf(getResources().getColor(R.color.gray)))) {
                    Snackbar.make(findViewById(R.id.start_btn), "Please select an activity", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        TextView message = findViewById(R.id.id_message);
        message.setText(userId);
        // Use the retrieved data (e.g., display it or use it in API calls)
    }

}