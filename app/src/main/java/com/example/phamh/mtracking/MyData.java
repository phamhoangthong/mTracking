package com.example.phamh.mtracking;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by phamh on 11/6/2017.
 */

public class MyData {
    public FloatBuffer data;
    private int mSize;
    private String mTag;

    public MyData() {

    }

    public MyData(String name, int size) {
        mTag = name;
        init(size);
    }

    public void init(int size) {
        mSize = size;
        ByteBuffer tbb = ByteBuffer.allocateDirect(mSize * Float.BYTES);
        tbb.order(ByteOrder.nativeOrder());
        //data.clear();
        data = tbb.asFloatBuffer();
        data.position(0);
    }

    public int getSize() {
        return mSize;
    }

    public int getIndex() {
        return data.position();
    }

    public void push(float mData) {
        int index = data.position();
        if(index >= mSize) {
            for(int i = 0; i < (mSize - 1); i++) {
                data.put(i,data.get(i+1));
            }
            data.put(mSize-1, mData);
            data.position(mSize);
        } else {
            data.put(mData);
        }
    }

    public void clear() {
        data.clear();
        data = null;
    }

    public float get(int index) {
        int t_index = data.position();
        float t_value = data.get(index);
        data.position(t_index);
        return t_value;
    }

    public String getName() {
        return mTag;
    }

    public void setName(String name) {
        mTag = name;
    }
}
