package com.vergo.demo.mvp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * <p>Created by Fenghj on 2019/7/3.</p>
 */
public abstract class BaseView<P extends BasePresenter, CONTRACT> extends AppCompatActivity {

    public P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = getPresenter();
        presenter.bindView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unBindView();
    }

    public abstract P getPresenter();
    public abstract CONTRACT getContract();
}
