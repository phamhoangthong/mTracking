package com.example.phamh.mtracking;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by phamh on 11/2/2017.
 */

public abstract class MyRender implements GLSurfaceView.Renderer {
    private boolean mFirstDraw;
    private int mWidth;
    private int mHeight;
    private long mLastTime;
    private int mFPS;

    public MyRender() {
        mFirstDraw = true;
        mWidth = -1;
        mHeight = -1;
        mLastTime = System.currentTimeMillis();
        mFPS = 0;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mWidth = -1;
        mHeight = -1;
        onCreate(gl);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mWidth = width;
        mHeight = height;
        onChange(gl ,mWidth, mHeight);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        onDrawFrame(gl ,mFirstDraw);
        mFPS++;
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastTime >= 1000) {
            mFPS = 0;
            mLastTime = currentTime;
        }
        if (mFirstDraw) {
            mFirstDraw = false;
        }
    }

    public int getFPS() {
        return mFPS;
    }

    public abstract void onCreate(GL10 gl);
    public abstract void onChange(GL10 gl, int width, int height);
    public abstract void onDrawFrame(GL10 gl ,boolean firstDraw);
}

