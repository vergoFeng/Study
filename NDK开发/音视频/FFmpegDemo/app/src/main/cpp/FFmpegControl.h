//
// Created by John on 2019/6/12.
//

#ifndef FFMPEGDEMO_FFMPEGCONTROLL_H
#define FFMPEGDEMO_FFMPEGCONTROLL_H

// 线程
#include <pthread.h>
#include <android/native_window_jni.h>
extern "C" {
#include <libavformat/avformat.h>
#include <libavutil/time.h>
};

// 控制层
class FFmpegControl {
public:
    FFmpegControl(const char *dataSource);
    ~FFmpegControl();
    void prepare();
    void prepareFFmpeg();
private:
    char *url;

    // 准备线程
    pthread_t prepare_thread;
    // 总上下文
    AVFormatContext *formatContext;
};


#endif //FFMPEGDEMO_FFMPEGCONTROLL_H
