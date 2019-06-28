package com.vergo.demo.aop.login.ascept;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.vergo.demo.aop.login.LoginActivity;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * <p>Created by Fenghj on 2019/6/28.</p>
 */
@Aspect // 定义一个切面类
public class LoginCheckAscept {
    private final static String TAG = "fhj >>> ";

    // 1、应用中用到了那些注解，放到当前的切入点进行处理（找到需要处理的切入点）
    // execution，以方法执行时作为切点，触发Aspect类
    // * *(..)) 可以处理ClickBehavior这个类所有的方法
    @Pointcut("execution(@com.vergo.demo.aop.login.annotation.LoginCheck * *(..))")
    public void pointcut() { }

    // 2、对切入点如何处理
    @Around("pointcut()")
    public Object joinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Context context = (Context) joinPoint.getThis();
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        boolean isLogin = sharedPreferences.getBoolean("isLogin", false);
        if(isLogin) {
            Log.e(TAG, "检测到已登录！");
            return joinPoint.proceed();
        } else {
            Log.e(TAG, "检测到未登录！");
            context.startActivity(new Intent(context, LoginActivity.class));
            return null;
        }
    }
}
