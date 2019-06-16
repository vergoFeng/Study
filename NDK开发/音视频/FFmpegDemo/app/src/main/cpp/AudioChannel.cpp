//
// Created by Huijun on 2019/6/13.
//

#include "AudioChannel.h"

AudioChannel::AudioChannel(int id, AVCodecContext *codecContext, JavaCallHelper *callHelper,
                           AVRational time_base) : BaseChannel(id, codecContext, callHelper,
                                                               time_base) {

}

void AudioChannel::play() {

}

void AudioChannel::stop() {

}