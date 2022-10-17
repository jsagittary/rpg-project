package com.dykj.rpg.game.module.task.consts;

/**
 * @Description 任务完成条件枚举
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/8
 */
public enum TaskFinishConditionEnum
{
    ROLE_LEVEL(1),//角色等级达到X级
    DURING_ACTIVATION(2),//激活期间
    ACCUMULATION_PERIOD(3),//累计期间
    POWER_SCORE(4),//战力评分达到XXXX
    CHALLENGE_MISSION(5),//挑战通过关卡XXX
    LOGIN_GAME(6),//登录游戏
    CONTINUOUS_LOGIN(7);//连续登录

    private int type;

    TaskFinishConditionEnum(int type)
    {
        this.type = type;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    /**
     * 激活期间枚举
     */
    public enum DuringActivationEnum
    {
        SPELLS_LEVEL(201),//【激活期间】提升法术等级X次
        QUALITY_EQUIP(202),//【激活期间】获得X件Y品质（见下文，品质相关）或以上装备
        LEARN_SPELLS(203),//【激活期间】学会X个Y品质（见下文，品质相关）或以上法术
        CHALLENGE_MISSION(204),//【激活期间】挑战X次任意关卡
        SPELLS_TEMPER(205),//【激活期间】完成灵魂法术淬炼X次
        USE_ITEM(206),//【激活期间】使用Y道具X次
        SPEND_GOLD(207),//【激活期间】花费X金币
        SPEND_DIAMOND(208)//【激活期间】花费X钻石
        ;

        private int subclassType;

        DuringActivationEnum(int subclassType)
        {
            this.subclassType = subclassType;
        }

        public int getSubclassType()
        {
            return subclassType;
        }

        public void setSubclassType(int subclassType)
        {
            this.subclassType = subclassType;
        }
    }

    /**
     * 累计期间枚举
     */
    public enum GrandTotalEnum
    {
        SPELLS_LEVEL(301),//【累计期间】成功提升任意法术等级X次
        QUALITY_EQUIP(302),//【累计期间】获得X件Y品质（见下文，品质相关）或以上装备
        LEARN_SPELLS(303),//【累计期间】学会X个Y品质（见下文，品质相关）或以上法术
        SPELLS_TEMPER(304),//【累计期间】完成灵魂法术淬炼X次
        SPEND_GOLD(305),//【累计期间】花费X金币
        SPEND_DIAMOND(306),//【累计期间】花费X钻石
        ;

        private int subclassType;

        GrandTotalEnum(int subclassType)
        {
            this.subclassType = subclassType;
        }

        public int getSubclassType()
        {
            return subclassType;
        }

        public void setSubclassType(int subclassType)
        {
            this.subclassType = subclassType;
        }
    }
}
