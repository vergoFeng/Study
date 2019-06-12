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
//    // 音视频解码demo例子
//    public void start(final String path) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                native_start(path, surfaceHolder.getSurface());
//            }
//        }).start();
//    }
//
//    public void soundDecode(final String inputPath, final String outputPath) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                native_sound(inputPath, outputPath);
//            }
//        }).start();
//
//    }
//
//    public  native void native_start(String path, Surface surface);
//
//    public  native void native_sound(String inputPath, String outputPath);

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        native_set_surface(surfaceHolder.getSurface());
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    public void prepare(String dataSource) {
        native_prepare(dataSource);
    }

    public void start() {
        native_start();
    }

    // =====================native初始化方法=====================
    public native void native_set_surface(Surface surface);
    public native void native_prepare(String dataSource);
    public native void native_start();

    // =====================native回调java层方法=====================
    public void onPrepare() {
        if(mOnPreparelListener != null) mOnPreparelListener.onPrepare();
    }

    public void onProgress(int progress) {
        if(mOnProgressListener != null) mOnProgressListener.onProgress(progress);
    }

    public void onError(int errorCode) {
        if(mOnErrorListener != null) mOnErrorListener.onError(errorCode);
    }

    public interface OnPreparelListener {
        void onPrepare();
    }

    public interface OnProgressListener {
        void onProgress(int progress);
    }

    public interface OnErrorListener {
        void onError(int errorCode);
    }

    private OnPreparelListener mOnPreparelListener;
    private OnProgressListener mOnProgressListener;
    private OnErrorListener mOnErrorListener;

    public void setOnPreparelListener(OnPreparelListener onPreparelListener) {
        mOnPreparelListener = onPreparelListener;
    }

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        mOnProgressListener = onProgressListener;
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        mOnErrorListener = onErrorListener;
    }
}
