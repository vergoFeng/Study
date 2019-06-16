//
// Created by Huijun on 2019/6/12.
//

#ifndef FFMPEGDEMO_VIDEOCHANNEL_H
#define FFMPEGDEMO_VIDEOCHANNEL_H

#include <pthread.h>
#include "JavaCallHelper.h"
#include "BaseChannel.h"

extern "C" {
#include <libavutil/imgutils.h>
};

/**
 * 定义接口RenderFrame
 * 参数一：srcSlice[]，rgb数据
 * 参数二：srcStride[]，rgb一行大小
 * 参数三，四：图片宽高
 */
typedef void (*RenderFrame)(uint8_t *, int, int, int);

class VideoChannel : public BaseChannel {
public:
    VideoChannel(int id, AVCodecContext *codecContext, JavaCallHelper *callHelper, AVRational time_base);

    void play() override;
    void stop() override;
    void decodePacket();
    void synchronizeFrame();
    void setRenderCallback(RenderFrame renderFrame);

private:
    // 解码线程
    pthread_t pid_decode_packet;
    // 播放线程
    pthread_t pid_synchronize;
    RenderFrame renderFrame;
};


#endif //FFMPEGDEMO_VIDEOCHANNEL_H
