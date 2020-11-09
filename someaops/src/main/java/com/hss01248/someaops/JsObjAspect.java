package com.hss01248.someaops;


import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;


import com.hss01248.logforaop.LogMethodAspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.net.URL;



@Aspect
public class JsObjAspect {

    public static final String TAG = "jsBridgeLog";
   public static boolean enableLog = false;

    @Around("@annotation(android.webkit.JavascriptInterface)")
    public Object addLog(ProceedingJoinPoint joinPoint) throws Throwable{
        return LogMethodAspect.logAround(enableLog, TAG,false, joinPoint, new LogMethodAspect.IAround() {
            @Override
            public void before(ProceedingJoinPoint joinPoin,String desc) {
               Object obj = joinPoin.getThis();
               /*if(obj instanceof BaseJsObj){
                   BaseJsObj baseJsObj = (BaseJsObj) obj;
                   DebugWebViewClient.logInJsConsole(baseJsObj.getWebView(),desc);
               }*/




                String name = "z_" + joinPoin.getSignature().toShortString()
                        .replace("(..)","")
                        .replace("()","")
                        .replace(".","_").replace("Html5JsObj","h5");
                Log.d("FA","event name:"+name);
                Bundle bundle = new Bundle();
                //bundle.putString("appversion", AppUtils.getAppVersionName());
                String url = getUrl();
                String path = "";
                try {
                    if(!TextUtils.isEmpty(url)){
                        path =  new URL(url).getPath();
                    }else {
                        path = "url empty";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bundle.putString("url", path);
                bundle.putString("args", LogMethodAspect.toStrings(joinPoin.getArgs()));
                logEvent(name, bundle);
            }

            @Override
            public String descExtraForLog() {
                return "url:"+getUrl();
            }
        });
    }

    private void logEvent(String name, Bundle bundle) {

        //数据上报用
    }

    static String getUrl() {

        return "";
    }
}
