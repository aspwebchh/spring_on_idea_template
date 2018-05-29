package common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;
import java.util.List;

@Aspect
public class Logger {

    @Around("execution(* controller.Default.*(..))")
    public void watchPerformance(ProceedingJoinPoint jp) {
        try {
            System.out.println("Silencing cell phones");
            System.out.println("Taking seats");
            jp.proceed();
            System.out.println("CLAP CLAP CLAP!!!");
        } catch (Throwable e) {
            System.out.println("Demanding a refund");
        }
    }

//    @Before("execution(* controller.Default.*(..))")
//    public void before(JoinPoint join){
//        //获取方法名
//        String mathName=join.getSignature().getName();
//        //获取参数列表
//        List<Object> args = Arrays.asList(join.getArgs());
//
//        System.out.println("前置通知---->before   方法名是:"+mathName+"\t参数列表是:"+args);
//    }

//    @After("execution(* controller.Default.*(..))")
//    public void after(){
//        System.out.println("后置通知---->after....");
//    }

//    @AfterReturning("execution(* controller.Default.*(..))")
//    public void after() {
//        System.out.println("后置通知---->after returning....");
//    }

//    @AfterThrowing("execution(* controller.Default.*(..))")
//    public void after(){
//        System.out.println("后置通知---->after throwing....");
//    }

    @Pointcut( value = "execution(* controller.Default.*(..))")
    public void pointcut() {
        System.out.println("pointcut");
    }

    @After( "pointcut()")
    public void after() {
        System.out.println("后置通知---->after....");
    }
}
