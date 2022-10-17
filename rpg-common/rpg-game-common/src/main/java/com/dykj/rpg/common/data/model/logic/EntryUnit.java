package com.dykj.rpg.common.data.model.logic;

/**
 * @author jyb
 * @date 2020/11/25 9:56
 * @Description 单个词条信息
 */
public class EntryUnit {
    /**
     * 1 属性  2 技能
     */
    private int type;

    /**
     * 属性id ，技能id
     */
    private int id;

    /***
     * 为属性的子类型
     * 为技能的时候去查 skill_attr_basic影响的详细信息（确定影响的是哪个字段）,为0的时候 表示直接读技能的基础数据，为新增技能(新增技能要检查是否有替换skill_basic)
     */
    private int typeId;
    /**
     * 为属性的时候 1值 2 增比  3 减比
     * <p>  DateFixConstant 枚举
     * 为属技能的时候 1值 2 增比  3 减比
     */
    private int pram;
    /**
     * 值
     */
    private int value;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getPram() {
        return pram;
    }

    public void setPram(int pram) {
        this.pram = pram;
    }

    public EntryUnit(int type, int id, int typeId, int pram, int value) {
        this.type = type;
        this.id = id;
        this.typeId = typeId;
        this.pram = pram;
        this.value = value;
    }

    public String convertToString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(type).append(":").append(id).append(":").append(typeId).append(":").append(pram).append(":").append(value);
        return stringBuffer.toString();
    }

    public EntryUnit(String unitStr) {
        String[] units = unitStr.split("\\:");
        this.type = Integer.valueOf(units[0]);
        this.id = Integer.valueOf(units[1]);
        this.typeId = Integer.valueOf(units[2]);
        this.pram = Integer.valueOf(units[3]);
        this.value = Integer.valueOf(units[4]);
    }


    public String key() {
        return new StringBuffer().append(id).append(":").append(typeId).append(":").append(pram).toString();
    }
}