## RxJava背压模式

### 前言

在RxJava1.X的时候没有背压模式，当上游不停地发射数据，下游处理不过来组塞了，下游背压增大，导致内存泄露。所有在RxJava2.X之后增加了背压模式。

背压的上游被观察者是Flowable，下游观察者是Subscriber。使用方式和Observable极为相似。

所以当需求发射大量的数据，并且要考虑到下游处理不过来的情况下，就需要使用背压模式Flowable。

### 背压策略

- **ERROR**：上游不停的发射大量事件，下游阻塞了 处理不过来，放入缓存池，如果池子满了，就会抛出异常，最大128个事件
- **BUFFER**：上游不停的发射大量事件，下游阻塞了 处理不过来，放入缓存池，”等待“下游来接收事件处理
- **DROP**：上游不停的发射大量事件，下游阻塞了 处理不过来，放入缓存池，如果池子满了，就会把后面发射的事件丢弃
- **LASTEST**：上游不停的发射大量事件，下游阻塞了 处理不过来，放入缓存池，只存储128个事件，如果超过了128，此时如果Flowable继续发送数据的话， 缓存池会将之后的数据都给丢弃掉,但是还会缓存发送的事件中最后一次发送的数据。

### ERROR

```java
Flowable
	.create(new FlowableOnSubscribe<Integer>() {
	    @Override
	    public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
			for (int i = 0; i < Integer.MAX_VALUE; i++) {
			    emitter.onNext(i);
			}
	    }
	}, BackpressureStrategy.ERROR)
	.subscribe(new Subscriber<Integer>() {
	    @Override
	    public void onSubscribe(Subscription s) {
			mSubscription = s;
			// 只请求输出10次，给下游打印
             mSubscription.request(10);
	    }

	    @Override
	    public void onNext(Integer integer) {
			Log.i(TAG, "onNext: " + integer);
	    }

	    @Override
	    public void onError(Throwable t) {
			Log.i(TAG, "onError: " + t.getMessage());
	    }

	    @Override
	    public void onComplete() {
	    }
	});
```

```
I/fhj: onNext: 0
I/fhj: onNext: 1
I/fhj: onNext: 2
I/fhj: onNext: 3
I/fhj: onNext: 4
I/fhj: onNext: 5
I/fhj: onNext: 6
I/fhj: onNext: 7
I/fhj: onNext: 8
I/fhj: onNext: 9
I/fhj: onError: create: could not emit value due to lack of requests
```

打印了10次后发生异常，异常表示上游还有剩余的事件，无法处理，因为没有去请求。

### BUFFER

```java
Flowable
        .create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 500; i++) {
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                mSubscription = s;
                mSubscription.request(500);
            }

            @Override
            public void onNext(Integer integer) {
                try {
                    Thread.currentThread().sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Log.i(TAG, "onNext: " + integer);
            }

            @Override
            public void onError(Throwable t) {
                Log.i(TAG, "onError: " + t.getMessage());

            }

            @Override
            public void onComplete() {

            }
        });
```

即使上游发送的事件数超过128，都不会发生异常，且能全部打印出来。

### 总结

ERROR：上游发射事件不能超过128个，并且上游发射的事件如果下游没有及时处理(调用request()方法)，都会抛出异常。但是在异步模式下，只要发射的事件数不超过128个，那么无论下游有没有处理事件(调用request()方法)，都不会抛出异常。

BUFFER：该模式下不会出现异常，并且发射的事件数量不受限制

DROP：上游发射事件超过128个，将会丢弃掉剩余的事件

LASTEST：上游发射事件超过128个，将会丢弃掉缓存池所有的事件，但是仍然会缓存发送最后的一次事件。