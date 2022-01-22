package com.example.physicalalarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View clockFace = findViewById(R.id.clock_wedges);
        ImageView topLeftWedge = findViewById(R.id.top_left_wedge);
        ImageView topRightWedge = findViewById(R.id.top_right_wedge);
        ImageView bottomLeftWedge = findViewById(R.id.bottom_left_wedge);
        ImageView bottomRightWedge = findViewById(R.id.bottom_right_wedge);
        final SpringAnimation anim1X = new SpringAnimation(clockFace,
                DynamicAnimation.ROTATION);
        topLeftWedge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim1X.animateToFinalPosition(45f);
                Toast.makeText(getApplicationContext(), "9-12 quadrant pressed", Toast.LENGTH_SHORT).show();
            }
        });
        topRightWedge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "12-3 quadrant pressed", Toast.LENGTH_SHORT).show();
            }
        });
        bottomLeftWedge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "6-9 quadrant pressed", Toast.LENGTH_SHORT).show();
            }
        });
        bottomRightWedge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "3-6 quadrant pressed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}