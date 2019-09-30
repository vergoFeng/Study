## RxJava操作符（异常操作符）

### 异常操作符

让两个或者多个被观察者合并。

1. startWait，concatWith ：先创建被观察者，然后再组合其他的被观察者，然后再订阅
2. concat/merge/zip：直接合并多个被观察者，然后订阅

#### 1、onErrorReturn操作符

```java
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
        return 400; // 400代表有错误，给下游观察者
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
```
```
D/fhj: onNext: 0
D/fhj: onNext: 1
D/fhj: onNext: 2
D/fhj: onNext: 3
D/fhj: onNext: 4
D/fhj: onErrorReturn: 抛
D/fhj: onNext: 400
```

总结：

- 异常可以使用 throw 方式抛出，也可以使用rxJava的标准异常抛出方式 emitter.onError() 。
- 接收到异常后，会中断上游后续发射的所有事件。
- 使用onErrorReturn操作符，异常信息会经过处理继续交给下游的onNext()方法执行。
- 不使用onErrorReturn操作符，异常会交给下游的onError()方法执行，下游onError()方法只能接收emitter.onError()。

#### 2、onErrorResumeNext操作符

```java
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

```

```
D/fhj: concat: 1
D/fhj: concat: 2
D/fhj: concat: 3
D/fhj: concat: 4
```

总结：

- 与onErrorReturn操作符相似，不同点是 onErrorReturn 可以返回标识，onErrorResumeNext可以返回被观察者，这个被观察者里面可以再次发射多次事件给下游。

#### 3、onExceptionResumeNext操作符

```java
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
```

```
D/fhj: onNext: 0
D/fhj: onNext: 1
D/fhj: onNext: 2
D/fhj: onNext: 3
D/fhj: onNext: 4
D/fhj: onNext: 404
```

总结：

- 能在发生异常是捕获到，防止程序crash（这种错误异常一定可以接收的，才这样使用）

#### 4、retry操作符

```java
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
```

```
D/fhj: onNext: 0
D/fhj: onNext: 1
D/fhj: onNext: 2
D/fhj: onNext: 3
D/fhj: onNext: 4
D/fhj: retry: 已经重试了:1次  e：抛出异常
D/fhj: onNext: 0
D/fhj: onNext: 1
D/fhj: onNext: 2
D/fhj: onNext: 3
D/fhj: onNext: 4
D/fhj: retry: 已经重试了:2次  e：抛出异常
D/fhj: onNext: 0
D/fhj: onNext: 1
D/fhj: onNext: 2
D/fhj: onNext: 3
D/fhj: onNext: 4
D/fhj: retry: 已经重试了:3次  e：抛出异常
D/fhj: onError: 抛出异常
```

总结：

- return false; 代表不去重试  return true; 不停的重试。
- 可以设置重试次数，统计重试了几次。
- 重试结束后如果还是异常会执行下游onError()方法。