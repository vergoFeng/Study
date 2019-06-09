#include <jni.h>
#include <string>
#include <android/native_window_jni.h>
#include <zconf.h>
#include <unistd.h>

extern "C"{
#include "libavcodec/avcodec.h"
#include <libswscale/swscale.h>
#include <libavutil/imgutils.h>
#include <libavformat/avformat.h>
}

extern "C"
JNIEXPORT void JNICALL
Java_com_vergo_demo_ffmpeg_WangyiPlayer_native_1start(JNIEnv *env, jobject instance, jstring path_,
                                                      jobject surface) {
    const char *path = env->GetStringUTFChars(path_, 0);

    // 初始化FFmpeg的网络模块
    avformat_network_init();

    // 总上下文AVFormatContext
    AVFormatContext *avFormatContext = avformat_alloc_context();

    AVDictionary *param = NULL;
    // 设置超时时间，单位是微秒，1秒=1000000微秒
    av_dict_set(&param, "timeout", "3000000", 0);
    // 有个int返回值，0：返回成功；非0：返回失败
    int ret = avformat_open_input(&avFormatContext, path, NULL, &param);
    if (ret) {
        return;
    }

    // 寻找视频流
    int video_stream_idx = -1;
    // 通知FFmpeg将流解析出来
    avformat_find_stream_info(avFormatContext, NULL);
    for (int i = 0; i < avFormatContext->nb_streams; ++i) {
        if (avFormatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            // 如果这个流的类型是视频流，记录视频流的索引
            video_stream_idx = i;
            break;
        }
    }
    // 获取视频流的解码参数
    // AVCodecParameters里面有对当前视频的宽度，高度，延迟时间等的描述
    AVCodecParameters *codecpar = avFormatContext->streams[video_stream_idx]->codecpar;

    // 获取解码器
    AVCodec *avCodec = avcodec_find_decoder(codecpar->codec_id);
    // 解码器的上下文AVCodecContext
    AVCodecContext *avCodecContext = avcodec_alloc_context3(avCodec);
    // 将获取视频流的解码参数传入解码器上下文
    avcodec_parameters_to_context(avCodecContext, codecpar);
    // 打开解码器
    avcodec_open2(avCodecContext, avCodec, NULL);

    // 解码，
    // 在解码过程中是看不到相关yuv的数据的，因为被封装成了AVPacket，所以要读取yuv数据必须从AVPacket中找
    // 实例化AVPacket对象
    AVPacket *avPacket = av_packet_alloc();

    // avCodecContext->pix_fmt当前视频的编码格式
    // flags：转换方式：
    //  重视速度：fast_bilinear, point
    //  重视质量：gauss, bilinear
    //  重视锐度：cubic, spline, lanczos
    //  得到一个转码上下文SwsContext
    SwsContext *swsContext = sws_getContext(avCodecContext->width, avCodecContext->height,
                                            avCodecContext->pix_fmt,
                                            avCodecContext->width, avCodecContext->height,
                                            AV_PIX_FMT_RGBA, SWS_BILINEAR,
                                            0, 0, 0);

    ANativeWindow *nativeWindow = ANativeWindow_fromSurface(env, surface);
    // 设置缓冲区
    ANativeWindow_setBuffersGeometry(nativeWindow, avCodecContext->width, avCodecContext->height,
                                     WINDOW_FORMAT_RGBA_8888);
    ANativeWindow_Buffer outBuffer;

    AVFrame *avFrame = av_frame_alloc();
    // 接收的容器
    uint8_t *dst_data[0];
    // 每一行的首地址
    int dst_linesize[0];
    // 声明一个图片
    av_image_alloc(dst_data, dst_linesize, avCodecContext->width, avCodecContext->height,
                   AV_PIX_FMT_RGBA, 1);

    // av_read_frame 从视频流中读取数据包到AVPacket中
    // 返回值0：读取成功；< 0：读取错误或者到文件末尾
    while (av_read_frame(avFormatContext, avPacket) >= 0) {
        avcodec_send_packet(avCodecContext, avPacket);

        ret = avcodec_receive_frame(avCodecContext, avFrame);
        if (ret == AVERROR(EAGAIN)) {
            continue;
        } else if (ret < 0) {
            break;
        }

        if (avPacket->stream_index == video_stream_idx) {
            if (ret==0) {
                // 锁住NativeWindow
                ANativeWindow_lock(nativeWindow, &outBuffer, NULL);
                // 绘制
                sws_scale(swsContext, avFrame->data, avFrame->linesize, 0, avFrame->height, dst_data,
                          dst_linesize);

                // 缓冲区渲染
                uint8_t *firstWindow = static_cast<uint8_t *>(outBuffer.bits);
                // 输入源（rgb）
                uint8_t *src_data = dst_data[0];
                // 拿到一行有多少个字节 RGBA
                int dstStride = outBuffer.stride * 4;
                int src_linesize = dst_linesize[0];
                for (int i = 0; i < outBuffer.height; ++i) {
                    // 内存拷贝，来进行渲染，一行一行拷贝
                    memcpy(firstWindow + i * dstStride, src_data + i * src_linesize, dstStride);
                }

                // 解锁NativeWindow
                ANativeWindow_unlockAndPost(nativeWindow);
                usleep(1000 * 16);
            }
        }
    }
    av_free(&outBuffer);
    av_frame_free(&avFrame);
    ANativeWindow_release(nativeWindow);
    avcodec_close(avCodecContext);
    avformat_free_context(avFormatContext);

    env->ReleaseStringUTFChars(path_, path);
}