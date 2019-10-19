package com.example.galleryapp.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.example.galleryapp.Camera.CameraActivity;
import com.example.galleryapp.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ImageView imageView = findViewById(R.id.SplashScreen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public  void run() {
                Intent in=new Intent(SplashScreen.this, CameraActivity.class);
                startActivity(in);
                finish();
            }
        },2000);

        final Animation out = new AlphaAnimation(0.0f,1.0f);
        out.setDuration(2000);
        imageView.bringToFront();
        imageView.startAnimation(out);
        imageView.setVisibility(View.INVISIBLE);

    }
}
