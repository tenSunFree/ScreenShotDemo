package com.example.administrator.screenshotdemo;

import android.app.Application;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.Screen;

/**
 * Created by penglu on 2016/10/26.
 */

public class CustomApplication extends Application {

    private static CustomApplication instance;
    public static ImageView imageView;
    public static boolean isVisibleImageView = false;

    public static CustomApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.screenshots3);
        FloatWindow
                .with(getApplicationContext())
                .setView(imageView)
                .setWidth(90).setHeight(90)                                                         // 設置imageView 顯示的大小
                .setX(Screen.width,1f).setY(Screen.height,0.75f)                           // 設置imageView 顯示的相對位置
                .setMoveType(MoveType.slide)                                                          // 可拖动, 释放后自动贴边
                .setMoveStyle(500,new BounceInterpolator())
                .setDesktopShow(true)
                .build();
    }
}
