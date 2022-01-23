package com.example.physicalalarm;

import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import android.app.Fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends Activity {

    private View clockFace;
    private View clockTicksAndNumbers;

    //Fragment vars
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    private Button goToScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clockFace = findViewById(R.id.clock_face);
        ImageView topLeftWedge = findViewById(R.id.top_left_wedge);
        ImageView topRightWedge = findViewById(R.id.top_right_wedge);
        ImageView bottomLeftWedge = findViewById(R.id.bottom_left_wedge);
        ImageView bottomRightWedge = findViewById(R.id.bottom_right_wedge);
        goToScreen = findViewById(R.id.button1);

        goToScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleChangeFragment(view);
            }
        });


        topLeftWedge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SpringAnimation rotate = new SpringAnimation(clockFace,
                        DynamicAnimation.ROTATION);
                SpringForce springForce = new SpringForce();
                springForce.setStiffness(100f);
                springForce.setDampingRatio(1);
                rotate.setSpring(springForce);
                rotate.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                        animateZoomIn();
                        animatePanDown();
                    }
                });
                rotate.animateToFinalPosition(45f);
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

        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);

                int hourAngle = 90-(hour/12)*360;
                int minuteAngle = 90-(minute/60)*360;

                // TODO set the angle of the two images to this

                someHandler.postDelayed(this, 1000);
            }
        }, 10);
    }

    //Changing fragments (might want to delete later)
    public void handleChangeFragment(View view) {
        Fragment selectedFragment = new RingingScreenFragment();
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main, selectedFragment);
        goToScreen.setVisibility(View.GONE);
        fragmentTransaction.commit();
    }

    private void animateZoomIn() {
        final SpringAnimation zoomToTopAnimX = new SpringAnimation(clockFace, DynamicAnimation.SCALE_X);
        final SpringAnimation zoomToTopAnimY = new SpringAnimation(clockFace, DynamicAnimation.SCALE_Y);
        SpringForce springForce = new SpringForce();
        springForce.setStiffness(50f);
        springForce.setDampingRatio(1);
        zoomToTopAnimY.animateToFinalPosition(4f);
        zoomToTopAnimX.animateToFinalPosition(4f);
    }

    private void animatePanDown() {
        final SpringAnimation panDownAnimY  = new SpringAnimation(clockFace, DynamicAnimation.TRANSLATION_Y);
        SpringForce springForce = new SpringForce();
        springForce.setStiffness(100f);
        springForce.setDampingRatio(1);
        panDownAnimY.setSpring(springForce);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}