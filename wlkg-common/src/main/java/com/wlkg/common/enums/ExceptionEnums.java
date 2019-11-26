package com.wlkg.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//用枚举描述错误信息
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnums{
    /*
    1xxx 用户问题
    2xxx 商品问题
    3xxx 分类问题
    4xxx 品牌问题
    5xxx 规格问题
    6xxx 搜索问题
    7XXX 验证码或者用户
     */
    //定义常量，code-状态码 ， msg-信息
    PRICE_CANNOT_BE_NULL(400,"价格不能为空"),
    CATEGORY_NOT_FOUND(2001,"添加的不能为空"),
    INSERT_INTO_IS_NOT(2002,"添加内容为空"),
    Brand_is_not(4001,"品牌不存在"),
    GOODS_NOT_FOUND(2003,"商品不能为空"),
    GOODS_UPDATE_ERROR(2004,"商品修改失败"),
    CATEGORY_IS_EMPTY(2005,"商品搜索为空"),
    INVALID_VERFIY_CODE(7001,"验证码有误"),
    REGISTER_SUCCESS(7002,"注册成功了"),
    INVALID_USERNAME_PASSWORD(1001,"用户名或者密码错误"),
    NO_AUTHORIZED(1002,"没有授权");

    private int code;
    private String msg;

}
