//
// Created by Huijun on 2019/6/12.
//

#ifndef FFMPEGDEMO_MACRO_H
#define FFMPEGDEMO_MACRO_H

// 标记线程，因为子线程需要attach
#define THREAD_MAIN 1
#define THREAD_CHILD 2

// 错误代码
// 打不开视频
#define FFMEPG_CAN_NOT_OPEN_URL 1
// 找不到视频流
#define FFMEPG_CAN_NOT_FIND_STREAMS 2
// 找不到解码器
#define FFMEPG_FIND_DECODER_FAIL 3
// 无法根据解码器创建上下文
#define FFMEPG_ALLOC_CODEC_CONTEXT_FAIL 4
// 根据流信息，配置上下文参数失败
#define FFMEPG_CODEC_CONTEXT_PARAMETERS_FAIL 6
// 无打开解码器失败
#define FFMEPG_OPEN_DECODER_FAIL 7
// 没有音视频
#define FFMEPG_NOMEDIA 8


#endif //FFMPEGDEMO_MACRO_H
