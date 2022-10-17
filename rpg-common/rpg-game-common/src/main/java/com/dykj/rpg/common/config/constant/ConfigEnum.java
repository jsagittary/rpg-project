package com.dykj.rpg.common.config.constant;

/**
 * @Description 常量
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/03/05
*/
public enum ConfigEnum
{
    LEVELLIMIT(1),//玩家等级限制
    RANDNAMELIMIT(2),//随机取名次数的阈值
    BAGPAGE(3),//背包页签
    BAGSIZE(4),//玩家背包初始空间
    BAGADDCOST(5),//玩家背包格子单价（钻石）
    BAGEXADDNUM(6),//快速购买背包格子数量
    ROLEUI(7),//角色选项UI功能
    FASTIDLERESETTIME(8),//快速挂机重置时间（S)
    FASTIDLEREWARDTIME(9),//快速挂机奖励时长（S)
    FASTIDLECONSUME(10),//快速挂机消耗
    FASTIDLEFREENUMBER(11),//免费快速挂机次数
    FASTIDLERESETPOINTTIME(12),//快速挂机次数重置时间点（北京时间）
    IDLEREWARDUPPERLIMIT(13),//挂机奖励累计上限时间（S)
    IDLEREWARDDISPLAYFIRST(14),//挂机奖励显示一档时长（S)
    IDLEREWARDDISPLAYSECOND(15),//挂机奖励显示二档时长（S)
    IDLEREWARDDISPLAYTHIRD(16),//挂机奖励显示三档时长（S)
    QUICKQUENCHINGCOST(17),//快速淬炼参数1钻石价格
    QUICKQUENCHINGTIME(18),//快速淬炼参数2结算时间（S）
    GUARDIANCONSUME(19),//开启守护者消耗道具
    GUARDIANTIME(20),//守护者持续时长（s）
    EQMAXLV(35);//装备栏强化最大等级
    private int configType;

    ConfigEnum(int configType)
    {
        this.configType = configType;
    }

    public int getConfigType()
    {
        return configType;
    }
}