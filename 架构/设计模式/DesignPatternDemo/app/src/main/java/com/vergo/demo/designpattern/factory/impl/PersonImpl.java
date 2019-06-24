package com.vergo.demo.designpattern.factory.impl;

import android.util.Log;

import com.vergo.demo.designpattern.factory.bean.PersonBean;

/**
 * <p>Created by Fenghj on 2019/6/24.</p>
 */
public class PersonImpl implements IPerson {
    @Override
    public PersonBean getPersonInfo() {
        PersonBean personBean = new PersonBean();
        Log.v("fhj", personBean.toString());
        return personBean;
    }
}
