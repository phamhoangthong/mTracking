package com.example.phamh.mtracking;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    private IMU imu;
    private MyGLView mDisplay;
    private MyGLSurfaceView mGLView;

    private Handler mHandler;
    private Handler mPlotHandler;

    public MyStore myStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.i("MY_DEBUG","Begin program");
        myStore = new MyStore();
        myStore.add("RAW_ACC_X", 100);
        myStore.add("RAW_ACC_Y", 100);
        myStore.add("RAW_ACC_Z", 100);
        myStore.add("RAW_COMP_X", 100);
        myStore.add("RAW_COMP_Y", 100);
        myStore.add("RAW_COMP_Z", 100);
        myStore.add("RAW_GYRO_X", 100);
        myStore.add("RAW_GYRO_Y", 100);
        myStore.add("RAW_GYRO_Z", 100);
        myStore.add("ACC_X", 100);
        myStore.add("ACC_Y", 100);
        myStore.add("ACC_Z", 100);
        myStore.add("COMP_X", 100);
        myStore.add("COMP_Y", 100);
        myStore.add("COMP_Z", 100);
        myStore.add("GYRO_X", 100);
        myStore.add("GYRO_Y", 100);
        myStore.add("GYRO_Z", 100);
        //Log.i("MY_DEBUG","Created store data");

        mGLView = new MyGLSurfaceView(this);
        mGLView.setData(myStore);
        mPlotHandler = mGLView.getHandler();
        setContentView(mGLView);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message inputMessage) {
                if(inputMessage.obj.getClass().equals(MyDataTranfer.class)) {
                    MyDataTranfer myDataTranfer = (MyDataTranfer)inputMessage.obj;
                    if(myDataTranfer.type == IMU.IMU_DATA_ACC) {
                        //Log.i("MY_DEBUG","Haved data acc to plot");
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
        imu = new IMU(this, mHandler, myStore);
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
