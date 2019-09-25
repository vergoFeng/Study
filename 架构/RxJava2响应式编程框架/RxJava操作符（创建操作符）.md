## RxJava操作符（创建操作符）

### 创建操作符

#### 1、create操作符

```java
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
```

```
D/fhj: 上游和下游订阅成功 onSubscribe 1
D/fhj: 上游 subscribe: 开始发射
D/fhj: 下游接收 onNext: 发射事件
D/fhj: 下游接收完成 onComplete
D/fhj: 上游 subscribe: 发射完成
```

总结：

- 上游被观察者首先订阅下游观察者，调用onSubscribe()方法。
- 订阅完成后，上游被观察者通过发射器ObservableEmitter进行发射数据到下游。
- 下游观察者接收到数据，调用onNext()方法。
- 上游被观察者调用emitter.onComplete()方法，表示发射完毕，此时下游不会再接收到数据。下游此时会调用onComplete()方法。
- 下游调用完毕onComplete()方法后，才会继续执行上游的后续日志打印。

#### 2、just操作符

```java
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
            }
        });
```

```
D/fhj: onNext: 1
D/fhj: onNext: 2
D/fhj: onNext: 3
D/fhj: onNext: 4
D/fhj: onNext: 5
D/fhj: 下游接收完成 onComplete
```

总结：

- just操作符内部会自动发射数据
- 上游发射完毕后，会自动触发下游onComplete()方法。

#### 3、fromArray操作符

```java
String[] strings = {"a", "b", "c"};
Observable.fromArray(strings)
        .subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "onNext: " + s);
            }
        });
// 使用lambda后可简化
// Observable.fromArray(strings)
//         .subscribe(s -> Log.d(TAG, "onNext: " + s));
```

```
onNext: a
onNext: b
onNext: c
```

和just操作符类似，只是发射的数据类型为数组类型。

这里使用的Consumer是简化版的观察者，Observer是完整版的观察者。

#### 4、empty操作符

```java
Observable.empty()
        .subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Object o) {
                // 没有事件可以接收
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "下游接收完成 onComplete");
            }
        });
```

总结：

- Observer观察者类型只能是Object，因为上游被观察者没有发射任何有值的事件，下游观察者无法确定类型，所以默认为Object。
- 上游没有发射任何值，所以下游没有事件可以接收， onNext() 方法不会执行。当然简化版的Consumer也接收不到。
- 下游会触发onComplete()方法。
- 使用场景之一：做一个耗时操作，不需要任何数据来刷新UI。

#### 5、range操作符

```java
Observable.range(1, 5)
        .subscribe(integer -> Log.d(TAG, "accept: "+integer));
```
```
D/fhj: accept: 1
D/fhj: accept: 2
D/fhj: accept: 3
D/fhj: accept: 4
D/fhj: accept: 5
```

range参数：第一个参数为起始数字，第二个参数为从起始位置开始，发送count个数，每次加1。

#### 6、interval操作符

每隔指定时间发送事件

```java
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
```
```
09-25 14:05:12.799 3129-3168/com.vergo.rxjava.demo D/fhj: accept: 0
09-25 14:05:13.799 3129-3168/com.vergo.rxjava.demo D/fhj: accept: 1
09-25 14:05:14.799 3129-3168/com.vergo.rxjava.demo D/fhj: accept: 2
09-25 14:05:15.798 3129-3168/com.vergo.rxjava.demo D/fhj: accept: 3
09-25 14:05:16.798 3129-3168/com.vergo.rxjava.demo D/fhj: accept: 4
09-25 14:05:17.799 3129-3168/com.vergo.rxjava.demo D/fhj: accept: 5
09-25 14:05:18.798 3129-3168/com.vergo.rxjava.demo D/fhj: accept: 6
09-25 14:05:19.798 3129-3168/com.vergo.rxjava.demo D/fhj: accept: 7
09-25 14:05:20.798 3129-3168/com.vergo.rxjava.demo D/fhj: accept: 8
09-25 14:05:21.799 3129-3168/com.vergo.rxjava.demo D/fhj: accept: 9
09-25 14:05:22.799 3129-3168/com.vergo.rxjava.demo D/fhj: accept: 10
```

使用时需要在onDestory中切断下游，防止内存泄露。