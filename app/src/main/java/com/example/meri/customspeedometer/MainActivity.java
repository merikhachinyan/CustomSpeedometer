package com.example.meri.customspeedometer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.meri.customspeedometer.view.Speedometer;

public class MainActivity extends AppCompatActivity {

    private Speedometer mSpeedometer;
    private Button mStart;
    private TextView mSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpeedometer = findViewById(R.id.custom_view_main_activity_speedometer);


        mStart = findViewById(R.id.button_main_activity_start);
        mSpeed = findViewById(R.id.text_main_activity_speed);

        mStart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mSpeedometer.speedUp(mSpeed);
                        break;
                    case MotionEvent.ACTION_UP:
                        mSpeedometer.speedDown(mSpeed);
                        break;
                }
                return true;
            }
        });
    }
}
