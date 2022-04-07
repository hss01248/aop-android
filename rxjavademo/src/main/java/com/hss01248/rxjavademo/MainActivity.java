package com.hss01248.rxjavademo;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.WorkManager;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.utils.ForceStopRunnable;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @VisibleForTesting
    public boolean cleanUp() {
        Log.w("cl","11111");
        int i = 3/1;
        Log.w("cl","22222");
        return false;
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

    public void cleanup2(View view) {
      boolean i =   cleanUp();
      Log.d("result","xxx:"+i);
    }

    @SuppressLint("RestrictedApi")
    public void androidxWorkCleanUp(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean i =  new ForceStopRunnable(getApplicationContext(), WorkManagerImpl.getInstance(getApplicationContext())).cleanUp();
                Log.d("result2","xxx:"+i);
            }
        }).start();

    }
}