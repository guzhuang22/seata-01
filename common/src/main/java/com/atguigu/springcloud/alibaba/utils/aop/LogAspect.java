package com.atguigu.springcloud.alibaba.utils.aop;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对controller类的日志记录
 *
 * @author XHT
 * @date 2020/11/12
 */
//@Aspect
//@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution( * com.atguigu.springcloud.controller..*.*(..) )")
    public void controllerAspect() {}

    @Pointcut("execution(* com.atguigu.springcloud.service..*.*(..) )")
    public void serviceAspect() {}

    @Before("controllerAspect() || serviceAspect()")
    public void beforeMethod(JoinPoint joinPoint) {
        String clazzName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        logger.info("ClazzName:[{}],MethodName:[{}],start exec.", clazzName, methodName);
        try {
            Object[] args = joinPoint.getArgs();
            String[] paramNames = getFieldsName(clazzName, methodName);
            String argsStr = getParam(paramNames, args);
            logger.info("ClazzName:[{}],MethodName:[{}],args is:[{}].", clazzName, methodName, argsStr);
        } catch (Exception e) {
            logger.error("ClazzName:[{}],MethodName:[{}],get FieldsName error.", clazzName, methodName, e);
        }
    }

    @AfterReturning(returning = "result", pointcut = "controllerAspect() || serviceAspect()")
    public void afterReturningMethod(JoinPoint joinPoint, Object result) {
        String clazzName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        logger.info("ClazzName:[{}],MethodName:[{}],Result:[{}] End exec, Used Millis:[{}]. ", clazzName, methodName,
            JSONUtil.toJsonStr(result));
    }

//    @Around("controllerAspect()")
//    public Object processForController(ProceedingJoinPoint joinPoint) {
//        Object result = null;
//        String clazzName = joinPoint.getTarget().getClass().getName();
//        String methodName = joinPoint.getSignature().getName();
//        long startTime = System.currentTimeMillis();
//        try {
//            result = joinPoint.proceed();
//        } catch (Throwable t) {
//            logger.error("ClazzName:[{}],MethodName:[{}] Exec error: ", clazzName, methodName, t);
//            return new ResultData(ResultCode.ERROR);
//        } finally {
//            long costTime = System.currentTimeMillis() - startTime;
//            logger.info("ClazzName:[{}],MethodName:[{}],Used Millis:[{}]. ", clazzName, methodName, costTime);
//        }
//        return result;
//    }

    /**
     * 打印方法参数值 基本类型直接打印，非基本类型需要重写toString方法
     *
     * @param paramsArgsName
     *            方法参数名数组
     * @param paramsArgsValue
     *            方法参数值数组
     */
    private String getParam(String[] paramsArgsName, Object[] paramsArgsValue) {
        if (ArrayUtil.isEmpty(paramsArgsName) || ArrayUtil.isEmpty(paramsArgsValue)) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < paramsArgsName.length; i++) {
            // 参数名
            String name = paramsArgsName[i];
            // 参数值
            Object value = paramsArgsValue[i];
            buffer.append(name + " = ");
            if (ObjectUtil.isNotNull(value) && isPrimite(value)) {
                buffer.append(value + "  ,");
            } else {
                buffer.append(JSONUtil.toJsonStr(value) + ",");
            }
        }
        return buffer.toString();
    }

    /**
     * 判断是否为基本类型：包括String
     *
     * @param object1
     *            clazz
     * @return true：是; false：不是
     */
    private boolean isPrimite(Object object1) {
        if (object1 instanceof Integer) {
            return true;
        } else if (object1 instanceof Long) {
            return true;
        } else if (object1 instanceof Short) {
            return true;
        } else if (object1 instanceof Boolean) {
            return true;
        } else if (object1 instanceof Byte) {
            return true;
        } else if (object1 instanceof Character) {
            return true;
        } else if (object1 instanceof Double) {
            return true;
        } else if (object1 instanceof Float) {
            return true;
        } else if (object1 instanceof String) {
            return true;
        } else {
            return  false;
        }
    }
    /**
     * 获取方法参数名称
     *
     * @param clazzName
     * @param methodName
     * @return
     * @throws Exception
     */
    private String[] getFieldsName(String clazzName, String methodName) throws Exception {
        Class<?> clazz = Class.forName(clazzName);
        String clazzName2 = clazz.getName();
        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(clazz);
        pool.insertClassPath(classPath);

        CtClass ctClass = pool.get(clazzName2);
        CtMethod ctMethod = ctClass.getDeclaredMethod(methodName);
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute)codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            return null;
        }
        String[] paramsArgsName = new String[ctMethod.getParameterTypes().length];
        int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
        for (int i = 0; i < paramsArgsName.length; i++) {
            paramsArgsName[i] = attr.variableName(i + pos);
        }
        return paramsArgsName;
    }

}
