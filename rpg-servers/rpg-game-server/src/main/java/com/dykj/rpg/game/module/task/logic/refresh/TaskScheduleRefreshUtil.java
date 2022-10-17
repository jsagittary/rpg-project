package com.dykj.rpg.game.module.task.logic.refresh;

import com.dykj.rpg.common.config.model.ItemModel;
import com.dykj.rpg.common.consts.ItemTypeEnum;
import com.dykj.rpg.common.data.model.PlayerEquipModel;
import com.dykj.rpg.common.data.model.PlayerSkillModel;
import com.dykj.rpg.game.module.event.task.TaskEventManager;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.task.consts.TaskFinishConditionEnum;
import com.dykj.rpg.game.module.task.logic.response.TaskConditions;
import com.dykj.rpg.util.spring.BeanFactory;

/**
 * @Description 任务进度刷新
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/4/21
 */
public class TaskScheduleRefreshUtil
{

    /**
     * 根据具体类型进行任务进度刷新
     * @param player 玩家信息
     */
    public static void commonSchedule(Player player, TaskConditions... conditionsModels)
    {
        BeanFactory.getBean(TaskEventManager.class).doEvents(player, conditionsModels);
    }

    /**
     * 刷新货币类任务进度
     * @param player 玩家信息
     * @param itemModel 道具基础
     * @param currency 货币
     */
    public static void currencySchedule(Player player, ItemModel itemModel, int currency)
    {
        //如果当前道具类型为货币类型
        if (itemModel.getItemType() == ItemTypeEnum.CURRENCY.getItemType() && currency < 0)
        {
            //如果道具子类型为金币并且数量小于0则触发
            if (itemModel.getItemTypeDetail() == ItemTypeEnum.CurrencyTypeEnum.GOLD.getSubclassType())
            {
                goldSchedule(player, currency);
            }
            //如果道具子类型为钻石并且数量小于0则触发
            else if (itemModel.getItemTypeDetail() == ItemTypeEnum.CurrencyTypeEnum.RECHARGE_DIAMOND.getSubclassType())
            {
                diamondSchedule(player, currency);
            }
        }
    }

    /**
     * 刷新金币类任务进度
     * @param player 玩家信息
     * @param gold 金币
     */
    public static void goldSchedule(Player player, int gold)
    {
        /*
         * 触发任务进度刷新
         * 任务完成条件类型:
         *      【激活期间】花费X金币
         *      【累计期间】花费X金币
         */
        Object[] param = new Object[]{gold};
        BeanFactory.getBean(TaskEventManager.class).doEvents(player,
                new TaskConditions(TaskFinishConditionEnum.DURING_ACTIVATION, TaskFinishConditionEnum.DuringActivationEnum.SPEND_GOLD, param),
                new TaskConditions(TaskFinishConditionEnum.ACCUMULATION_PERIOD, TaskFinishConditionEnum.GrandTotalEnum.SPEND_GOLD, param));
    }

    /**
     * 刷新钻石类任务进度
     * @param player 玩家信息
     * @param diamond 钻石
     */
    public static void diamondSchedule(Player player, int diamond)
    {
        /*
         * 触发任务进度刷新
         * 任务完成条件类型:
         *      【激活期间】花费X钻石
         *      【累计期间】花费X钻石
         */
        Object[] param = new Object[]{diamond};
        BeanFactory.getBean(TaskEventManager.class).doEvents(player,
                new TaskConditions(TaskFinishConditionEnum.DURING_ACTIVATION, TaskFinishConditionEnum.DuringActivationEnum.SPEND_DIAMOND, param),
                new TaskConditions(TaskFinishConditionEnum.ACCUMULATION_PERIOD, TaskFinishConditionEnum.GrandTotalEnum.SPEND_DIAMOND, param));
    }

    /**
     * 刷新提示法术等级类任务进度
     * @param player 玩家信息
     */
    public static void spellsLevelSchedule(Player player)
    {
        /*
         * 触发任务进度刷新
         * 任务完成条件类型:
         *      【激活期间】提升法术等级X次任务
         *      【累计期间】成功提升任意法术等级X次
         */
        BeanFactory.getBean(TaskEventManager.class).doEvents(player,
                new TaskConditions(TaskFinishConditionEnum.DURING_ACTIVATION, TaskFinishConditionEnum.DuringActivationEnum.SPELLS_LEVEL),
                new TaskConditions(TaskFinishConditionEnum.ACCUMULATION_PERIOD, TaskFinishConditionEnum.GrandTotalEnum.SPELLS_LEVEL));
    }

    /**
     * 刷新装备类任务进度
     * @param player 玩家信息
     */
    public static void equipSchedule(Player player, PlayerEquipModel playerEquipModel)
    {
        /*
         * 触发任务进度刷新
         * 任务完成条件类型:
         *      【激活期间】获得X件Y品质（见下文，品质相关）或以上装备
         *      【累计期间】获得X件Y品质（见下文，品质相关）或以上装备
         */
        BeanFactory.getBean(TaskEventManager.class).doEvents(player,
                new TaskConditions(TaskFinishConditionEnum.DURING_ACTIVATION, TaskFinishConditionEnum.DuringActivationEnum.QUALITY_EQUIP, new Object[]{playerEquipModel}),
                new TaskConditions(TaskFinishConditionEnum.ACCUMULATION_PERIOD, TaskFinishConditionEnum.GrandTotalEnum.QUALITY_EQUIP));
    }

    /**
     * 刷新学会法术类任务进度
     * @param player 玩家信息
     */
    public static void learnSpellsSchedule(Player player, PlayerSkillModel playerSkillModel)
    {
        /*
         * 触发任务进度刷新
         * 任务完成条件类型:
         *      【激活期间】学会X个Y品质（见下文，品质相关）或以上法术
         *      【累计期间】学会X个Y品质（见下文，品质相关）或以上法术
         */
        BeanFactory.getBean(TaskEventManager.class).doEvents(player,
                new TaskConditions(TaskFinishConditionEnum.DURING_ACTIVATION, TaskFinishConditionEnum.DuringActivationEnum.LEARN_SPELLS, new Object[]{playerSkillModel}),
                new TaskConditions(TaskFinishConditionEnum.ACCUMULATION_PERIOD, TaskFinishConditionEnum.GrandTotalEnum.LEARN_SPELLS));
    }

    /**
     * 刷新法术淬炼类任务进度
     * @param player 玩家信息
     */
    public static void spellsTemperSchedule(Player player)
    {
        /*
         * 触发任务进度刷新
         * 任务完成条件类型:
         *      【激活期间】完成灵魂法术淬炼X次
         *      【累计期间】完成灵魂法术淬炼X次
         */
        BeanFactory.getBean(TaskEventManager.class).doEvents(player,
                new TaskConditions(TaskFinishConditionEnum.DURING_ACTIVATION, TaskFinishConditionEnum.DuringActivationEnum.SPELLS_TEMPER),
                new TaskConditions(TaskFinishConditionEnum.ACCUMULATION_PERIOD, TaskFinishConditionEnum.GrandTotalEnum.SPELLS_TEMPER));
    }
}