package com.atguigu.springcloud.alibaba.utils.globalexception;

import com.atguigu.springcloud.alibaba.utils.entity.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;


/*
 * @Author guzhuang
 * @Date 2020/11/18 10:10
 * @Desc <p> 参数校验全局异常捕获<p>
 * @Return
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandling {

    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandling.class);

    /**
     * 自定义异常
     */
    @ExceptionHandler(CommonRuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> exception(CommonRuntimeException e) {
        e.printStackTrace();
        logger.error("错误信息{}", e);
        Map<String, Object> json = new HashMap<>();
        json.put("code", e.getCode());
        json.put("msg", e.getMessage());
        return json;
    }

    /**
     * 拦截表单参数校验
     */
    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> bindException(BindException e) {
        logger.error("错误信息{}", e);
        Map<String, Object> json = new HashMap<>();
        BindingResult bindingResult = e.getBindingResult();
        json.put("code", ResultCode.PARAM_ERR.getCode());
        json.put("msg", Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        if (Objects.requireNonNull(bindingResult.getFieldError()) != null) {
            List<ObjectError> list=bindingResult.getAllErrors();
            if(!CollectionUtils.isEmpty(list)) {
                FieldError fieldError = (FieldError) bindingResult.getAllErrors().get(0);
                if (fieldError != null && fieldError.isBindingFailure()) {
                    json.put("msg", fieldError.getField() + "参数错误");
                }
            }
        }

        return json;
    }

    /**
     * 拦截请求方式
     */
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> bindException(HttpMediaTypeNotSupportedException e) {
        logger.error("错误信息{}", e);
        Map<String, Object> json = new HashMap<>();
        json.put("code", ResultCode.PARAM_ERR.getCode());
        json.put("msg", "不支持此请求方式");
        return json;
    }

    /**
     * 参数格式错误
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        Map<String, Object> json = new HashMap<>();
        logger.error("错误信息{}", e);
        json.put("code", ResultCode.PARAM_ERR.getCode());
        json.put("msg", "参数格式错误");
        return json;
    }

    /**
     * 参数格式错误
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> httpMessageNotReadable(HttpMessageNotReadableException e) {
        Map<String, Object> json = new HashMap<>();
        logger.error("错误信息:{}", e);
        json.put("code", ResultCode.PARAM_ERR.getCode());
        json.put("msg", "参数格式错误");
        return json;
    }

    // <3> 处理单个参数校验失败抛出的异常
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> constraintViolationExceptionHandler(ConstraintViolationException e) {
        logger.error("错误信息{}", e);
        Map<String, Object> json = new HashMap<>();
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<String> collect = constraintViolations.stream()
                .map(o -> o.getMessage())
                .collect(Collectors.toList());
        json.put("code", ResultCode.PARAM_ERR.getCode());
        json.put("msg", Objects.requireNonNull(collect.get(0)));
        return json;
    }


    /**
     * 通用异常
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public Map<String, Object> exception(Exception e) {
        e.printStackTrace();
        logger.error("错误信息{}", e);
        Map<String, Object> json = new HashMap<>();
        json.put("code", ResultCode.FAIL.getCode());
        json.put("msg", "操作失败,请联系系统管理员");
        return json;
    }

    /**
     * 上传文件过大
     */
    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleBusinessException(MaxUploadSizeExceededException e) {
        Map<String, Object> json = new HashMap<>();
        logger.error("错误信息:{}", e);
        json.put("code", ResultCode.PARAM_ERR.getCode());
        json.put("msg", "上传文件太大");
        return json;
    }
}