package com.vergo.rxjava.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 线程切换
 * <p>Created by Fenghj on 2019/9/30.</p>
 */
public class RxJavaSchedulersActivity extends AppCompatActivity {

    private static final String TAG = "fhj";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedulers);
    }

    @SuppressLint("CheckResult")
    public void schedulers(View view) {
        // RxJava如果不配置，默认就是主线程main
        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        Log.d(TAG, "上游所在线程: " + Thread.currentThread().getName());
                        emitter.onNext("");
                        emitter.onComplete();
                    }
                })
                // 给上游切换线程时，切换多次，只会在第一次切换，后面的不切换了
                .subscribeOn(Schedulers.io()) // 给上游切换成异步线程
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.computation())
                .subscribeOn(AndroidSchedulers.mainThread())

                // 给下游切换线程时，切换多次，每次都会去切换
                .observeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()) // 给下游切换成主线程

                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "下游所在线程: " + Thread.currentThread().getName());
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void schedulers2(View view) {
        // 默认情况
//        Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
//                Log.d(TAG, "上游发送了一次 1 ");
//                e.onNext(1);
//
//                Log.d(TAG, "上游发送了一次 2 ");
//                e.onNext(2);
//
//                Log.d(TAG, "上游发送了一次 3 ");
//                e.onNext(3);
//            }
//        }).subscribe(new Consumer<Integer>() { // 下游简化版
//            @Override
//            public void accept(Integer integer) throws Exception {
//                Log.d(TAG, "下游接收: " + integer);
//            }
//        });

        // 异步情况
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "subscribe: 上游发送了一次 1 ");
                e.onNext(1);

                Log.d(TAG, "subscribe: 上游发送了一次 2 ");
                e.onNext(2);

                Log.d(TAG, "subscribe: 上游发送了一次 3 ");
                e.onNext(3);
            }
        })
        .subscribeOn(Schedulers.io()) // 给上游分配 异步线程
        .observeOn(AndroidSchedulers.mainThread()) // 给下游分配 主线程

        .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "下游 accept: " + integer);
            }
        });

    }
}
