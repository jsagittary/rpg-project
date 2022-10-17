package com.dykj.rpg.game.module.gm.consts;

/**
 * @Description gm指令常量
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/23
 */
public class GmCommandConsts {
    /**
     * 指令前缀
     */
    public static final String GM_PRE = "@";

    /**
     * 添加道具 格式为: @item(类名称)_set(方法名) 道具id:道具数量 道具id:道具数量(多个以空格隔开)
     */
    public static final String GM_ITEM_ADD = GM_PRE + "item_add 道具id:道具数量 道具id:道具数量";

    /**
     * 修改技能等级(若无该技能则新增; 格式: @skill(类名称)_upd(方法名) 技能id:技能等级 技能id:技能等级(多个以空格隔开))
     */
    public static final String GM_SKILL_SET = GM_PRE + "skill_set 技能id:技能等级 技能id:技能等级";

    /**
     * 查询玩家身上所有技能列表
     */
    public static final String GM_SKILL_LIST = GM_PRE + "skill_list";

    /**
     * 角色升级(格式: @player(类名称)_upgrade(方法名) 角色等级)
     */
    public static final String GM_ROLES_UPGRADE = GM_PRE + "player_upgrade 角色等级";

    /**
     * 设置vip等级(格式: @player(类名称)_upgrade(方法名) vip等级)  临时方案
     */
    public static final String GM_SET_VIPGRADE = GM_PRE + "player_vipgrade vip等级";

    /**
     * 增加活跃度(格式: @player(类名称)_activity(方法名) 活跃度类型:活跃度数量)  临时方案
     */
    public static final String GM_SET_ACTIVITY = GM_PRE + "player_addActivity 活跃度类型:活跃度数量";

    /**
     * 重置当前角色所有日常、周常任务(格式: @task(类名称)_refreshTask(方法名))
     */
    public static final String GM_RESET_TASK = GM_PRE + "task_resetTask";

    /**
     * 重置当前角色所有主线任务状态	(格式: @task(类名称)_resetMainStatus(方法名))
     */
    public static final String GM_RESET_MAIN_STATUS = GM_PRE + "task_resetMainStatus";

    /**
     * 刷新活跃度状态	(格式: @task(类名称)_refreshActivity(方法名))
     */
    public static final String GM_REFRESH_ACTIVITY = GM_PRE + "task_refreshActivity";

    /**
     * 增加守护者时长	(格式: @task(类名称)_refreshActivity(方法名) 天数)
     */
    public static final String GM_ADD_PROTECTOR_DURATION = GM_PRE + "task_addDuration 天数";

    /**
     * 开启/关闭指定多个卡池ID(格式: @card(类名称)_switch(方法名) 卡池id:开关状态(0-关闭,1-开启),卡池id:开关状态(0-关闭,1-开启) (多个以逗号隔开))
     */
    public static final String GM_CARD_SWITCHSTATUS = GM_PRE + "card_switchStatus 卡池id:开关状态";

    public static final String GM_BATTLE_TEST = GM_PRE + "battle_mission missionId(副本id)";

    public static final String GM_EQUIP_TEST = GM_PRE + "battle_equip";


    public static final String GM_ITEM_TEST = GM_PRE + "battle_item";

    public static final String GM_MAIL = GM_PRE + "mail_add";

//    public static final String KAFKA_SYNC_TEST = GM_PRE + "kafka_sync";
//
//    public static final String KAFKA_ASYNC_TEST = GM_PRE + "kafka_async";
}

