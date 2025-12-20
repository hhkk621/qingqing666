package com.javasm.qingqing.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE) // 作用于方法
@Retention(RetentionPolicy.RUNTIME) // 运行时保留（AOP需反射读取）
@Documented
public @interface OperateModule {
    String value(); // 模块名称，如“用户管理-新增用户”
}