package com.example.phamh.mtracking;

import android.content.Context;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by phamh on 11/3/2017.
 */

public class MyPlot {
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
    private FloatBuffer dataChannel1;
    public static final int sample = 200;

    public MyPlot() {
        ByteBuffer tbb = ByteBuffer.allocateDirect(sample * 4);
        tbb.order(ByteOrder.nativeOrder());
        dataChannel1 = tbb.asFloatBuffer();
        dataChannel1.position(0);
    }

    public void draw(GL10 gl) {
        drawPlot2D(gl,5);
    }

    public void setLimit(float limit) {
        this.limit = limit;
    }

    private void drawPlot2D(GL10 gl, float axis_z) {
        gl.glPushMatrix();
        gl.glTranslatef(0, 0, - axis_z);
        gl.glScalef(axis_z,axis_z,1.0f);
        drawGridPlot2D(gl,0.1f,0.1f);
        drawAxisPlot2D(gl);
        drawChannelPlot2D(gl);
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
        gl.glLineWidth(4.0f);
        gl.glDrawArrays(GL10.GL_LINES,0,4);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }

    private void drawGridPlot2D(GL10 gl,float step_x, float step_y) {
        ByteBuffer vbb = ByteBuffer.allocateDirect(3000 * 4);
        vbb.order(ByteOrder.nativeOrder());
        ByteBuffer cbb = ByteBuffer.allocateDirect(4000 * 4);
        cbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        colorBuffer = cbb.asFloatBuffer();
        float temp_value = 0.0f;
        for(temp_value = 0.0f; temp_value <= 1.0f; temp_value += step_y) {
            vertexBuffer.put(-limit);
            vertexBuffer.put(temp_value);
            vertexBuffer.put(0.0f);
            vertexBuffer.put(limit);
            vertexBuffer.put(temp_value);
            vertexBuffer.put(0.0f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            vertexBuffer.put(-limit);
            vertexBuffer.put(-temp_value);
            vertexBuffer.put(0.0f);
            vertexBuffer.put(limit);
            vertexBuffer.put(-temp_value);
            vertexBuffer.put(0.0f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
        }

        for(temp_value = 0.0f; temp_value <= limit; temp_value += step_x) {
            vertexBuffer.put(temp_value);
            vertexBuffer.put(-1.0f);
            vertexBuffer.put(0.0f);
            vertexBuffer.put(temp_value);
            vertexBuffer.put(1.0f);
            vertexBuffer.put(0.0f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            vertexBuffer.put(-temp_value);
            vertexBuffer.put(-1.0f);
            vertexBuffer.put(0.0f);
            vertexBuffer.put(-temp_value);
            vertexBuffer.put(1.0f);
            vertexBuffer.put(0.0f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
            colorBuffer.put(0.5f);
        }
        int t_size_1 = vertexBuffer.position();
        int t_size_2 = colorBuffer.position();
        int size = t_size_2>>2;
        if(t_size_1/3 == size) {
            vertexBuffer.position(0);
            colorBuffer.position(0);
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
            gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
            gl.glLineWidth(1.0f);
            gl.glDrawArrays(GL10.GL_LINES,0,size);
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        }
        vbb.clear();
        vbb = null;
        cbb.clear();
        cbb = null;
        vertexBuffer.clear();
        colorBuffer.clear();
    }

    private void drawChannelPlot2D(GL10 gl) {
        int m_limit = dataChannel1.position();
        ByteBuffer vbb = ByteBuffer.allocateDirect(3*m_limit * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.position(0);
        int offset = sample/2;
        //Log.i("MY_DEBUG", "abc");
        for(int i = 0; i < m_limit; i++) {
            vertexBuffer.put(((float)(i - offset))*limit/((float)offset));
            vertexBuffer.put(dataChannel1.get(i)/10.0f);
            vertexBuffer.put(0.0f);
        }
        vertexBuffer.position(0);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glLineWidth(1.0f);
        gl.glColor4f(1.0f,0.0f,0.0f,1.0f);
        gl.glDrawArrays(GL10.GL_LINE_STRIP,0,m_limit);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        vbb.clear();
        vertexBuffer.clear();
        dataChannel1.position(m_limit);
        //Log.i("MY_DEBUG", String.format("pos : %d",m_limit));
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

    public void addDataChannel1(float mData) {
        if(dataChannel1.position() < sample) {
            dataChannel1.put(mData);
        } else {
            dataChannel1.position(0);
            for (int i = 0; i < (sample - 1); i++) {
                dataChannel1.put(i,dataChannel1.get(i+1));
            }
            dataChannel1.put(sample -1,mData);
            dataChannel1.position(sample);
        }
    }
}
