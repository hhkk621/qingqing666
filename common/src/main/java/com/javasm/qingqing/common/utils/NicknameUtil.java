package com.javasm.qingqing.common.utils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @className: NicknameUtil
 * @description:
 * @author: MrWang
 * @date: 2025/4/17 01:20
 * @version: 0.1
 * @since: jdk17
 */
public class NicknameUtil {
    // 二次元风格词库
    private static final String[] ANIME_PREFIX = {
            "萌新", "御宅", "星尘", "幻月", "绯红", "虚空", "量子", "次元", "喵帕斯", "中二",
            "暴走", "黑羽", "白夜", "疾风", "暗影", "苍穹", "绯想", "逆光", "绝对", "无限"
    };

    private static final String[] ANIME_SUFFIX = {
            "の使徒", "酱", "少年", "少女", "魔导师", "勇者", "小狐娘", "执事", "女仆", "骑士",
            "舰长", "指挥官", "观测者", "旅行者", "炼金术士", "契约者", "轮回者", "破晓", "黄昏", "领域"
    };

    // 武侠风格词库
    private static final String[] WUXIA_ADJECTIVE = {
            "孤影", "寒江", "惊鸿", "流云", "飞雪", "断魂", "绝尘", "苍龙", "玄铁", "紫电",
            "青霜", "血饮", "醉月", "追风", "踏浪", "碎星", "焚天", "凌霄", "幽冥", "幻灭",
            "霜天", "赤霄", "烟雨", "残阳", "落英", "无痕", "断水", "惊雷", "穿云", "破军",
            "九幽", "天罡", "地煞", "龙吟", "凤鸣", "乾坤", "八荒", "六合", "北斗", "南离"
    };

    private static final String[] WUXIA_NOUN = {
            "剑客", "刀狂", "琴魔", "枪圣", "书生", "刺客", "隐士", "掌门", "少主", "妖女",
            "尊者", "真人", "仙子", "修罗", "鬼面", "居士", "浪子", "镖头", "神捕", "蛊师",
            "飞鹰", "银钩", "铁扇", "判官", "飞贼", "药王", "毒尊", "神丐", "剑痴", "刀皇",
            "棍魔", "鞭侠", "戟王", "暗器", "流星锤", "判官笔", "屠龙刀", "倚天剑", "打狗棒", "孔雀翎",
            "金轮", "玉箫", "铁琵琶", "铜人", "银针", "血滴子", "生死符", "冰魄针", "暴雨梨花", "子母剑"
    };
    public static String generateStylizedNickname() {
        // 50%概率选择二次元风格
        if (ThreadLocalRandom.current().nextBoolean()) {
            return ANIME_PREFIX[ThreadLocalRandom.current().nextInt(ANIME_PREFIX.length)]
                    + ANIME_SUFFIX[ThreadLocalRandom.current().nextInt(ANIME_SUFFIX.length)];
        }
        // 50%概率选择武侠风格
        else {
            return WUXIA_ADJECTIVE[ThreadLocalRandom.current().nextInt(WUXIA_ADJECTIVE.length)]
                    + WUXIA_NOUN[ThreadLocalRandom.current().nextInt(WUXIA_NOUN.length)];
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++)
            System.out.println(generateStylizedNickname());
    }
}
