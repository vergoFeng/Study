## RxJava操作符（合并操作符）

### 合并操作符

让两个或者多个被观察者合并。

1. startWait，concatWith ：先创建被观察者，然后再组合其他的被观察者，然后再订阅
2. concat/merge/zip：直接合并多个被观察者，然后订阅


#### 1、startWith操作符

```java
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
```
```
D/fhj: startWith: 3
D/fhj: startWith: 4
D/fhj: startWith: 1
D/fhj: startWith: 2
```

被观察者1.startWith(被观察者2)，先执行被观察者2里发射的事件，再执行被观察者1里反射的事件。

#### 2、concatWith操作符

```java
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
        Log.d(TAG, "concatWith: " + integer);
    }
});
```
```
D/fhj: concatWith: 1
D/fhj: concatWith: 2
D/fhj: concatWith: 3
D/fhj: concatWith: 4
```

与stratWith相反，被观察者1.startWith(被观察者2)，先执行被观察者1里发射的事件，再执行被观察者2里反射的事件。

#### 3、concat操作符

```java
Observable.concat(
        Observable.just(1),
        Observable.just(2),
        Observable.just(3),
        Observable.just(4))
        .subscribe(integer -> Log.d(TAG, "concat: " + integer));
```

```
D/fhj: concat: 1
D/fhj: concat: 2
D/fhj: concat: 3
D/fhj: concat: 4
```

总结：

  - 最多能合并4个被观察者。
  - 被观察者按照顺序发射事件。

#### 4、merge操作符

```java
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
```

```
D/fhj: merge: 1
D/fhj: merge: 6
D/fhj: merge: 11
D/fhj: merge: 16
D/fhj: merge: 2
D/fhj: merge: 7
D/fhj: merge: 12
D/fhj: merge: 17
D/fhj: merge: 3
D/fhj: merge: 8
D/fhj: merge: 13
D/fhj: merge: 18
D/fhj: merge: 4
D/fhj: merge: 9
D/fhj: merge: 14
D/fhj: merge: 19
D/fhj: merge: 5
D/fhj: merge: 15
D/fhj: merge: 10
D/fhj: merge: 20
```

总结：

- 最多能合并4个被观察者。
- 和concat操作符不同的是，被观察者是并发发射事件的。

#### 5、zip操作符

```java
Observable observable1 = Observable.just("语文", "数学", "英语", "物理");
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
```

```
D/fhj: zip 考试得分: 语文：89
D/fhj: zip 考试得分: 数学：100
D/fhj: zip 考试得分: 英语：96
```

总结：

- 最多能合并9个被观察者。
- 被观察者发射的事件需要对应关系，如果没有对应就会被忽略。比如被观察1中物理事件就被忽略了。