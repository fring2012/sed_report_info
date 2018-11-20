package com.adupdate.sed_report_demo.aspect;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;


@Aspect
public class LogAspect {

    @Pointcut("@annotation(com.adupdate.sed_report_demo.aspect.annotation.MethodNameLog)")
    public void onMethodNameLog(){}

    @Around("onMethodNameLog()")
    public void  onMethodNameLog(final ProceedingJoinPoint joinPoint)  throws Throwable{
        Log.d(joinPoint.getTarget().getClass().getSimpleName(),joinPoint.getSignature().getName());

        joinPoint.proceed();
    }

    public String getMethodName(ProceedingJoinPoint joinPoint){
        Signature sig = joinPoint.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        return msig.getName();
    }



    public Method getMethod(ProceedingJoinPoint joinPoint){
        Signature sig = joinPoint.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        Object target = joinPoint.getTarget();
        Method currentMethod = null;
        Log.d("11111", "getMethod: " + sig.getName());
        try {
            currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return currentMethod;
    }
}
