package com.dykj.rpg.common.attribute.consts;

/**
 * @author jyb
 * @date 2018年4月18日 上午10:51:51
 * 属性两种存在方式
 */
public enum AttributeTypeEnum {

    /**
     * 值
     */
    VALUE(1),

    /**
     *  万分比
     */
    PARENT(2);

    private int type;

    private AttributeTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
