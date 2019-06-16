//
// 音视频播放
// Created by Huijun on 2019/6/12.
//
#include <jni.h>
#include <string>
#include <android/native_window.h>
//#include <zconf.h>
//#include <unistd.h>

#include "FFmpegControl.h"
#include "JavaCallHelper.h"

JavaCallHelper *javaCallHelper;

ANativeWindow *nativeWindow = 0;
FFmpegControl *ffmpegControl;

void renderFrame(uint8_t *data, int linesize, int width, int height) {
    // 渲染

    // 设置缓冲区
    ANativeWindow_setBuffersGeometry(nativeWindow, width, height, WINDOW_FORMAT_RGBA_8888);
    ANativeWindow_Buffer window_buffer;

    if(ANativeWindow_lock(nativeWindow, &window_buffer, 0)) {
        ANativeWindow_release(nativeWindow);
        nativeWindow = 0;
        return;
    }

    // 缓冲区
    uint8_t *window_data = static_cast<uint8_t *>(window_buffer.bits);
    int window_linesize = window_buffer.stride * 4;
    uint8_t *src_data = data;

    for (int i = 0; i < window_buffer.height; ++i) {
        memcpy(window_data + i * window_linesize, src_data + i * linesize, window_linesize);
    }

    // 解锁NativeWindow
    ANativeWindow_unlockAndPost(nativeWindow);
}

// native子线程要回调java层，需将native线程绑定到jvm
JavaVM *javaVM = NULL;
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    javaVM = vm;
    return JNI_VERSION_1_4;
}

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

    javaCallHelper = new JavaCallHelper(javaVM, env, instance);

    // 实例化控制层，并初始化
    ffmpegControl = new FFmpegControl(javaCallHelper, dataSource);
    ffmpegControl->setRenderCallback(renderFrame);
    ffmpegControl->prepare();

    env->ReleaseStringUTFChars(dataSource_, dataSource);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_vergo_demo_ffmpeg_WangyiPlayer_native_1start(JNIEnv *env, jobject instance) {

    // 播放
    if(ffmpegControl) {
        ffmpegControl->start();
    }

}