#include <jni.h>
#include <string>
#include <android/log.h>
//#include <fmod.hpp>
//
//using namespace FMOD;

extern "C" JNIEXPORT jstring JNICALL
Java_com_vergo_demo_cmakedemo_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {

//    System *system;
//    System_Create(&system);
//    unsigned int version;
//    system->getVersion(&version);
    __android_log_print(ANDROID_LOG_ERROR, "fhj", "FMOD Version:");

    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
