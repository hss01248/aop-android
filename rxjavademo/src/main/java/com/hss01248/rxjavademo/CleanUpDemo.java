package com.hss01248.rxjavademo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @Despciption todo
 * @Author hss
 * @Date 07/04/2022 16:31
 * @Version 1.0
 */
@Aspect
public class CleanUpDemo {

    @Around("execution(* com.hss01248.rxjavademo.MainActivity.cleanUp())")
    public Object cleanUp(ProceedingJoinPoint joinPoint) throws Throwable{
        return safeExecute(joinPoint,false);
    }

    static Object safeExecute(ProceedingJoinPoint joinPoint){
        return safeExecute(joinPoint,null);
    }

    /**
     *
     * public boolean cleanUp() {...}
     * Caused by java.lang.IncompatibleClassChangeError
     * Class 'java.lang.Boolean' does not implement interface 'org.aspectj.lang.JoinPoint'
     * in call to 'java.lang.Object[] org.aspectj.lang.JoinPoint.getArgs()'
     * @param joinPoint
     * @param objIfException
     * @return
     */
    static Object safeExecute(ProceedingJoinPoint joinPoint,Object objIfException){
        try {
            return joinPoint.proceed(joinPoint.getArgs());
            /*if(joinPoint.getArgs() == null){
                return joinPoint.proceed();
            }else {
                return joinPoint.proceed(joinPoint.getArgs());
            }*/
        }catch (Throwable throwable){
           throwable.printStackTrace();
            return objIfException;
        }
    }
}
