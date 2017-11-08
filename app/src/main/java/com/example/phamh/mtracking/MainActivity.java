package com.example.phamh.mtracking;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    public MyStore myStore;
    private IMU imu;
    private MyGLSurfaceView mGLView;
    private Handler mHandler;
    private Handler mPlotHandler;

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

        setContentView(R.layout.activity_main);
        mGLView = findViewById(R.id.myGLSurfaceView);
        mGLView.setData(myStore);
        mPlotHandler = mGLView.getHandler();

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message inputMessage) {
                if(inputMessage.obj.getClass().equals(MyDataTranfer.class)) {
                    MyDataTranfer myDataTranfer = (MyDataTranfer)inputMessage.obj;
                    if(myDataTranfer.type == IMU.IMU_DATA_ACC) {
                        Float[] mTemp = (Float[]) myDataTranfer.data;
                        TextView m_Text = findViewById(R.id.textViewAccX);
                        m_Text.setText(String.format("%3.2f", mTemp[0]));
                        m_Text = findViewById(R.id.textViewAccY);
                        m_Text.setText(String.format("%3.2f", mTemp[1]));
                        m_Text = findViewById(R.id.textViewAccZ);
                        m_Text.setText(String.format("%3.2f", mTemp[2]));
                        //Log.i("MY_DEBUG","Haved data acc to plot");
                        MyDataTranfer mDataTranfer = new MyDataTranfer();
                        mDataTranfer.type = 1;
                        mDataTranfer.data = 0.0f;
                        Message mgs = mPlotHandler.obtainMessage();
                        mgs.obj = mDataTranfer;
                        mPlotHandler.sendMessage(mgs);
                    }
                    if(myDataTranfer.type == IMU.IMU_DATA_COMP) {
                        //Log.i("MY_DEBUG","Haved data compass to plot");
                        Float[] mTemp = (Float[]) myDataTranfer.data;
                        TextView m_Text = findViewById(R.id.textViewCompX);
                        m_Text.setText(String.format("%3.2f", mTemp[0]));
                        m_Text = findViewById(R.id.textViewCompY);
                        m_Text.setText(String.format("%3.2f", mTemp[1]));
                        m_Text = findViewById(R.id.textViewCompZ);
                        m_Text.setText(String.format("%3.2f", mTemp[2]));
                        //MyDataTranfer mDataTranfer = new MyDataTranfer();
                        //mDataTranfer.type = 1;
                        //mDataTranfer.data = 0.0f;
                        //Message mgs = mPlotHandler.obtainMessage();
                        //mgs.obj = mDataTranfer;
                        //mPlotHandler.sendMessage(mgs);
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
