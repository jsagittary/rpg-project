package com.dykj.rpg.common.consts;

/**
 * @Description 物品操作类型
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/25
 */
public enum ItemOperateEnum {
    GM(1001),//GM指令添加
    SELL(1002),//出售
    EXCHANGE(1003),//兑换
    DECOMPOSITION(1004),//分解
    USE(1005),//使用
    DISCARD(1006),//丢弃
    HAND_UP_AWARD(1007),//挂机奖励
    GET_HAND_UP_AWARD_COST(1008),//挂机奖励

    MISSION_AWARD(1009),//关卡通关奖励

    BATTLE_USE_ITEM(1010),//战斗使用物品

    TRAIN_SOUL_SKILL_COST(1011),//培养灵魂之影技能消耗
    UP_EQUIP_POS_COST(1012),//培养装备栏等级消耗
    TASK_REWARDS(1013),//任务完成奖励
    PROTECTOR_OPEN_REWARDS(1014),//守护者奖励
    ACTIVITY_REWARD(1015),//活跃度奖励
    OPEN_PROTECTOR(1016),//开通守护者
    DRAW_CARD(1017),//抽卡奖励

    BUY_SOUL_SKIN(1018),//购买灵魂之影皮肤
    GET_MAIL_AWARD(1019),
    BACKPACK_EXTENSION(1020);//背包扩展

    private int type;

    ItemOperateEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
