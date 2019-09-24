package com.vergo.rxjava.demo.observer_pattern;

/**
 * 观察者实现类
 * <p>Created by Fenghj on 2019/9/24.</p>
 */
public class ObserverImpl implements Observer {
    @Override
    public <T> void changeAction(T info) {
        System.out.println(info);
    }
}
