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
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * 合并操作符
 * <p>Created by Fenghj on 2019/9/26.</p>
 */
public class RxJavaMergeActivity extends AppCompatActivity {

    private static final String TAG = "fhj";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge);
    }

    @SuppressLint("CheckResult")
    public void startWith(View view) {
        // 第一个被观察者
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onComplete();
            }
        })
        // 第二个被观察者
        .startWith(Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onComplete();
            }
        }))
        .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "startWith: " + integer);
            }
        });

//        Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                emitter.onNext(1);
//                emitter.onNext(2);
//                emitter.onComplete();
//            }
//        })
//        .startWith(new ObservableSource<Integer>() {
//            @Override
//            public void subscribe(Observer<? super Integer> observer) {
//                observer.onNext(3);
//                observer.onComplete();
//            }
//        })
//        .subscribe(new Consumer<Integer>() {
//            @Override
//            public void accept(Integer integer) throws Exception {
//                Log.d(TAG, "startWith: " + integer);
//            }
//        });

//        Observable.just(1, 2)
//                .startWith(3)
//                .subscribe(integer -> Log.d(TAG, "startWith: " + integer));
    }

    @SuppressLint("CheckResult")
    public void concatWith(View view) {
        // 第一个被观察者
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onComplete();
            }
        })
        // 第二个被观察者
        .concatWith(Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onComplete();
            }
        }))
        .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "startWith: " + integer);
            }
        });
    }

    @SuppressLint("CheckResult")
    public void concat(View view) {
        Observable.concat(
                Observable.just(1),
                Observable.just(2),
                Observable.just(3),
                Observable.just(4))
                .subscribe(integer -> Log.d(TAG, "concat: " + integer));
    }

    @SuppressLint("CheckResult")
    public void merge(View view) {
        Observable observable1 = Observable.intervalRange(1, 5, 1, 1, TimeUnit.SECONDS);
        Observable observable2 = Observable.intervalRange(6, 5, 1, 1, TimeUnit.SECONDS);
        Observable observable3 = Observable.intervalRange(11, 5, 1, 1, TimeUnit.SECONDS);
        Observable observable4 = Observable.intervalRange(16, 5, 1, 1, TimeUnit.SECONDS);

        Observable.merge(observable1, observable2, observable3, observable4)
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.d(TAG, "merge: " + o);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void zip(View view) {
        Observable observable1 = Observable.just("语文", "数学", "英语");
        Observable observable2 = Observable.just(89, 100, 96);
        Observable.zip(observable1, observable2, new BiFunction<String, Integer, StringBuffer>() {
            @Override
            public StringBuffer apply(String string, Integer integer) throws Exception {
                return new StringBuffer().append(string).append("：").append(integer);
            }
        })
        .subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Log.d(TAG, "zip 考试得分: " + o);
            }
        });
    }
}
