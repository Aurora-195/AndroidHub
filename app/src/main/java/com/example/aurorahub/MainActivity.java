package com.example.aurorahub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

        TextView message = findViewById(R.id.id_message);
        message.setText(userId);
        // Use the retrieved data (e.g., display it or use it in API calls)
    }

}