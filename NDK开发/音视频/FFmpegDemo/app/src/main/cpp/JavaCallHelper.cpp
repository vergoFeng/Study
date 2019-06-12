//
// Created by John on 2019/6/12.
//

#include "JavaCallHelper.h"

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

}

void JavaCallHelper::onProgress(int thread, int progress) {

}

void JavaCallHelper::onError(int thread, int code) {

}
