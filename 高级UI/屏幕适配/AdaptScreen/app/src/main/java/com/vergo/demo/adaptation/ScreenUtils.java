package com.vergo.demo.adaptation;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 屏幕相关工具类<br/>
 * <p>Created by Fenghj on 2018/5/29.</p>
 */

public class ScreenUtils {
    private ScreenUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }
    /**
     * 获取屏幕的宽度（单位：px）
     *
     * @return int 屏幕宽px
     */
    public static int getScreenWidth() {
        WindowManager windowManager = (WindowManager) Utils.getApp()
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getRealMetrics(dm);// 给白纸设置宽高
        }
        return dm.widthPixels;
    }

    /**
     * 获取屏幕的高度（单位：px）
     *
     * @return int 屏幕高px
     */
    public static int getScreenHeight() {
        WindowManager windowManager = (WindowManager) Utils.getApp()
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getRealMetrics(dm);// 给白纸设置宽高
        }
        return dm.heightPixels;
    }

    /**
     * 判断是否是横屏.
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isLandscape() {
        return Utils.getApp().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 判断是否是竖屏.
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isPortrait() {
        return Utils.getApp().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 获取状态栏高度(px)
     *
     * @return 状态栏高度px
     */
    public static int getStatusBarHeight() {
        Resources resources = Utils.getApp().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if(resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
