package com.vergo.skin.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.vergo.skin.library.R;
import com.vergo.skin.library.core.ViewsMatch;
import com.vergo.skin.library.model.AttrsBean;

/**
 * <p>Created by Fenghj on 2019/7/17.</p>
 */
public class SkinTextView extends AppCompatTextView implements ViewsMatch {

    private AttrsBean bean;

    public SkinTextView(Context context) {
        this(context, null);
    }

    public SkinTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public SkinTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        bean = new AttrsBean();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SkinTextView, defStyleAttr, 0);
        bean.saveViewResource(typedArray, R.styleable.SkinTextView);
        typedArray.recycle();
    }

    @Override
    public void skinnableView() {
        // 根据自定义属性，获取styleable中的background属性
        int key = R.styleable.SkinTextView[R.styleable.SkinTextView_android_background];
        // 根据styleable获取控件某属性的resourceId
        int backgroundResourceId = bean.getViewResource(key);
        if(backgroundResourceId > 0) {
            // 兼容包转换
            Drawable drawable = ContextCompat.getDrawable(getContext(), backgroundResourceId);
            // 控件自带api，这里不用setBackgroundColor()因为在9.0测试不通过
            // setBackgroundDrawable本来过时了，但是兼容包重写了方法
            setBackgroundDrawable(drawable);
        }
    }
}
