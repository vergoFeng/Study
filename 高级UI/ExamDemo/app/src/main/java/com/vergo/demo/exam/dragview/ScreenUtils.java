package com.vergo.demo.exam.dragview;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ScreenUtils {
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(dm);// 给白纸设置宽高
        }
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(dm);// 给白纸设置宽高
        }
        return dm.heightPixels;
    }
}
