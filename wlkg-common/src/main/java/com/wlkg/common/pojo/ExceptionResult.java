package com.wlkg.common.pojo;

import com.wlkg.common.enums.ExceptionEnums;
import lombok.Data;

 //封装异常的信息返回----给用户的
@Data
public class ExceptionResult {
    private int status;
    private String message;
    //时间戳
    private Long timestamp;
    //构造方法，传的是枚举对象
    public ExceptionResult(ExceptionEnums exceptionEnums){
        this.status = exceptionEnums.getCode();
        this.message = exceptionEnums.getMsg();
        //当前时间戳
        this.timestamp = System.currentTimeMillis();
 }
}
