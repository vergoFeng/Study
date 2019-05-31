package com.vergo.demo.exam.pwdinput;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Created by Fenghj on 2019/5/9.</p>
 */
public class PwdInputView extends LinearLayout {

    private Context context;
    private List<String> pwdList; //保存密码
    private TextView[] textViews = new TextView[6]; //文本数组

    public PwdInputView(Context context) {
        this(context, null);
    }

    public PwdInputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PwdInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initParent();
        initTextView();
    }

    private void initParent() {
        this.setOrientation(HORIZONTAL);
        // 设置圆角背景
        GradientDrawable inputBgDrawable = new GradientDrawable();
        inputBgDrawable.setCornerRadius(dp2px(5));
        inputBgDrawable.setStroke(dp2px(0.5f), Color.parseColor("#c4c4c4"));
        this.setBackground(inputBgDrawable);
        // 绘制分割线
        GradientDrawable lineDrawable = new GradientDrawable();
        lineDrawable.setColor(Color.parseColor("#c4c4c4"));
        lineDrawable.setSize(dp2px(0.5f), getHeight());
        this.setDividerDrawable(lineDrawable);
        this.setShowDividers(SHOW_DIVIDER_MIDDLE);
    }

    private void initTextView() {
        pwdList = new ArrayList<>();
        //添加密码框
        LayoutParams params = new LayoutParams(0, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        params.gravity = Gravity.CENTER;
        for (int i = 0; i < textViews.length; i++) {
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            textViews[i] = textView;
            textViews[i].setTextSize(30);
            textViews[i].setTextColor(Color.parseColor("#333333"));
            textViews[i].setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_CLASS_NUMBER);
            this.addView(textView, params);
        }

    }

    public void inputPwd(String s) {
        if(pwdList.size() == 6) return;
        pwdList.add(s);
        int length = pwdList.size();
        for (int i = 0; i < 6; i++) {
            if (i < length) {
                for (int j = 0; j < length; j++) {
                    textViews[j].setText(pwdList.get(j));
                }
            } else {
                textViews[i].setText("");
            }
        }
    }

    public void deletePwd() {
        if(pwdList.size() > 0){
            textViews[pwdList.size() - 1].setText("");
            pwdList.remove(pwdList.size() - 1);
        }
    }

    private int dp2px(final float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
