package com.vergo.skin.demo;

import android.app.Application;

import com.vergo.skin.library.SkinManager;

/**
 * <p>Created by Fenghj on 2019/7/19.</p>
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);
    }
}
