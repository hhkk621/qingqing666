package com.javasm.qingqing.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @className: SaveToken
 * @author: gfs
 * @date: 2025/12/3 10:31
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SaveToken {
}
