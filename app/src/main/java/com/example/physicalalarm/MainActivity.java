package com.example.physicalalarm;

import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import android.app.Fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends Activity {

    private View clockFace;
    private View clockPartsGroup;
    private View minuteHand;
    private View hourHand;
    private View addAlarmButton;
    private GridLayout displayedAlarms;

    private AtomicBoolean zoomAnimEnded;
    private boolean clockExpanded;

    private Animation fadeIn;
    private Animation fadeOut;

    float y1 = 0;
    float y2 = 0;

    //Fragment vars
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    private Button goToScreen;

    private AlarmTimeManager alarmTimeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        zoomAnimEnded = new AtomicBoolean(true);
        clockExpanded = false;

        clockFace = findViewById(R.id.clock_face);
        clockPartsGroup = findViewById(R.id.clock_parts);
        // view group to fade out when zooming in

        hourHand = findViewById(R.id.hour_hand);
        minuteHand = findViewById(R.id.minute_hand);
        addAlarmButton = findViewById(R.id.add_alarm_button); 
        displayedAlarms = findViewById(R.id.displayed_alarms);

        alarmTimeManager = new AlarmTimeManager(getApplicationContext());
        alarmTimeManager.addAlarmTime(new AlarmTime(22, 20, false));
        alarmTimeManager.addAlarmTime(new AlarmTime(22, 10, false));
        alarmTimeManager.addAlarmTime(new AlarmTime(22, 30, false));
        alarmTimeManager.addAlarmTime(new AlarmTime(22, 40, false));


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
                            //Toast.makeText(getApplicationContext(), "swipe up", Toast.LENGTH_SHORT).show();
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

        addAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);
        fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setDuration(500);

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
                ArrayList<AlarmTime> alarmTimes = loadInAlarms(3);

                SpringForce springForce = new SpringForce();
                springForce.setStiffness(100f);
                springForce.setDampingRatio(1);
                rotate.setSpring(springForce);
                if (!clockExpanded && zoomAnimEnded.get()) {
                    Toast.makeText(getApplicationContext(), "9-12 quadrant pressed", Toast.LENGTH_SHORT).show();
                    rotate.animateToFinalPosition(45f);
                    animatePanDown();
                    animateZoomIn();
                    clockExpanded = true;
                } else if (zoomAnimEnded.get()) {
                    Toast.makeText(getApplicationContext(), "tapped after expanded", Toast.LENGTH_SHORT).show();
                    animateZoomOut();
                    animatePanUp();
                    rotate.animateToFinalPosition(0f);
                    clockExpanded = false;
                }
            }
        });
        topRightWedge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SpringAnimation rotate = new SpringAnimation(clockFace,
                        DynamicAnimation.ROTATION);
                loadInAlarms(0);
                SpringForce springForce = new SpringForce();
                springForce.setStiffness(100f);
                springForce.setDampingRatio(1);
                rotate.setSpring(springForce);
                if (!clockExpanded && zoomAnimEnded.get()) {
                    Toast.makeText(getApplicationContext(), "12-3 quadrant pressed", Toast.LENGTH_SHORT).show();
                    rotate.animateToFinalPosition(-45f);
                    animatePanDown();
                    animateZoomIn();
                    clockExpanded = true;
                } else if (zoomAnimEnded.get()) {
                    Toast.makeText(getApplicationContext(), "tapped after expanded", Toast.LENGTH_SHORT).show();
                    animateZoomOut();
                    animatePanUp();
                    rotate.animateToFinalPosition(0f);
                    clockExpanded = false;
                }
            }
        });
        bottomLeftWedge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SpringAnimation rotate = new SpringAnimation(clockFace,
                        DynamicAnimation.ROTATION);
                loadInAlarms(2);
                SpringForce springForce = new SpringForce();
                springForce.setStiffness(100f);
                springForce.setDampingRatio(1);
                rotate.setSpring(springForce);
                if (!clockExpanded && zoomAnimEnded.get()) {
                    Toast.makeText(getApplicationContext(), "6-9 quadrant pressed", Toast.LENGTH_SHORT).show();
                    rotate.animateToFinalPosition(135f);
                    animatePanDown();
                    animateZoomIn();
                    clockExpanded = true;
                } else if (zoomAnimEnded.get()) {
                    Toast.makeText(getApplicationContext(), "tapped after expanded", Toast.LENGTH_SHORT).show();
                    animateZoomOut();
                    animatePanUp();
                    rotate.animateToFinalPosition(0f);
                    clockExpanded = false;
                }
            }
        });
        bottomRightWedge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SpringAnimation rotate = new SpringAnimation(clockFace,
                        DynamicAnimation.ROTATION);
                loadInAlarms(2);
                SpringForce springForce = new SpringForce();
                springForce.setStiffness(100f);
                springForce.setDampingRatio(1);
                rotate.setSpring(springForce);
                if (!clockExpanded && zoomAnimEnded.get()) {
                    Toast.makeText(getApplicationContext(), "3-6 quadrant pressed", Toast.LENGTH_SHORT).show();
                    rotate.animateToFinalPosition(-135f);
                    animatePanDown();
                    animateZoomIn();
                    clockExpanded = true;
                } else if (zoomAnimEnded.get()) {
                    Toast.makeText(getApplicationContext(), "tapped after expanded", Toast.LENGTH_SHORT).show();
                    animateZoomOut();
                    animatePanUp();
                    rotate.animateToFinalPosition(0f);
                    clockExpanded = false;
                }
            }
        });

        final SpringAnimation rotateHourHand = new SpringAnimation(hourHand,
                DynamicAnimation.ROTATION);
        final SpringAnimation rotateMinuteHand = new SpringAnimation(minuteHand,
                DynamicAnimation.ROTATION);

        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                float hour = calendar.get(Calendar.HOUR);
                float minute = calendar.get(Calendar.MINUTE);

                float hourAngle = ((hour / 12) * 360) % 360 - 90;
                float minuteAngle = ((minute / 60) * 360) % 360 - 90;

                rotateHourHand.animateToFinalPosition(hourAngle);
                rotateMinuteHand.animateToFinalPosition(minuteAngle);

                someHandler.postDelayed(this, 1000);
            }
        }, 10);
    }

    private ArrayList<AlarmTime> loadInAlarms(int quadrantNumber) {
        switch (quadrantNumber) {
            case 0:
                return alarmTimeManager.getAlarmTimes12to3();
            case 1:
                return alarmTimeManager.getAlarmTimes3to6();
            case 2:
                return alarmTimeManager.getAlarmTimes6to9();
            case 3:
                return alarmTimeManager.getAlarmTimes9to12();
        }
    }

    //Changing fragments (might want to delete later)
    public void handleChangeFragment(View view) {
        Fragment selectedFragment = new NumberPickerFragment();
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main, selectedFragment);
        goToScreen.setVisibility(View.GONE);
        clockFace.setVisibility(View.GONE);
        fragmentTransaction.commit();
    }

    private void animateZoomOut() {
        zoomAnimEnded.set(false);

        clockPartsGroup.startAnimation(fadeIn);
        addAlarmButton.startAnimation(fadeIn);
        clockPartsGroup.setVisibility(View.VISIBLE);
        addAlarmButton.setVisibility(View.VISIBLE);

        final SpringAnimation zoomToTopAnimX = new SpringAnimation(clockFace, DynamicAnimation.SCALE_X);
        final SpringAnimation zoomToTopAnimY = new SpringAnimation(clockFace, DynamicAnimation.SCALE_Y);
        SpringForce springForce = new SpringForce();
        springForce.setStiffness(2f);
        springForce.setDampingRatio(1f);
        zoomToTopAnimX.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                zoomAnimEnded.set(true);
            }
        });
        zoomToTopAnimY.animateToFinalPosition(1f);
        zoomToTopAnimX.animateToFinalPosition(1f);
    }

    private void animateZoomIn() {
        zoomAnimEnded.set(false);
        clockPartsGroup.startAnimation(fadeOut);
        addAlarmButton.startAnimation(fadeIn);
        clockPartsGroup.setVisibility(View.GONE);
        addAlarmButton.setVisibility(View.GONE);
        final SpringAnimation zoomToTopAnimX = new SpringAnimation(clockFace, DynamicAnimation.SCALE_X);
        final SpringAnimation zoomToTopAnimY = new SpringAnimation(clockFace, DynamicAnimation.SCALE_Y);
        SpringForce springForce = new SpringForce();
        springForce.setStiffness(2f);
        springForce.setDampingRatio(1f);
        zoomToTopAnimX.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                zoomAnimEnded.set(true);
            }
        });
        zoomToTopAnimY.animateToFinalPosition(4f);
        zoomToTopAnimX.animateToFinalPosition(4f);
    }

    private void animatePanUp() {
        final SpringAnimation panDownAnimY  = new SpringAnimation(clockFace, DynamicAnimation.TRANSLATION_Y);
        SpringForce springForce = new SpringForce();
        springForce.setStiffness(25f);
        springForce.setDampingRatio(1);
        panDownAnimY.setSpring(springForce);
        panDownAnimY.animateToFinalPosition(0f);
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