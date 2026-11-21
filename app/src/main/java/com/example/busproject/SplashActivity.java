package com.example.busproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.window.SplashScreen;


import static com.example.busproject.LoginActivity.SHARED_PREFERENCE;

public class SplashActivity extends AppCompatActivity {
    TextView tvsplash;
    ImageView imbus,impin;
    View decorview;

    private static int Splash_Timeout=4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_App_SplashScreen);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tvsplash=findViewById(R.id.splashtv);
        imbus=findViewById(R.id.splashbus);
        impin=findViewById(R.id.splashpin);


        decorview = getWindow().getDecorView();
        decorview.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                if(i==0){
                    decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp =getSharedPreferences(SHARED_PREFERENCE,MODE_PRIVATE);
                boolean hasloggedin =sp.getBoolean("loggedin",false);
                String username =sp.getString("username","");
                String email =sp.getString("email","");
                Log.d("Email",username);

                if(!username.isEmpty()){
                    User.setUserName(username);
                    User.setEmail(email);
                }

                if(hasloggedin){
                    Intent ToLogin =new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(ToLogin);
                    finish();
                }
                else{
                    Intent ToLogin =new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(ToLogin);
                    finish();
                }
            }
        },Splash_Timeout);

        Animation anim1 = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.animation1);
        imbus.startAnimation(anim1);

        Animation anim2 = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.animation2);
        impin.startAnimation(anim2);

        Animation anim3 = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.animation3);
        tvsplash.startAnimation(anim3);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus){
            decorview.setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
