package com.wlkg.common.advice;

import com.wlkg.common.enums.ExceptionEnums;
import com.wlkg.common.exception.WlkgException;
import com.wlkg.common.pojo.ExceptionResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
/*
@ControllerAdive:默认情况下，会拦截所有加了@Controller的类
@ExceptionHandler(RuntimeException.class):作用在方法上，声明要处理的异常类型，
可以有多个，这里指定的是RuntimeException。被声明的方法可以看做一个SpringMVC的Handler
 参数是要处理的异常，类型必须要匹配
返回结果可以是ModelAndView、ResponseEntity等，基本与Hanlder类似
这里等于从新定义了一个返回结果，我们可以随意指定想要返回的类型。此处使用了String
 */

@ControllerAdvice
public class CommonExceptionHandler {
    //该方法是处理的异常类型
    @ExceptionHandler(WlkgException.class)
    public ResponseEntity<ExceptionResult> handleException(WlkgException e){
        ExceptionEnums exceptionEnums = e.getExceptionEnums();
        ExceptionResult result = new ExceptionResult(exceptionEnums);
        //我们暂定返回状态码， 然后从异常中获取友好提示信息
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
