package com.example.phamh.mtracking;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    private IMU imu;
    private MyGLView mDisplay;
    private MyGLSurfaceView mGLView;

    Handler mHandler;
    Handler mPlotHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLView = new MyGLSurfaceView(this);
        mPlotHandler = mGLView.getHandler();
        setContentView(mGLView);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message inputMessage) {
                if(inputMessage.obj.getClass().equals(MyDataTranfer.class)) {
                    MyDataTranfer myDataTranfer = (MyDataTranfer)inputMessage.obj;
                    if(myDataTranfer.type == IMU.IMU_DATA_ACC) {
                        Double[] mTemp = (Double[])myDataTranfer.data;
                        MyDataTranfer mDataTranfer = new MyDataTranfer();
                        mDataTranfer.type = 1;
                        mDataTranfer.data = mTemp[0];
                        Message mgs = mPlotHandler.obtainMessage();
                        mgs.obj = mDataTranfer;
                        mPlotHandler.sendMessage(mgs);
                    }
                }
            }
        };
        imu = new IMU(this, mHandler);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mDisplay.onResume();
        mGLView.onResume();
        imu.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mDisplay.onPause();
        mGLView.onPause();
        imu.stop();
    }
}
