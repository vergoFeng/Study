package com.vergo.demo.designpattern.factory.impl;

import android.util.Log;

import com.vergo.demo.designpattern.factory.bean.PersonBean;

/**
 * <p>Created by Fenghj on 2019/6/24.</p>
 */
public class StudentImpl implements IPerson {
    @Override
    public PersonBean getPersonInfo() {
        PersonBean personBean = new PersonBean("小明", 18);
        Log.v("fhj", "student>>>"+personBean.toString());
        return personBean;
    }
}
