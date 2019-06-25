package com.vergo.demo.designpattern.facade.impl;

import android.graphics.Bitmap;

import com.vergo.demo.designpattern.facade.thing.DiskCache;

/**
 * <p>Created by Fenghj on 2019/6/25.</p>
 */
public class DiskCacheImpl implements DiskCache {
    @Override
    public Bitmap findBitmapFromDisk(String url) {
        System.out.println("通过图片url，寻找磁盘中缓存的图片");
        return null;
    }
}
