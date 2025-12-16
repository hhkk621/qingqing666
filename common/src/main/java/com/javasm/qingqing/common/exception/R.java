package com.javasm.qingqing.common.exception;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//当前类，计划作为全局的返回数据类型
@Data
public class R {
    private Integer code;
    private String msg;
    private String timestamp;
    private Object data;

    public static R ok(){
        return ok(null);
    }

    public static R ok(Object data){
        R r = new R();
        r.setCode(200);
        r.setMsg("success");
        r.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        if (data != null)
            r.setData(data);
        return r;
    }

    public static R error(String msg){
        return error(500,msg);
    }

    public static R error(Integer code,String msg){
        R r = new R();
        r.setCode(code);
        r.setMsg(msg);
        r.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return r;
    }
}
