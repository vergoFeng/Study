//
// 音视频播放
// Created by Huijun on 2019/6/12.
//
#include <jni.h>
#include <string>
#include <android/native_window_jni.h>
//#include <zconf.h>
//#include <unistd.h>

extern "C"{
#include <libavcodec/avcodec.h>
}

#include "FFmpegControl.h"
#include "JavaCallHelper.h"

JavaCallHelper *javaCallHelper;

ANativeWindow *nativeWindow = 0;
FFmpegControl *ffmpegControl;

extern "C"
JNIEXPORT void JNICALL
Java_com_vergo_demo_ffmpeg_WangyiPlayer_native_1set_1surface(JNIEnv *env, jobject instance,
                                                             jobject surface) {
    if(nativeWindow) {
        // 释放掉之前的
        ANativeWindow_release(nativeWindow);
    }
    // 创建新的窗口用于视频显示
    nativeWindow = ANativeWindow_fromSurface(env, surface);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_vergo_demo_ffmpeg_WangyiPlayer_native_1prepare(JNIEnv *env, jobject instance,
                                                        jstring dataSource_) {
    const char *dataSource = env->GetStringUTFChars(dataSource_, 0);

    // 实例化控制层，并初始化
    ffmpegControl = new FFmpegControl(dataSource);
    ffmpegControl->prepare();

    env->ReleaseStringUTFChars(dataSource_, dataSource);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_vergo_demo_ffmpeg_WangyiPlayer_native_1start(JNIEnv *env, jobject instance) {

    // TODO

}