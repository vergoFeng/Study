package com.vergo.demo.designpattern.facade.impl;

import android.graphics.Bitmap;

import com.vergo.demo.designpattern.facade.thing.MemoryCache;

/**
 * <p>Created by Fenghj on 2019/6/25.</p>
 */
public class MemoryCacheImpl implements MemoryCache {
    @Override
    public Bitmap findBitmapFromMemory(String url) {
        System.out.println("通过图片url，寻找内存中缓存的图片");
        return null;
    }
}
