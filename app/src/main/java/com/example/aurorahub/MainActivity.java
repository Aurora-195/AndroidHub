package com.example.aurorahub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        // Get the data passed from MainActivity
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences("AuroraData", Context.MODE_PRIVATE);

        String userId = sharedPreferences.getString("userId", "none");
        String userLogin = sharedPreferences.getString("userLogin", "none");
        String activities = sharedPreferences.getString("userActivities", "none");

        Button addLog = findViewById(R.id.add_log_btn);
        Button settings = findViewById(R.id.settings_btn);
        Button signOut = findViewById(R.id.sign_out_btn);

        addLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), AddLogActivity.class);
                startActivity(intent);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to another activity and pass user data

                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //clear saved user data
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                //open login menu
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        TextView message = findViewById(R.id.id_message);
        message.setText(userId);
    }

}