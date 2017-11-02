package com.example.phamh.mtracking;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;

public class MainActivity extends AppCompatActivity {

    private IMU imu;
    private GLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //imu = new IMU(this,null);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mGLView = new MyGLSurfaceView(surfaceView.getContext());
        setContentView(mGLView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
        //imu.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //imu.stop();
        mGLView.onPause();
    }

}
