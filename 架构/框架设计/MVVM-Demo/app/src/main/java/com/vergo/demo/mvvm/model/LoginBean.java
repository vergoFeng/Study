package com.vergo.demo.mvvm.model;

import android.databinding.ObservableField;

/**
 * <p>Created by Fenghj on 2019/7/3.</p>
 */
public class LoginBean {
    //被观察的属性（切记：必须是public修饰符，因为是DataBinding的规范）
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> password = new ObservableField<>();
}
