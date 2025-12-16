package com.javasm.qingqing.common.utils;


import com.javasm.qingqing.common.exception.ExceptionEnum;
import com.javasm.qingqing.common.exception.JavasmException;

public class ParameterUtils {

    public static void checkParameter(Object... objects){
        if (objects == null || objects.length == 0){
            throw new JavasmException(ExceptionEnum.Parameter_NUll);
        }
        for (Object obj : objects){
            if (obj == null){
                throw new JavasmException(ExceptionEnum.Parameter_NUll);
            }
            if (obj instanceof String){
                if ("".equals(obj)){
                    throw new JavasmException(ExceptionEnum.Parameter_NUll);
                }
            }
        }
    }
}
