package com.vergo.demo.designpattern.facade;

import com.vergo.demo.designpattern.facade.impl.DiskCacheImpl;
import com.vergo.demo.designpattern.facade.impl.MemoryCacheImpl;
import com.vergo.demo.designpattern.facade.impl.NetwokLoaderImpl;
import com.vergo.demo.designpattern.facade.thing.DiskCache;
import com.vergo.demo.designpattern.facade.thing.MemoryCache;
import com.vergo.demo.designpattern.facade.thing.NetworkLoader;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * <p>Created by Fenghj on 2019/6/25.</p>
 */
public class FacadeActivityTest {

    private final String url = "http://xxxxxx";
    @Test
    public void onCreate() {
        // 常规写法
//        MemoryCache memoryCache = new MemoryCacheImpl();
//        memoryCache.findBitmapFromMemory(url);
//
//        DiskCache diskCache = new DiskCacheImpl();
//        diskCache.findBitmapFromDisk(url);
//
//        NetworkLoader networkLoader = new NetwokLoaderImpl();
//        networkLoader.loaderFromNetwork(url);

        // 外观模式
        ImageLoader imageLoader = new ImageLoader(url);
        imageLoader.loader();

    }
}