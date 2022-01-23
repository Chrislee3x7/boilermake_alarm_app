package com.example.physicalalarm;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import android.app.AlarmManager;
import android.app.Fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

//import com.example.physicalalarm.databinding.ActivityMainBinding;

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
    private View alarmsScrollView;
    private RelativeLayout alarmIndicators;

    private AtomicBoolean zoomAnimEnded;
    private boolean clockExpanded;

    private Animation fadeIn;
    private Animation fadeOut;

    float y1 = 0;
    float y2 = 0;

    //Fragment vars
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    private ImageButton goToScreen;

    //private ActivityMainBinding binding;
    private AlarmTimeManager alarmTimeManager;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Calendar calendar;

    public static AlarmTime selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intent intent = getIntent();
        //Ringtone r = (Ringtone) intent.getSerializableExtra("Ringing");
        if (SoundPlayer.isPlaying) {
            Toast.makeText(getApplicationContext(), "Ringing intent found", Toast.LENGTH_SHORT).show();
            Fragment selectedFragment = new RingingScreenFragment();
            fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.main, selectedFragment);
            fragmentTransaction.commit();
        }
        zoomAnimEnded = new AtomicBoolean(true);
        clockExpanded = false;

        clockFace = findViewById(R.id.clock_face);
        clockPartsGroup = findViewById(R.id.clock_parts);
        // view group to fade out when zooming in

        hourHand = findViewById(R.id.hour_hand);
        minuteHand = findViewById(R.id.minute_hand);
        addAlarmButton = findViewById(R.id.add_alarm_button);
        displayedAlarms = findViewById(R.id.displayed_alarms);

        alarmsScrollView = findViewById(R.id.alarms_scroll_view);
        alarmIndicators = findViewById(R.id.alarm_indicators);

        alarmTimeManager = new AlarmTimeManager(getApplicationContext());


        alarmTimeManager.addAlarmTime(new AlarmTime(22, 12, false));

        createNotificationChannel();

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
                        if (deltaY > MIN_DISTANCE) {
                            //Toast.makeText(getApplicationContext(), "swipe up", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            // consider as something else - a screen tap for example
                        }
                        break;
                }
                return true;
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

        goToScreen = findViewById(R.id.add_alarm_button);

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
                loadInAlarms(3);
                SpringForce springForce = new SpringForce();
                springForce.setStiffness(100f);
                springForce.setDampingRatio(1);
                rotate.setSpring(springForce);
                if (!clockExpanded && zoomAnimEnded.get()) {
//                    Toast.makeText(getApplicationContext(), "9-12 quadrant pressed", Toast.LENGTH_SHORT).show();
                    rotate.animateToFinalPosition(45f);
                    animatePanDown();
                    animateZoomIn();
                    clockExpanded = true;
                } else if (zoomAnimEnded.get()) {
//                    Toast.makeText(getApplicationContext(), "tapped after expanded", Toast.LENGTH_SHORT).show();
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
//                    Toast.makeText(getApplicationContext(), "12-3 quadrant pressed", Toast.LENGTH_SHORT).show();
                    rotate.animateToFinalPosition(-45f);
                    animatePanDown();
                    animateZoomIn();
                    clockExpanded = true;
                } else if (zoomAnimEnded.get()) {
//                    Toast.makeText(getApplicationContext(), "tapped after expanded", Toast.LENGTH_SHORT).show();
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
//                    Toast.makeText(getApplicationContext(), "6-9 quadrant pressed", Toast.LENGTH_SHORT).show();
                    rotate.animateToFinalPosition(135f);
                    animatePanDown();
                    animateZoomIn();
                    clockExpanded = true;
                } else if (zoomAnimEnded.get()) {
//                    Toast.makeText(getApplicationContext(), "tapped after expanded", Toast.LENGTH_SHORT).show();
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
                loadInAlarms(1);
                SpringForce springForce = new SpringForce();
                springForce.setStiffness(100f);
                springForce.setDampingRatio(1);
                rotate.setSpring(springForce);
                if (!clockExpanded && zoomAnimEnded.get()) {
//                    Toast.makeText(getApplicationContext(), "3-6 quadrant pressed", Toast.LENGTH_SHORT).show();
                    rotate.animateToFinalPosition(-135f);
                    animatePanDown();
                    animateZoomIn();
                    clockExpanded = true;
                } else if (zoomAnimEnded.get()) {
//                    Toast.makeText(getApplicationContext(), "tapped after expanded", Toast.LENGTH_SHORT).show();
                    animateZoomOut();
                    animatePanUp();
                    rotate.animateToFinalPosition(0f);
                    clockExpanded = false;
                }
            }
        });

        loadInAlarmIndicators();

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

    private void loadInAlarms(int quadrantNumber) {
        displayedAlarms.removeAllViews();
        ArrayList<AlarmTime> alarmTimes;
        switch (quadrantNumber) {
            case 0:
                alarmTimes = alarmTimeManager.getAlarmTimes12to3();
                break;
            case 1:
                alarmTimes = alarmTimeManager.getAlarmTimes3to6();
                break;
            case 2:
                alarmTimes = alarmTimeManager.getAlarmTimes6to9();
                break;
            case 3:
                alarmTimes = alarmTimeManager.getAlarmTimes9to12();
                break;
            default:
                alarmTimes = null;
        }
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        for (AlarmTime alarmTime : alarmTimes) {
            Button newAlarm = new Button(getApplicationContext());
            newAlarm.setText(alarmTime.toString());
            newAlarm.setTextColor(Color.WHITE);
            newAlarm.setTextSize(20f);
            newAlarm.setWidth((int) (145 * scale));
            if(alarmTime.isOn()){
                newAlarm.setBackground(getDrawable(R.drawable.rounded_rectangle_gray));
            }
            else{
                newAlarm.setBackground(getDrawable(R.drawable.rounded_rectangle));
            }
            newAlarm.setHeight((int) (100 * scale));
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins((int) (10 * scale), (int) (10 * scale),
                    (int) (10 * scale), (int) (10 * scale));
            newAlarm.setLayoutParams(params);
            newAlarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlarmTime a = alarmTimeManager.getAlarmViaString((String) newAlarm.getText());
                    a.setOn(!a.isOn());
                    if (a.isOn()) {
                        calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, a.getHour());
                        calendar.set(Calendar.MINUTE, a.getMinute());
                        calendar.set(Calendar.SECOND,0);
                        calendar.set(Calendar.MILLISECOND,0);
                        setAlarm();
                        newAlarm.setBackground(getDrawable(R.drawable.rounded_rectangle_gray));
                    } else {
                        newAlarm.setBackground(getDrawable(R.drawable.rounded_rectangle));
                    }
//                    Toast.makeText(getApplicationContext(), "clicked1", Toast.LENGTH_SHORT).show();
                }
            });
            newAlarm.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view){
                    AlarmTime a = alarmTimeManager.getAlarmViaString((String) newAlarm.getText());
                    setSelectedTime(a);
                    Fragment selectedFragment = new alarm_screen();
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.main, selectedFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;
                }
            });
            displayedAlarms.addView(newAlarm);
        }
    }


    public void loadInAlarmIndicators() {
        ArrayList<AlarmTime> alarmTimes = alarmTimeManager.getAlarmTimes();
        alarmIndicators.removeAllViews();
        int index = 0;
        for (AlarmTime a : alarmTimes) {
            ImageView iv = new ImageView(getApplicationContext());
            iv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT));
            iv.setBackground(getDrawable(R.drawable.ic_alarmtimeindicator));
            alarmIndicators.addView(iv);

            float hour = a.getHour();
            float minute = a.getMinute();

            float angle = (((hour % 12) * 60 + minute) / 720 * 360) % 360 - 90;
            iv.setRotation(angle);
        }
    }
    public static void setSelectedTime(AlarmTime alarmTime) {
        selectedTime = alarmTime;
    }

    //Changing fragments (might want to delete later)
    public void handleChangeFragment(View view) {
        Fragment selectedFragment = new NumberPickerFragment();
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main, selectedFragment);
        fragmentTransaction.addToBackStack(null);
//        goToScreen.setVisibility(View.GONE);
//        clockFace.setVisibility(View.GONE);
        fragmentTransaction.commit();
    }

    private void animateZoomOut() {
        zoomAnimEnded.set(false);

        clockPartsGroup.startAnimation(fadeIn);
        addAlarmButton.startAnimation(fadeIn);
        clockPartsGroup.setVisibility(View.VISIBLE);
        addAlarmButton.setVisibility(View.VISIBLE);
        alarmsScrollView.startAnimation(fadeOut);
        alarmsScrollView.setVisibility(View.GONE);
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
        addAlarmButton.startAnimation(fadeOut);
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
                alarmsScrollView.startAnimation(fadeIn);
                alarmsScrollView.setVisibility(View.VISIBLE);
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

    public AlarmTimeManager getAlarmTimeManager() {
        return alarmTimeManager;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setAlarm() {

        alarmManager = (AlarmManager) getSystemService(getApplicationContext().ALARM_SERVICE);

        Intent intent = new Intent(this,AlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(this, 0,intent,0);

        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(this, "Alarm Set Successfully", Toast.LENGTH_SHORT).show();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "SHAKEAWAKE";
            String description = "It's time to rise and shine!";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("SHAKEAWAKE",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }
}