package com.auroratime.aurorahub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_settings);
    }

    public void startLinkingProcess(View view) {
        // The URL to start the OAuth flow, pointing to your backend or directly to Google's OAuth server
        String authUrl = "https://auroratime.org/auth/google";
        Intent intent;
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl));
        startActivity(intent);
    }

}