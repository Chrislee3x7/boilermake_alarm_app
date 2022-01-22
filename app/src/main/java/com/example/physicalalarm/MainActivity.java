package com.example.physicalalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button topLeftWedge = findViewById(R.id.top_left_wedge);
        Button topRightWedge = findViewById(R.id.top_right_wedge);
        Button bottomLeftWedge = findViewById(R.id.bottom_left_wedge);
        Button bottomRightWedge = findViewById(R.id.bottom_right_wedge);
        topLeftWedge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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