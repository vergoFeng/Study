//
// Created by Huijun on 2019/6/12.
//

#ifndef FFMPEGDEMO_FFMPEGCONTROLL_H
#define FFMPEGDEMO_FFMPEGCONTROLL_H

// 线程
#include <pthread.h>
#include <android/native_window_jni.h>
#include "JavaCallHelper.h"
#include "macro.h"
#include "VideoChannel.h"
#include "AudioChannel.h"

// 控制层
class FFmpegControl {
public:
    FFmpegControl(JavaCallHelper *pHelper, const char *dataSource);
    ~FFmpegControl();

    void prepare();
    void prepareFFmpeg();
    void start();
    void readFrame();

    void setRenderCallback(RenderFrame renderFrame);

private:
    char *url;
    bool isPlaying; // 是否正在播放中

    // 准备线程
    pthread_t prepare_thread;
    // 解码线程，直到播放完毕才销毁
    pthread_t pid_decode;

    // 总上下文
    AVFormatContext *formatContext;
    // 回调java层
    JavaCallHelper *javaCallHelper;
    // 视频解码播放
    VideoChannel *videoChannel;
    // 音频解码播放
    AudioChannel *audioChannel;

    RenderFrame renderFrame;
};


#endif //FFMPEGDEMO_FFMPEGCONTROLL_H
