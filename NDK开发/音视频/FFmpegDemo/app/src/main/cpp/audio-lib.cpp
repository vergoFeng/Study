////
//// 音频解码demo
//// Created by Huijun on 2019/6/9.
////
//#include <jni.h>
//#include <string>
//#include <android/native_window_jni.h>
//#include <zconf.h>
//#include <unistd.h>
//
//extern "C"{
//// 解码
//#include "libavcodec/avcodec.h"
//// 缩放
//#include <libswscale/swscale.h>
//// 封装格式
//#include <libavformat/avformat.h>
//// 重采样
//#include <libswresample/swresample.h>
//}
//
//extern "C"
//JNIEXPORT void JNICALL
//Java_com_vergo_demo_ffmpeg_WangyiPlayer_native_1sound(JNIEnv *env, jobject instance,
//                                                      jstring inputPath_, jstring outputPath_) {
//    const char *inputPath = env->GetStringUTFChars(inputPath_, 0);
//    const char *outputPath = env->GetStringUTFChars(outputPath_, 0);
//
//    // 初始化FFmpeg的网络模块
//    avformat_network_init();
//
//    // 总上下文AVFormatContext
//    AVFormatContext *avFormatContext = avformat_alloc_context();
//
//    AVDictionary *param = NULL;
//    // 设置超时时间，单位是微秒，1秒=1000000微秒
//    av_dict_set(&param, "timeout", "3000000", 0);
//    // 有个int返回值，0：返回成功；非0：返回失败
//    int ret = avformat_open_input(&avFormatContext, inputPath, NULL, &param);
//    if (ret) {
//        return;
//    }
//
//    // 寻找音频流
//    int audio_stream_idx = -1;
//    // 通知FFmpeg将流解析出来
//    avformat_find_stream_info(avFormatContext, NULL);
//    for (int i = 0; i < avFormatContext->nb_streams; ++i) {
//        if (avFormatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
//            // 如果这个流的类型是音频流，记录音频流的索引
//            audio_stream_idx = i;
//            break;
//        }
//    }
//
//    AVCodecParameters *codecpar = avFormatContext->streams[audio_stream_idx]->codecpar;
//    AVCodec *avCodec = avcodec_find_decoder(codecpar->codec_id);
//    AVCodecContext *avCodecContext = avcodec_alloc_context3(avCodec);
//    avcodec_parameters_to_context(avCodecContext, codecpar);
//    avcodec_open2(avCodecContext, avCodec, NULL);
//
//    AVPacket *avPacket = av_packet_alloc();
//
//    AVFrame *avFrame = av_frame_alloc();
//
//    // 音频转换上下文
//    SwrContext *swrContext = swr_alloc();
//
//    // 输入的参数：
//    // 采样位数
//    AVSampleFormat in_sample_fmt = avCodecContext->sample_fmt;
//    // 采样频率
//    int in_sample_rate = avCodecContext->sample_rate;
//    // 通道数
//    uint64_t in_channel_layout = avCodecContext->channel_layout;
//
//    // 输出的参数：
//    // 采样位数
//    AVSampleFormat out_sample_fmt = AV_SAMPLE_FMT_S16;
//    // 采样频率
//    int out_sample_rate = 44100;
//    // 通道数
//    uint64_t out_channel_layout = AV_CH_LAYOUT_STEREO;
//
//    // 设置参数
//    swr_alloc_set_opts(swrContext, out_channel_layout, out_sample_fmt, out_sample_rate,
//                       in_channel_layout, in_sample_fmt, in_sample_rate, 0, NULL);
//
//    // 初始化转换器其他的默认参数
//    swr_init(swrContext);
//
//    // 初始化缓冲区
//    uint8_t  *out_buffer = static_cast<uint8_t *>(av_malloc(2 * 44100));
//    FILE *fp_pcm = fopen(outputPath, "wb");
//
//    while (av_read_frame(avFormatContext, avPacket) >= 0) {
//        avcodec_send_packet(avCodecContext, avPacket);
//
//        ret = avcodec_receive_frame(avCodecContext, avFrame);
//        if (ret == AVERROR(EAGAIN)) {
//            continue;
//        } else if (ret < 0) {
//            break;
//        }
//
//        if (avPacket->stream_index == audio_stream_idx) {
//            // 将一帧frame转换为统一的格式
//            swr_convert(swrContext, &out_buffer, 2 * 44100,
//                    (const uint8_t **)(avFrame->data), avFrame->nb_samples);
//
//            // ------------将缓冲区输出到文件中-------------
//            int out_channel_nb = av_get_channel_layout_nb_channels(out_channel_layout);
//            // 缓冲区大小
//            int out_buffer_size = av_samples_get_buffer_size(NULL, out_channel_nb, avFrame->nb_samples, out_sample_fmt, 1);
//
//            fwrite(out_buffer, 1, out_buffer_size, fp_pcm);
//        }
//    }
//    fclose(fp_pcm);
//    av_free(out_buffer);
//    swr_free(&swrContext);
//    avcodec_close(avCodecContext);
//    avformat_close_input(&avFormatContext);
//
//    env->ReleaseStringUTFChars(inputPath_, inputPath);
//    env->ReleaseStringUTFChars(outputPath_, outputPath);
//}