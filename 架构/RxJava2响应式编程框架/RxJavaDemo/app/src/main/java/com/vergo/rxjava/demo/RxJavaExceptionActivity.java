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
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * 异常操作符
 * <p>Created by Fenghj on 2019/9/30.</p>
 */
public class RxJavaExceptionActivity extends AppCompatActivity {

    private static final String TAG = "fhj";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception);
    }

    public void onErrorReturn(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 10; i++) {
                    if(i == 5) {
                        // RxJava中是不标准的
                        throw new IllegalAccessException("抛出异常");

                        // RxJava标准的
//                        emitter.onError(new IllegalAccessException("抛出异常"));
                    }
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        })
        .onErrorReturn(new Function<Throwable, Integer>() {
            @Override
            public Integer apply(Throwable throwable) throws Exception {
                Log.d(TAG, "onErrorReturn: " + throwable.getMessage());
                return 400;
            }
        })
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
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void onErrorResumeNext(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 10; i++) {
                    if(i == 5) {
                        // RxJava标准的
                        emitter.onError(new Error("抛出异常"));
                    } else {
                        emitter.onNext(i);
                    }
                }
                emitter.onComplete();
            }
        })
        .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> apply(Throwable throwable) throws Exception {
                // 返回的是被观察者，所以可以再多次发射给下游观察者
                return Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        emitter.onNext(400);
                        emitter.onNext(400);
                        emitter.onNext(400);
                        emitter.onNext(400);
                        emitter.onComplete();
                    }
                });
            }
        })
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
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void onExceptionResumeNext(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 10; i++) {
                    if(i == 5) {
                        // RxJava标准的
                        emitter.onError(new Exception("抛出异常"));
                    } else {
                        emitter.onNext(i);
                    }
                }
                emitter.onComplete();
            }
        })
        .onExceptionResumeNext(new ObservableSource<Integer>() {
            @Override
            public void subscribe(Observer<? super Integer> observer) {
                observer.onNext(404);
            }
        })
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
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void retry(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 10; i++) {
                    if(i == 5) {
                        // RxJava标准的
                        emitter.onError(new Exception("抛出异常"));
                    } else {
                        emitter.onNext(i);
                    }
                }
                emitter.onComplete();
            }
        })
        // 演示一
//        .retry(new Predicate<Throwable>() {
//            @Override
//            public boolean test(Throwable throwable) throws Exception {
//                return false; // false：不重试；true：一直重试，不停的重试
//            }
//        })
        // 演示二，重试3次，最后如果还是异常则发射到下游onError()方法
//        .retry(3, new Predicate<Throwable>() {
//            @Override
//            public boolean test(Throwable throwable) throws Exception {
//            Log.d(TAG, "retry: " + throwable.getMessage());
//                return true;
//            }
//        })
        // 演示三，重试了多少次
        .retry(new BiPredicate<Integer, Throwable>() {
            @Override
            public boolean test(Integer integer, Throwable throwable) throws Exception {
                Log.d(TAG, "retry: 已经重试了:" + integer + "次  e：" + throwable.getMessage());
                return integer != 3;
            }
        })
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
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
