package com.auroratime.aurorahub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the status bar and bottom navigation bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().getDecorView().getWindowInsetsController().hide(
                    android.view.WindowInsets.Type.statusBars() | android.view.WindowInsets.Type.navigationBars());
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
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
        Button start = findViewById(R.id.start_btn);
        ImageButton activity1 = findViewById(R.id.activity1);
        ImageButton activity2 = findViewById(R.id.activity2);
        ImageButton activity3 = findViewById(R.id.activity3);
        ImageButton activity4 = findViewById(R.id.activity4);
        final int[] activity = {0};
        ArrayList<ImageButton> actArray = new ArrayList<ImageButton>();
        actArray.add(activity1);
        actArray.add(activity2);
        actArray.add(activity3);
        actArray.add(activity4);
        class actButton {
            private int number;
            actButton(int num) {
                number = num;
            }

            public void click() {
                ImageButton act = actArray.remove(number);
                if (act.getImageTintList() == ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.gray))) {
                    act.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.light_gray)));
                    actArray.add(number, act);
                } else {
                    act.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.gray)));
                    for(ImageButton i : actArray) {
                        i.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.light_gray)));
                    }
                    actArray.add(number, act);
                }
                activity[0] = number + 1;
            }
            public void start() {

            }

            public void stop() {

            }
        }
        try {
            String[] names = actNames(activities);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        TextView test = findViewById(R.id.textView4);
        test.setText(activities);

        activity1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lights up the corresponding activity icon while dimming the others
                actButton a1 = new actButton(0);
                a1.click();
            }
        });

        activity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lights up the corresponding activity icon while dimming the others
                actButton a2 = new actButton(1);
                a2.click();
            }
        });

        activity3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lights up the corresponding activity icon while dimming the others
                actButton a3 = new actButton(2);
                a3.click();
            }
        });

        activity4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lights up the corresponding activity icon while dimming the others
                actButton a4 = new actButton(3);
                a4.click();
            }
        });


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

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Arrays.asList(activity1.getImageTintList(), activity2.getImageTintList(), activity3.getImageTintList(), activity4.getImageTintList())
                        .contains(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.gray)))) {
                    Snackbar.make(findViewById(R.id.start_btn), "Please select an activity", Snackbar.LENGTH_SHORT).show();
                } else {
                    String activityName = "";
                    try {
                        String[] names = actNames(activities);
                        activityName = names[activity[0] - 1];
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(start.getText().toString().equals("Start Activity")) {
                        start.setText("Stop Activity");

                        new ActivityTask("start", activityName, userId).execute();

                        // Existing code for starting the activity
                        Animation fadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
                        ObjectAnimator right = ObjectAnimator.ofFloat(activity1, "translationX", 220f);
                        ObjectAnimator left = ObjectAnimator.ofFloat(activity1, "translationX", -220f);
                        ObjectAnimator up = ObjectAnimator.ofFloat(activity1, "translationY", -220f);
                        ObjectAnimator down = ObjectAnimator.ofFloat(activity1, "translationY", 220f);
                        right.setDuration(1000);
                        left.setDuration(1000);
                        up.setDuration(1000);
                        down.setDuration(1000);
                        switch(activity[0]) {
                            case 1:
                                right.setTarget(activity1);
                                down.setTarget(activity1);
                                AnimatorSet a1 = new AnimatorSet();
                                a1.play(right).with(down);
                                a1.start();
                                activity2.startAnimation(fadeout);
                                activity3.startAnimation(fadeout);
                                activity4.startAnimation(fadeout);
                                activity1.setClickable(false);
                                activity2.setClickable(false);
                                activity3.setClickable(false);
                                activity4.setClickable(false);
                                break;
                            case 2:
                                left.setTarget(activity2);
                                down.setTarget(activity2);
                                AnimatorSet a2 = new AnimatorSet();
                                a2.play(left).with(down);
                                a2.start();
                                activity1.startAnimation(fadeout);
                                activity3.startAnimation(fadeout);
                                activity4.startAnimation(fadeout);
                                activity1.setClickable(false);
                                activity2.setClickable(false);
                                activity3.setClickable(false);
                                activity4.setClickable(false);
                                break;
                            case 3:
                                right.setTarget(activity3);
                                up.setTarget(activity3);
                                AnimatorSet a3 = new AnimatorSet();
                                a3.play(right).with(up);
                                a3.start();
                                activity1.startAnimation(fadeout);
                                activity2.startAnimation(fadeout);
                                activity4.startAnimation(fadeout);
                                activity1.setClickable(false);
                                activity2.setClickable(false);
                                activity3.setClickable(false);
                                activity4.setClickable(false);
                                break;
                            case 4:
                                left.setTarget(activity4);
                                up.setTarget(activity4);
                                AnimatorSet a4 = new AnimatorSet();
                                a4.play(left).with(up);
                                a4.start();
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
                        start.setText("Start Activity");

                        new ActivityTask("stop", activityName, userId).execute();

                        // Existing code for stopping the activity
                        Animation fadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
                        Animation fadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                        ObjectAnimator right = ObjectAnimator.ofFloat(activity1, "translationX", 5f);
                        ObjectAnimator left = ObjectAnimator.ofFloat(activity1, "translationX", -5f);
                        ObjectAnimator up = ObjectAnimator.ofFloat(activity1, "translationY", -5f);
                        ObjectAnimator down = ObjectAnimator.ofFloat(activity1, "translationY", 5f);
                        right.setDuration(1000);
                        left.setDuration(1000);
                        up.setDuration(1000);
                        down.setDuration(1000);
                        switch(activity[0]) {
                            case 1:
                                left.setTarget(activity1);
                                up.setTarget(activity1);
                                AnimatorSet a1 = new AnimatorSet();
                                a1.play(left).with(up);
                                a1.start();
                                activity2.startAnimation(fadein);
                                activity3.startAnimation(fadein);
                                activity4.startAnimation(fadein);
                                activity1.setClickable(true);
                                activity2.setClickable(true);
                                activity3.setClickable(true);
                                activity4.setClickable(true);
                                break;
                            case 2:
                                right.setTarget(activity2);
                                up.setTarget(activity2);
                                AnimatorSet a2 = new AnimatorSet();
                                a2.play(right).with(up);
                                a2.start();
                                activity1.startAnimation(fadein);
                                activity3.startAnimation(fadein);
                                activity4.startAnimation(fadein);
                                activity1.setClickable(true);
                                activity2.setClickable(true);
                                activity3.setClickable(true);
                                activity4.setClickable(true);
                                break;
                            case 3:
                                left.setTarget(activity3);
                                down.setTarget(activity3);
                                AnimatorSet a3 = new AnimatorSet();
                                a3.play(left).with(down);
                                a3.start();
                                activity1.startAnimation(fadein);
                                activity2.startAnimation(fadein);
                                activity4.startAnimation(fadein);
                                activity1.setClickable(true);
                                activity2.setClickable(true);
                                activity3.setClickable(true);
                                activity4.setClickable(true);
                                break;
                            case 4:
                                right.setTarget(activity4);
                                down.setTarget(activity4);
                                AnimatorSet a4 = new AnimatorSet();
                                a4.play(right).with(down);
                                a4.start();
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
    }

    private String[] actNames(String json) throws JSONException {
        JSONArray array = new JSONArray(json);
        String[] activities = new String[array.length()];
        for(int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            activities[i] = object.getString("name");
        }

        return activities;
    }

    public void startActivity(String username, String password, String activity) {

    }

    public class ActivityTask extends AsyncTask<String, Void, String> {

        private String operationType; // "start" or "stop"
        private String activityName;
        private String userId;

        public ActivityTask(String operationType, String activityName, String userId) {
            this.operationType = operationType;
            this.activityName = activityName;
            this.userId = userId;
        }

        @Override
        protected String doInBackground(String... params) {
            String baseUrl = "https://auroratime.org/users/" + userId + "/";
            String endpoint = operationType.equals("start") ? "start-activity" : "end-activity";
            String urlString = baseUrl + endpoint;

            try {
                JSONObject json = new JSONObject();
                json.put("activityName", activityName);

                return NetworkUtils.post(urlString, json.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Handle the result of the network operation if needed
        }
    }
}
