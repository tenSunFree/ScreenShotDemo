package com.example.administrator.screenshotdemo.util;

import android.widget.Toast;

import com.example.administrator.screenshotdemo.CustomApplication;


/**
 * Created by l4656_000 on 2015/12/27.
 */
public class ToastUtil {
    public static void show(String msg){
        Toast.makeText(CustomApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }
    public static void show(int rid){
        Toast.makeText(CustomApplication.getInstance(), rid, Toast.LENGTH_SHORT).show();
    }
    public static void showLong(int rid){
        Toast.makeText(CustomApplication.getInstance(), rid, Toast.LENGTH_LONG).show();
    }
}
