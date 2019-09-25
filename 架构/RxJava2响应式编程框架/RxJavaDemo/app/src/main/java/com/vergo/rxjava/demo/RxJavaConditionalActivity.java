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
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * 条件操作符demo
 * <p>Created by Fenghj on 2019/9/25.</p>
 */
public class RxJavaConditionalActivity extends AppCompatActivity {

    private static final String TAG = "fhj";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conditional);
    }

    @SuppressLint("CheckResult")
    public void all(View view) {
        Observable.just(1, 2, 3)
                .all(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer >= 1;
                    }
                })
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.d(TAG, "all条件操作符: " + aBoolean);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void contains(View view) {
        Observable.just(1, 2, 3)
                .contains(1)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.d(TAG, "contains条件操作符: " + aBoolean);
                    }
                });
    }

    public void isEmpty(View view) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext(null);
            }
        })
        .isEmpty()
        .subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                Log.d(TAG, "isEmpty条件操作符: " + aBoolean);
            }
        });
    }

    @SuppressLint("CheckResult")
    public void any(View view) {
        Observable.just(1, 2, 3)
                .any(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer > 2;
                    }
                })
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.d(TAG, "any条件操作符: " + aBoolean);
                    }
                });
    }
}
