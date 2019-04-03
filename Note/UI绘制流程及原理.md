## UI绘制流程及原理

### View是如何被添加到屏幕窗口上

1. 创建顶层布局容器DecorView
2. 在顶层布局中加载基础布局ViewGroup
3. 将ContentView添加到基础布局中的FrameLayout中

#### 源码分析

通常一个Activity加载一个View时，在onCreate方法中调用setContentView方法，传入一个布局资源id。查看源码得知，在Activity类中，又调用了`getWindow().setContentView(layoutResID);`。

##### 什么是Window？

```
/**
 * <p>The only existing implementation of this abstract class is
 * android.view.PhoneWindow, which you should instantiate when needing a
 * Window.
 */
public abstract class Window {...}
```

源码注释表明：Window是一个抽象类，它仅有一个实现类是android.view.PhoneWindow。

##### PhoneWindow

进入PhoneWindow类，找到setContentView()方法

```
@Override
public void setContentView(int layoutResID) {
    // Note: FEATURE_CONTENT_TRANSITIONS may be set in the process of installing the window
    // decor, when theme attributes and the like are crystalized. Do not check the feature
    // before this happens.
    if (mContentParent == null) {
    	// 1. 
        installDecor();
    } else if (!hasFeature(FEATURE_CONTENT_TRANSITIONS)) {
        mContentParent.removeAllViews();
    }

    if (hasFeature(FEATURE_CONTENT_TRANSITIONS)) {
        final Scene newScene = Scene.getSceneForLayout(mContentParent, layoutResID,
                getContext());
        transitionTo(newScene);
    } else {
    	// 2. 通过LayoutInflater一个对象来解析传入的布局资源ID
        mLayoutInflater.inflate(layoutResID, mContentParent);
    }
    ...
}
```

##### installDecor方法

```
private void installDecor() {
	// 1.
	if (mDecor == null) {
		// 在布局资源加载的过程中首先创建了DecorView的对象
		// DecorView是一个容器，继承于FrameLayout
		mDecor = generateDecor(-1);
	}
	// 2.
	if (mContentParent == null) {
		mContentParent = generateLayout(mDecor);
		...
	}
}

protected ViewGroup generateLayout(DecorView decor) {
	// 1. 一系列if判断，根据系统主题属性，设置相关特性
	if (a.getBoolean(R.styleable.Window_windowNoTitle, false)) {
		requestFeature(FEATURE_NO_TITLE);
	}
	...
	
	// Inflate the window decor. 解析窗口View
	int layoutResource;
	int features = getLocalFeatures();
	// 2. 一系列if判断，根据features值，对layoutResource赋值不同的窗口View布局资源
	if ((features & (1 << FEATURE_SWIPE_TO_DISMISS)) != 0) {
		// screen_swipe_dismiss 等为系统源码内的布局资源
		layoutResource = R.layout.screen_swipe_dismiss;
	}
	...

	// 3. 解析布局资源， 并add到DecorView中
	mDecor.onResourcesLoaded(mLayoutInflater, layoutResource);
	
	// 4. find contentParent
	// ID_ANDROID_CONTENT = com.android.internal.R.id.content
	// 它是主容器布局的ID，而且是一定存在的
	ViewGroup contentParent = (ViewGroup)findViewById(ID_ANDROID_CONTENT);
	...
	return contentParent;
}
```

#### 总结

![](images/ui_1.png)

1. 系统会创建一个顶层布局容器DecorView，它是一个ViewGroup容器，继承FrameLayout，是PhoneWindow持有的一个实例，是所有应用窗口的顶层View，在系统内部进行初始化。
2. 当DecorView初始化完成后，系统会根据不同的主题特性去加载不同的基础容器。这个基础容器中，必定存在一个ID为com.android.internal.R.id.content的FrameLayout容器。
3. 当setContentView方法传入的自己的布局资源ID，经过解析后add到FrameLayout中。

### View的绘制流程

1、绘制入口

```
ActivityThread.handleResumeActivity
-->WindowManagerImpl.addView(decroView, layoutParmars);
-->WindowManagerGlobal.addView()
```

2、绘制的类及方法

```
ViewRootImpl.setView(decroView, layoutParmars, parentView)
-->ViewRootImpl.requestLayout()-->scheduleTraversals()-->doTraversal()
-->performTraversals
```

3、绘制三大步骤

```
测量：ViewRootImpl.performMeasure
布局：ViewRootImpl.performLayout
绘制：ViewRootImpl.performDraw
```

