package com.vergo.demo.materialdesign.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;

/**
 * <p>Created by Fenghj on 2019/4/12.</p>
 */
public class ScaleBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    private boolean isAnimation;

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
            // 向上滑动，此时隐藏控件
            scaleHide(child);
        } else if(dyConsumed < 0 && !isAnimation && child.getVisibility() == View.INVISIBLE) {
            // 向下滑动，此时显示控件
            scaleShow(child);
        }
    }

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
