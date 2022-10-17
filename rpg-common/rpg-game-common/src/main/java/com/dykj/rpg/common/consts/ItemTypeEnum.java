package com.dykj.rpg.common.consts;

import java.util.stream.Stream;

/**
 * @Description 道具类型枚举
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/25
 */
public enum ItemTypeEnum {
    EQUIPMENT(1),//装备
    SKILL_BOOK(2),//技能书
    GEM(3),//宝石
    MATERIAL(4),//材料
    FRAGMENTS(5),//碎片
    CONSUMABLES(6),//消耗品
    CURRENCY(7),//货币
    EXPERIENCE(8),//经验
    AI(9),//雕纹
    RUNE(10),//符文



    GENERIC(0);

    private int itemType;//道具类型

    ItemTypeEnum(int itemType) {
        this.itemType = itemType;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    /**
     * 获取子类型枚举值
     *
     * @param subclassTypeEnum 子类型枚举
     * @param <T>
     * @return
     */
    public <T> int getSubclassTypeEnum(T subclassTypeEnum) {
        return ((ItemType) subclassTypeEnum).getAppointSubclassType(subclassTypeEnum);
    }


    /**
     * 货币类型枚举
     */
    public enum CurrencyTypeEnum implements ItemType {
        RECHARGE_DIAMOND(1),//充值钻石
        BIND_DIAMOND(2),//绑定钻石
        GOLD(3),//金币
        ARENA_COIN(4),//竞技场币
        CLIMBING_COIN(5),//爬塔币
        GUILD_COIN(6);//公会币

        private int subclassType;//货币类型子类id

        CurrencyTypeEnum(int subclassType) {
            this.subclassType = subclassType;
        }

        public int getSubclassType() {
            return subclassType;
        }

        public void setSubclassType(int subclassType) {
            this.subclassType = subclassType;
        }

        @Override
        public int getAppointSubclassType(Object typeEnum) {
            return Stream.of(CurrencyTypeEnum.values()).filter(e -> e.equals(typeEnum)).findFirst().get().getSubclassType();
        }
    }

    /**
     * 技能书类型枚举
     */
    public enum SkillBookTypeEnum implements ItemType {
        MAGIC(1),//魔能
        BLOOD_SOUL(2),//血魂
        GANA(3),//咖娜
        GIANT(4),//巨神
        IMAGINARY_GOD(5),//虚神
        SHIMOTSUKI(6),//霜月
        ELVES(7);//精灵

        private int subclassType;//技能书类型子类id

        SkillBookTypeEnum(int subclassType) {
            this.subclassType = subclassType;
        }

        public int getSubclassType() {
            return subclassType;
        }

        public void setSubclassType(int subclassType) {
            this.subclassType = subclassType;
        }

        @Override
        public int getAppointSubclassType(Object typeEnum) {
            return Stream.of(SkillBookTypeEnum.values()).filter(e -> e.equals(typeEnum)).findFirst().get().getSubclassType();
        }
    }

    /**
     * 消耗品类型枚举
     */
    public enum ConsumablesTypeEnum implements ItemType {
        BATTLE_POTION(1),//战斗药水
        SEEK_KNOWLEDGE(201),//求知之钥
        CHALLENGE_TICKET(3),//竞技场挑战券
        EQUIPMENT_SCROLL(5),//装备卷轴
        TREASURE_CHEST(601),//奖励宝箱
        RANDOM_TREASURE_CHEST(602),//随机宝箱
        EXPERIENCE_POTION(7);//经验药水


        private int subclassType;//消耗品类型子类id

        ConsumablesTypeEnum(int subclassType) {
            this.subclassType = subclassType;
        }

        public int getSubclassType() {
            return subclassType;
        }

        public void setSubclassType(int subclassType) {
            this.subclassType = subclassType;
        }

        @Override
        public int getAppointSubclassType(Object typeEnum) {
            return Stream.of(ConsumablesTypeEnum.values()).filter(e -> e.equals(typeEnum)).findFirst().get().getSubclassType();
        }
    }

    /**
     * 经验类型枚举
     */
    public enum ExperienceTypeEnum implements ItemType {
        ROLE_EXPERIENCE(1),//角色经验
        DAILY_ACTIVITY(201),//日活跃度
        MONTHLY_ACTIVITY(202);//周活跃值

        private int subclassType;//经验类型子类id

        ExperienceTypeEnum(int subclassType) {
            this.subclassType = subclassType;
        }

        public int getSubclassType() {
            return subclassType;
        }

        public void setSubclassType(int subclassType) {
            this.subclassType = subclassType;
        }

        @Override
        public int getAppointSubclassType(Object typeEnum) {
            return Stream.of(ExperienceTypeEnum.values()).filter(e -> e.equals(typeEnum)).findFirst().get().getSubclassType();
        }
    }
}
