//
// Created by Huijun on 2019/6/12.
//

#include "VideoChannel.h"

VideoChannel::VideoChannel(int id, AVCodecContext *codecContext, JavaCallHelper *callHelper,
                           AVRational time_base) : BaseChannel(id, codecContext, callHelper,
                                                               time_base) {

}

void *decode(void *args ) {
    VideoChannel *videoChannel = static_cast<VideoChannel *>(args);
    videoChannel->decodePacket();
    return 0;
}

void *synchronize(void *args ) {
    VideoChannel *videoChannel = static_cast<VideoChannel *>(args);
    videoChannel->synchronizeFrame();
    return 0;
}

void VideoChannel::play() {
    // 是两个队列处于工作状态
    packet_queue.setWork(1);
    frame_queue.setWork(1);
    isPlaying = true;

    // 创建解码线程
    pthread_create(&pid_decode_packet, NULL, decode, this);
    // 创建播放线程
    pthread_create(&pid_synchronize, NULL, synchronize, this);

}

void VideoChannel::stop() {

}

void VideoChannel::decodePacket() {
    AVPacket *packet = av_packet_alloc();
    while (isPlaying) {
        if(frame_queue.size() > 100) {
            av_usleep(10 * 1000);
            continue;
        }

        int ret = packet_queue.deQueue(packet);
        if(!isPlaying) {
            break;
        }
        if(!ret) {
            continue;
        }

        // 解码成frame
        ret = avcodec_send_packet(codecContext, packet);
        // 释放掉packet
        releasePacket(packet);
        if(ret == AVERROR(EAGAIN)) {
            // 需要更多数据
            continue;
        } else if(ret < 0) {
            // 失败
            break;
        }

        AVFrame *frame = av_frame_alloc();
        ret = avcodec_receive_frame(codecContext, frame);
        if(ret == 0) {
            frame_queue.enQueue(frame);
        }
    }
    // 保险起见
    releasePacket(packet);
}

void VideoChannel::synchronizeFrame() {
    // 转换器上下文
    SwsContext *swsContext = sws_getContext(codecContext->width, codecContext->height, codecContext->pix_fmt,
                                            codecContext->width, codecContext->height, AV_PIX_FMT_RGBA,
                                            SWS_BILINEAR, 0, 0, 0);
    // 接收的容器
    uint8_t *dst_data[4];
    // 每一行的首地址
    int dst_linesize[4];
    // 声明一个图片
    av_image_alloc(dst_data, dst_linesize, codecContext->width, codecContext->height, AV_PIX_FMT_RGBA, 1);

    AVFrame *frame = 0;
    while (isPlaying) {
        int ret = frame_queue.deQueue(frame);
        if(!isPlaying) {
            break;
        }
        if(!ret) {
            continue;
        }

        // 转换
        sws_scale(swsContext, reinterpret_cast<const uint8_t *const *>(frame->data),
                frame->linesize, 0, frame->height, dst_data, dst_linesize);
        // 绘制，回调到控制层
        renderFrame(dst_data[0], dst_linesize[0], codecContext->width, codecContext->height);
        // 延迟16ms
        av_usleep(16 * 1000);

        releaseFrame(frame);
    }

    av_free(&dst_data[0]);
    isPlaying = 0;
    releaseFrame(frame);
    sws_freeContext(swsContext);;
}

void VideoChannel::setRenderCallback(RenderFrame renderFrame) {
    this->renderFrame = renderFrame;
}
