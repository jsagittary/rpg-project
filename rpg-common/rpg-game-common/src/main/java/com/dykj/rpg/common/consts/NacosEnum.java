package com.dykj.rpg.common.consts;

/**
 * @author jyb
 * @date 2020/12/29 19:54
 * @Description
 */
public enum  NacosEnum {
    PHYSICAL_MACHINE("physical_machine","UC");


    private String  dataId;
    private String  group;


    NacosEnum(String dataId, String group) {
        this.dataId = dataId;
        this.group = group;
    }


    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}