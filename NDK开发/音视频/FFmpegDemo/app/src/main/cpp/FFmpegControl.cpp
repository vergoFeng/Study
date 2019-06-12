//
// Created by John on 2019/6/12.
//

#include "FFmpegControl.h"

void *prepareFFmpeg_(void *args) {
    // 无法访问对象的成员变量，这里通过pthread_create中第四个参数，将对象传进来
    // 将args强转为FFmpegControl对象
    FFmpegControl *ffmpegControl = static_cast<FFmpegControl *>(args);
    ffmpegControl->prepareFFmpeg();

    //
    return 0;
}

FFmpegControl::FFmpegControl(const char *dataSource) {
    // 另开辟一个字符串
    url = new char[strlen(dataSource) + 1];
    strcpy(url, dataSource);
}

FFmpegControl::~FFmpegControl() {

}

void FFmpegControl::prepare() {
    // 第三个参数传一个函数，类似于java中thread的run方法
    // 第四个参数是对当前线程传递的参数，会传递到函数的args中
    pthread_create(&prepare_thread, NULL, prepareFFmpeg_, this);
}

void FFmpegControl::prepareFFmpeg() {
    // 在子线程中执行，能访问到对象的属性

    // 初始化网络模块
    avformat_network_init();
    // 获取总上下文
    formatContext = avformat_alloc_context();

    AVDictionary *opts = NULL;
    // 设置超时时间3秒
    av_dict_set(&opts, "timeout", "3000000", 0);
    // 打开视频url
    int ret = avformat_open_input(&formatContext, url, NULL, &opts);
    if (ret != 0) {
        // 打开返回失败，反射java层
        return;
    }

    for (int i = 0; i < formatContext->nb_streams; ++i) {
        // 流的解码参数
        AVCodecParameters *codecParameters = formatContext->streams[i]->codecpar;
        // 解码器
        AVCodec *codec = avcodec_find_decoder(codecParameters->codec_id);
    }
}
