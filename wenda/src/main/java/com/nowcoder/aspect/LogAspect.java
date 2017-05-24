package com.nowcoder.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * Created by seu on 2017/5/24.
 */

@Aspect//指定其是一个切面
@Component//指定其作为一个组件在依赖注入的时候构造出来
public class LogAspect {

    //设置一个logger,
    private static final Logger logger= LoggerFactory.getLogger(LogAspect.class);

    //execution表示执行
    //第一个*表示返回值类型
    //com.nowcoder.controller为包名
    //第二个*表示类名（通配符）
    //第三个*表示方法名
    //(...)表示参数列表
    //JoinPoint表示切点类
    @Before("execution(* com.nowcoder.controller.*.*(..))")
    public void beforeMethod(JoinPoint joinPoint){
        StringBuilder sb = new StringBuilder();
        for(Object arg:joinPoint.getArgs()){
            sb.append("args:"+arg.toString());
        }
        logger.info("before method::"+sb.toString()+new Date());
    }

    @After("execution(* com.nowcoder.controller.*.*(..))")
    public void afterMethod(){
        logger.info("after method::"+new Date());
    }
}
