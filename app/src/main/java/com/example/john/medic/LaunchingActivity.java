package com.example.john.medic;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LaunchingActivity extends AppCompatActivity {
    ImageView imageView;
    private static int SPLASH_TIME_OUT=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launching);
        imageView=findViewById(R.id.ivProfile);
        Animation fade= AnimationUtils.loadAnimation(this,R.anim.anim);
        imageView.setAnimation(fade);
        new Handler().postDelayed(new Runnable() {

    /*
     * Showing splash screen with a timer. This will be useful when you
     * want to show case your app logo / company
     */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(LaunchingActivity.this,MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
