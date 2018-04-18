package com.example.administrator.screenshotdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.screenshotdemo.screen.ScreenCaptureActivity;
import com.example.administrator.screenshotdemo.util.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private View mView;
    int isClickImageView = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mView = findViewById(R.id.v_main);

        if (CustomApplication.isVisibleImageView == true) {
            CustomApplication.imageView.setVisibility(View.GONE);
            CustomApplication.isVisibleImageView = false;
            ToastUtil.show("關閉 截圖按鈕");
        } else {
            CustomApplication.imageView.setVisibility(View.VISIBLE);
            CustomApplication.isVisibleImageView = true;
            ToastUtil.show("開啟 截圖按鈕");
        }

        CustomApplication.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isClickImageView == 1) {
                    makeScreen();
                }
                if (isClickImageView == 0) {
                    isClickImageView = 1;
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            isClickImageView = 0;
                        }
                    }, 500);
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, 300);
    }

    /**
     * 检查权限是否被授予, 如果沒有的話 就先请求获取权限, 再進行其它行為
     */
    public void getScreen(View view) {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkWritePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (checkWritePermission != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 125);
                return;
            } else {
                makeScreen();
            }
        } else if (Build.VERSION.SDK_INT >= 21) {
            makeScreen();
        } else {
            // 5.0 以下可用
            getScreen2();
        }
    }


    public void getScreen2() {
        int[] location = new int[2];
        mView.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        View view = getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        /** 获取状态栏高度 */
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        System.out.println(statusBarHeight);                                                         // 获取屏幕长和高

        int width = getWindowManager().getDefaultDisplay().getWidth();
        int height = getWindowManager().getDefaultDisplay().getHeight();                            // 去掉标题栏
        Bitmap bm = Bitmap.createBitmap(b1, x, y, mView.getWidth(), mView.getHeight());
        view.destroyDrawingCache();
        saveBitmapTofile(bm, System.currentTimeMillis() + ".jpg");
    }

    /**
     * 保存图片到指定文件夹
     *
     * @param bmp
     * @param filename
     * @return
     */
    private boolean saveBitmapTofile(Bitmap bmp, String filename) {
        if (bmp == null || filename == null)
            return false;
        File f = new File("/sdcard/AAB/");
        if (!f.exists()) {
            f.mkdirs();                                                                             // 創建指定路徑的文件夾
        }
        Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;                                  // 表示以PNG压缩算法进行图像压缩, 压缩后的格式可以是".png", 是一种无损压缩
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream("/sdcard/AAB/" + filename);
            Toast.makeText(this, "YES", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bmp.compress(format, quality, stream);                                               // 將該Bitmap 壓縮成指定的檔案
    }

    /**
     * 创建文件夹，跳转去截图
     */
    public void makeScreen() {
        CustomApplication.imageView.setVisibility(View.GONE);
        File f = new File("/sdcard/AAB/");
        if (!f.exists()) {
            f.mkdirs();
        }
        Intent intent = new Intent(this, ScreenCaptureActivity.class);
        startActivity(intent);
    }

    /**
     * Android 6.0以後 執行階段取得使用者授權, 或被使用者拒絕後, 會執行onRequestPermissionsResult()
     *
     * @param requestCode,  請求代碼
     * @param permissions,  請求授權的名稱
     * @param grantResults, 請求授權的結果: PERMISSION_GRANTED, PERMISSION_DENTED
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 125:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeScreen();
                } else {
                    ToastUtil.show("权限打开失败");
                }
                break;
        }
    }
}
