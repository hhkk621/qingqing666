package com.javasm.qingqing.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 作用于方法
@Retention(RetentionPolicy.RUNTIME)
public @interface OperateContent {
    String value(); // 操作内容，如"查看用户表"
}