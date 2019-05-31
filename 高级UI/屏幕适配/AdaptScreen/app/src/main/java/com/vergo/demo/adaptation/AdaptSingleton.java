package com.vergo.demo.adaptation;

/**
 * <p>Created by Fenghj on 2019/4/15.</p>
 */
public class AdaptSingleton {
    private volatile static AdaptSingleton instance;

    // 屏幕显示宽高
    private float screenWidth;
    private float screenHeight;

    //设计稿参考宽高
    private static final float STANDARD_WIDTH = 1080f;
    private static final float STANDARD_HEIGHT = 1920f;

    private AdaptSingleton() {
        initWH();
    }

    public static synchronized AdaptSingleton getInstance() {
        if(instance == null) {
            synchronized (AdaptSingleton.class) {
                if(instance == null) {
                    instance = new AdaptSingleton();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化获取屏幕宽高值
     */
    private void initWH() {
        if(screenWidth == 0 || screenHeight == 0) {
            if(ScreenUtils.isPortrait()) {
                // 竖屏
                screenWidth = ScreenUtils.getScreenWidth();
                // 竖屏下显示高度需减去状态栏高度，一般设计图不包含状态栏
                screenHeight = ScreenUtils.getScreenHeight() - ScreenUtils.getStatusBarHeight();
            } else {
                // 横屏
                screenWidth = ScreenUtils.getScreenHeight();
                screenHeight = ScreenUtils.getScreenWidth() - ScreenUtils.getStatusBarHeight();
            }
        }
    }

    /**
     * 获取水平方向缩放比例
     * @return 水平缩放比例
     */
    public float getHorizontalScale() {
        return screenWidth / STANDARD_WIDTH;
    }

    /**
     * 获取垂直方向缩放比例
     * @return 垂直缩放比例
     */
    public float getVerticalScale() {
        return screenHeight / STANDARD_HEIGHT;
    }
}
