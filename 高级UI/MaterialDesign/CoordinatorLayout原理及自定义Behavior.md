### CoordinatorLayout原理

#### 源码分析

给子View设置Behavior的方式和原理分析

```
// 在LayoutParams中获取到xml中设置的layout_behavior属性值
// 同时也提供了Behavior对象的 get 和 set 方法
public static class LayoutParams extends MarginLayoutParams {
	LayoutParams(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ...
        this.mBehaviorResolved = a.hasValue(styleable.CoordinatorLayout_Layout_layout_behavior);
        if (this.mBehaviorResolved) {
        	// 获取layout_behavior属性
            this.mBehavior = CoordinatorLayout.parseBehavior(context, attrs, a.getString(styleable.CoordinatorLayout_Layout_layout_behavior));
        }
    }

    // 通过 CoordinatorLayout.LayoutParams.getBehavior() 来获得 Behavior 实例对象
    public CoordinatorLayout.Behavior getBehavior() {
        return this.mBehavior;
    }

    // 通过 setBehavior() 来设置一个 Behavior 对象
    public void setBehavior(@Nullable CoordinatorLayout.Behavior behavior) {
        ...
    }
}

static CoordinatorLayout.Behavior parseBehavior(Context context, AttributeSet attrs, String name) {
    if (TextUtils.isEmpty(name)) {
        return null;
    } else {
    	// 表示behavior的全包名路径
        String fullName;
        if (name.startsWith(".")) {
        	// 当设置的 layout_behavior 属性值不包含包名，就加上包名
            fullName = context.getPackageName() + name;
        } else if (name.indexOf(46) >= 0) {
            fullName = name;
        } else {
            fullName = !TextUtils.isEmpty(WIDGET_PACKAGE_NAME) ? WIDGET_PACKAGE_NAME + '.' + name : name;
        }

        try {
            Map<String, Constructor<CoordinatorLayout.Behavior>> constructors = (Map)sConstructors.get();
            if (constructors == null) {
                constructors = new HashMap();
                sConstructors.set(constructors);
            }

            Constructor<CoordinatorLayout.Behavior> c = (Constructor)((Map)constructors).get(fullName);
            if (c == null) {
            	// 通过反射对Behavior进行实例化
                Class<CoordinatorLayout.Behavior> clazz = context.getClassLoader().loadClass(fullName);
                c = clazz.getConstructor(CONSTRUCTOR_PARAMS);
                c.setAccessible(true);
                ((Map)constructors).put(fullName, c);
            }
            // 返回实例化的Behavior对象
            return (CoordinatorLayout.Behavior)c.newInstance(context, attrs);
        } catch (Exception var7) {
            throw new RuntimeException("Could not inflate Behavior subclass " + fullName, var7);
        }
    }
}

static final Class<?>[] CONSTRUCTOR_PARAMS;
static {
	...
	// 用到了两参的构造函数，这也是在自定义Behavior时，需要使用到两个参数的构造函数的原因
	CONSTRUCTOR_PARAMS = new Class[]{Context.class, AttributeSet.class};
}
```

CoordinatorLayout是如何结合Behavior实现对子View的事件响应？

CoordinatorLayout采用的是一种内嵌滑动机制，内嵌滑动机制提供了一套父View和子View嵌套滑动的交互机制，前提条件是父View需要实现 NestedScrollingParent 接口，而子View需要实现 NestedScrollingChild 接口。

通过NestedScrollingParentHelper帮助类来辅助父View和子View的交互。

从RecyclerView的滑动进行分析，查看RecyclerView的源码，找到onInterceptTouchEvent方法。

##### RecyclerView

```
public boolean onInterceptTouchEvent(MotionEvent e) {
    int action = e.getActionMasked();
    switch(action) {
    case 0:
    	// DOWN 事件
        ...
        // 
        this.startNestedScroll(nestedScrollAxis, 0);
        break;
    ...
    }

    return this.mScrollState == 1;
}

public boolean startNestedScroll(int axes, int type) {
	// 就调用到 NestedScrollingChildHelper 中的 startNestedScroll 方法
    return this.getScrollingChildHelper().startNestedScroll(axes, type);
}

private NestedScrollingChildHelper getScrollingChildHelper() {
    if (this.mScrollingChildHelper == null) {
    	// 创建 NestedScrollingChildHelper 对象，将RecyclerView自身传给它
        this.mScrollingChildHelper = new NestedScrollingChildHelper(this);
    }

    return this.mScrollingChildHelper;
}
```

##### NestedScrollingChildHelper

```
public boolean startNestedScroll(int axes, int type) {
    if (this.hasNestedScrollingParent(type)) {
        return true;
    } else {
    	// 判断是否支持内嵌滑动
        if (this.isNestedScrollingEnabled()) {
        	// mView 表示 RecyclerView，（这里暂时它的父View就是CoordinateLayout）
            ViewParent p = this.mView.getParent();

            for(View child = this.mView; p != null; p = p.getParent()) {
            	// 调用了 onStartNestedScroll 方法
                if (ViewParentCompat.onStartNestedScroll(p, child, this.mView, axes, type)) {
                    ...
                }
                ...
            }
        }

        return false;
    }
}
```

##### ViewParentCompat

```
public static boolean onStartNestedScroll(ViewParent parent, View child, View target, int nestedScrollAxes, int type) {
	// 判断当前父View是否实现了NestedScrollingParent2接口
    if (parent instanceof NestedScrollingParent2) {
    	// 进而调用到父View中的 onStartNestedScroll 方法。
    	// 这里分析的是父View就是CoordinatorLayout，则进入CoordinatorLayout中查看onStartNestedScroll方法
        return ((NestedScrollingParent2)parent).onStartNestedScroll(child, target, nestedScrollAxes, type);
    }
    ...
}
```

##### CoordinatorLayout

```
public boolean onStartNestedScroll(View child, View target, int axes, int type) {
    boolean handled = false;
    int childCount = this.getChildCount();

    // 遍历所有子View
    for(int i = 0; i < childCount; ++i) {
        View view = this.getChildAt(i);
        // 当View可见时
        if (view.getVisibility() != 8) {
        	// 获取子View的LayoutParams
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)view.getLayoutParams();
            // 进而获取它的Behavior
            CoordinatorLayout.Behavior viewBehavior = lp.getBehavior();
            if (viewBehavior != null) {
            	// Behavior不为空，则执行Behavior的onStartNestedScroll方法
                boolean accepted = viewBehavior.onStartNestedScroll(this, view, child, target, axes, type);
                handled |= accepted;
                lp.setNestedScrollAccepted(type, accepted);
            } else {
                lp.setNestedScrollAccepted(type, false);
            }
        }
    }

    return handled;
}
```

##### 总结

事件从RecyclerView开始到NestedScrollingChildHelper，然后经过ViewParentCompat回到CoordinatorLayout中，最后交由Behavior处理。

#### 自定义Behavior

自定义一个向上滑动缩放隐藏，向下滑动缩放显示的Behavior

```
public class ScaleBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
    // 是否正在动画中的标志位
    private boolean isAnimation;
    // 必须实现两个参数的构造函数
    public ScaleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        // 垂直滚动
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
	    
        if(dyConsumed > 0 && !isAnimation && child.getVisibility() == View.VISIBLE) {
            // 向上滑动，view不在动画中，view可见，此时隐藏控件
            scaleHide(child);
        } else if(dyConsumed < 0 && !isAnimation && child.getVisibility() == View.INVISIBLE) {
            // 向下滑动，view不在动画中，view不可见，此时显示控件
            scaleShow(child);
        }
    }
    // 缩小隐藏
    private void scaleHide(final V child) {
        ViewCompat.animate(child)
                .scaleX(0)
                .scaleY(0)
                .setDuration(500)
                .setInterpolator(new FastOutLinearInInterpolator())
                .setListener(new ViewPropertyAnimatorListenerAdapter(){
                    @Override
                    public void onAnimationStart(View view) {
                        super.onAnimationStart(view);
                        isAnimation = true;
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        super.onAnimationEnd(view);
                        isAnimation = false;
                        child.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        super.onAnimationCancel(view);
                        isAnimation = false;
                    }
                });
    }
    // 放大显示
    private void scaleShow(final V child) {
        child.setVisibility(View.VISIBLE);
        ViewCompat.animate(child)
                .scaleX(1)
                .scaleY(1)
                .setDuration(500)
                .setInterpolator(new LinearOutSlowInInterpolator())
                .setListener(new ViewPropertyAnimatorListenerAdapter(){
                    @Override
                    public void onAnimationStart(View view) {
                        super.onAnimationStart(view);
                        isAnimation = true;
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        super.onAnimationEnd(view);
                        isAnimation = false;
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        super.onAnimationCancel(view);
                        isAnimation = false;
                    }
                });
    }
}
```

显示效果

![](images/behavior.gif)