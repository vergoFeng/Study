### 编译FFmpeg

FFmpeg是一个开源的音视频处理程序，也可以把它看成一个用于处理音视频的库。如果需要在Android中使用这个库，那就需要进行交叉编译。

#### 编译环境

NDK：17 [Linux版本下载地址](https://dl.google.com/android/repository/android-ndk-r17c-linux-x86_64.zip)

FFmpeg：4.0.2 [下载地址](https://ffmpeg.org/release/ffmpeg-4.0.2.tar.bz2)

#### 安装NDK环境

> 这里使用的是Win10子系统Ubuntu。

##### 下载NDK

```shell
wget https://dl.google.com/android/repository/android-ndk-r17c-linux-x86_64.zip
```

##### 解压

```shell
unzip android-ndk-r17c-linux-x86_64.zip
```

##### 添加环境变量

1、执行 `vim ~/.bashrc` 命令

2、添加环境变量

```shell
# 配置ndk存放路径
NDKROOT=/home/vergo/android-ndk-r17c
# 追加到PATH路径中
export PATH=$NDKROOT:$PATH
```

3、执行 `source ~/.bashrc` 命令，使配置好的环境变量生效

#### 下载FFmpeg

```shell
wget https://ffmpeg.org/release/ffmpeg-4.0.2.tar.bz2
```

##### 解压

```shell
tar xvf ffmpeg-4.0.2.tar.bz2
```

解压后进入ffmpeg目录，可以看到里面有各种文件、文档。需要关注的是configure 文件，这个文件是一个shell脚本，作用为生成makefile文件，然后使用make执行。

#### 编译

交叉编译shell脚本内容

```shell
#!/bin/bash
NDK_ROOT=/home/vergo/android-ndk-r17c
#TOOLCHAIN 变量指向ndk中的交叉编译gcc所在的目录
TOOLCHAIN=$NDK_ROOT/toolchains/arm-linux-androideabi-4.9/prebuilt/linux-x86_64/
#FLAGS与INCLUDES变量 可以从AS ndk工程的.externativeBuild/cmake/debug/armeabi-v7a/build.ninja中拷贝，需要注意的是**地址**
FLAGS="-isystem $NDK_ROOT/sysroot/usr/include/arm-linux-androideabi -D__ANDROID_API__=21 -g -DANDROID -ffunction-sections -funwind-tables -fstack-protector-strong -no-canonical-prefixes -march=armv7-a -mfloat-abi=softfp -mfpu=vfpv3-d16 -mthumb -Wa,--noexecstack -Wformat -Werror=format-security -std=c++11  -O0 -fPIC"
INCLUDES="-isystem $NDK_ROOT/sources/cxx-stl/llvm-libc++/include -isystem $NDK_ROOT/sources/android/support/include -isystem $NDK_ROOT/sources/cxx-stl/llvm-libc++abi/include"

#执行configure脚本，用于生成makefile
#--prefix : 安装目录
#--enable-small : 优化大小
#--disable-programs : 不编译ffmpeg程序(命令行工具)，我们是需要获得静态(动态)库。
#--disable-avdevice : 关闭avdevice模块，此模块在android中无用
#--disable-encoders : 关闭所有编码器 (播放不需要编码)
#--disable-muxers :  关闭所有复用器(封装器)，不需要生成mp4这样的文件，所以关闭
#--disable-filters :关闭视频滤镜
#--enable-cross-compile : 开启交叉编译（ffmpeg比较**跨平台**,并不是所有库都有这么happy的选项 ）
#--cross-prefix: 看右边的值应该就知道是干嘛的，gcc的前缀 xxx/xxx/xxx-gcc 则给xxx/xxx/xxx-
#--disable-shared --enable-static 不写也可以，默认就是这样的。
#--sysroot: 
#--extra-cflags: 会传给gcc的参数
#--arch: 编译的平台，Android手机是arm
#--target-os: 编译的平台，支持Windows，Ios，Android，Mac, Linux
PREFIX=./android/armeabi-v7a2
./configure \
--prefix=$PREFIX \
--enable-small \
--disable-programs \
--disable-avdevice \
--disable-encoders \
--disable-muxers \
--disable-filters \
--enable-cross-compile \
--cross-prefix=$TOOLCHAIN/bin/arm-linux-androideabi- \
--disable-shared \
--enable-static \
--sysroot=$NDK_ROOT/platforms/android-21/arch-arm \
--extra-cflags="$FLAGS $INCLUDES" \
--extra-cflags="-isysroot $NDK_ROOT/sysroot" \
--arch=arm \
--target-os=android 

make clean
make install

```




