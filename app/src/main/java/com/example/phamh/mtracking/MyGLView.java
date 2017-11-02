package com.example.phamh.mtracking;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by phamh on 11/1/2017.
 */

public class MyGLView {

    private Context mContext;
    private LinearLayout  mLayout;
    private GLSurfaceView mGLView;

    private boolean hasGLES20() {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        return info.reqGlEsVersion >= 0x20000;
    }

    public MyGLView (Context context, LinearLayout layout) {
        this.mContext = context;
        this.mLayout = layout;
        if(hasGLES20()) {
            Log.i("MY_DEBUG", "Have GLES 2.0");
        } else {
            Log.i("MY_DEBUG", "Haven't GLES 2.0");
        }
        mGLView = new GLSurfaceView(mContext);

        mLayout.addView(mGLView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Log.i("MY_DEBUG", "abc");
    }

    public void onResume() {
        mGLView.onResume();
    }

    public void onPause() {
        mGLView.onPause();
    }
}

class MyGLSurfaceView extends GLSurfaceView {
    private MyRender m_renderer;
    private Plot m_plot;

    public MyGLSurfaceView(Context context) {
        super(context);
        m_renderer = new MyRender() {
            @Override
            public void onCreate(GL10 gl) {
                GLES20.glClearColor(0f, 0.0f, 0.0f, 1f);
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
                Log.i("MY_DEBUG", "opengles draw");
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
                gl.glMatrixMode(GL10.GL_MODELVIEW);
                gl.glLoadIdentity();
                m_plot.draw(gl);
                GLU.gluLookAt(gl, 0, 0, 0, 0f, 0f, -1f, 0f, 1.0f, 0.0f);
            }
        };
        setRenderer(m_renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        m_plot = new Plot();
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}

class Plot {
    private float vertices[] = {
        -1.0f,  1.0f, -1.0f,
        -1.0f, -1.0f, -5.0f,
        1.0f, -1.0f, -7.0f,
        1.0f,  1.0f, -9.0f,
    };

    private float colors[] = {
        1.0f , 0.0f, 0.0f, 0.5f,
        0.0f, 1.0f, 0.0f, 0.5f,
        0.0f, 0.0f, 1.0f, 0.5f,
        0.0f, 1.0f, 1.0f, 0.5f
    };

    private float limit;

    private short[] indices = { 0, 1, 2, 0, 2, 3 };

    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    private ShortBuffer indexBuffer;

    public Plot() {
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        colorBuffer = cbb.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);

        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indexBuffer = ibb.asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);
    }

    public void draw(GL10 gl) {
        drawPlot2D(gl,5);
        /*gl.glFrontFace(GL10.GL_CCW);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glCullFace(GL10.GL_BACK);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_SHORT, indexBuffer);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        gl.glDisable(GL10.GL_CULL_FACE);*/
    }

    public void setLimit(float limit) {
        this.limit = limit;
    }

    private void drawPlot2D(GL10 gl, float axis_z) {
        gl.glPushMatrix();
        gl.glTranslatef(0, 0, - axis_z);
        gl.glScalef(0.5f*axis_z,0.5f*axis_z,1.0f);
        drawAxisPlot2D(gl);
        gl.glPopMatrix();
    }

    private void drawAxisPlot2D(GL10 gl) {
        float vertices_axis[] = {-limit,0,0, limit,0,0,0,-1,0, 0,1,0};
        float colors_axis[] = {1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f};
        loadData(vertices_axis,colors_axis);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
        gl.glDrawArrays(GL10.GL_LINES,0,4);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }

    private void drawGridPlot2D(GL10 gl,float step_x, float step_y) {
        ArrayList<Float> lines = new ArrayList<>();
        ArrayList<Float> colors = new ArrayList<>();
        float temp_value = 0.0f;
        for(temp_value = 0.0f; temp_value < 1.0f; temp_value += step_y) {
            lines.add(-limit);
            lines.add(temp_value);
            lines.add(0.0f);
            lines.add(limit);
            lines.add(temp_value);
            lines.add(0.0f);
            lines.add(-limit);
            lines.add(-temp_value);
            lines.add(0.0f);
            lines.add(limit);
            lines.add(-temp_value);
            lines.add(0.0f);
            colors.add(0.25f);
            colors.add(0.25f);
            colors.add(0.25f);
            colors.add(1.0f);
            colors.add(0.25f);
            colors.add(0.25f);
            colors.add(0.25f);
            colors.add(1.0f);
        }
        for(temp_value = 0.0f; temp_value < limit; temp_value += step_x) {
            lines.add(temp_value);
            lines.add(-limit);
            lines.add(0.0f);
            lines.add(temp_value);
            lines.add(limit);
            lines.add(0.0f);
            lines.add(-temp_value);
            lines.add(-limit);
            lines.add(0.0f);
            lines.add(temp_value);
            lines.add(limit);
            lines.add(0.0f);
            colors.add(0.25f);
            colors.add(0.25f);
            colors.add(0.25f);
            colors.add(1.0f);
            colors.add(0.25f);
            colors.add(0.25f);
            colors.add(0.25f);
            colors.add(1.0f);
        }

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
        gl.glDrawArrays(GL10.GL_LINES,0,4);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }

    private void loadData(float vertices[], float colors[]) {
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        colorBuffer = cbb.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);
    }
}
