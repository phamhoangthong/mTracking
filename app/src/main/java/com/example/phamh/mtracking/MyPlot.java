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
    private MyStore myStore;
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
        //Log.i("MY_DEBUG", "begin draw plot");
        gl.glPushMatrix();
        gl.glTranslatef(0, 0, - axis_z);
        gl.glScalef(axis_z,axis_z,1.0f);
        drawAxisGridPlot2D(gl,0.1f,0.1f, 1.0f,1.0f,1.0f);
        /*
        drawChannelPlot2D(gl,myStore.getData("RAW_ACC_X"),1.0f,0.0f,0.5f,0.15f,0.15f,0.5f);
        drawChannelPlot2D(gl,myStore.getData("RAW_ACC_Y"),1.0f,0.0f,0.15f,0.5f,0.15f,0.5f);
        drawChannelPlot2D(gl,myStore.getData("RAW_ACC_Z"),0.1f,0.0f,0.15f,0.15f,0.5f,0.5f);
        drawChannelPlot2D(gl,myStore.getData("ACC_X"),1.0f,0.0f,1.0f,0.0f,0.0f,1.0f);
        drawChannelPlot2D(gl,myStore.getData("ACC_Y"),1.0f,0.0f,0.0f,1.0f,0.0f,1.0f);
        drawChannelPlot2D(gl,myStore.getData("ACC_Z"),0.1f,0.0f,0.0f,0.0f,1.0f,1.0f);
        */
        drawChannelPlot2D(gl,myStore.getData("RAW_COMP_X"),0.02f,0.0f,0.5f,0.15f,0.15f,0.5f);
        drawChannelPlot2D(gl,myStore.getData("RAW_COMP_Y"),0.02f,0.0f,0.15f,0.5f,0.15f,0.5f);
        drawChannelPlot2D(gl,myStore.getData("RAW_COMP_Z"),0.02f,0.0f,0.15f,0.15f,0.5f,0.5f);
        drawChannelPlot2D(gl,myStore.getData("COMP_X"),0.02f,0.0f,1.0f,0.0f,0.0f,1.0f);
        drawChannelPlot2D(gl,myStore.getData("COMP_Y"),0.02f,0.0f,0.0f,1.0f,0.0f,1.0f);
        drawChannelPlot2D(gl,myStore.getData("COMP_Z"),0.02f,0.0f,0.0f,0.0f,1.0f,1.0f);
        //drawChannelPlot2D(gl,myStore.getData("RAW_ACC_Z"),0.0f,0.0f,1.0f,0.5f);
        //Log.i("MY_DEBUG", "finish draw plot");
        gl.glPopMatrix();
    }

    private void drawAxisGridPlot2D(GL10 gl,float step_x, float step_y, float red, float green, float blue) {
        //Log.i("MY_DEBUG", "begin draw axis & grid plot");
        int m_size_x, m_size_y;
        m_size_x = (int)(limit/step_x);
        m_size_y = (int)(1.0f/step_y);
        //Log.i("MY_DEBUG", String.format("Size x : %d - Size y : %d - Limit = %f",m_size_x,m_size_y,limit));
        ByteBuffer vbb = ByteBuffer.allocateDirect((m_size_y  + m_size_x - 1)*48);
        vbb.order(ByteOrder.nativeOrder());
        FloatBuffer vertexBuffer;
        vertexBuffer = vbb.asFloatBuffer();
        float temp_value = 0.0f;
        int index = 0;
        for(index = 1; index < m_size_y; index++) {
            temp_value = step_y*(float)index;
            vertexBuffer.put(-limit);
            vertexBuffer.put(temp_value);
            vertexBuffer.put(0.0f);
            vertexBuffer.put(limit);
            vertexBuffer.put(temp_value);
            vertexBuffer.put(0.0f);
            vertexBuffer.put(-limit);
            vertexBuffer.put(-temp_value);
            vertexBuffer.put(0.0f);
            vertexBuffer.put(limit);
            vertexBuffer.put(-temp_value);
            vertexBuffer.put(0.0f);
        }
        //Log.i("MY_DEBUG", String.format("Index : %d",vertexBuffer.position()));

        for(index = 1; index < m_size_x; index++) {
            temp_value = step_x*(float)index;
            vertexBuffer.put(temp_value);
            vertexBuffer.put(-1.0f);
            vertexBuffer.put(0.0f);
            vertexBuffer.put(temp_value);
            vertexBuffer.put(1.0f);
            vertexBuffer.put(0.0f);
            vertexBuffer.put(-temp_value);
            vertexBuffer.put(-1.0f);
            vertexBuffer.put(0.0f);
            vertexBuffer.put(-temp_value);
            vertexBuffer.put(1.0f);
            vertexBuffer.put(0.0f);
        }

        //Log.i("MY_DEBUG", String.format("Index : %d",vertexBuffer.position()));

        vertexBuffer.put(0);
        vertexBuffer.put(-1.0f);
        vertexBuffer.put(0.0f);
        vertexBuffer.put(0);
        vertexBuffer.put(1.0f);
        vertexBuffer.put(0.0f);
        vertexBuffer.put(-limit);
        vertexBuffer.put(0);
        vertexBuffer.put(0.0f);
        vertexBuffer.put(limit);
        vertexBuffer.put(0.0f);
        vertexBuffer.put(0.0f);
        //Log.i("MY_DEBUG", String.format("Index : %d",vertexBuffer.position()));

        vertexBuffer.position(0);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glLineWidth(1.0f);
        gl.glColor4f(red,green,blue,0.5f);
        gl.glDrawArrays(GL10.GL_LINES,0,4*(m_size_y+m_size_x-2));
        vertexBuffer.position(0);
        gl.glLineWidth(3.0f);
        gl.glColor4f(red,green,blue,1.0f);
        gl.glDrawArrays(GL10.GL_LINES,4*(m_size_y + m_size_x - 2),4);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        vbb.clear();
        vertexBuffer.clear();
        //Log.i("MY_DEBUG", "finish draw axis & grid plot");
    }

    private void drawChannelPlot2D(GL10 gl, MyData mBuf, float scale, float offset,float red, float green, float blue,float optical) {
        int m_limit = mBuf.getSize();
        ByteBuffer vbb = ByteBuffer.allocateDirect(3*m_limit * 4);
        vbb.order(ByteOrder.nativeOrder());
        FloatBuffer vertexBuffer;
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.position(0);
        int m_offset = m_limit/2;
        for(int i = 0; i < m_limit; i++) {
            vertexBuffer.put(((float)(i - m_offset))*limit/((float)m_offset));
            vertexBuffer.put(scale*mBuf.get(i)+offset);
            vertexBuffer.put(0.0f);
        }
        vertexBuffer.position(0);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glLineWidth(1.0f);
        gl.glColor4f(red,green,blue,optical);
        gl.glDrawArrays(GL10.GL_LINE_STRIP,0,m_limit);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        vbb.clear();
        vertexBuffer.clear();
        dataChannel1.position(m_limit);
    }

    public void addStore(MyStore mStore) {
        this.myStore = mStore;
    }
}
