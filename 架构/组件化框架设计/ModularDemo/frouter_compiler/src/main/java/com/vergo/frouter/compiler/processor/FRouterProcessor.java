package com.vergo.frouter.compiler.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.vergo.froute.annotation.FRouter;
import com.vergo.froute.annotation.enums.RouterType;
import com.vergo.froute.annotation.model.RouterBean;
import com.vergo.frouter.compiler.utils.Constants;
import com.vergo.frouter.compiler.utils.EmptyUtils;

import org.omg.SendingContext.RunTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

// AutoService则是固定的写法，加个注解即可
// 通过auto-service中的@AutoService可以自动生成AutoService注解处理器，用来注册
// 用来生成 META-INF/services/javax.annotation.processing.Processor 文件
@AutoService(Processor.class)
// 允许/支持的注解类型，让注解处理器处理
@SupportedAnnotationTypes({Constants.AROUTER_ANNOTATION_TYPES})
// 指定JDK编译版本
@SupportedSourceVersion(SourceVersion.RELEASE_7)
// 注解处理器接收的参数
@SupportedOptions({Constants.MODULE_NAME, Constants.APT_PACKAGE})
public class FRouterProcessor extends AbstractProcessor {

    // 操作Element工具类 (类、函数、属性都是Element)
    private Elements mElements;
    // type(类信息)工具类，包含用于操作TypeMirror的工具方法
    private Types mTypes;
    // Messager用来报告错误，警告和其他提示信息
    private Messager mMessager;
    // 文件生成器 类/资源，Filter用来创建新的类文件，class文件以及辅助文件
    private Filer mFiler;
    // 子模块名，如：user。需要拼接类名时用到（必传）ARouter$$Group$$user
    private String moduleName;
    // 包名，用于存放APT生成的类文件
    private String packageNameForAPT;

    // 临时map存储，用来存放路由组Group对应的详细Path类对象，生成路由路径类文件时遍历
    // key:组名"app", value:"app"组的路由路径"ARouter$$Path$$app.class"
    private Map<String, List<RouterBean>> tempPathMap = new HashMap<>();

    // 临时map存储，用来存放路由Group信息，生成路由组类文件时遍历
    // key:组名"app", value:类名"ARouter$$Path$$app.class"
    private Map<String, String> tempGroupMap = new HashMap<>();

    // 该方法主要用于一些初始化的操作，通过该方法的参数ProcessingEnvironment可以获取一些列有用的工具类
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElements = processingEnvironment.getElementUtils();
        mTypes = processingEnvironment.getTypeUtils();
        mMessager = processingEnvironment.getMessager();
        mFiler = processingEnvironment.getFiler();

        mMessager.printMessage(Diagnostic.Kind.NOTE, ">>>>init>>>>");

        Map<String, String> map = processingEnvironment.getOptions();
        if(!EmptyUtils.isEmpty(map)) {
            moduleName = processingEnvironment.getOptions().get(Constants.MODULE_NAME);
            packageNameForAPT = processingEnvironment.getOptions().get(Constants.APT_PACKAGE);

            mMessager.printMessage(Diagnostic.Kind.NOTE, "moduleName："+moduleName);
            mMessager.printMessage(Diagnostic.Kind.NOTE, "packageNameForAPT："+packageNameForAPT);
        }

        if(EmptyUtils.isEmpty(moduleName) || EmptyUtils.isEmpty(packageNameForAPT)) {
            throw new RuntimeException("注解处理器需要的参数moduleName或者packageName为空，请在对应build.gradle配置参数");
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if(!EmptyUtils.isEmpty(set)) {
            // 获取所有被 @FRouter 注解的元素集合
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(FRouter.class);
            if(!EmptyUtils.isEmpty(elements)) {
                parseElements(elements);
            }
            return true;
        }
        return false;
    }

    /**
     * 解析注解的元素集合
     * @param elements
     */
    private void parseElements(Set<? extends Element> elements) {
        TypeElement activityElement = mElements.getTypeElement(Constants.ACTIVITY);
        TypeMirror activityMirror = activityElement.asType();

        for (Element element : elements) {
            // 获取每个元素的类信息
            TypeMirror typeMirror = element.asType();
            mMessager.printMessage(Diagnostic.Kind.NOTE, "遍历的元素信息为：" + typeMirror.toString());

            // 获取每个类上面的 @FRouter 注解，对应的path值
            FRouter fRouter = element.getAnnotation(FRouter.class);

            // 路由的详细信息，封装到实体类
            RouterBean routerBean = new RouterBean.Builder()
                    .setGroup(fRouter.group())
                    .setPath(fRouter.path())
                    .setElement(element)
                    .build();

            // 高级判断：ARouter注解仅能用在类之上，并且是规定的 RouterType 类型
            // 类型工具类方法isSubtype，相当于instance一样
            if(mTypes.isSubtype(typeMirror, activityMirror)) {
                routerBean.setType(RouterType.ACTIVITY);
            } else {
                // 不匹配抛出异常，这里谨慎使用！考虑维护问题
                throw new RuntimeException("注解的类型错误");
            }
            // 赋值临时map存储，用来存放路由组Group对应的详细Path类对象
            valueOfPathMap(routerBean);
        }

        // 获取ARouterLoadGroup、ARouterLoadPath类型（生成类文件需要实现的接口）
        TypeElement groupTypeElement = mElements.getTypeElement(Constants.FROUTER_GROUP);
        TypeElement pathTypeElement = mElements.getTypeElement(Constants.FROUTER_PATH);

        // 第一步：生成路由组Group对应详细Path类文件，如：ARouter$$Path$$app
        createPathFile(pathTypeElement);

        // 第二步：生成路由组Group类文件（没有第一步，取不到类文件），如：ARouter$$Group$$app
        createGroupFile(groupTypeElement, pathTypeElement);
    }

    /**
     * 赋值临时map存储，用来存放路由组Group对应的详细Path类对象，生成路由路径类文件时遍历
     *
     * @param routerBean 路由详细信息，最终实体封装类
     */
    private void valueOfPathMap(RouterBean routerBean) {
        if(checkRouterPath(routerBean)) {
            List<RouterBean> routerBeans = tempPathMap.get(routerBean.getGroup());
            if(EmptyUtils.isEmpty(routerBeans)) {
                routerBeans = new ArrayList<>();
                routerBeans.add(routerBean);
                tempPathMap.put(routerBean.getGroup(), routerBeans);
            } else {
                routerBeans.add(routerBean);
//                for (RouterBean bean : routerBeans) {
//                    if(!routerBean.getPath().equalsIgnoreCase(bean.getPath())) {
//                        routerBeans.add(routerBean);
//                    }
//                }
            }
        } else {
            mMessager.printMessage(Diagnostic.Kind.ERROR, "@FRouter注解未按规范配置，如：/user/UserActivity");
        }
    }

    /**
     * 校验@ARouter注解的值，如果group未填写就从必填项path中截取数据
     *
     * @param routerBean 路由详细信息，最终实体封装类
     */
    private boolean checkRouterPath(RouterBean routerBean) {
        String group = routerBean.getGroup();
        String path = routerBean.getPath();

        // @ARouter注解中的path值，必须要以 / 开头（模仿阿里Arouter规范）
        if (EmptyUtils.isEmpty(path) || !path.startsWith("/")) {
            mMessager.printMessage(Diagnostic.Kind.ERROR, "@ARouter注解中的path值，必须要以 / 开头");
            return false;
        }

        // 比如开发者代码为：path = "/MainActivity"，最后一个 / 符号必然在字符串第1位
        if (path.lastIndexOf("/") == 0) {
            // 架构师定义规范，让开发者遵循
            mMessager.printMessage(Diagnostic.Kind.ERROR, "@ARouter注解未按规范配置，如：/user/UserActivity");
            return false;
        }

        // 从第一个 / 到第二个 / 中间截取，如：/app/MainActivity 截取出 app 作为group
        String finalGroup = path.substring(1, path.indexOf("/", 1));

        if(finalGroup.contains("/")) {
            // 比如开发者代码为：path = "/user/login/LoginActivity"
            mMessager.printMessage(Diagnostic.Kind.ERROR, "@ARouter注解未按规范配置，如：/user/UserActivity");
            return false;
        }

        // @ARouter注解中的group有赋值情况
        if (!EmptyUtils.isEmpty(group) && !group.equals(moduleName)) {
            // 架构师定义规范，让开发者遵循
            mMessager.printMessage(Diagnostic.Kind.ERROR, "@ARouter注解中的group值必须和子模块名一致！");
            return false;
        } else {
            routerBean.setGroup(finalGroup);
        }

        return true;
    }

    /**
     * 生成路由组Group对应详细Path，如：FRouter$$Path$$app
     *
     * @param pathTypeElement FRouterLoadPath接口信息
     */
    private void createPathFile(TypeElement pathTypeElement) {
        if(EmptyUtils.isEmpty(tempPathMap)) return;
        // 1、构建方法
        // 方法的返回值Map<String, RouterBean>
        TypeName methodReturn = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouterBean.class));

        // 遍历分组，每一个分组创建一个路径类文件，如：FRouter$$Path$$user
        for (Map.Entry<String, List<RouterBean>> stringListEntry : tempPathMap.entrySet()) {
            // 方法配置：public Map<String, RouterBean> loadPath()
            MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder(Constants.PATH_METHOD_NAME)
                    .addAnnotation(Override.class)  // 重写注解
                    .addModifiers(Modifier.PUBLIC)  // public修饰符
                    .returns(methodReturn);         // 返回值
            // 遍历之前：Map<String, RouterBean> pathMap = new HashMap<>();
            methodSpecBuilder.addStatement("$T<$T, $T> $N = new $T<>()",
                    ClassName.get(Map.class),
                    ClassName.get(String.class),
                    ClassName.get(RouterBean.class),
                    Constants.PATH_PARAMETER_NAME,
                    HashMap.class);

            List<RouterBean> pathList = stringListEntry.getValue();
            // 方法内容配置（遍历每个分组中每个路由详细路径）
            /*
            pathMap.put("/user/UserActivity",
                RouterBean.create(RouterType.ACTIVITY, UserActivity.class,
                        "/user/UserActivity", "user"));
             */
            for (RouterBean routerBean : pathList) {
                methodSpecBuilder.addStatement("$N.put($S, $T.create($T.$L, $T.class, $S, $S))",
                        Constants.PATH_PARAMETER_NAME,
                        routerBean.getPath(),
                        ClassName.get(RouterBean.class),
                        ClassName.get(RouterType.class),
                        routerBean.getType(),
                        ClassName.get((TypeElement) routerBean.getElement()),
                        routerBean.getPath(),
                        routerBean.getGroup());
            }

            methodSpecBuilder.addStatement("return $N", Constants.PATH_PARAMETER_NAME);

            // 2、构建类
            String finalClassName = Constants.PATH_FILE_NAME + stringListEntry.getKey();
            TypeSpec typeSpec = TypeSpec.classBuilder(finalClassName)
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(ClassName.get(pathTypeElement))
                    .addMethod(methodSpecBuilder.build())
                    .build();

            // 3、构建包
            JavaFile javaFile = JavaFile.builder(packageNameForAPT, typeSpec)
                    .build();

            // 4、写入到文件生成器
            try {
                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 非常重要一步！！！！！路径文件生成出来了，才能赋值路由组tempGroupMap
            tempGroupMap.put(stringListEntry.getKey(), finalClassName);
        }
    }

    private void createGroupFile(TypeElement groupTypeElement, TypeElement pathTypeElement) {

    }
}
