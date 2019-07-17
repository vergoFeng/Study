package com.vergo.skin.library.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.vergo.skin.library.view.SkinTextView;

/**
 * 换肤时控件加载器
 * <p>Created by Fenghj on 2019/7/17.</p>
 */
public class ChangeSkinViewInflater {
    /**
     * 自动匹配控件名，并初始化控件对象
     * @param name      控件名
     * @param context   上下文
     * @param attrs     某控件对应所有属性
     * @return 控件
     */
    public View createView(String name, Context context, AttributeSet attrs) {
        View view = null;

        switch (name) {
            case "TextView":
                view = new SkinTextView(context, attrs);
                verifyNotNull(view, name);
                break;
        }

        return view;
    }

    /**
     * 校验控件不为空（源码方法，由于private修饰，只能复制过来了。为了代码健壮，可有可无）
     *
     * @param view 被校验控件，如：AppCompatTextView extends TextView（v7兼容包，兼容是重点！！！）
     * @param name 控件名，如："ImageView"
     */
    private void verifyNotNull(View view, String name) {
        if (view == null) {
            throw new IllegalStateException(this.getClass().getName() + " asked to inflate view for <" + name + ">, but returned null");
        }
    }
}
