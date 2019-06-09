package com.vergo.demo.ffmpeg;

import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class WangyiPlayer implements SurfaceHolder.Callback{
    static {
        System.loadLibrary("player-lib");
    }
    private SurfaceHolder surfaceHolder;
    public void setSurfaceView(SurfaceView surfaceView) {
        if (null != this.surfaceHolder) {
            this.surfaceHolder.removeCallback(this);
        }
        this.surfaceHolder = surfaceView.getHolder();
        this.surfaceHolder.addCallback(this);

    }
    public void start(final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                native_start(path, surfaceHolder.getSurface());
            }
        }).start();
    }

    public void soundDecode(final String inputPath, final String outputPath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                native_sound(inputPath, outputPath);
            }
        }).start();

    }

    public  native void native_start(String path, Surface surface);

    public  native void native_sound(String inputPath, String outputPath);

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        this.surfaceHolder = surfaceHolder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
