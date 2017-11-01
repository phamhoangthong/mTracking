package com.example.phamh.mtracking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private IMU imu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imu = new IMU(this,null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        imu.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        imu.stop();
    }
}
