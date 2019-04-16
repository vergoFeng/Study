package com.vergo.demo.adaptation;

import android.app.Application;

/**
 * <p>Created by Fenghj on 2019/4/16.</p>
 */
public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
