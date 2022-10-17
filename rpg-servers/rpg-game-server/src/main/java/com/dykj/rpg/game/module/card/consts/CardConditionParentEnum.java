package com.dykj.rpg.game.module.card.consts;

/**
 * @Description 抽卡判定大类枚举
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/4/1
 */
public enum CardConditionParentEnum
{
    RARITY(1);//稀有度

    private int type;

    CardConditionParentEnum(int type)
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
     * 稀有度枚举
     */
    public enum RarityEnum
    {
        ORDINARY(1),//普通
        excellent(2),//精良
        epic(3),//史诗
        legend(4),//传说
        ancient(5),//远古
        Archaic(6),//太古
        Holy_legendary(7)//超神
        ;

        private int subclassType;

        RarityEnum(int subclassType)
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
