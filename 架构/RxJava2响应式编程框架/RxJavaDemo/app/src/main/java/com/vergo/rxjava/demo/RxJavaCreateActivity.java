package com.vergo.rxjava.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * <p>Created by Fenghj on 2019/9/24.</p>
 */
public class RxJavaCreateActivity extends AppCompatActivity {

    private static final String TAG = "fhj";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
    }

    public void create(View view) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "上游 subscribe: 开始发射");
                emitter.onNext("发射事件");
                emitter.onComplete();
                Log.d(TAG, "上游 subscribe: 发射完成");
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "上游和下游订阅成功 onSubscribe 1");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "下游接收 onNext: " + s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "下游接收完成 onComplete");
            }
        });
    }

    public void just(View view) {
        Observable.just(1, 2, 3, 4, 5)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "下游接收完成 onComplete");
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void fromArray(View view) {
        String[] strings = {"a", "b", "c"};
        Observable.fromArray(strings)
                .subscribe(s -> Log.d(TAG, "onNext: " + s));
    }

    public void empty(View view) {
        Observable.empty()
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @SuppressLint("CheckResult")
    public void range(View view) {
        Observable.range(1, 5)
                .subscribe(integer -> Log.d(TAG, "accept: "+integer));
    }

    Disposable d;
    @SuppressLint("CheckResult")
    public void interval(View view) {
        d = Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(aLong -> Log.d(TAG, "accept: "+aLong));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        d.dispose();
    }
}
