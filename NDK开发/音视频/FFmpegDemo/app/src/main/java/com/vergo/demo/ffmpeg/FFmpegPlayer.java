package com.vergo.demo.ffmpeg;

import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * <p>Created by Fenghj on 2019/6/3.</p>
 */
public class FFmpegPlayer implements SurfaceHolder.Callback {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    // 绘制视频的过程放在ndk中执行，所以需要将SurfaceView和视频地址传入ndk中
    // 但是ndk中并没有SurfaceView，这里就需要使用SurfaceHolder，可以获取到Surface，就传递给native层。
    private SurfaceHolder mSurfaceHolder;

    /**
     * 设置SurfaceView
     * @param surfaceView SurfaceView对象
     */
    public void setSurfaceView(SurfaceView surfaceView) {
        // SurfaceHolder.Callback 是一个监听SurfaceView的创建过程的接口，需要把这个监听设置在SurfaceView中
        if(null != this.mSurfaceHolder) {
            this.mSurfaceHolder.removeCallback(this);
        }
        this.mSurfaceHolder = surfaceView.getHolder();
        this.mSurfaceHolder.addCallback(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.mSurfaceHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void start(String path) {
        native_start(path, mSurfaceHolder.getSurface());
    }

    private native void native_start(String path, Surface surface);
}
