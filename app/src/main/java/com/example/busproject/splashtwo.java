package com.example.busproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import pl.droidsonroids.gif.GifImageView;

public class splashtwo extends AppCompatActivity {

    TextView lettera,lettera2;
    GifImageView claw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashtwo);

        lettera=findViewById(R.id.a);
        lettera2=findViewById(R.id.a2);
        claw=findViewById(R.id.claw);


        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation anim1 = AnimationUtils.loadAnimation(splashtwo.this,R.anim.animlesh1);
                lettera.startAnimation(anim1);

                final Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation anim2 = AnimationUtils.loadAnimation(splashtwo.this,R.anim.animlesh2);
                        lettera.startAnimation(anim2);

                        final Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Animation anim3 = AnimationUtils.loadAnimation(splashtwo.this,R.anim.animlesh3);
                                lettera.startAnimation(anim3);
                                lettera.setVisibility(View.INVISIBLE);
                                final Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        lettera2.setVisibility(View.VISIBLE);
                                    }
                                }, 300);
                            }
                        }, 480);
                    }
                }, 220);
            }
        }, 450);





    }
}