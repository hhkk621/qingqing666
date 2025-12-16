package com.javasm.qingqing.webuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户财产表(WebUserPossession)表实体类
 *
 * @author makejava
 * @since 2025-11-29 15:09:47
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebUserPossession extends Model<WebUserPossession> implements Cloneable{

    @TableId(type = IdType.INPUT)
    private Integer uid;
    //财富值：消费增长
    private Long richNum;
    //魅力值：接单收礼物增长
    private Long charmNum;

    @Override
    public WebUserPossession clone(){
        Object clone = null;
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return (WebUserPossession) clone;
    }
}

