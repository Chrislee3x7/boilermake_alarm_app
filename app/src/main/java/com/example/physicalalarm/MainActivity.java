package com.example.physicalalarm;

import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends Activity {

    private View clockFace;
    private View clockTicksAndNumbers;
    private AtomicBoolean zoomAnimEnded;
    private boolean 

    float y1 = 0;
    float y2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        zoomAnimEnded = new AtomicBoolean(false);
        clockFace = findViewById(R.id.clock_face);
        clockTicksAndNumbers = findViewById(R.id.clock_ticks_numbers);

        clockFace.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int MIN_DISTANCE = 200;
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        y1 = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        y2 = event.getY();
                        float deltaY = y1 - y2;
                        if (deltaY > MIN_DISTANCE)
                        {
                            Toast.makeText(getApplicationContext(), "swipe up", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            // consider as something else - a screen tap for example
                        }
                        break;
                }
                return true;
            }
        });

        ImageView topLeftWedge = findViewById(R.id.top_left_wedge);
        ImageView topRightWedge = findViewById(R.id.top_right_wedge);
        ImageView bottomLeftWedge = findViewById(R.id.bottom_left_wedge);
        ImageView bottomRightWedge = findViewById(R.id.bottom_right_wedge);
//        topLeftWedge.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final int MIN_DISTANCE = 200;
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        y1 = event.getY();
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        y2 = event.getY();
//                        float deltaY = y1 - y2;
//                        if (deltaY > MIN_DISTANCE)
//                        {
//                            Toast.makeText(getApplicationContext(), "swipe up", Toast.LENGTH_SHORT).show();
//                        }
//                        else
//                        {
//                            // consider as something else - a screen tap for example
//                        }
//                        break;
//                }
//                return true;
//            }
//        });

        topLeftWedge.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:

                }
                return false;
            }
        });

//        topLeftWedge.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                zoomAnimEnded.set(false);
//                final SpringAnimation rotate = new SpringAnimation(clockFace,
//                        DynamicAnimation.ROTATION);
//                SpringForce springForce = new SpringForce();
//                springForce.setStiffness(100f);
//                springForce.setDampingRatio(1);
//                rotate.setSpring(springForce);
//                rotate.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
//                    @Override
//                    public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
//                        animatePanDown();
//                        animateZoomIn();
//                    }
//                });
//                rotate.animateToFinalPosition(45f);
//                Toast.makeText(getApplicationContext(), "9-12 quadrant pressed", Toast.LENGTH_SHORT).show();
//            }
//        });
        topRightWedge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "12-3 quadrant pressed", Toast.LENGTH_SHORT).show();
            }
        });
        bottomLeftWedge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "6-9 quadrant pressed", Toast.LENGTH_SHORT).show();
            }
        });
        bottomRightWedge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "3-6 quadrant pressed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int MIN_DISTANCE = 200;
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                y2 = event.getY();
                float deltaY = y1 - y2;
                if (deltaY > MIN_DISTANCE)
                {
                    Toast.makeText(getApplicationContext(), "swipe up", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void animateZoomIn() {
        clockTicksAndNumbers.setVisibility(View.GONE);
        final SpringAnimation zoomToTopAnimX = new SpringAnimation(clockFace, DynamicAnimation.SCALE_X);
        final SpringAnimation zoomToTopAnimY = new SpringAnimation(clockFace, DynamicAnimation.SCALE_Y);
        SpringForce springForce = new SpringForce();
        springForce.setStiffness(0.1f);
        springForce.setDampingRatio(0.9f);
        zoomToTopAnimX.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                zoomAnimEnded.set(true);
            }
        });
        zoomToTopAnimY.animateToFinalPosition(4f);
        zoomToTopAnimX.animateToFinalPosition(4f);
    }

    private void animatePanDown() {
        final SpringAnimation panDownAnimY  = new SpringAnimation(clockFace, DynamicAnimation.TRANSLATION_Y);
        SpringForce springForce = new SpringForce();
        springForce.setStiffness(25f);
        springForce.setDampingRatio(1);
        panDownAnimY.setSpring(springForce);
        panDownAnimY.animateToFinalPosition(1500f);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}