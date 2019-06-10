## FFmpeg命令详解

### FFmpeg常用命令

- 基本信息查询命令
- 录制命令
- 处理原始数据
- 裁剪与合并命令
- 直播命令
- 各种滤镜命令

### 录制命令

```
ffmpeg -f gdigrab -framerate 30 -offset_x 0 -offset_y 0 -video_size 1920*1080 -i desktop out.mpg
```

gdigrab：表明是通过gdi（windows模式视频处理）抓屏的方式；（mac下 avfoundation）

-framerate 30：表示录制的帧率为30；

-offset_x：左上偏移量x；

-offset_y：左上偏移量y；

-video_size：需要录制的宽度和高度；

-i：输入路径和名称以及各式mpg；

desktop：告诉ffmpeg，录制的是屏幕，而不是一个窗口（可以录制一个窗口，不过得用窗口的ID）

### 分解复用命令

将完整的视频文件进行拆分。将拆分的信息作为素材，合成所需要的新视频

**第一步（抽取音频流）**

```
ffmpeg -i input.mp4 -acodec copy -vn out.aac
```

-acodec：指定音频编码器

copy：指明只拷贝，不做编解码

-vn：v代表视频，n代表 no 也就是无视频的意思。

**第二步（抽取视频流）**

```
ffmpeg -i input.mp4 -vcodec copy -an out.h264
```

-vcodec：指定视频编码器

copy：指明只拷贝，不做编解码

-an：a代表音频，n代表 no 也就是无音频的意思。

**第三步（合成视频）**

```
ffmpeg -i out.h264 -i out.aac -vcodec copy -acodec copy out.mp4
```

```
视频格式转换
ffmpeg -i input.mp4 -vcodec copy -acodec copy out.flv
```

### 处理原始数据

获取未经过编码的画面和音频。

画面信息一般是yuv；音频信息一般是pcm。

**提取YUV数据**

```
ffmpeg -i input.mp4 -an -c:v rawvideo -pix_fmt yuv420p out.yuv
```

-c:v rawvideo：指定将视频转成原始数据

-pix_fmt yuv420p：指定转换格式为yuv420p

> 未经过编码的数据需要用到ffplay播放（ffplay -s 608*368 out.yuv）
>
> 如果报错："SDL_OpenAudio (2 channels, 44100 Hz): WASAPI can't initialize audio client"
> 需要先设置环境一个变量SDL_AUDIODRIVER=directsound

**提取PCM数据**

```
ffmpeg -i input.mp4 -vn -ar 44100 -ac 2 -f s16le out.pcm
```

-ar：指定音频采样率，44100即44.1KHz

-ac：指定音频声道channel， 2为双声道

-f：数据存储格式（例如 s16le），s：Signed 有符号的，16：每一个数值用16位表示，l：little，e：end

> 未经过编码的数据需要用到ffplay播放（ffplay -ar 44100 -ac 2 -f s16le out.pcm）

### FFmpeg滤镜

原视频 —> 编码数据包 —> 修改的数据帧 —> 编码数据包 —> 原视频

#### 裁剪滤镜

```
ffmpeg -i input.mp4 -vf crop=in_w-200:in_h-200 -c:v libx264 -c:a copy crop.mp4
```

[滤镜详解](滤镜解析.md)