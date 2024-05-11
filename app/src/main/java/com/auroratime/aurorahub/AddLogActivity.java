package com.auroratime.aurorahub;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AddLogActivity extends AppCompatActivity implements View.OnTouchListener{

    private Spinner spinnerActivities;

    private TextView tvDuration, tvStartTime, tvEndTime;
    private Button btSubmit, btBack;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_log);

        //if we come from a deep link, but the user is not logged in - go to the login page
        if(!isLoggedIn(getApplicationContext())){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences("AuroraData", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "none");
        String activitiesJson = sharedPreferences.getString("userActivities", "none");
        boolean isLoggedIn = sharedPreferences.getBoolean("loggedIn", false);

        spinnerActivities = findViewById(R.id.spinnerActivities);
        tvDuration = findViewById(R.id.tvDuration);
        tvStartTime = findViewById(R.id.tvStartTime);
        tvEndTime = findViewById(R.id.tvEndTime);
        btSubmit = findViewById(R.id.submitButton);
        btBack = findViewById(R.id.cancelButton);
        tvStartTime.setOnTouchListener(this);
        tvEndTime.setOnTouchListener(this);

        // Get the activities from the intent
        setupSpinner(activitiesJson);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PostLogTask((String)tvStartTime.getText(), (String)tvEndTime.getText()).execute();
            }
        });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //Deep link handling
        handleIntent();
    }
    @Override
    public boolean onTouch(View v, MotionEvent event){
        if (event.getAction() != MotionEvent.ACTION_UP) return false;
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        switch (v.getId()){
            case R.id.tvStartTime:
                if(tvStartTime.getText() != getText(R.string.click_to_select_start_time)){
                    hour = Integer.parseInt(String.valueOf(tvStartTime.getText()).split(":",2)[0]);
                    minute = Integer.parseInt(String.valueOf(tvStartTime.getText()).split(":",2)[1]);
                }
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddLogActivity.this,
                        new TimePickerDialog.OnTimeSetListener(){
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                                if (minute >= 10) tvStartTime.setText(hourOfDay + ":" + minute);
                                else tvStartTime.setText(hourOfDay + ":0" + minute);
                                calculateAndUpdateDuration();
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
                System.out.println("start");
                break;
            case R.id.tvEndTime:
                if(tvEndTime.getText() != getText(R.string.click_to_select_finish_time)){
                    hour = Integer.parseInt(String.valueOf(tvEndTime.getText()).split(":",2)[0]);
                    minute = Integer.parseInt(String.valueOf(tvEndTime.getText()).split(":",2)[1]);
                }
                timePickerDialog = new TimePickerDialog(AddLogActivity.this,
                        new TimePickerDialog.OnTimeSetListener(){
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                                if(minute >= 10) tvEndTime.setText(hourOfDay + ":" + minute);
                                else tvEndTime.setText(hourOfDay + ":0" + minute);
                                calculateAndUpdateDuration();
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
                System.out.println("end");
                break;
        }
        return true;
    }
    private void calculateAndUpdateDuration() {
        if (!tvStartTime.getText().toString().equals(getString(R.string.click_to_select_start_time))
                && !tvEndTime.getText().toString().equals(getString(R.string.click_to_select_finish_time))) {

            // Splitting the times into hours and minutes
            String[] startTimeSplit = tvStartTime.getText().toString().split(":");
            String[] endTimeSplit = tvEndTime.getText().toString().split(":");
            int startHours = Integer.parseInt(startTimeSplit[0]);
            int startMinutes = Integer.parseInt(startTimeSplit[1]);
            int endHours = Integer.parseInt(endTimeSplit[0]);
            int endMinutes = Integer.parseInt(endTimeSplit[1]);

            // Calculating the duration
            if (endMinutes < startMinutes) {
                endMinutes += 60;
                endHours -= 1;
            }
            int durationHours = endHours - startHours;
            int durationMinutes = endMinutes - startMinutes;

            String output = "";
            if (durationHours > 0) {
                output += durationHours + " hour" + (durationHours > 1 ? "s" : "");
            }
            if (durationMinutes > 0) {
                if (!output.isEmpty()) output += " and ";
                output += durationMinutes + " minute" + (durationMinutes > 1 ? "s" : "");
            }

            // Handling the button enabled state and invalid input
            if (durationHours < 0 || durationMinutes < 0) {
                output = "Invalid time range";
                btSubmit.setEnabled(false);
            } else {
                btSubmit.setEnabled(true);
            }

            tvDuration.setText(output);
        }
    }
    private void setupSpinner(String activitiesJson) {
        try {
            System.out.println("activitiesJson = " + activitiesJson);
            JSONArray activities = new JSONArray(activitiesJson);
            ArrayList<String> activityList = new ArrayList<>();
            for (int i = 0; i < activities.length(); i++) {
                JSONObject activityObject = activities.getJSONObject(i);
                String activityName = activityObject.getString("name");
                activityList.add(activityName);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, activityList);
            spinnerActivities.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class PostLogTask extends AsyncTask<String, Void, String> {
        private String startTime;
        private String endTime;

        JSONObject payload = new JSONObject();

        public PostLogTask(String startTime, String endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        @Override
        protected String doInBackground(String... urls) {
            String urlString = "https://auroratime.org/users/" + userId;
            String resultToDisplay = "";
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                JSONObject activityInstance = new JSONObject();
                activityInstance.put("startTime", TimeConverter.convertToISO8601UTC(startTime));
                activityInstance.put("endTime", TimeConverter.convertToISO8601UTC(endTime));
                activityInstance.put("status", "completed");

// Add the activityInstance to the payload
                payload.put("activityInstance", activityInstance);
                payload.put("name", spinnerActivities.getSelectedItem().toString());

// Write to the output stream
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(payload.toString());
                System.out.println(payload.toString());
                System.out.println("UserID: " + userId);
                writer.flush();
                writer.close();

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"))) {
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line).append('\n');
                        }
                        resultToDisplay = sb.toString();

                    }
                } else {
                    resultToDisplay = "Error " + responseCode;
                }
            } catch (Exception e) {
                e.printStackTrace();
                resultToDisplay = "Error during HTTP request: " + e.getMessage();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return resultToDisplay;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("PostLogTask", "Response received: " + result);
            Toast.makeText(AddLogActivity.this, "Activity Recorded Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    public boolean isLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AuroraData", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("loggedIn", false);
    }
    private void handleIntent() {

        //
        Intent intent = getIntent();
        Uri data = intent.getData();  // This gets the Uri set above

        //extract query parameters from the Uri
        if (data != null) {
            String activityName = data.getQueryParameter("activityName");
            String duration = data.getQueryParameter("duration");

            // Use these values to update your UI or backend
            Log.d("AddLogActivity", "Activity Name: " + activityName + ", Duration: " + duration);
            System.out.println("AddLogActivity: Activity Name: " + activityName + ", Duration: " + duration);
        } else {
            Log.d("AddLogActivity", "No data in intent");
            System.out.println("AddLogActivity: No data in intent");
        }
    }
}