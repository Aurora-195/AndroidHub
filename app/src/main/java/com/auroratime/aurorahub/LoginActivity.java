package com.auroratime.aurorahub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        if(isLoggedIn(getApplicationContext())){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        EditText usernameInput = findViewById(R.id.username_input);
        EditText passwordInput = findViewById(R.id.password_input);
        Button loginBtn = findViewById(R.id.login_btn);


        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();

                if (!username.isEmpty() && !password.isEmpty()){
                    attemptToLogin(username, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Username or Password cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void attemptToLogin(String username, String password){
        new LoginTask().execute("https://auroratime.org/users/login", username, password);
    }

    private class LoginTask extends AsyncTask<String, Void, String> {
        // AsyncTask implementation as previously described
        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0]; // URL to call
            String resultToDisplay = "";
            InputStream in = null;
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("login", params[1]);
                jsonParam.put("password", params[2]);

                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(jsonParam.toString());
                writer.flush();
                writer.close();

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                    in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append('\n');
                    }
                    resultToDisplay = sb.toString();
                } else {
                    resultToDisplay = "Error " + responseCode;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("LoginTask", "HTTP request error: " + e.getMessage(), e);
                resultToDisplay +=  "Error during HTTP request: " + e.getMessage();

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return resultToDisplay;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                Log.d("LoginResponse", "Response received: " + result);
                JSONObject jsonResponse = new JSONObject(result);
                if (jsonResponse.has("user")) { // Check if the response contains "user" object
                    JSONObject user = jsonResponse.getJSONObject("user");
                    String userId = user.getString("id");
                    String userLogin = user.getString("login");
                    //Assuming "activities" is a JSONArray or similar
                    String activities = user.getJSONArray("activities").toString();

                    // Navigate to another activity and pass user data
                    saveUserData(getApplicationContext(), userId, userLogin, activities);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    // Show error message
                    String message = jsonResponse.optString("message", "Login failed. Unknown error.");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Log.e("LoginTask", "Error parsing login response: " + result, e);
                Toast.makeText(getApplicationContext(), "Login failed. Please try again.", Toast.LENGTH_LONG).show();
            }
        }

        private String getPostDataString(String username, String password) throws UnsupportedEncodingException {
            return "username=" + URLEncoder.encode(username, "UTF-8") +
                    "&password=" + URLEncoder.encode(password, "UTF-8");
        }
    }

    // This function stores the data of the logged user to avoid logging in later and share the data
    // with other features
    private void saveUserData(Context context, String userId, String userLogin, String activities){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AuroraData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("userId", userId);
        editor.putString("userLogin", userLogin);
        editor.putString("userActivities", activities);
        editor.putBoolean("loggedIn", true);

        editor.apply();
    }

    public boolean isLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AuroraData", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("loggedIn", false);
    }
}
