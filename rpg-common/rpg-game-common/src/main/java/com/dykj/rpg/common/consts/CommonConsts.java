package com.dykj.rpg.common.consts;

/**
 * @Author: jyb
 * @Date: 2020/9/4 13:56
 * @Description:
 */
public interface CommonConsts {
    /**
     * pc端验证码
     */
    String TEST_VALIDATE_CODE = "Let Me In!";

    /**
     * 账号key
     */
    String ACCOUNT_KYE = "account_key";


    /**
     * 角色列表
     */
    String PLAYER_IDS = "PLAYER_IDS";

    /**
     * 数据库监听
     */
    String DB_LISTEN = "db_listen";


    /**
     * 三十分钟
     */
    int PLAYER_CACHE_INVALID = 30 * 60 * 1000;


    /**
     * DB 自增的基础值 serverID *DB_AUTO_INCREMENT
     */
    int DB_AUTO_INCREMENT=131072;

    /**
     * 是否为负数
     */
    String NUMBER_TYPE = "^-\\d+$";

    /**
     * gm指令键值对格式
     */
    String GM_COMMAND_MAP = "^@(\\w+)_+(\\w+)(\\s+(\\w+:+(\\w+|(\\d+|-+\\d+))))*$";

    /**
     * gm指令数组格式
     */
    String GM_COMMAND_ARR = "^@(\\w+)_+(\\w+)(\\s+(\\w+|(\\d+|-+\\d+)))*$";

    /**
     * 替换前后中括号
     */
    String STR_BRACKETS = "[\\[\\]]";

    String STR_SYMBOL_UNDERSCORE = "_";
    String STR_FORWARD_SLASH = "\\\\";
    String STR_BACKSLASH = "/";
    String STR_POINT = ".";
    String STR_COLON = ":";
    String STR_COMMA = ",";
    String STR_QUESTION_MARK = "?";
    String STR_SPE_SYMBOL_ONE = "&";
    String STR_SPE_SYMBOL_TWO = "=";
    String STR_SPACE = "\\s+";//空格
    String STR_CLASS = "class";
    String STR_MODEL = "Model";

    /**
     * 万分比
     */
    float THOUSAND =10000f;

    /**
     * 一万
     */
    int THOUSAND_VALUE =10000;
    /**
     * 第一个关卡
     */
    int FIRST_MISSION= 1001;

    String JOB_GROUP_NAME = "TASK_JOB_GROUP";//任务组别名
    String TRIGGER_GROUP_NAME = "TASK_TRIGGER_GROUP"; //触发器组别名
}
