//
// Created by Huijun on 2019/6/13.
//

#ifndef FFMPEGDEMO_AUDIOCHANNEL_H
#define FFMPEGDEMO_AUDIOCHANNEL_H

#include "JavaCallHelper.h"
#include <libavcodec/avcodec.h>

class AudioChannel {
public:
    AudioChannel(int id, JavaCallHelper *callHelper, AVCodec *avCodec);
    ~AudioChannel();

    void start();
};


#endif //FFMPEGDEMO_AUDIOCHANNEL_H
