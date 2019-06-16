//
// Created by Huijun on 2019/6/13.
//

#ifndef FFMPEGDEMO_BASECHANNEL_H
#define FFMPEGDEMO_BASECHANNEL_H

#include "safe_queue.h"
#include "JavaCallHelper.h"

extern "C"{
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libswscale/swscale.h>
#include <libavutil/time.h>
};

class BaseChannel {
public:
    BaseChannel(int id, AVCodecContext *codecContext, JavaCallHelper *callHelper,
            AVRational time_base) : channelId(id),codecContext(codecContext),callHelper(callHelper) {

    }

    virtual ~BaseChannel() {
        if(codecContext) {
            avcodec_close(codecContext);
            avcodec_free_context(&codecContext);
            codecContext = 0;
        }
        packet_queue.clear();
        frame_queue.clear();
    }

    static void releasePacket(AVPacket *&packet) {
        if(packet) {
            av_packet_free(&packet);
            packet = 0;
        }
    }
    static void releaseFrame(AVFrame *&frame) {
        if(frame) {
            av_frame_free(&frame);
            frame = 0;
        }
    }

    // 虚函数，播放和暂停
    virtual void play() = 0;
    virtual void stop() = 0;

    // 队列
    SafeQueue<AVPacket *> packet_queue;
    SafeQueue<AVFrame *> frame_queue;

    // 会遇到多线程的问题，所以用volatile修饰
    volatile int channelId;
    volatile bool isPlaying;

    AVCodecContext *codecContext;
    JavaCallHelper *callHelper;
};

#endif //FFMPEGDEMO_BASECHANNEL_H
