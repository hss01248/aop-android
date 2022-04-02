package com.hss01248.rxjavademo;

import com.hss01248.logforaop.LogMethodAspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * @Despciption todo
 * @Author hss
 * @Date 02/04/2022 09:36
 * @Version 1.0
 */
@Aspect
public class CallAspect {

    @Before("execution(* com.hss01248.rxjavademo.MyObservable.*(..)) || execution(* com.hss01248.rxjavademo.MyObserver.*(..))")
    public void weaveJoinPoint(JoinPoint joinPoint) throws Throwable {
        LogMethodAspect.logBefore(true,"aspect",joinPoint,new LogMethodAspect.IBefore(){
            @Override
            public String descExtraForLog(){
                return "";
            }
        });
    }
}
