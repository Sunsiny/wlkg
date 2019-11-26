package com.wlkg.common.exception;

import com.wlkg.common.enums.ExceptionEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


//自定义异常----是为了抛枚举对象异常的信息
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WlkgException extends RuntimeException {
    private ExceptionEnums exceptionEnums;
}
