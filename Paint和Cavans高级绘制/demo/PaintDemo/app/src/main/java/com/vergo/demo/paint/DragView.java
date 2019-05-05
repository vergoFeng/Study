package com.vergo.demo.paint;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * <p>Created by Fenghj on 2019/5/5.</p>
 */
public class DragView extends View {

    private Context mContext;
    private float downX;  // 点击时的x坐标
    private float downY;  // 点击时的y坐标
    private int width;    // DragView的宽度
    private int height;   // DragView的高度
    private int maxWidth;
    private int maxHeight;

    public DragView(Context context) {
        super(context);
        mContext = context;
    }

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public DragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        maxWidth = ScreenUtils.getScreenWidth(mContext) - width;
        maxHeight = ScreenUtils.getScreenHeight(mContext) - height - getStatusBarHeight();
    }

    // 获取状态栏高度
    public int getStatusBarHeight(){
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourceId);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Toast.makeText(mContext, "触摸在View内", Toast.LENGTH_SHORT).show();
                downX = event.getX(); // 按下时x坐标
                downY = event.getY(); // 按下时y坐标
                break;
            case MotionEvent.ACTION_MOVE:
                final float translationX = event.getX() - downX; // x轴滑动距离
                final float translationY = event.getY() - downY; // y轴滑动距离

//                int l,r,t,b; // 上下左右四点移动后的偏移量
//                l = (int) (getLeft() + translationX);
//                r = l + width;
//                t = (int) (getTop() + translationY);
//                b = t + height;
//                Log.v("fhj", l + "," + t + "," + r + "," + b);
//                this.layout(l, t, r, b);

                float tragetX = getX() + translationX;
                float tragetY = getY() + translationY;

                if(tragetX < 0) {
                    tragetX = 0;
                }
                if(tragetY < 0) {
                    tragetY = 0;
                }
                if(tragetX > maxWidth) {
                    tragetX = maxWidth;
                }
                if(tragetY > maxHeight) {
                    tragetY = maxHeight;
                }

                setTranslationX(tragetX);
                setTranslationY(tragetY);

                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}
