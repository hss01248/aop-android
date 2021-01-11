package com.hss01248.rxjavademo;

public interface MyObserver<T> {

    void onNext(T t);

    void onError(Throwable throwable);

    void onComplete();
}
