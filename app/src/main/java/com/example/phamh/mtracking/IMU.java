package com.example.phamh.mtracking;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by phamh on 11/1/2017.
 */

public class IMU {
    public static final int IMU_DATA_ACC = 1;
    public final int IMU_DATA_ACC_Y = 2;
    public final int IMU_DATA_ACC_Z = 3;
    private static final int RAW_DATA_ACC = 1000;
    private static final int RAW_DATA_COMP = 1001;
    private static final int RAW_DATA_GYRO = 1002;
    private static final int BUFFER_SIZE = 1000;
    private static final int FILTER_SIZE = 10;
    private MyStore myStore;

    private class ReadRawSensor implements SensorEventListener {
        private Handler mHander;

        public ReadRawSensor(Handler handler) {
            this.mHander = handler;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.equals(mAcc)) {
                Double[] mTemp = new Double[3];
                mTemp[0] = (double)event.values[0];
                mTemp[1] = (double)event.values[1];
                mTemp[2] = (double)event.values[2];
                MyDataTranfer myDataTranfer = new MyDataTranfer();
                myDataTranfer.type = RAW_DATA_ACC;
                myDataTranfer.data = mTemp;
                Message message = mHander.obtainMessage();
                message.obj = myDataTranfer;
                mHander.sendMessage(message);
            } else if (event.sensor.equals(mComp)) {
                Double[] mTemp = new Double[3];
                mTemp[0] = (double)event.values[0];
                mTemp[1] = (double)event.values[1];
                mTemp[2] = (double)event.values[2];
                MyDataTranfer myDataTranfer = new MyDataTranfer();
                myDataTranfer.type = RAW_DATA_COMP;
                myDataTranfer.data = mTemp;
                Message message = mHander.obtainMessage();
                message.obj = myDataTranfer;
                mHander.sendMessage(message);
            } else if (event.sensor.equals(mGyro)) {
                Double[] mTemp = new Double[3];
                mTemp[0] = (double)event.values[0];
                mTemp[1] = (double)event.values[1];
                mTemp[2] = (double)event.values[2];
                MyDataTranfer myDataTranfer = new MyDataTranfer();
                myDataTranfer.type = RAW_DATA_GYRO;
                myDataTranfer.data = mTemp;
                Message message = mHander.obtainMessage();
                message.obj = myDataTranfer;
                mHander.sendMessage(message);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    /*private class FilterMean {
        public FilterMean() {

        }

        public float filter(FloatBuffer input, FloatBuffer output, int size_filter) {
            int index_input = input.position();
            int index_output = output.position();
            if(index_input >= size_filter) {

            } else {
                
            }
        }
    }*/

    private Handler mHandlerTranferData;
    private Context mContext;
    private SensorManager sensorManager;
    private Sensor mAcc;
    private Sensor mComp;
    private Sensor mGyro;

    private Double[] rawDataAcc;
    private Double[] rawDataComp;
    private Double[] rawDataGyro;

    Handler mHandlerRawSensor = new Handler() {
        @Override
        public void handleMessage(Message inputMessage) {
            if(inputMessage.obj.getClass().equals(MyDataTranfer.class)) {
                MyDataTranfer myDataTranfer = (MyDataTranfer)inputMessage.obj;
                if(myDataTranfer.type == RAW_DATA_ACC) {
                    //Log.i("MY_DEBUG","Have data acc");
                    Double[] mTemp = (Double[])myDataTranfer.data;
                    if(mTemp.length == 3) {
                        rawDataAcc[0] = mTemp[0];
                        rawDataAcc[1] = mTemp[1];
                        rawDataAcc[2] = mTemp[2];
                        myStore.push("RAW_ACC_X", (float)mTemp[0].doubleValue());
                        myStore.push("RAW_ACC_Y", (float)mTemp[1].doubleValue());
                        myStore.push("RAW_ACC_Z", (float)mTemp[2].doubleValue());
                        //Log.i("MY_DEBUG","Pushed data acc");
                        if(mHandlerTranferData != null) {
                            MyDataTranfer m_data = new MyDataTranfer();
                            m_data.type = IMU_DATA_ACC;
                            m_data.data = mTemp;
                            Message msg = mHandlerTranferData.obtainMessage();
                            msg.obj = m_data;
                            mHandlerTranferData.sendMessage(msg);
                        }
                        //Log.i("MY_ACC", String.format("X = %2.3f - Y = %2.3f - Z = %2.3f",rawDataAcc[0],rawDataAcc[1],rawDataAcc[2]));
                    }
                } else if(myDataTranfer.type == RAW_DATA_COMP) {
                    Double[] mTemp = (Double[])myDataTranfer.data;
                    if(mTemp.length == 3) {
                        rawDataComp[0] = mTemp[0];
                        rawDataComp[1] = mTemp[1];
                        rawDataComp[2] = mTemp[2];
                        myStore.push("RAW_COMP_X", (float)mTemp[0].doubleValue());
                        myStore.push("RAW_COMP_Y", (float)mTemp[1].doubleValue());
                        myStore.push("RAW_COMP_Z", (float)mTemp[2].doubleValue());
                        //Log.i("MY_COMP", String.format("X = %2.3f - Y = %2.3f - Z = %2.3f",rawDataComp[0],rawDataComp[1],rawDataComp[2]));
                    }
                } else if(myDataTranfer.type == RAW_DATA_GYRO) {
                    Double[] mTemp = (Double[])myDataTranfer.data;
                    if(mTemp.length == 3) {
                        rawDataGyro[0] = mTemp[0];
                        rawDataGyro[1] = mTemp[1];
                        rawDataGyro[2] = mTemp[2];
                        myStore.push("RAW_GYRO_X", (float)mTemp[0].doubleValue());
                        myStore.push("RAW_GYRO_Y", (float)mTemp[1].doubleValue());
                        myStore.push("RAW_GYRO_Z", (float)mTemp[2].doubleValue());
                        //Log.i("MY_GYRO", String.format("X = %2.3f - Y = %2.3f - Z = %2.3f",rawDataGyro[0],rawDataGyro[1],rawDataGyro[2]));
                    }
                }
            }
        }
    };

    ReadRawSensor readRawSensor;
    public IMU(Context mContext, final Handler mHander, MyStore mStore) {
        this.mHandlerTranferData = mHander;
        this.mContext = mContext;
        this.myStore = mStore;

        rawDataAcc = new Double[3];
        rawDataComp = new Double[3];
        rawDataGyro = new Double[3];

        sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mAcc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if(mAcc == null){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
            mBuilder.setMessage("Device haven't accelerometer");
            //mBuilder.setCancelable(true);
            mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog mAlert = mBuilder.create();
            mAlert.show();
        }

        mComp = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(mComp == null){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
            mBuilder.setMessage("Device haven't compass");
            //mBuilder.setCancelable(true);
            mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog mAlert = mBuilder.create();
            mAlert.show();
        }

        mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if(mGyro == null){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
            mBuilder.setMessage("Device haven't gyroscope");
            //mBuilder.setCancelable(true);
            mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog mAlert = mBuilder.create();
            mAlert.show();
        }

        readRawSensor = new ReadRawSensor(mHandlerRawSensor);
    }

    public void start() {
        if(mAcc != null) {
            sensorManager.registerListener(readRawSensor,mAcc,10000,100);
        }

        if(mComp != null) {
            sensorManager.registerListener(readRawSensor,mComp,10000,100);
        }

        if(mGyro != null) {
            sensorManager.registerListener(readRawSensor,mGyro,10000,100);
        }

    }

    public void stop(){
        sensorManager.unregisterListener(readRawSensor);
    }
}
