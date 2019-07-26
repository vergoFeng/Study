package com.vergo.frouter.compiler.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.vergo.froute.annotation.FRouter;
import com.vergo.frouter.compiler.utils.Constants;
import com.vergo.frouter.compiler.utils.EmptyUtils;

import java.io.IOException;
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
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(FRouter.class);

            for (Element element : elements) {
                String packageName = mElements.getPackageOf(element).getQualifiedName().toString();

                String className = element.getSimpleName().toString();

                //最终编译生成的java文件
                String finalClassName = className + "$$ARouter";
                FRouter fRouter = element.getAnnotation(FRouter.class);
                String path = fRouter.path();

                MethodSpec methodSpec = MethodSpec.methodBuilder("getTargetClass")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(Class.class)
                        .addParameter(String.class, "path")
                        .addStatement("return path.equalsIgnoreCase($S) ? $T.class : null",
                                path, ClassName.get((TypeElement) element))
                        .build();

                TypeSpec typeSpec = TypeSpec.classBuilder(finalClassName)
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addMethod(methodSpec)
                        .build();

                JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();

                try {
                    javaFile.writeTo(mFiler);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }
}
