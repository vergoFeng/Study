package com.vergo.rxjava.demo.observer_pattern;

/**
 * 被观察者标准
 * <p>Created by Fenghj on 2019/9/24.</p>
 */
public interface Observable {
    /**
     * 在被观察者中来注册观察者
     * @param observer 观察者对象
     */
    void registerObserver(Observer observer);

    /**
     * 在被观察者中移除观察者
     * @param observer 观察者对象
     */
    void removeObserver(Observer observer);

    /**
     * 通知所有注册的观察者
     */
    void notifyObservers();
}
