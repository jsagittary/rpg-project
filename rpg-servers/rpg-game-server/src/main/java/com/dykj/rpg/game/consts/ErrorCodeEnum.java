package com.dykj.rpg.game.consts;

/**
 * @Author: jyb
 * @Date: 2020/10/12 11:07
 * @Description:
 */
public enum ErrorCodeEnum {


    SUCCESS(100, "成功"),
    ERROR(101, "服务器异常"),
    CLIENT_PRAMS_ERROR(102, "客户端参数异常"),
    DATA_ERROR(103, "服务器数据异常"),
    CONFIG_ERROR(104, "服务器静态数据配置异常"),
    ACCOUNT_NOT_EXIST(1001, "账号不存在"),
    LOGIN_MSG_ERROR(1002, "登录信息不存在"),
    PLAYER_NOT_EXIST(1003, "玩家不存在"),
    PLAYER_NAME_REPEAT(1004, "该名称已经存在"),
    CHARACTER_NOT_EXIST(1005, "职业不存在"),
    GM_COMMAND_FORMAT_ERROR(1006, "GM指令格式错误"),
    GM_COMMAND_ERROR(1007, "GM指令执行异常, 请检查后重试"),
    GM_ADD_ITEM_ERROR(1008, "GM指令执行添加道具失败, 没有匹配到对应道具ID"),
    EQUIP_CHARACTER_ERROR(1009, "该装备与职业不符"),
    PLAYER_LEVEL_ERROR(1010, "您的等级不够，无法做此操作"),
    ITEM_UPDATE_ERROR(1011, "道具更新失败, 请检查后重试"),
    ITEM_NOT_SELL(1012, "道具不可出售!"),
    ITEM_QUANTITY_NOT_ENOUGH(1013, "道具数量不足"),
    ITEM_NUM_TOTAL_UPPERLIMIT_SEND_MAIL(1014, "道具数量达到总上限则进行邮件发送"),
    ITEM_NUM_DAILY_UPPERLIMIT(1015, "道具数量达到每日上限"),
    ITEM_SELLTYPE_SELLPRICE_ERROR(1016, "道具出售货币ID或道具出售价格配置错误"),
    ITEM_LOCKED(1017, "道具已上锁"),
    ITEM_NOT_SUPPORT_LOCKED(1018, "当前道具不支持上锁"),
    ITEM_NOT_EXIST(1019, "背包中该道具不存在"),
    DIAMOND_NOT_ENOUGH_QUANTITY(1020, "钻石数量不够!"),
    PLAYER_HAS_NO_SKILL(1021, "您未拥有该技能"),
    SKILL_EXISTED(1022, "技能已存在"),
    SKILL_NOT_EXISTED(1023, "技能不存在"),
    SKILL_LIST_IS_NULL(1024, "技能列表为空"),
    SKILL_QUANTITY_NOT_ENOUGH(1025, "技能升级所需要消耗的道具数量不足"),
    SKILL_LEVEL_UPPER_LIMIT(1026, "技能等级已到达上限"),
    EQUIP_PART_ERROR(1027, "该装备无法穿戴到该位置"),
    HAND_UP_AWARD_ERROR(1028, "挂机时间较短,还没有挂机奖励"),
    QUICK_HAND_UP_NUM_ERROR(1029, "快速挂机次数已经不足"),
    ENTER_BATTLE_SERVER_ERROR(1030, "进入战斗服异常"),
    ROLES_LEVEL_UPPER_LIMIT(1031, "角色等级已到达上限"),
    WEAR_ATTRIBUTE_ERROR(1032, "属性不够，无法穿戴此装备"),

    HAND_UP_COST_NOT_ENOUGH(1033, "领取挂机奖励元宝不足"),

    SKILL_UP_TYPE_ERROR(1034, "核心槽不能装备治疗技能"),
    SKILL_BOOK_QUANTITY_NOT_ENOUGH(1035, "该品质的技能书数量不够"),
    TRAIN_SOUL_SKILL_IS_ENOUGH(1036, "在等待的灵魂之影技能已达到上限"),

    TRAIN_SOUL_SKILL_TIME_ERROR(1037, "灵魂之影技能等待时间未到"),
    EQUIP_ADD_FAILED(1038, "装备添加失败"),
    EQUIP_UP_MAX_LV(1039, "装备栏强化等级以达到上限"),
    TASK_NOT_TRIGGERED(1040, "任务未触发"),
    TASK_UNDONE(1041, "任务未完成"),
    NOT_RECEIVE_ACTIVITY_REWARDS(1042, "当前活跃度不足以领取奖励"),
    ALREADY_RECEIVE_ACTIVITY_REWARDS(1043, "已领取过活跃度奖励"),
    ALREADY_OPENED_GUARDIAN(1044, "已开通守护者奖励"),
    NOT_OPENED_GUARDIAN(1045, "未开通守护者奖励"),
    NOT_MATCH_CORRESPONDING_TYPE_TASK(1046, "未匹配到对应类型任务"),
    RECEIVED_TASK_REWARD(1047, "已领取过任务奖励"),
    RECEIVED_PROTECTOR_REWARD(1048, "已领取过守护者奖励"),
    CARD_ACHIEVED_EXTRACT_LIMIT(1049, "该卡池已达到抽取上限"),
    NOT_CARD_OPEN_CONDITIONS(1050, "不符合卡池开放条件"),
    BUTTON_CD_NOT_COOLING(1051, "当前卡池按钮CD还未冷却"),
    CARRIED_CARD_GM_ERROR(1052, "执行卡池GM指令异常"),
    CURRENT_CARD_CLOSED(1053, "当前卡池已关闭"),
    PROFESSION_NOT_CARD_OPEN_CONDITION(1054, "当前职业不符合卡池开放条件"),
    SKILL_NOT_RISING_STAR_CONDITION(1055, "当前技能等级未达到升星条件"),
    SKILL_RISING_STAR_UPPER_LIMIT(1056, "当前技能星级已达到上限"),
    SKILL_GRADE_BEYOND_RANGE(1057, "当前技能等级已超出对应升星范围"),
    SKILL_NOT_SOUL_ERROR(1058, "此技能不是灵魂技能无法穿戴"),
    RUNE_ALREADY_ASSEMBLED_SKILL(1059, "当前符文已装配在该技能上"),
    RUNE_SLOT_FULL(1060, "符文槽已满"),
    RUNE_NOT_ALREADY_ASSEMBLED_SKILL(1061, "当前符文未装配在该技能上, 无法卸载"),
    RUNE_NOT_ALREADY_ASSEMBLED_SKILL_REPLACE(1062, "当前符文未装配在该技能上, 无法替换"),
    RUNE_IN_ASSEMBLY_STATUS(1063, "当前符文处于装配状态, 无法替换"),
    RUNE_AND_SKILL_NOT_ADAPTED(1064, "当前符文与该技能未适配"),
    SKILL_NOT_CAN_TRAIN(1065, "此技能无法被淬魂"),
    ;


    private int code;

    private String desc;

    ErrorCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }
}
