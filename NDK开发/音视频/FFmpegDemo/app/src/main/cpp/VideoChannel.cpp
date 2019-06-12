//
// Created by Huijun on 2019/6/12.
//

#include "VideoChannel.h"

VideoChannel::VideoChannel(int id, AVCodecContext *codecContext, JavaCallHelper *callHelper,
                           AVRational time_base) : BaseChannel(id, codecContext, callHelper,
                                                               time_base) {

}

void VideoChannel::play() {
    // 是两个队列处于工作状态
    packet_queue.setWork(1);
    frame_queue.setWork(1);
    isPlaying = true;

    // 解码线程

}

void VideoChannel::stop() {

}
