package com.vergo.demo.designpattern.factory.simple;

import com.vergo.demo.designpattern.factory.impl.IPerson;
import com.vergo.demo.designpattern.factory.impl.PersonImpl;

/**
 * <p>Created by Fenghj on 2019/6/24.</p>
 */
public class SimpleFactory {
    public static IPerson getPerson() {
        return new PersonImpl();
    }
}
