package com.dykj.rpg.db.consts;

/**
 * @Description
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/8/7
 */
public interface PropertyConstants
{
    /** 字符常量：逗号 , */
    String COMMA = ",";
    /** 字符常量：中括号（左） [ */
    String BRACKET_START = "[";
    /** 字符常量：中括号（右） ] */
    String BRACKET_END = "]";
    /** 字符常量：冒号 : */
    String COLON = ":";
    /** 字符常量：反斜杠 \ */
    String BACKSLASH = "\\";
    /** 字符常量：下划线 _ */
    String UNDERSCORE = "_";
    /** 匹配最后一位字符, {0}可替换字符 */
    String MATCH_LAST_CHARACTER = "(?){0}(?!.*{0})";

    Long NUMBER_TEN_THOUSAND = 10000L;
}