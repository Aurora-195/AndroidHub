package com.example.aurorahub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        String userId = getIntent().getStringExtra("userId");
        String userLogin = getIntent().getStringExtra("userLogin");
        String activities = getIntent().getStringExtra("activities");
        Button settings = findViewById(R.id.settings_btn);
        Button signOut = findViewById(R.id.sign_out_btn);

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

        TextView message = findViewById(R.id.id_message);
        message.setText(userId);
        // Use the retrieved data (e.g., display it or use it in API calls)
    }

}