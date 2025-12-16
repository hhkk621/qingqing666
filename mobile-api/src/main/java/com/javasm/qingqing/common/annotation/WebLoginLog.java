package com.javasm.qingqing.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @className: WebLoginLog
 * @author: gfs
 * @date: 2025/11/29 11:47
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WebLoginLog {
}
