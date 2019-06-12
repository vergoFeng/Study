//
// Created by John on 2019/6/12.
//

#ifndef FFMPEGDEMO_JAVACALLHELPER_H
#define FFMPEGDEMO_JAVACALLHELPER_H

#include <jni.h>

class JavaCallHelper {
public:
    JavaCallHelper(JavaVM *_javaNm, JNIEnv *_env, jobject &_jobj);
    ~JavaCallHelper();

    void onPrepare(int thread);
    void onProgress(int thread, int progress);
    void onError(int thread, int code);

private:
    JavaVM *javaVM;
    JNIEnv *env;
    jobject jobj;
    jmethodID jmid_prepare;
    jmethodID jmid_progress;
    jmethodID jmid_error;
};


#endif //FFMPEGDEMO_JAVACALLHELPER_H
