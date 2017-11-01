package com.example.phamh.mtracking;

import android.util.Log;

import java.io.InterruptedIOException;

/**
 * Created by phamh on 11/1/2017.
 */

public class Delay {

    public Delay(){
    }

    public void waitSecond(int time) {
        try {
            Thread.sleep(time*1000);
        } catch (InterruptedException e) {
            Log.e("Delay","waitSecond - " + e.toString());
        }
    }

    public void waitMilisecond(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Log.e("Delay","waitMilisecond" + e.toString());
        }
    }
}
