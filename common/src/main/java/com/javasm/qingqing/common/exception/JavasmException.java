package com.javasm.qingqing.common.exception;

import lombok.Getter;

/**
 * @className: JavasmException
 * @author: gfs
 * @date: 2025/11/20 15:04
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@Getter
public class JavasmException extends RuntimeException {

  private ExceptionEnum exceptionEnum;

    public JavasmException(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }
}
