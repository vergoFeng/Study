## RxJava线程切换

RxJava如果不配置，默认就是主线程。在实际运用中，比如点击下载按钮下载图片，下载完后显示图片。下载图片这种网络耗时操作必须在子线程中执行，界面显示图片必须在主线程中执行。所以需要进行线程切换。

### 简单线程切换

```java
Observable
        .create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "上游所在线程: " + Thread.currentThread().getName());
                emitter.onNext("");
                emitter.onComplete();
            }
        })
        .subscribeOn(Schedulers.io()) // 给上游切换成异步线程
        .observeOn(AndroidSchedulers.mainThread())  // 给下游切换成主线程
        .subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "下游所在线程: " + Thread.currentThread().getName());
            }
        });
```
```
D/fhj: 上游所在线程: RxCachedThreadScheduler-1
D/fhj: 下游所在线程: main
```

上游使用 subscribeOn() 切换线程，下游使用 observeOn() 切换线程

### 线程模式

1. Schedulers.io()： io流操作，网络操作，文件流，耗时操作等异步操作
2. Schedulers.newThread()：比较常规的异步线程
3. Schedulers.computation()：需要cpu大量计算的线程
4. AndroidSchedulers.mainThread()：专供android切换到主线程

以上4种模式，io()和mainThread()模式是比较常用的。下面代码测试上游和下游线程的切换

### 线程切换注意点

#### 多次切换

```java
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
```
```
D/fhj: 上游所在线程: RxCachedThreadScheduler-1
D/fhj: 下游所在线程: main
```

**总结**

- 给上游切换线程时，切换多次，只会在第一次切换，后面的不切换了
- 给下游切换线程时，切换多次，每次都会去切换

#### 同步异步现象

默认情况下，就是上游和下游都在主线程中。

上游发一次，下游接收一次，上游发一次，下游接收一次，上游发一次，下游接收一次。

```java
Observable.create(new ObservableOnSubscribe<Integer>() {
    @Override
    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
        Log.d(TAG, "上游发送了一次 1 ");
        e.onNext(1);

        Log.d(TAG, "上游发送了一次 2 ");
        e.onNext(2);

        Log.d(TAG, "上游发送了一次 3 ");
        e.onNext(3);
    }
}).subscribe(new Consumer<Integer>() {
    @Override
    public void accept(Integer integer) throws Exception {
        Log.d(TAG, "下游接收: " + integer);
    }
});
```
```
D/fhj: 上游发送了一次 1 
D/fhj: 下游接收: 1
D/fhj: 上游发送了一次 2 
D/fhj: 下游接收: 2
D/fhj: 上游发送了一次 3 
D/fhj: 下游接收: 3
```

配置好异步线程，就是异步的表现

```java
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
}).subscribeOn(Schedulers.io()) // 给上游分配 异步线程
  .observeOn(AndroidSchedulers.mainThread()) // 给下游分配 主线程

  .subscribe(new Consumer<Integer>() {
    @Override
    public void accept(Integer integer) throws Exception {
        Log.d(TAG, "下游 accept: " + integer);
    }
});

```
```
D/fhj: subscribe: 上游发送了一次 1 
D/fhj: subscribe: 上游发送了一次 2 
D/fhj: subscribe: 上游发送了一次 3 
D/fhj: 下游 accept: 1
D/fhj: 下游 accept: 2
D/fhj: 下游 accept: 3
```