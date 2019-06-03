#include <jni.h>
#include <string>

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_vergo_demo_ffmpeg_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(av_version_info());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_vergo_demo_ffmpeg_FFmpegPlayer_native_1start(JNIEnv *env, jobject instance, jstring path_,
                                                      jobject surface) {
    const char *path = env->GetStringUTFChars(path_, 0);
    // 调用FFmpeg方法进行视频的绘制

    // 初始化FFmpeg的网络模块
    avformat_network_init();

    // 总上下文
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
        if(avFormatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
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
    // 解码器的上下文
    AVCodecContext *avCodecContext = avcodec_alloc_context3(avCodec);
    // 将获取视频流的解码参数传入解码器上下文
    avcodec_parameters_to_context(avCodecContext, codecpar);
    // 打开解码器
    avcodec_open2(avCodecContext, avCodec, NULL);

    // 解码，从AVPacket中获取yuv数据
    // 实例化AVPacket对象
    AVPacket *avPacket = av_packet_alloc();
    // 从视频流中读取数据包到AVPacket中
    // 返回值0：读取成功；<0：读取错误或者到文件末尾
    av_read_frame(avFormatContext, avPacket);
//    avcodec_send_packet(avCodecContext, avPacket);

    env->ReleaseStringUTFChars(path_, path);
}