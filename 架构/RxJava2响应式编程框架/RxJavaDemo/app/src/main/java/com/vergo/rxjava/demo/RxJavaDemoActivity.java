package com.vergo.rxjava.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RxJavaDemoActivity extends AppCompatActivity {

    private static final String TAG = "fhj";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

    }

    public void r01(View view) {
        // 上游 Observable 被观察者
        Observable observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            /**
             * 发射器
             * @param emitter 事件
             */
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "上游subscribe: 发射事件");
                // 发射事件
                emitter.onNext(1);
                Log.d(TAG, "上游subscribe: 发射完成");
            }
        });

        // 下游 Observer 观察者
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "下游onNext: " + integer);
            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onComplete() {

            }
        };

        // 订阅，被观察者订阅观察者
        observable.subscribe(observer);
    }

    public void r02(View view) {
        // 链式调用
        Observable.create(new ObservableOnSubscribe<String>() {
            /**
             * 发射器
             * @param emitter 事件
             */
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "上游 subscribe: 开始发射");
                // 发射事件
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

    public void r03(View view) {
        Observable.create(new ObservableOnSubscribe<String>() {
            /**
             * 发射器
             * @param emitter 事件
             */
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "上游 subscribe: 开始发射");
                // 发射事件
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
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "下游接收完成 onComplete");
            }
        });
    }

    /**
     * 流程整理2
     * @param vieww
     */
    public void r04(View vieww) {
        // 上游 Observable 被观察者
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                // 发射
                Log.d(TAG, "上游 subscribe: 开始发射..."); // todo 2
                emitter.onNext("RxJavaStudy");

                // emitter.onComplete(); // 发射完成  // todo 4

                // 上游的最后log才会打印
                // Log.d(TAG, "上游 subscribe: 发射完成");

                // emitter.onError(new IllegalAccessException("error rxJava"));

                // TODO 结论：在 onComplete();/onError 发射完成 之后 再发射事件  下游不再接收上游的事件
                /*emitter.onNext("a");
                emitter.onNext("b");
                emitter.onNext("c");*/
                // 发一百个事件

                emitter.onError(new IllegalAccessException("error rxJava")); // 发射错误事件
                emitter.onComplete(); // 发射完成
                // TODO 结论：已经发射了onComplete();， 再发射onError RxJava会报错，不允许
                // TODO 结论：先发射onError，再onComplete();，不会报错， 有问题（onComplete不会接收到了）
            }
        }).subscribe(
                // 下游 Observer 观察者
                new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // 弹出 加载框 ....
                        Log.d(TAG, "上游和下游订阅成功 onSubscribe 1"); // todo 1
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "下游接收 onNext: " + s); // todo 3
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "下游接收 onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        // 隐藏加载框
                        Log.d(TAG, "下游接收完成 onComplete"); // todo 5  只有接收完成之后，上游的最后log才会打印
                    }
                });

    }


    Disposable d;

    /**
     * 切断下游，让下游不再接收上游的事件，也就是说不会去更新UI
     * @param view
     */
    public void r05(View view) {
        // TODO 上游 Observable
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 100; i++) {
                    e.onNext(i);
                }
                e.onComplete();
            }
        })

                // 订阅下游
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        RxJavaDemoActivity.this.d = d;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "下游接收 onNext: " + integer);

                        // 接收上游的一个事件之后，就切断下游，让下游不再接收
                        // d.dispose();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 切断下游
        if (d != null) d.dispose();
    }

}
