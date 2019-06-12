//
// Created by Huijun on 2019/6/12.
//

#ifndef FFMPEGDEMO_VIDEOCHANNEL_H
#define FFMPEGDEMO_VIDEOCHANNEL_H

#include <pthread.h>
#include "JavaCallHelper.h"
#include "BaseChannel.h"

class VideoChannel : public BaseChannel {
public:
    VideoChannel(int id, AVCodecContext *codecContext, JavaCallHelper *callHelper, AVRational time_base);

    void play() override;
    void stop() override;

private:
    pthread_t pid_decode;
    pthread_t pid_play;
};


#endif //FFMPEGDEMO_VIDEOCHANNEL_H
