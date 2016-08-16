package com.example.ljman.centerscroll;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        CenterHighlightScrollView scrollView= (CenterHighlightScrollView) findViewById(R.id.scrollview);
        ArrayList<String> datalist=new ArrayList<>();
        for (int i = 0; i < 366; i++) {
            datalist.add(i+"");
        }
        //设置数据集，总宽度，条目数（条目数必须是奇数），监听器
        scrollView.setParameter(datalist, getWindowManager().getDefaultDisplay().getWidth(), 7, new CenterHighlightScrollView.ScrollListener() {
            //条目居中的回调方法，可以自行修改这个监听器的参数以满足需求
            @Override
            public void scrolllistener(String data) {
                Log.w(TAG, "scrolllistener:  data "+data);
            }
        });
    }
}
