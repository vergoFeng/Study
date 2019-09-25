package com.vergo.rxjava.demo;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;

/**
 * 变换操作符demo
 * <p>Created by Fenghj on 2019/9/25.</p>
 */
public class RxJavaTransformActivity extends AppCompatActivity {

    private static final String TAG = "fhj";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transform);
    }

    @SuppressLint("CheckResult")
    public void map(View view) {
        Observable.just(1)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        Log.d(TAG, "map1 apply：" + integer);
                        return "map1变换[" + integer + "]";
                    }
                })
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(String s) throws Exception {
                        Log.d(TAG, "map2 apply：" + s);
                        return Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
//                        return null;
                    }
                })
//                .subscribe(new Observer<Bitmap>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Bitmap bitmap) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.d(TAG, "onError: "+e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        Log.d(TAG, "下游接收: " + bitmap.toString());
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void flatMap(View view) {
        Observable.just(1, 2, 3)
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) throws Exception {
//                        return Observable.just("a", "b", "c");
                        return Observable.just("a", "b", "c").delay(3, TimeUnit.SECONDS);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "下游接收 变换操作符发射的事件 accept: " + s);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void concatMap(View view) {
        Observable.just(1, 2, 3)
                .concatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) throws Exception {
                        Log.d(TAG, "上游发射的事件 apply ：" + integer);
                        return Observable.just("a", "b", "c").delay(3, TimeUnit.SECONDS);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "下游接收 变换操作符发射的事件 accept: " + s);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void groutBy(View view) {
        Observable.just(5000, 6000, 7000, 8000, 9000, 10000)
                .groupBy(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return integer >= 8000 ? "高端配置电脑" : "低端配置电脑";
                    }
                })
                .subscribe(new Consumer<GroupedObservable<String, Integer>>() {
                    @Override
                    public void accept(GroupedObservable<String, Integer> stringIntegerGroupedObservable) throws Exception {
                        Log.d(TAG, "accept: 类别：" + stringIntegerGroupedObservable.getKey());
                        // 以上代码还不能把信息打印全，只是拿到了分组的key

                        // GroupedObservable是Observable子类
                        stringIntegerGroupedObservable.subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                Log.d(TAG, "accept: 价格："+integer);
                            }
                        });
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void buffer(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 100; i++) {
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        })
        .buffer(10)
        .subscribe(new Consumer<List<Integer>>() {
            @Override
            public void accept(List<Integer> integers) throws Exception {
                Log.d(TAG, "accept: " + integers.toString());
            }
        });
    }
}
