## ButterKnife详解与原理分析

### ButterKnife优势

- 强大的View绑定和Click事件处理功能，简化代码，提升开发效率
- 方便的处理Adapter里的ViewHolder绑定问题
- 运行时不会影响APP的效率，使用配置方便
- 代码清晰，可读性强

### 和IOC架构的区别

共同特点：同样实现了解耦的目的

核心技术：IOC使用的是运行时通过反射技术（reflect），ButterKnife使用的是注解处理器技术（APT）

开发使用：两者几乎一样

代码难易：IOC编程更具挑战性

程序稳定：两者暂未发现致命的缺陷

两者缺陷：反射会消耗一定的性能，APT会增加apk的大小

开发追求：更偏向编译期的APT技术

### 原理

#### 1、BindView原理

进入bind()方法，跟踪进入如下代码：

```java
public static Unbinder bind(@NonNull Object target, @NonNull View source) {
    Class<?> targetClass = target.getClass();
    if (debug) Log.d(TAG, "Looking up binding for " + targetClass.getName());
  
    // 1、发现当前绑定类的构造方法
    Constructor<? extends Unbinder> constructor = findBindingConstructorForClass(targetClass);

    if (constructor == null) {
      return Unbinder.EMPTY;
    }

    //noinspection TryWithIdenticalCatches Resolves to API 19+ only type.
    try {
      // 2、new出这个构造方法
      return constructor.newInstance(target, source);
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Unable to invoke " + constructor, e);
    } catch (InstantiationException e) {
      throw new RuntimeException("Unable to invoke " + constructor, e);
    } catch (InvocationTargetException e) {
      Throwable cause = e.getCause();
      if (cause instanceof RuntimeException) {
        throw (RuntimeException) cause;
      }
      if (cause instanceof Error) {
        throw (Error) cause;
      }
      throw new RuntimeException("Unable to create binding instance.", cause);
    }
  }
```

继续跟进 `findBindingConstructorForClass()`方法

```java
private static Constructor<? extends Unbinder> findBindingConstructorForClass(Class<?> cls) {
  ...
  // 1、获取初始化绑定时传进来的类名
  String clsName = cls.getName();
  ...
    
  try {
    // 2、根据类名拼上"_ViewBinding"去加载这个APT生成的类
    Class<?> bindingClass = cls.getClassLoader().loadClass(clsName + "_ViewBinding");
    // 3、再通过getConstructor来获取构造方法
    bindingCtor = (Constructor<? extends Unbinder>) bindingClass.getConstructor(cls, View.class);
    if (debug) Log.d(TAG, "HIT: Loaded binding class and constructor.");
  } catch (ClassNotFoundException e) {
    if (debug) Log.d(TAG, "Not found. Trying superclass " + cls.getSuperclass().getName());
    bindingCtor = findBindingConstructorForClass(cls.getSuperclass());
  } catch (NoSuchMethodException e) {
    throw new RuntimeException("Unable to find binding constructor for " + clsName, e);
  }
  BINDINGS.put(cls, bindingCtor);
  // 4、返回类的构造方法
  return bindingCtor;
}
```

之后就是执行newInstance()方法，进入到生成的类的构造方法中

```java
@UiThread
public MainActivity_ViewBinding(MainActivity target) {
  this(target, target.getWindow().getDecorView());
}

@UiThread
public MainActivity_ViewBinding(final MainActivity target, View source) {
  this.target = target;

  View view;
  // 具体的findView的操作
  view = Utils.findRequiredView(source, R.id.textview, "field 'textView' and method 'click'");
  target.textView = Utils.castView(view, R.id.textview, "field 'textView'", TextView.class);
  view7f0700a9 = view;
  // 绑定点击事件监听
  view.setOnClickListener(new DebouncingOnClickListener() {
    @Override
    public void doClick(View p0) {
      target.click(p0);
    }
  });
}
```

注意：这里有通过target.textView来访问改控件，所以控件的修饰符布不能为privite或static。

最终还是通过findViewById来初始化控件

```java
public static View findRequiredView(View source, @IdRes int id, String who) {
  View view = source.findViewById(id);
  ...
}
```

#### 2、OnClick原理

在上面APT生成的类的构造方法中可以看出，已经对控件进行了点击事件的监听

```java
view.setOnClickListener(new DebouncingOnClickListener() {
  @Override
  public void doClick(View p0) {
    target.click(p0);
  }
});
```

进入DebouncingOnClickListener类，可以看出它是一个实现了 View.OnClickListener 接口的抽象类。

```java
public abstract class DebouncingOnClickListener implements View.OnClickListener {
  static boolean enabled = true;

  private static final Runnable ENABLE_AGAIN = () -> enabled = true;

  @Override public final void onClick(View v) {
    if (enabled) {
      enabled = false;
      v.post(ENABLE_AGAIN);
      doClick(v);
    }
  }

  public abstract void doClick(View v);
}
```

调用onClick方法的时候，就会触发doClick方法回调，最终再交给OnClick注解所绑定的方法来处理。