package com.vergo.skin.library.core;

import android.content.Context;
import android.support.v7.app.AppCompatViewInflater;
import android.util.AttributeSet;
import android.view.View;

import com.vergo.skin.library.view.SkinnableButton;
import com.vergo.skin.library.view.SkinnableImageView;
import com.vergo.skin.library.view.SkinnableLinearLayout;
import com.vergo.skin.library.view.SkinnableRelativeLayout;
import com.vergo.skin.library.view.SkinnableTextView;

/**
 * 自定义控件加载器（可以考虑该类不被继承）
 */
public final class ChangeSkinViewInflater extends AppCompatViewInflater {

    /**
     * 自动匹配控件名，并初始化控件对象
     * @param name     控件名
     * @param context  上下文
     * @param attrs    某控件对应所有属性
     * @return 控件
     */
    public View createView(String name, Context context, AttributeSet attrs) {
        View view = null;
        switch (name) {
            case "LinearLayout":
                // view = super.createTextView(context, attrs); // 源码写法
                view = new SkinnableLinearLayout(context, attrs);
                this.verifyNotNull(view, name);
                break;
            case "RelativeLayout":
                view = new SkinnableRelativeLayout(context, attrs);
                this.verifyNotNull(view, name);
                break;
            case "TextView":
                view = new SkinnableTextView(context, attrs);
                this.verifyNotNull(view, name);
                break;
            case "ImageView":
                view = new SkinnableImageView(context, attrs);
                this.verifyNotNull(view, name);
                break;
            case "Button":
                view = new SkinnableButton(context, attrs);
                this.verifyNotNull(view, name);
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
