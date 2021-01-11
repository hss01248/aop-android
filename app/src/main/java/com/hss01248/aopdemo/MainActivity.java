package com.hss01248.aopdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.hss01248.aopdemo.rxjava.MyFunc;
import com.hss01248.aopdemo.rxjava.MyObservable;
import com.hss01248.aopdemo.rxjava.MyObserver;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doRx();
    }

    private void doRx() {
        MyObservable.create(new MyObservable<String>() {
            @Override
            public void subscrib(MyObserver<String> observer) {
                for (int i = 0; i < 10; i++) {
                    observer.onNext(i + "");
                }
                Log.d("create:",","+Thread.currentThread().getName());

            }
        }).map(new MyFunc<Integer, String>() {
            @Override
            public Integer apply(String s) {
                Log.d("map1:","integer:"+s+","+Thread.currentThread().getName());
                return Integer.parseInt(s) * 2;
            }
        }).map(new MyFunc<String, Integer>() {
            @Override
            public String apply(Integer integer) {
                Log.d("map2:","integer:"+integer+","+Thread.currentThread().getName());
                return integer+"->String";
            }
        }).subscribOnIO()
        .observerOnMainThread()
        .subscrib(new MyObserver<String>() {
            @Override
            public void onNext(String s) {
                Log.d("onNext:","finnaly:"+s+","+Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();

            }

            @Override
            public void onComplete() {
                Log.d("onComplete:","finnaly: onComplete");
            }
        });

    }
}