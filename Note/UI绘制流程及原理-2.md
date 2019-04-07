## UI绘制流程及原理

### 三、UI绘制详细步骤

1. 测量performMeasure

   ```
   view.measure-->view.onMeasure-->view.setMeasuredDimension-->setMeasuredDimensionRaw
   ```

2. 布局performLayout

   ```
   view.layout-->view.onLayout
   ```

3. 绘制performLayout

   ```
   viewRootImpl.draw(fullRedrawNeeded)-->viewRootImpl.drawSoftware-->view.draw(Canvas)
   ```


#### 源码分析

##### 1、View的测量

##### 确定DecorView的MeasureSpec

###### ViewRootImpl

```
private void performTraversals() {
	// 1. 测量
	// 通过getRootMeasureSpec方法，生成DecorView的MeasureSpec
	int childWidthMeasureSpec = getRootMeasureSpec(mWidth, lp.width);
	int childHeightMeasureSpec = getRootMeasureSpec(mHeight, lp.height);
	performMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
}

private void performMeasure(int childWidthMeasureSpec, int childHeightMeasureSpec) {
    if (mView == null) {
        return;
    }
    Trace.traceBegin(Trace.TRACE_TAG_VIEW, "measure");
    try {
    	// mView就是顶层DecorView，调用了DecorView的measure方法
    	// 继续进入View类中的measure方法，会发现最后是对childWidthMeasureSpec, childHeightMeasureSpec值进行保存，确定控件的宽和高
        mView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    } finally {
        Trace.traceEnd(Trace.TRACE_TAG_VIEW);
    }
}
```

###### View

```
// measure方法是final类型的，它不能被重写
public final void measure(int widthMeasureSpec, int heightMeasureSpec) {
	...
	if (forceLayout || needsLayout) {
		if (cacheIndex < 0 || sIgnoreMeasureCache) {
        	// measure ourselves, this should set the measured dimension flag back
        	onMeasure(widthMeasureSpec, heightMeasureSpec);
        	mPrivateFlags3 &= ~PFLAG3_MEASURE_NEEDED_BEFORE_LAYOUT;
		}
		...
	}
}

protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
			getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
}
public static int getDefaultSize(int size, int measureSpec) {
    int result = size;
    int specMode = MeasureSpec.getMode(measureSpec);
    int specSize = MeasureSpec.getSize(measureSpec);

    switch (specMode) {
    case MeasureSpec.UNSPECIFIED:
        result = size;
        break;
    // View测量的过程中，当测量规格中的模式为 AT_MOST 和 EXACTLY时，测量的尺寸都赋值为测量规格中的size
    case MeasureSpec.AT_MOST:
    case MeasureSpec.EXACTLY:
        result = specSize;
        break;
    }
    return result;
}

protected final void setMeasuredDimension(int measuredWidth, int measuredHeight) {
    ...
    setMeasuredDimensionRaw(measuredWidth, measuredHeight);
}

private void setMeasuredDimensionRaw(int measuredWidth, int measuredHeight) {
    // 赋值操作
    mMeasuredWidth = measuredWidth;
    mMeasuredHeight = measuredHeight;
    // 标记位设置，表示赋值完成
    mPrivateFlags |= PFLAG_MEASURED_DIMENSION_SET;
}
```

###### MeasureSpec

测量View时需要测量的两部分：模式(mode) 和 尺寸(size)，这两部分被封装到了MeasureSpec类中。

MeasureSpec是一个32位的int值，前2位表示模式，后30位表示尺寸。

```
public static class MeasureSpec {
	private static final int MODE_SHIFT = 30;
	// 3左移30位，11000000000000000000000000000000
	private static final int MODE_MASK  = 0x3 << MODE_SHIFT;
	
	// 0左移30位，00000000000000000000000000000000
	// 父容器不对View做任何限制，一般系统内内部使用
	public static final int UNSPECIFIED = 0 << MODE_SHIFT;
	
	// 1左移30位，01000000000000000000000000000000
	// 精确模式，父容器检测出View的大小，View的大小就是MeasureSpec;
	// 对应控件的 LayoutParams.MATCH_PARENT 以及 固定大小
	public static final int EXACTLY     = 1 << MODE_SHIFT;
	
	// 2左移30位，10000000000000000000000000000000
	// 最大模式，父容器指定一个可用大小，View的大小不能超过这个值；
	// 对应控件的 LayoutParams.WRAP_CONTENT 
	public static final int AT_MOST     = 2 << MODE_SHIFT;
	
	/**
     * 将模式和尺寸转换成对应的 MeasureSpec
     * mode + size --> MeasureSpec
     *
     * @param size the size of the measure specification
     * @param mode the mode of the measure specification
     * @return the measure specification based on size and mode
     */
	public static int makeMeasureSpec(@IntRange(from = 0, to = (1 << MeasureSpec.MODE_SHIFT) - 1) int size,
                                          @MeasureSpecMode int mode) {
        if (sUseBrokenMakeMeasureSpec) {
            return size + mode;
        } else {
        	// ~MODE_MASK : 00111111111111111111111111111111
            return (size & ~MODE_MASK) | (mode & MODE_MASK); 
        } 
    }
}

// MeasureSpec --> mode + size
public static int getMode(int measureSpec) {
	return (measureSpec & MODE_MASK);
}
public static int getSize(int measureSpec) {
	return (measureSpec & ~MODE_MASK);
}
```

DecorView 的 MeasureSpec 由窗口大小和自身 LayoutParams 决定，遵守如下规则：

1. LayoutParams.MATCH_PARENT：精确模式，窗口大小
2. LayoutParams.WRAP_CONTENT：最大模式，最大为窗口大小
3. 固定大小：精确模式，大小为LayoutParams的大小

##### 确定View的MeasureSpec

因为DecorView是继承与FrameLayout，所以继续跟进查看FrameLayout的onMeasure方法。

###### FrameLayout

```
// widthMeasureSpec，heightMeasureSpec 当前容器测量规格
@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	int count = getChildCount();
	...省略...
	// for循环子控件
	for (int i = 0; i < count; i++) {
	    final View child = getChildAt(i);
	    if (mMeasureAllChildren || child.getVisibility() != GONE) {
	        // 传递当前容器的测量规格
	        measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
	        
	        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
	        maxWidth = Math.max(maxWidth,
	                child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
	        maxHeight = Math.max(maxHeight,
	                child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
	        childState = combineMeasuredStates(childState, child.getMeasuredState());
	        if (measureMatchParentChildren) {
	            if (lp.width == LayoutParams.MATCH_PARENT ||
	                    lp.height == LayoutParams.MATCH_PARENT) {
	                mMatchParentChildren.add(child);
	            }
	        }
	    }
	}
	
	// 计算容器最大的宽和高
	// Account for padding too
    maxWidth += getPaddingLeftWithForeground() + getPaddingRightWithForeground();
    maxHeight += getPaddingTopWithForeground() + getPaddingBottomWithForeground();

    // Check against our minimum height and width
    maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
    maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

    // Check against our foreground's minimum height and width
    final Drawable drawable = getForeground();
    if (drawable != null) {
        maxHeight = Math.max(maxHeight, drawable.getMinimumHeight());
        maxWidth = Math.max(maxWidth, drawable.getMinimumWidth());
    }
	// 设置自身宽高
    setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
            resolveSizeAndState(maxHeight, heightMeasureSpec,
                    childState << MEASURED_HEIGHT_STATE_SHIFT));
}
```

###### ViewGroup

```
protected void measureChildWithMargins(View child,
            int parentWidthMeasureSpec, int widthUsed,
            int parentHeightMeasureSpec, int heightUsed) {
    final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
    // 获取子控件的测量规格 MeasureSpec
    final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
            mPaddingLeft + mPaddingRight + lp.leftMargin + lp.rightMargin
                    + widthUsed, lp.width);
    final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
            mPaddingTop + mPaddingBottom + lp.topMargin + lp.bottomMargin
                    + heightUsed, lp.height);
	// 测量子控件尺寸
    child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
}

/**
 * @param spec 父容器的尺寸 MeasrueSpec
 * @param padding 父容器当前已经使用的空间
 * @param childDimension 子控件尺寸
 */
public static int getChildMeasureSpec(int spec, int padding, int childDimension) {
    // 获取父容器对应的mode和size
    int specMode = MeasureSpec.getMode(spec);
    int specSize = MeasureSpec.getSize(spec);
    
    // 根据父容器的mode，来对子控件的mode和size进行赋值
    ...省略...
    
    // 将子控件的size和mode生成MeasureSepc并返回
    return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
}
```

View的MeasureSpec由父容器的MeasureSpec和自身LayoutParams决定

|              |        EXACTLY         |        AT_MOST         |      UNSPECIFIED      |
| :----------: | :--------------------: | :--------------------: | :-------------------: |
|    dp/px     | EXACTLY<br/>childSize  | EXACTLY<br/>childSize  | EXACTLY<br/>childSize |
| match_parent | EXACTLY<br/>parentSize | AT_MOST<br/>parentSize |   UNSPECIFIED<br/>0   |
| wrap_content | AT_MOST<br/>parentSize | AT_MOST<br/>parentSize |   UNSPECIFIED<br/>0   |

ViewGroup测量过程：measure --> onMeasure (测量子控件的宽高)  --> setMeasuredDimension --> setMeasuredDimensionRaw (保存自己的宽高)

View测量过程：measure --> onMeasure  --> setMeasuredDimension --> setMeasuredDimensionRaw (保存自己的宽高)

> 在View类中的getDefaultSize方法里可以得知，当自定义view时，如果不重写onMeasure方法，则在布局文件中设置match_parent和wrap_content的效果是一样的，都是父容器的剩余空间。

##### 2、View的布局

- 调用view.layout确定自身的位置，即确定mLeft，mTop，mRight，mBottom的值。
- 如果是ViewGroup类型，需要调用onLayout确定子View的位置。

###### ViewRootImpl

```
private void performTraversals() {
	...省略...
	WindowManager.LayoutParams lp = mWindowAttributes;
	// 2. 布局
	// lp：顶层布局容器的布局属性
	// mWidth, mHeight：顶层布局的宽和高
	performLayout(lp, mWidth, mHeight);
	
	...省略...
}

private void performLayout(WindowManager.LayoutParams lp, int desiredWindowWidth,
            int desiredWindowHeight) {
    // 顶层DecorView
    final View host = mView;

    try {
        host.layout(0, 0, host.getMeasuredWidth(), host.getMeasuredHeight());
    }
}
```

###### View

```
public void layout(int l, int t, int r, int b) {

	// 确定View的左、上、右、下的值
    int oldL = mLeft;
    int oldT = mTop;
    int oldR = mRight;
    int oldB = mBottom;

    // 通过 setFrame 方法来确定
    boolean changed = isLayoutModeOptical(mParent) ?
            setOpticalFrame(l, t, r, b) : setFrame(l, t, r, b);
    
    if (changed || (mPrivateFlags & PFLAG_LAYOUT_REQUIRED) == PFLAG_LAYOUT_REQUIRED) {
        onLayout(changed, l, t, r, b);
        ...
    }
    ...
}

// 空方法，供子类来实现
// 如果是个ViewGroup，需要在方法中进行子控件的布局摆放；如果是个View，则可以不用实现
protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
}
```

##### 3、View的绘制

- 绘制背景 drawBackground(canvas)
- 绘制自己 onDraw(canvas)
- 绘制子View dispatchDraw(canvas)
- 绘制前景，滚动条等装饰 onDrawForeground(canvas)

###### ViewRootImpl

```
private void performTraversals() {
	...省略...
	// 3. 绘制
	performDraw();
	...省略...
}

private void performDraw() {
    ...省略...
	try {
	    // 跟进 draw 方法
        boolean canUseAsync = draw(fullRedrawNeeded);
        if (usingAsyncReport && !canUseAsync) {
            mAttachInfo.mThreadedRenderer.setFrameCompleteCallback(null);
            usingAsyncReport = false;
        }
    } finally {
        mIsDrawing = false;
        Trace.traceEnd(Trace.TRACE_TAG_VIEW);
    }
    ...省略...
}

private boolean draw(boolean fullRedrawNeeded) {
    ...省略...
    // 主要看 drawSoftware 方法
    if (!drawSoftware(surface, mAttachInfo, xOffset, yOffset,
                        scalingRequired, dirty, surfaceInsets)) {
        return false;
    }
}

private boolean drawSoftware(Surface surface, AttachInfo attachInfo, int xoff, int yoff,
        boolean scalingRequired, Rect dirty, Rect surfaceInsets) {
    ...省略...
    // 关键方法，调用 DecorView 的 draw 方法
    mView.draw(canvas);
    ...省略...
}
```

###### View

```
public void draw(Canvas canvas) {
    /*
     * 绘制遍历执行必须以适当的顺序执行的几个绘制步骤有：
     *
     *      1. 绘制背景
     *      2. 如果需要，进行图层的保存
     *      3. 绘制view的内容
     *      4. 绘制子view
     *      5. 如有必要，绘制淡化边缘并恢复图层
     *      6. 绘制装饰（例如滚动条）
     */

	// Step 1, draw the background, if needed
    int saveCount;

    if (!dirtyOpaque) {
        drawBackground(canvas);
    }

    // skip step 2 & 5 if possible (common case)
    final int viewFlags = mViewFlags;
    boolean horizontalEdges = (viewFlags & FADING_EDGE_HORIZONTAL) != 0;
    boolean verticalEdges = (viewFlags & FADING_EDGE_VERTICAL) != 0;
    if (!verticalEdges && !horizontalEdges) {
        // Step 3, draw the content
        if (!dirtyOpaque) onDraw(canvas);

        // Step 4, draw the children
        dispatchDraw(canvas);

        drawAutofilledHighlight(canvas);

        // Overlay is part of the content and draws beneath Foreground
        if (mOverlay != null && !mOverlay.isEmpty()) {
            mOverlay.getOverlayView().dispatchDraw(canvas);
        }

        // Step 6, draw decorations (foreground, scrollbars)
        onDrawForeground(canvas);

        // Step 7, draw the default focus highlight
        drawDefaultFocusHighlight(canvas);

        if (debugDraw()) {
            debugDrawFocus(canvas);
        }

        // we're done...
        return;
    }
}
```

