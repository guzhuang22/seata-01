package com.atguigu.springcloud.alibaba.utils.aop;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Aspect
@Component
public class ClientCallAspect {
    private static final Logger logger = LoggerFactory.getLogger(ClientCallAspect.class);

    @Pointcut("execution( * com.atguigu.springcloud.controller..*.*(..) )")
    public void pointcutLock() {
    }

    @Before("pointcutLock()")
    public void before(JoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method targetMethod = methodSignature.getMethod();
        String name = point.getSignature().getName();
        String str = String.format("请求微服务模块 --> 类名：%s --> 方法名： %s() ",
                targetMethod.getDeclaringClass().getSimpleName(), name);
        String requestInfo = DateUtil.now() + str + "请求参数：";
        requestInfo = requestInfo.concat(JSONUtil.toJsonStr(point.getArgs()));
        logger.info(requestInfo);
    }

    @AfterReturning(pointcut = "pointcutLock()", returning = "res")
    public void after(JoinPoint joinPoint, Object res) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        String name = joinPoint.getSignature().getName();
        String str = String.format("请求微服务模块 --> 类名：%s --> 方法名： %s() ",
                targetMethod.getDeclaringClass().getSimpleName(), name);
        JSONObject object = JSONUtil.parseObj(res);
        if(object.get("body")!=null){
            logger.info("{}  调用服务：{}  返回参数：{}", DateUtil.now(), str, "文件流");
        }else {
            logger.info("{}  调用服务：{}  返回参数：{}", DateUtil.now(), str, JSONUtil.toJsonStr(res));
        }
    }


}
