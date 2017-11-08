package com.example.phamh.mtracking;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by phamh on 11/1/2017.
 */
class MyGLSurfaceView extends GLSurfaceView {
    private MyRender m_renderer;
    private MyPlot m_plot;
    private Handler mHandler;
    private int count = 0;


    public MyGLSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        m_renderer = new MyRender() {
            @Override
            public void onCreate(GL10 gl) {
                GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1f);
                gl.glShadeModel(GL10.GL_SMOOTH);
                gl.glClearDepthf(1.0f);
                gl.glDepthFunc(GL10.GL_LEQUAL);
                gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
            }

            @Override
            public void onChange(GL10 gl, int width, int height) {
                gl.glViewport(0, 0, width, height);
                float ratio = (float) width / height;
                gl.glMatrixMode(GL10.GL_PROJECTION);
                gl.glLoadIdentity();
                gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
                m_plot.setLimit(ratio);
            }

            @Override

            public void onDrawFrame(GL10 gl ,boolean firstDraw) {
                //Log.i("MY_DEBUG", "render");
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
                gl.glMatrixMode(GL10.GL_MODELVIEW);
                gl.glLoadIdentity();
                m_plot.draw(gl);
                GLU.gluLookAt(gl, 0, 0, 0, 0f, 0f, -1f, 0f, 1.0f, 0.0f);
            }
        };
        setRenderer(m_renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        m_plot = new MyPlot();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message inputMessage) {
                if(inputMessage.obj.getClass().equals(MyDataTranfer.class)) {
                    MyDataTranfer myDataTranfer = (MyDataTranfer)inputMessage.obj;
                    if(myDataTranfer.type == 1) {
                        //requestRender();
                        count++;
                        if(count >= 10) {
                            count = 1;
                            requestRender();
                        }
                    }
                }
            }
        };

    }

    public void setData(MyStore mStore) {
        this.m_plot.addStore(mStore);
    }

    public Handler getHandler() {
        return mHandler;
    }
}