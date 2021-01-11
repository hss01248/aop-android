package com.hss01248.rxjavademo;

import android.os.Handler;
import android.os.Looper;


public abstract class MyObservable<T> {


    public  abstract void subscrib(MyObserver<T> observer);

    /**
     * 创建observable
     * @param observable
     * @param <T>
     * @return
     */
    public static <T> MyObservable<T> create(MyObservable<T> observable){
        return observable;
    }


    /**
     * 各种函数定义和实现. 这里时map变换函数
     * @param func
     * @param <R>
     * @return
     */
    public <R> MyObservable<R>  map(MyFunc<R,T> func){
        return new MyObservable<R>() {
            @Override
            public void subscrib(MyObserver<R> observer) {
                MyObservable.this.subscrib(new MyObserver<T>() {
                    @Override
                    public void onNext(T t) {
                        //往下执行时,进行函数调用
                        R r = func.apply(t);
                        observer.onNext(r);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        observer.onError(throwable);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
            }
        };
    }

    /**
     * 切换上流线程示例
     * @return
     */
    public MyObservable<T> subscribOnIO(){
      return  new MyObservable<T>(){
            @Override
            public void subscrib(MyObserver<T> observer) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //往上订阅时切换线程
                        MyObservable.this.subscrib(observer);
                    }
                }).start();
            }
        };
    }

    /**
     * 切换下流线程
     * @return
     */
    public MyObservable<T> observerOnMainThread(){
       return new MyObservable<T>(){
            @Override
            public void subscrib(MyObserver<T> observer) {
                MyObservable.this.subscrib(new MyObserver<T>() {
                    @Override
                    public void onNext(T t) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                //往下执行时切换线程
                                observer.onNext(t);
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        //同上
                    }

                    @Override
                    public void onComplete() {
                        //同上
                    }
                });
            }
        };
    }
}
