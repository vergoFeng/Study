package com.vergo.demo.designpattern.facade.thing;

import android.graphics.Bitmap;

/**
 * <p>Created by Fenghj on 2019/6/25.</p>
 */
public interface MemoryCache {
    Bitmap findBitmapFromMemory(String url);
}
