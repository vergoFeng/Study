//
// Created by John on 2019/5/21.
//
#include <jni.h>
int test() {
    return 123;
}

// com.vergo.demo.ndk
jint Java_com_vergo_demo_ndk_MainActivity_nativeTest() {
    return test();
}

