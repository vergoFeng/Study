package com.vergo.demo.mvp.base;

import java.lang.ref.WeakReference;

/**
 * <p>Created by Fenghj on 2019/7/3.</p>
 */
public abstract class BasePresenter<V extends BaseView, M extends BaseModel, CONTRACT> {
    public M m;
    // 绑定View层弱引用
    private WeakReference<V> mWeakReference;

    public BasePresenter() {
        m = getModel();
    }

    public void bindView(V v) {
        mWeakReference = new WeakReference<>(v);
    }

    public void unBindView() {
        if(mWeakReference != null) {
            mWeakReference.clear();
            mWeakReference = null;
            System.gc();
        }
    }

    // 获取View，P -- V
    public V getView() {
        if(mWeakReference != null) {
            return mWeakReference.get();
        }
        return null;
    }

    public abstract M getModel();

    // 获取子类具体契约（Model层和View层协商的共同业务）
    public abstract CONTRACT getContract();
}
