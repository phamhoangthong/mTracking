package com.example.phamh.mtracking;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private IMU imu;
    private MyGLView mDisplay;
    private MyGLSurfaceView mGLView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //LinearLayout layout = (LinearLayout)findViewById(R.id.OpenGLES);
        //mDisplay = new MyGLView(this, layout);
        //imu = new IMU(this,null);
        //SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mDisplay.onResume();
        mGLView.onResume();
        //imu.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mDisplay.onPause();
        //imu.stop();
        mGLView.onPause();
    }
}
