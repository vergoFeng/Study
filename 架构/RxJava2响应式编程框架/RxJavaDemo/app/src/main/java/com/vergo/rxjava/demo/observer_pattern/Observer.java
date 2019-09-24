package com.vergo.rxjava.demo.observer_pattern;

/**
 * 观察者标准
 * <p>Created by Fenghj on 2019/9/24.</p>
 */
public interface Observer {
    /**
     * 接收到被观察者的消息进行处理
     * @param info
     * @param <T>
     */
    <T> void changeAction(T info);
}
