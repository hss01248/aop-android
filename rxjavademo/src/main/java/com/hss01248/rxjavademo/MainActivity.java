package com.hss01248.rxjavademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void doRx() {
        MyObservable.create(new MyObservable<String>() {
            @Override
            public void subscrib(MyObserver<String> observer) {
                Log.d("create:", "," + Thread.currentThread().getName());
                for (int i = 0; i < 1; i++) {
                    observer.onNext(i + "");
                }


            }
        }).subscribOnIO()
                .map(new MyFunc<Integer, String>() {
                    @Override
                    public Integer apply(String s) {
                        Log.d("map1:", "apply integer:" + s + "," + Thread.currentThread().getName());
                        return Integer.parseInt(s) * 2;
                    }
                }).subscribOnIO()
                .observerOnMainThread()
                .map(new MyFunc<String, Integer>() {
                    @Override
                    public String apply(Integer integer) {
                        Log.d("map2:", "apply integer:" + integer + "," + Thread.currentThread().getName());
                        return integer + " 加 String";
                    }
                })
                .observerOnBackThread()
                .subscrib(new MyObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        Log.d("onNext:", "finnally:" + s + "," + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();

                    }

                    @Override
                    public void onComplete() {
                        Log.d("onComplete:", "finnaly: onComplete");
                    }
                });

    }

    public void rx(View view) {
        doRx();
    }
}