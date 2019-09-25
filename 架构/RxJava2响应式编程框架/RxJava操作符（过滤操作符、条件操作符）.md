## RxJava操作符（过滤操作符、条件操作符）

### 过滤操作符

上游和下游数据流向时进行过滤操作

#### 1、filter操作符

```java
Observable.just(1, 2, 3, 4)
        .filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                //return true; // 不去过滤，会发射到下游，反之false就是过滤不会发射到下游
                return integer > 3;
            }
        })
        .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "filter过滤: " + integer);
            }
        });
```

```
D/fhj: filter过滤: 4
```

总结：

- test方法，返回true，表示不过滤，直接发射到下游。
- test方法，返回false，表示过滤，不会发射到下游。

#### 2、take操作符

```java
Observable.interval(1, TimeUnit.SECONDS)
        .take(5)
        .subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.d(TAG, "take过滤：" + aLong);
            }
        });
```

```
D/fhj: take过滤： 0
D/fhj: take过滤： 1
D/fhj: take过滤： 2
D/fhj: take过滤： 3
D/fhj: take过滤： 4
```

总结：

- 需要配合定时器操作符才能体现take操作符的真正价值。
- 当定时器执行take设置的个数后，就会停止计时。

#### 3、distinct操作符

过滤掉重复的事件

```java
Observable.just(1, 1, 2, 2, 3, 3, 4, 4, 5)
        .distinct()
        .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "distinct过滤：" + integer);
            }
        });
```

```
D/fhj: distinct过滤： 1
D/fhj: distinct过滤： 2
D/fhj: distinct过滤： 3
D/fhj: distinct过滤： 4
D/fhj: distinct过滤： 5
```

#### 4、elementAt操作符

只发送指定下标的事件

```java
Observable.just(1, 2, 3, 4, 5, 6)
        .elementAt(2)
        .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "elementAt过滤：" + integer);
            }
        });
```
```
D/fhj: elementAt过滤： 3
```

elementAt还可以指定默认值，当过滤的下标在发射的事件中找不到的时候会发送默认值。

### 条件操作符

#### 1、all操作符

```java
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
```
```
D/fhj: all条件操作符: true
```

上游test()方法返回全部为true，下游才会为true，否则下游接收的为false

#### 2、contains操作符

```java
Observable.just(1, 2, 3)
        .contains(1)
        .subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                Log.d(TAG, "contains条件操作符: " + aBoolean);
            }
        });
```
```
D/fhj: contains条件操作符: true
```

只要上游发送的事件中包含contains里的事件，下游接收为true

#### 3、isEmpty操作符

```java
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
```
```
D/fhj: isEmpty条件操作符: true
```

判断是否发射的数据为空，如果为空，返回true；如果不为空，返回false

#### 4、any操作符

```java
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
```
```
D/fhj: any条件操作符: true
```

和all操作符相反，只要一个发射的数据满足条件，下游接收为true。只有当所有的发射的数据不满足条件，下游才为false。