package com.vergo.skin.library.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.vergo.skin.library.core.ChangeSkinViewInflater;

/**
 * <p>Created by Fenghj on 2019/7/17.</p>
 */
public class SkinActivity extends AppCompatActivity {
    private ChangeSkinViewInflater mInflater;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(openChangeSkin()) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            LayoutInflaterCompat.setFactory2(layoutInflater, this);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        if(openChangeSkin()) {
            if(mInflater == null) {
                mInflater = new ChangeSkinViewInflater();
            }
            return mInflater.createView(name, context, attrs);
        }
        return super.onCreateView(name, context, attrs);
    }

    /**
     * 可重写，用来控制是否开启换肤功能
     * @return true：开启换肤；false：关闭换肤
     */
    public boolean openChangeSkin() {
        return true;
    }
}
