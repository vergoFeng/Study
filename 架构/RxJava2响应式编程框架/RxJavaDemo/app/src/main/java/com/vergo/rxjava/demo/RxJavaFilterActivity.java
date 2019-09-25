package com.vergo.rxjava.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * 过滤操作符demo
 * <p>Created by Fenghj on 2019/9/25.</p>
 */
public class RxJavaFilterActivity extends AppCompatActivity {

    private static final String TAG = "fhj";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
    }

    @SuppressLint("CheckResult")
    public void filter(View view) {
        Observable.just(1, 2, 3, 4)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer > 3;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "filter过滤: " + integer);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void take(View view) {
        Observable.interval(1, TimeUnit.SECONDS)
                .take(5)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "take过滤：" + aLong);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void distinct(View view) {
        Observable.just(1, 1, 2, 2, 3, 3, 4, 4, 5)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "distinct过滤：" + integer);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void elementAt(View view) {
        Observable.just(1, 2, 3, 4, 5, 6)
                .elementAt(2)
//                .elementAt(10, 0)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "elementAt过滤：" + integer);
                    }
                });
    }
}
