## RxJava操作符（变换操作符）

### 变换操作符

在上游被观察者和下游观察者之间加入变换操作，用来对数据进行变换。

#### 1、map操作符

```java
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
            }
        })
        .subscribe(new Consumer<Bitmap>() {
            @Override
            public void accept(Bitmap bitmap) throws Exception {
                Log.d(TAG, "下游接收: " + bitmap.toString());
            }
        });
```
```
D/fhj: map1 apply：1
D/fhj: map2 apply：map1变换[1]
D/fhj: 下游接收: android.graphics.Bitmap@f443f0
```

总结：

- 上游被观察者可以通过map操作符多次转换。
- 下游接收的数据以最后一次map转换作为最终数据。

#### 2、flatMap操作符

```java
Observable.just(1, 2, 3)
        .flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                Log.d(TAG, "上游发射的事件 apply ：" + integer);
                // return Observable.just("a", "b", "c");
                return Observable.just("a", "b", "c").delay(3, TimeUnit.SECONDS);
            }
        })
        .subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "下游接收 变换操作符发射的事件 accept: " + s);
            }
        });
```
```
D/fhj: 上游发射的事件 apply ：1
D/fhj: 上游发射的事件 apply ：2
D/fhj: 上游发射的事件 apply ：3
D/fhj: 下游接收 变换操作符发射的事件 accept: a
D/fhj: 下游接收 变换操作符发射的事件 accept: a
D/fhj: 下游接收 变换操作符发射的事件 accept: b
D/fhj: 下游接收 变换操作符发射的事件 accept: c
D/fhj: 下游接收 变换操作符发射的事件 accept: b
D/fhj: 下游接收 变换操作符发射的事件 accept: c
D/fhj: 下游接收 变换操作符发射的事件 accept: a
D/fhj: 下游接收 变换操作符发射的事件 accept: b
D/fhj: 下游接收 变换操作符发射的事件 accept: c
```

总结：

- 下游接收的数据是没有顺序的，说以flatMap是无序的。
- flatMap需要重新发射新的数据。(ObservableSource是Observable的父类)

#### 3、concatMap操作符

```java
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
```

```
D/fhj: 上游发射的事件 apply ：1
D/fhj: 下游接收 变换操作符发射的事件 accept: a
D/fhj: 下游接收 变换操作符发射的事件 accept: b
D/fhj: 下游接收 变换操作符发射的事件 accept: c
D/fhj: 上游发射的事件 apply ：2
D/fhj: 下游接收 变换操作符发射的事件 accept: a
D/fhj: 下游接收 变换操作符发射的事件 accept: b
D/fhj: 下游接收 变换操作符发射的事件 accept: c
D/fhj: 上游发射的事件 apply ：3
D/fhj: 下游接收 变换操作符发射的事件 accept: a
D/fhj: 下游接收 变换操作符发射的事件 accept: b
D/fhj: 下游接收 变换操作符发射的事件 accept: c
```

总结：

- 下游接收的数据是有顺序的，说以concatMap是有序的。
- concatMap需要重新发射新的数据。

#### 4、groupBy操作符

```java
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
```

```
D/fhj: accept: 类别：低端配置电脑
D/fhj: accept: 价格：5000
D/fhj: accept: 价格：6000
D/fhj: accept: 价格：7000
D/fhj: accept: 类别：高端配置电脑
D/fhj: accept: 价格：8000
D/fhj: accept: 价格：9000
D/fhj: accept: 价格：10000
```

总结：

- 通过该操作符能够对数据进行分组发送，例子中发送两组。
- 如果需要拿到每一组具体的内容，可对GroupedObservable进行重新订阅。

#### 5、buffer操作符

很多的数据不想全部一起发射出去，可以分批次，先缓存到Buffer。

```java
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
```

```
D/fhj: accept: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
D/fhj: accept: [10, 11, 12, 13, 14, 15, 16, 17, 18, 19]
D/fhj: accept: [20, 21, 22, 23, 24, 25, 26, 27, 28, 29]
D/fhj: accept: [30, 31, 32, 33, 34, 35, 36, 37, 38, 39]
D/fhj: accept: [40, 41, 42, 43, 44, 45, 46, 47, 48, 49]
D/fhj: accept: [50, 51, 52, 53, 54, 55, 56, 57, 58, 59]
D/fhj: accept: [60, 61, 62, 63, 64, 65, 66, 67, 68, 69]
D/fhj: accept: [70, 71, 72, 73, 74, 75, 76, 77, 78, 79]
D/fhj: accept: [80, 81, 82, 83, 84, 85, 86, 87, 88, 89]
D/fhj: accept: [90, 91, 92, 93, 94, 95, 96, 97, 98, 99]
```

总结：

- 下游接收数据类型为list集合类型。
- 每次发送的集合大小为缓存大小。