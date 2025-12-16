package com.javasm.qingqing.game.vo;

import lombok.Data;

/**
 * @className: HomeGameVo
 * @author: gfs
 * @date: 2025/11/29 16:09
 * @version: 0.1
 * @since: jdk17
 * @description:
 */
@Data
public class HomeGameVo {
    private Integer uid;
    private String headPic;
    private String nickname;
    private String gameName;
    private String price;
    private Integer gameId;
}
