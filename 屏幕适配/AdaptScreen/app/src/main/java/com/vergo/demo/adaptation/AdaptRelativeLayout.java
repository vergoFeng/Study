package com.vergo.demo.adaptation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * <p>Created by Fenghj on 2019/4/15.</p>
 */
public class AdaptRelativeLayout extends RelativeLayout {
    // 标记位用来控制只执行一次控件的适配测量
    private boolean flag;

    public AdaptRelativeLayout(Context context) {
        this(context, null);
    }

    public AdaptRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdaptRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(!flag) {
            // 获取横向缩放比
            float scale = AdaptSingleton.getInstance().getHorizontalScale();
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                // 计算横向宽高目标值
                lp.width = (int) (lp.width * scale);
                lp.height = (int) (lp.height * scale);
                // 计算margin目标值
                lp.topMargin = (int) (lp.topMargin * scale);
                lp.bottomMargin = (int) (lp.bottomMargin * scale);
                lp.leftMargin = (int) (lp.leftMargin * scale);
                lp.rightMargin = (int) (lp.rightMargin * scale);
                // 判断当子View是否是当前容器，不是的话就计算子View的padding目标值
                if(!(child instanceof AdaptRelativeLayout)) {
                    setViewPadding(this, scale);
                }
            }
            // 计算自身padding目标值
            setViewPadding(this, scale);
            flag = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    // 计算padding的目标值后并重新设置View的padding
    private void setViewPadding(View view, float scale) {
        int left = (int) (view.getPaddingLeft() * scale);
        int right = (int) (view.getPaddingRight() * scale);
        int top = (int) (view.getPaddingTop() * scale);
        int bottom = (int) (view.getPaddingBottom() * scale);
        this.setPadding(left, top, right, bottom);
    }
}
