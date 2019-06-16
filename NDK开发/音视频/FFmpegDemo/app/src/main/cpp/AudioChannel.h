//
// Created by Huijun on 2019/6/13.
//

#ifndef FFMPEGDEMO_AUDIOCHANNEL_H
#define FFMPEGDEMO_AUDIOCHANNEL_H

#include <pthread.h>
#include "JavaCallHelper.h"
#include "BaseChannel.h"

class AudioChannel : public BaseChannel{
public:
    AudioChannel(int id, AVCodecContext *codecContext, JavaCallHelper *callHelper, AVRational time_base);

    void play() override;
    void stop() override;
};


#endif //FFMPEGDEMO_AUDIOCHANNEL_H
