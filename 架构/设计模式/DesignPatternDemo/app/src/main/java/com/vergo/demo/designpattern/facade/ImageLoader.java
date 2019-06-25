package com.vergo.demo.designpattern.facade;

import com.vergo.demo.designpattern.facade.impl.DiskCacheImpl;
import com.vergo.demo.designpattern.facade.impl.MemoryCacheImpl;
import com.vergo.demo.designpattern.facade.impl.NetwokLoaderImpl;
import com.vergo.demo.designpattern.facade.thing.DiskCache;
import com.vergo.demo.designpattern.facade.thing.MemoryCache;
import com.vergo.demo.designpattern.facade.thing.NetworkLoader;

/**
 * <p>Created by Fenghj on 2019/6/25.</p>
 */
public class ImageLoader {

    private String url;
    private MemoryCache memoryCache;
    private DiskCache diskCache;
    private NetworkLoader networkLoader;

    public ImageLoader(String url) {
        this.url = url;
        memoryCache = new MemoryCacheImpl();
        diskCache = new DiskCacheImpl();
        networkLoader = new NetwokLoaderImpl();
    }

    public void loader() {
        memoryCache.findBitmapFromMemory(url);
        diskCache.findBitmapFromDisk(url);
        networkLoader.loaderFromNetwork(url);
    }
}
