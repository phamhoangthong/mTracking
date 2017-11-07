package com.example.phamh.mtracking;

import java.util.ArrayList;

/**
 * Created by phamh on 11/6/2017.
 */

public class MyStore {
    public ArrayList<MyData> data;

    public MyStore() {
        data = new ArrayList<MyData>();
    }

    public void push(String name, float mdata) {
        for (MyData mData: data) {
            if(mData.getName().equals(name)) {
                mData.push(mdata);
            }
        }
    }
}
