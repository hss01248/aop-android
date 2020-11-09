package com.hss01248.someaops;

import com.hss01248.logforaop.LogMethodAspect;
import com.hss01248.logforaop.LogMethodAspect.IBefore;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;



/**
 * by hss
 * data:2020/7/17
 * desc:
 */
@Aspect
public class RNAspect {

    private static final String TAG = "RNAspect";


    @Before("execution(* com.facebook.react.bridge.Promise.*(..))  ||  @annotation(com.facebook.react.bridge.ReactMethod)")
    public void weaveJoinPoint(JoinPoint joinPoint) throws Throwable {
        if(joinPoint.getThis().getClass().getName().equals("com.facebook.react.uimanager.UIManagerModule")){
            return;
        }
        LogMethodAspect.logBefore(true,TAG,joinPoint,new IBefore(){
            @Override
            public String descExtraForLog(){
                return "";
            }
        });
    }


}
