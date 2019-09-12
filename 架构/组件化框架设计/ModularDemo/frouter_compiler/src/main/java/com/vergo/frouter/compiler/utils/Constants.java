package com.vergo.frouter.compiler.utils;

/**
 * <p>Created by Fenghj on 2019/7/25.</p>
 */
public class Constants {
    // 注解处理器中支持的注解类型
    public static final String AROUTER_ANNOTATION_TYPES = "com.vergo.froute.annotation.FRouter";

    // 每个子模块的模块名
    public static final String MODULE_NAME = "moduleName";
    // 包名，用于存放APT生成的类文件
    public static final String APT_PACKAGE = "packageNameForAPT";

    // String全类名
    public static final String STRING = "java.lang.String";
    // Activity全类名
    public static final String ACTIVITY = "android.app.Activity";

    // 包名前缀封装
    public static final String BASE_PACKAGE = "com.vergo.frouter.api";
    // 路由组Group加载接口
    public static final String FROUTER_GROUP = BASE_PACKAGE + ".template.FRouterLoadGroup";
    // 路由组Group对应的详细Path加载接口
    public static final String FROUTER_PATH = BASE_PACKAGE + ".template.FRouterLoadPath";

    // 路由组Group，参数名
    public static final String GROUP_PARAMETER_NAME = "groupMap";
    // 路由组Group，方法名
    public static final String GROUP_METHOD_NAME = "loadGroup";
    // APT生成的路由组Group类文件名
    public static final String GROUP_FILE_NAME = "FRouter$$Group$$";
    // 路由组Group对应的详细Path，参数名
    public static final String PATH_PARAMETER_NAME = "pathMap";
    // 路由组Group对应的详细Path，方法名
    public static final String PATH_METHOD_NAME = "loadPath";
    // APT生成的路由组Group对应的详细Path类文件名
    public static final String PATH_FILE_NAME = "FRouter$$Path$$";
    // 获取参数，方法名
    public static final String PARAMETER_NAMR = "target";
    // 获取参数，参数名
    public static final String PARAMETER_METHOD_NAME = "loadParameter";
}
