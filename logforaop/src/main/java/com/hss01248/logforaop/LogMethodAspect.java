package com.hss01248.logforaop;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public class LogMethodAspect {

    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static Object logAround(boolean enableLog, String TAG, boolean logResultIfReturnNull, ProceedingJoinPoint joinPoint, IAround around) throws Throwable{
        long start = System.currentTimeMillis();
        String des = "";
        //before
        try {
            String s = joinPoint.getSignature().toShortString();
            //Html5JsObj.handleBackBehavior(..)
            des = s;
            if(enableLog){
                Object[] args = joinPoint.getArgs();
                if(args!= null && s.contains("..")){
                    des = s.replace("..", toStrings(args));
                }
                if(joinPoint.getThis() != null){
                    des = Integer.toHexString(joinPoint.getThis().hashCode())+"@"+des;
                }

                //des = des+", \ninvoke by url:"+url;
                Log.v(TAG, Thread.currentThread().getName()+", start of "+des+", \n"+(around==null ? "":around.descExtraForLog()));
            }
            if(around != null){
                around.before(joinPoint,des);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        try {
            Object obj =   joinPoint.proceed();
            if(enableLog){

                if(obj !=null){
                    Log.i(TAG, "end of "+des+", return val:"+obj+",cost:"+(System.currentTimeMillis() - start)+"ms \n"+(around==null ? "":around.descExtraForLog()));
                }else {
                    if(logResultIfReturnNull){
                        Log.i(TAG, "end of "+des+", return val:"+obj+",cost:"+(System.currentTimeMillis() - start)+"ms \n"+(around==null ? "":around.descExtraForLog()));
                    }
                }
            }
            if(around != null){
                around.onResult(joinPoint,obj,des,System.currentTimeMillis() - start);
            }
            return obj;
        }catch (Throwable throwable){
            if(enableLog){
                Log.w(TAG, "exception happened: "+throwable.getClass().getSimpleName()+" "+des+" "+
                        throwable.getMessage()+",cost:"+(System.currentTimeMillis() - start)+"ms \n"+(around==null ? "":around.descExtraForLog()));
                Log.w(TAG, throwable.toString());
            }
            try {
                if(around != null){
                    around.handleException(joinPoint,throwable,System.currentTimeMillis() - start);
                }
            }catch (Throwable throwable1){
                throwable1.printStackTrace();
            }

            throw throwable;
        }
    }

    public static void logBefore(boolean enableLog, String TAG,  JoinPoint joinPoint, IBefore before) throws Throwable{
        String des = "";
        //before
        try {
            String s = joinPoint.getSignature().toShortString();
            //Html5JsObj.handleBackBehavior(..)
            des = s;
            if(enableLog){
                Object[] args = joinPoint.getArgs();
                if(args!= null && s.contains("..")){
                    des = s.replace("..", toStrings(args));
                }
                if(joinPoint.getThis() != null){
                    des = Integer.toHexString(joinPoint.getThis().hashCode())+"@"+des;
                }
                //des = des+", \ninvoke by url:"+url;
                Log.d(TAG, Thread.currentThread().getName()+", start of "+des+", \n"+(before==null ? "":before.descExtraForLog()));
            }
            if(before != null){
                before.before(joinPoint,des);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }


    }

    public static String toStrings(Object[] args) {
        StringBuilder sb = new StringBuilder();
        if(args == null){
            return "";
        }
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if(arg instanceof WebView){
                sb.append("webview").append(arg.hashCode());
            }else {
                sb.append(toStr(arg));
            }
            if(i != args.length-1){
                sb.append(",");
            }
        }
        return sb.toString();
    }

    private static String toStr(Object arg) {
        try {
            if(arg == null){
                return "null";
            }
            String  str =  gson.toJson(arg);
            if(TextUtils.isEmpty(str) || "null".equals(str)){
                //return ObjParser.parseObj(arg);
                //return arg.getClass().getSimpleName()+arg.toString()
                return arg.toString();
            }
            return str;
        }catch (Throwable throwable){
            return ObjParser.parseObj(arg);
        }
    }

    public interface IAround{
      default   void before(ProceedingJoinPoint joinPoin, String desc){}

       default void onResult(ProceedingJoinPoint joinPoin, Object result, String desc, long cost){}

       default Object handleException(ProceedingJoinPoint joinPoin, Throwable throwable, long cost){
          return null;
      }

       default String descExtraForLog(){
          return "";
       }
    }

    public interface IBefore {
        default void before(JoinPoint joinPoin, String desc) {
        }
        default String descExtraForLog(){
            return "";
        }
    }
}
