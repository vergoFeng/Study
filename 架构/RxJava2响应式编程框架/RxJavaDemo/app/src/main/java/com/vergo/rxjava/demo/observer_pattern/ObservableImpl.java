package com.vergo.rxjava.demo.observer_pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * 被观察者实现类
 * <p>Created by Fenghj on 2019/9/24.</p>
 */
public class ObservableImpl implements Observable {
    // 观察者集合
    private List<Observer> mObserverList = new ArrayList<>();
    @Override
    public void registerObserver(Observer observer) {
        mObserverList.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        mObserverList.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : mObserverList) {
            // 在被观察者实现类中，通知所有注册好的观察者
            observer.changeAction("被观察者发生改变");
        }
    }
}
