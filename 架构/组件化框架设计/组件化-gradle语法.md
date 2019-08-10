## 组件化-gradle语法

### 什么是Gradle？

- Gradle构建工具的出现让工程有无线可能
- Gradle的核心是基于 Groovy 脚本语言，Groovy 脚本基于 Java 且拓展了 Java。因此 Gradle 需要依赖 JDK 和 Groovy 库。
- 和 ant、maven 构建有区别，gradle是一种编程思想。


### 配置公共config.gradle

```groovy
ext {
    // 定义一个项目全局变量isLibrary，用于动态切换：组件化模式 / 集成化模式
    // false: 组件化模式（子模块可以独立运行）
    // true : 集成化模式（打包整个项目apk，子模块不可独立运行）
    isLibrary = true

    // 包名，用于存放APT生成的类文件
    packageNameForAPT = "com.vergo.modular.apt"

    // 各组件模块applicationId
    appId = [
            "app"   : "com.vergo.modular.product",
            "user"  : "com.vergo.modular.user"
    ]

    // defaultConfig
    androidConfig = [
            compileSdkVersion   : 28,
            buildToolsVersion   : "29.0.0",
            minSdkVersion       : 19,
            targetSdkVersion    : 28,
            versionCode         : 1,
            versionName         : "1.0",
    ]

    //测试版本和发布版本url地址
    url = [
            "debug"  : "http://www.baidu.com",
            "release": "http://www.google.com"
    ]
    
    supportLibrary = "28.0.0"
    // 配置依赖库
    dependencies = [
            // ********************** android-support **********************
            "appcompat"          : "com.android.support:appcompat-v7:${supportLibrary}",
            "constraint"         : "com.android.support.constraint:constraint-layout:1.1.3",
            "design"             : "com.android.support:design:${supportLibrary}",
            "recyclerview"       : "com.android.support:recyclerview-v7:${supportLibrary}",
            "cardview"           : "com.android.support:cardview-v7:${supportLibrary}"
    ]
}
```

大致分为几个模块：

- isLibrary：表示当前环境是集成化环境还是组件化环境，正式版本是集成化环境，值为true。开发过程中可以切换环境，方便测试
- androidConfig：全局配置当前sdk版本和app版本
- url：配置正式版本和测试版本服务器地址
- dependencies ：配置依赖库

### 子模块Module配置

在它的build.gradle中可以引用上面的配置。

#### 1、集成化环境和组件化环境的切换

```groovy
if(isLibrary) {
    // 集成环境
    apply plugin: 'com.android.library'
} else {
    // 组件环境
    apply plugin: 'com.android.application'
}

android {
    ...
    defaultConfig {
        if(!isLibrary) {
            // 如果是集成化模式，不能有applicationId
            // 组件化模式能独立运行才能有applicationId
            applicationId rootProject.ext.appId.user
        }
        // 这个方法接收三个非空的参数，第一个：确定值的类型，第二个：指定key的名字，第三个：传值（必须是String）
        // 为什么需要定义这个？因为src代码中有可能需要用到跨模块交互，如果是组件化模块显然不行
        // 切记：不能在android根节点，只能在defaultConfig或buildTypes节点下
        buildConfigField ("boolean", "isLibrary", String.valueOf(isLibrary))
    }
    sourceSets {
        main {
            if(isLibrary) {
                // 集成化模式，整个项目打包apk
                manifest.srcFile 'src/main/AndroidManifest.xml'

                // release 时 debug 目录下文件不需要合并到主工程
                java {
                    exclude '**/debug/**'
                }
            } else {
                // 组件化模式，需要单独运行时
                manifest.srcFile 'src/main/debug/AndroidManifest.xml'
            }
        }
    }
}
```

#### 2、配置SDK版本和app版本号等

```groovy
android {
    compileSdkVersion rootProject.ext.androidConfig.compileSdkVersion
    buildToolsVersion rootProject.ext.androidConfig.buildToolsVersion
    defaultConfig {
        minSdkVersion rootProject.ext.androidConfig.minSdkVersion
        targetSdkVersion rootProject.ext.androidConfig.targetSdkVersion
        versionCode rootProject.ext.androidConfig.versionCode
        versionName rootProject.ext.androidConfig.versionName
    }
}
```

#### 3、配置本地依赖库和第三方依赖库等

```groovy
def support = rootProject.ext.dependencies

dependencies {
    // 循环引用配置中的依赖
    support.each{ k, v -> implementation v } 
}
```

### app配置

app模块可以看成是应用程序的主模块，我们可以在里面创建业务逻辑，也可以不作任何操作，所有的工作都交给子模块即可。它的配置和module相同点同样需要配置buildConfig，以及第三方依赖库。不同点就是在依赖module的时候，如下：

```groovy
dependencies {
    ...
    // 如果是集成化模式，做发布版本时。各个模块都不能独立运行了
    if(isLibrary) {
        implementation project(':user')
    }
}

```

只有在集成化环境module作为library才能依赖，否则module作为可独立运行的app是不能依赖的。

以上就完成了基础gradle的配置。除了这些还有其他的一些gradle常用配置。

### 其他配置

#### 1、开启分包

```groovy
defaultConfig {
     // 开启分包
     multiDexEnabled true
     // 设置分包配置
     // multiDexKeepFile file('multidex-config.txt')
}
```

#### 2、svg使用v7包兼容

```groovy
defaultConfig {
     // 将svg图片生成 指定维度的png图片
     // vectorDrawables.generatedDensities('xhdpi','xxhdpi')
     // 使用support-v7兼容（5.0版本以上）
     vectorDrawables.useSupportLibrary = true
}
```

#### 3、只保留指定和默认资源

```groovy
defaultConfig {
    // 只保留指定和默认资源
    resConfigs('zh-rCN')
}
```

#### 4、配置so库CPU架构

```groovy
defaultConfig {
    // 配置so库CPU架构（真机：arm，模拟器：x86）
    // x86  x86_64  mips  mips64
    ndk {
        abiFilters('armeabi', 'armeabi-v7a')
        // 为了模拟器启动
        //abiFilters('x86', 'x86_64')
    }
}
```

#### 5、签名配置

```groovy
android {
    ...
    // 签名配置（隐形坑：必须写在buildTypes之前）
    signingConfigs {
        debug {
            // 天坑：填错了，编译不通过还找不到问题
            storeFile file('C:/Users/Administrator/.android/debug.keystore')
            storePassword "android"
            keyAlias "androiddebugkey"
            keyPassword "android"
        }
        release {
            // 签名证书文件
            storeFile file('D:/NetEase/netease.jks')
            // 签名证书的类型
            storeType "netease"
            // 签名证书文件的密码
            storePassword "net163"
            // 签名证书中密钥别名
            keyAlias "netease"
            // 签名证书中该密钥的密码
            keyPassword "net163"
            // 是否开启V2打包
            v2SigningEnabled true
        }
    }

    buildTypes {
        debug {
            // 对构建类型设置签名信息
            signingConfig signingConfigs.debug
        }

        release {
            minifyEnabled false
            // 对构建类型设置签名信息
            signingConfig signingConfigs.release
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

}
```

#### 6、AdbOptions 可以对 adb 操作选项添加配置

```groovy
android {
    ...
    // AdbOptions 可以对 adb 操作选项添加配置
    adbOptions {
        // 配置操作超时时间，单位毫秒
        timeOutInMs = 5 * 1000_0

        // adb install 命令的选项配置
        installOptions '-r', '-s'
    }
}
```

#### 7、dex分包配置

```groovy
android {
    ...
    // 对 dx 操作的配置，接受一个 DexOptions 类型的闭包，配置由 DexOptions 提供
    dexOptions {
        // 配置执行 dx 命令是为其分配的最大堆内存
        javaMaxHeapSize "4g"
        // 配置是否预执行 dex Libraries 工程，开启后会提高增量构建速度，不过会影响 clean 构建的速度，默认 true
        preDexLibraries = false
        // 配置是否开启 jumbo 模式，代码方法是超过 65535 需要强制开启才能构建成功
        jumboMode true
        // 配置 Gradle 运行 dx 命令时使用的线程数量
        threadCount 8
        // 配置multidex参数
        additionalParameters = [
                '--multi-dex', // 多dex分包
                '--set-max-idx-number=50000', // 每个包内方法数上限
                // '--main-dex-list=' + '/multidex-config.txt', // 打包到主classes.dex的文件列表
                '--minimal-main-dex'
        ]
    }
}
```

#### 8、lint检查配置

```groovy
android {
    ...
    // 执行 gradle lint 命令即可运行 lint 检查，默认生成的报告在 outputs/lint-results.html 中
    lintOptions {
        // 遇到 lint 检查错误会终止构建，一般设置为 false
        abortOnError false
        // 将警告当作错误来处理（老版本：warningAsErros）
        warningsAsErrors false
        // 检查新 API
        check 'NewApi'
    }
}
```

#### 9、配置JDK版本

```groovy
android {
    ...
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
```

#### 10、配置apk名称

```groovy
android {
    ...
    // gradle3.0 以后
    android.applicationVariants.all { variant ->
        def buildType = variant.buildType.name
        if (buildType.equals('release')) {
            variant.outputs.all { output ->
                def buildName = "app"
                def releaseApkName = buildName + '-' + buildType + "_V" + versionName + '.apk'
                outputFileName = releaseApkName
            }
        }
    }
}
```



















