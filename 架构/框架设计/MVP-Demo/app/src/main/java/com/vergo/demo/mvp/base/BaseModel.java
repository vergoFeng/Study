package com.vergo.demo.mvp.base;

/**
 * <p>Created by Fenghj on 2019/7/3.</p>
 */
public abstract class BaseModel<P extends BasePresenter, CONTRACT> {
    public P p;

    public BaseModel(P p) {
        this.p = p;
    }

    public abstract CONTRACT getContract();
}
