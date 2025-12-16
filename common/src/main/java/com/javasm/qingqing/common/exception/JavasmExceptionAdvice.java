package com.javasm.qingqing.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @className: JavasmExceptionAdvice
 * @author: gfs
 * @date: 2025/11/20 15:06
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@RestControllerAdvice
public class JavasmExceptionAdvice {

    @ExceptionHandler(JavasmException.class)
    public ResponseEntity<ExceptionEnum> f1(JavasmException ex){
        //自定义异常，都是程序运行过程中，正常发生的业务，不需要打印异常信息
        ExceptionEnum exceptionEnum = ex.getExceptionEnum();
        return ResponseEntity.ok(exceptionEnum);
    }

    //处理运行时异常
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<R> handleRuntimeException(RuntimeException ex){
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorResponse(ExceptionEnum.SYSTEM_ERROR));
    }
    //HTTP异常
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<R> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex){
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(buildErrorResponse(ExceptionEnum.RESOURCE_NOT_FOUND));
    }

    //全局异常
    @ExceptionHandler(Exception.class)
    public ResponseEntity<R> handleException(Exception e){
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorResponse(ExceptionEnum.SYSTEM_ERROR));
    }
    private R buildErrorResponse(ExceptionEnum errorCode) {
        return R.error(errorCode.getCode(),errorCode.getMsg());
    }
}
