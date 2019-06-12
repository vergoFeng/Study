//
// Created by Huijun on 2019/6/12.
//

#include "JavaCallHelper.h"
#include "macro.h"

JavaCallHelper::JavaCallHelper(JavaVM *_javaNm, JNIEnv *_env, jobject &_jobj) : javaVM(_javaNm), env(_env) {
    //  : javaVM(_javaNm), env(_env) 等于
    //    this->javaVM = _javaNm;
    //    this->env = _env;

    // 实例化全局的jobj对象
    jobj = env->NewGlobalRef(_jobj);

    // 反射
    jclass jclazz = env->GetObjectClass(jobj);
    // 得到方法的引用，实际上是ArtMethod结构体
    jmid_prepare = env->GetMethodID(jclazz, "onPrepare", "()V");
    jmid_progress = env->GetMethodID(jclazz, "onProgress", "(I)V");
    jmid_error = env->GetMethodID(jclazz, "onError", "(I)V");
}

JavaCallHelper::~JavaCallHelper() {

}

void JavaCallHelper::onPrepare(int thread) {
    if(thread == THREAD_CHILD) {
        JNIEnv *jniEnv;
        // 绑定主线程
        if(javaVM->AttachCurrentThread(&jniEnv, 0) != JNI_OK) {
            return;
        }
        jniEnv->CallVoidMethod(jobj, jmid_prepare);
        // 解绑
        javaVM->DetachCurrentThread();
    } else {
        env->CallVoidMethod(jobj, jmid_prepare);
    }
}

void JavaCallHelper::onProgress(int thread, int progress) {
    if(thread == THREAD_CHILD) {
        JNIEnv *jniEnv;
        // 绑定主线程
        if(javaVM->AttachCurrentThread(&jniEnv, 0) != JNI_OK) {
            return;
        }
        jniEnv->CallVoidMethod(jobj, jmid_progress, progress);
        // 解绑
        javaVM->DetachCurrentThread();
    } else {
        env->CallVoidMethod(jobj, jmid_progress, progress);
    }
}

void JavaCallHelper::onError(int thread, int code) {
    if(thread == THREAD_CHILD) {
        JNIEnv *jniEnv;
        // 绑定主线程
        if(javaVM->AttachCurrentThread(&jniEnv, 0) != JNI_OK) {
            return;
        }
        jniEnv->CallVoidMethod(jobj, jmid_error, code);
        // 解绑
        javaVM->DetachCurrentThread();
    } else {
        env->CallVoidMethod(jobj, jmid_error, code);
    }
}
