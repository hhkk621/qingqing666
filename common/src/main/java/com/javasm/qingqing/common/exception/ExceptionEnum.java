package com.javasm.qingqing.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 提前想好，整个项目，所有的接口，都应该返回统一的格式
 * {
 *     code:200
 *     msg:success
 *     timestamp：时间
 *     data:{}
 * }
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
//在默认情况下，enum类型的数据，会把User_Not_Found字符串当做值返回
//加上注解之后，会把属性当做json返回
//这里的code，是根据业务由后端开发人员，随意定义的，然后前端开发人员来遵守，一旦确定，不能随意更改
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ExceptionEnum {
    User_Not_Found(1001,"用户不存在"),
    Parameter_NUll(1002,"参数为空"),
    Not_Login(1003, "未登录"),
    Password_Error(1004, "密码错误"),
    User_Exist(1005, "用户已存在"),

    Token_Expired_Error(2001,"令牌过期"),
    Token_Unsupported_Error(2002,"令牌错误"),
    Token_Signature_Error(2003,"令牌签名错误"),



    SYSTEM_ERROR(500,"系统异常"),
    RESOURCE_NOT_FOUND(405,"请求地址不存在"),
    ;
    private Integer code;
    private String msg;
}
