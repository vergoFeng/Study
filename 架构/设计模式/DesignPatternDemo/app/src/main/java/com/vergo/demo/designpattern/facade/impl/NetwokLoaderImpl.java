package com.vergo.demo.designpattern.facade.impl;

import android.graphics.Bitmap;

import com.vergo.demo.designpattern.facade.thing.NetworkLoader;

/**
 * <p>Created by Fenghj on 2019/6/25.</p>
 */
public class NetwokLoaderImpl implements NetworkLoader {
    @Override
    public Bitmap loaderFromNetwork(String url) {
        System.out.println("通过图片url，通过网络加载图片");
        return null;
    }
}
