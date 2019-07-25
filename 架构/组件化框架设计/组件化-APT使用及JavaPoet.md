## 组件化-APT使用及JavaPoet

###  一、APT

APT(Annotation Processing Tool)

是一种处理注解的工具，它对源代码文件进行检测找出其中的 Annotation，根据注解自动生成代码，如果想要自定义的注解处理器能够正常运行，必须要通过APT工具来进行处理。

也可以这样理解，只有通过声明APT工具后，程序在编译期自定义注解解释器才能执行。

#### 常用API

| 属性名                | 说明                                                |
| --------------------- | --------------------------------------------------- |
| getEnclosedElements() | 返回该元素直接包含的子元素                          |
| getEnclosingElement() | 返回包含该element的父element，与上一个方法相反      |
| getKind()             | 返回element的类型，判断是哪种element                |
| getModifiers()        | 获取修饰关键字，如public static final等关键字       |
| getSimpleName()       | 获取名字，不带包名                                  |
| getQualifiedName()    | 获取全名，如果是类的话，包含完整的包名路径          |
| getParameters()       | 获取方法的参数元素，每一个元素是一个VariableElement |
| getReturnType()       | 获取方法元素的返回值                                |
| getConstantValue()    | 如果属性变量被final修饰，则可以使用该方法获取它的值 |

#### 使用

##### 1、创建注解的 Java Library，命名为annotation

创建注解类，命名ARouter.java。

```java
@Target(ElementType.TYPE)// 该注解作用在类之上
@Retention(RetentionPolicy.CLASS) // 要在编译时进行一些预处理操作，注解会在class文件中存在
public @interface ARouter {
    String path();
}
```

##### 2、创建注解处理器的 Java Library，命名为compiler

**（1）添加依赖**

在当前library的build.gradle中添加如下依赖：

```groovy
// As-3.4.1 + gradle5.1.1-all + auto-service:1.0-rc4
compileOnly'com.google.auto.service:auto-service:1.0-rc4'
annotationProcessor'com.google.auto.service:auto-service:1.0-rc4'
```

**（2）创建注解处理器类**

创建ARouterProcesspr.java类，继承至AbstractProcessor，代码如下：

```java
// AutoService则是固定的写法，加个注解即可
// 通过auto-service中的@AutoService可以自动生成AutoService注解处理器，用来注册
// 用来生成 META-INF/services/javax.annotation.processing.Processor 文件
@AutoService(Processor.class)
// 允许/支持的注解类型，让注解处理器处理
@SupportedAnnotationTypes({"com.xinyartech.annotation.ARouter"})
// 指定JDK编译版本
@SupportedSourceVersion(SourceVersion.RELEASE_7)
// 注解处理器接收的参数
@SupportedOptions("content")
public class ARouterCompiler extends AbstractProcessor {

    // 操作Element工具类 (类、函数、属性都是Element)
    private Elements elementUtils;
    // type(类信息)工具类，包含用于操作TypeMirror的工具方法
    private Types typeUtils;
    // 文件生成器 类/资源，Filter用来创建新的类文件，class文件以及辅助文件
    private Filer mFiler;
    // Messager用来报告错误，警告和其他提示信息
    private Messager mMessager;

    // 该方法主要用于一些初始化的操作，通过该方法的参数ProcessingEnvironment可以获取一些列有用的工具类
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessager = processingEnvironment.getMessager();
        mFiler = processingEnvironment.getFiler();
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();

        mMessager.printMessage(Diagnostic.Kind.NOTE, processingEnvironment.getOptions().get(
                "content"));

    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set.isEmpty()) return false;
        
        //拿到所有被ARouter注解的类
        Set<? extends Element> elements =
                roundEnvironment.getElementsAnnotatedWith(ARouter.class);
        //遍历所有的类节点
        for (Element element : elements) {
            //获取完整包名
            String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
            //获取类名
            String className = element.getSimpleName().toString();

            //打印当前类信息
            mMessager.printMessage(Diagnostic.Kind.NOTE, "当前ARouter注解的类：" + className);

            //最终编译生成的java文件
            String finalClassName = className + "$$ARouter";

            //拿到当前类的注解
            ARouter aRouter = element.getAnnotation(ARouter.class);
            String path = aRouter.path();
			
			//采用拼接的方式生成注解文件
	    	try {
                //创建java文件 用于处理业务代码
                JavaFileObject sourceFile =  filer.createSourceFile(packageName + "." + finalClassName);
                //返回可操作java文件的对象
                Writer writer = sourceFile.openWriter();
                //设置包名
                writer.write("package " + packageName + ";\n");
                //设置类名
                writer.write("public class " + finalClassName + " {\n");
                //添加方法
                writer.write("public static Class<?> findTargetClass(String path){\n");
                //拿到类注解
                ARouter aRouter = element.getAnnotation(ARouter.class);
                //拿到当前注解的路径
                String aRouterPath = aRouter.path();
                //以下为方法逻辑
                writer.write("if(path.equalsIgnoreCase(\"" + aRouterPath + "\")){\n");
                writer.write("return " + className + ".class;\n}\n");
                writer.write("return null;\n");
                writer.write("}\n}");

                //切记不要忘记关闭write
                writer.close();
                
            } catch (IOException e) {
                e.printStackTrace();
            }

        return true;
    }
}
```

##### 3、主项目app

**（1）添加依赖**

在app的build.gradle中添加如下依赖

```groovy
//注解依赖
implementation project(path: ':annotation')

//依赖注解处理器
annotationProcessor project(':compiler')
```

如果需要传递参数给注解处理器，比如我们在注解处理器接受的key为"content"，那么可以在app的build.gradle配置传递的值，代码如下：

```groovy
defaultConfig {
   ...
   // 在gradle文件中配置选项参数值（用于APT传参接收）
    // 切记：必须写在defaultConfig节点下
    javaCompileOptions {
        annotationProcessorOptions {
            arguments = [content : 'hello javaPoet']
        }
    }
}
```

**（2）调用**

```java
public void jump(View view) {
    startActivity(new Intent(this, UserActivity$$ARouter.getTargetClass("/user/UserActivity")));
}
```
### 二、JavaPote

JavaPote是square推出的开源java代码生成框架，提供Java Api生成.java源文件

这个框架非常实用，是Java面向对象OOP语法

可以很方便的使用它根据注解生成对应代码

通过这种自动化生成代码的方式，可以让我们用更简洁优雅的方式替代繁琐冗余的重复工作

#### 常用API

| 类对象         | 说明                                        |
| -------------- | ------------------------------------------- |
| MethodSpec     | 代表一个构造函数或方法声明                  |
| TypeSpec       | 代表一个类，接口，或者枚举声明              |
| FieldSpec      | 代表一个成员变量，一个字段声明              |
| JavaFile       | 包含一个顶级类的Java文件                    |
| ParameterSpec  | 用来创建参数                                |
| AnnotationSpec | 用来创建注解                                |
| ClassName      | 用来包装一个类                              |
| TypeName       | 类型，如在添加返回值类型是使用TypeName.VOID |

#### 使用

##### （1）在build.config中加入依赖

在compiler注解处理器中添加

```
implementation "com.squareup:javapoet:1.9.0"
```

##### （2）修改ARouterProcesspr.java类中代码生成方式

主要的代码在process()方法中。代码如下：

```java
@Override
public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
    if (set.isEmpty()) return false;

    //拿到所有被ARouter注解的类
    Set<? extends Element> elements =
            roundEnvironment.getElementsAnnotatedWith(ARouter.class);

    for (Element element : elements) {
        //获取完整包名
        String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
        //获取类名
        String className = element.getSimpleName().toString();

        //打印当前类信息
        mMessager.printMessage(Diagnostic.Kind.NOTE, "当前ARouter注解的类：" + className);

        //最终编译生成的java文件
        String finalClassName = className + "$$ARouter";

        //拿到当前类的注解
        ARouter aRouter = element.getAnnotation(ARouter.class);
        String path = aRouter.path();

        //文件处理器编写代码 ，采用javaPoet方式 先写方法，在写类，最后写包 method->class->package

        //构建方法 $T:代表类 $S:代表字符串
        MethodSpec methodSpec = MethodSpec.methodBuilder("getTargetClass")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(Class.class)
                .addParameter(String.class, "path")
                .addStatement("return path.equalsIgnoreCase($S) ? " +
                        "$T.class : null", path, ClassName.get((TypeElement) element))
                .build();

        //构建类
        TypeSpec typeSpec = TypeSpec.classBuilder(finalClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(methodSpec)
                .build();

        //构建包
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .build();

        //写入到文件生成器
        try {
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    return true;
}
```
