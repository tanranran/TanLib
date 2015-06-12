package com.example.tan.myapplication;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.util.Timer;

public class MainActivity extends Activity {

    private ImageView imageView;
    private int i=0;
    private int[] imgRes;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageView=new ImageView(this);
        setContentView(R.layout.activity_main);
        setTitle("测试");
        
//        imgRes=new int[]{R.drawable.f1,R.drawable.f2,R.drawable.f1,R.drawable.f2,R.drawable.f1,R.drawable.f2};
//        SceneAnimation s=new SceneAnimation(imageView,imgRes,300);
    }

}
