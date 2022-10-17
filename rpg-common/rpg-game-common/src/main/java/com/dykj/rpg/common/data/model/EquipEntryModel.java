package com.dykj.rpg.common.data.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.dykj.rpg.common.data.model.logic.Entry;
import com.dykj.rpg.db.annotation.Column;
import com.dykj.rpg.db.annotation.PrimaryKey;
import com.dykj.rpg.db.data.BaseModel;

/**
 * @author jyb
 * @date 2020/11/25 10:22
 * @Description
 */
public class EquipEntryModel extends BaseModel {
    /**
     * 装备实例id
     */
    @Column(primaryKey = PrimaryKey.GENERAL)
    private long instanceId;

    /**
     * 词条的位置
     */
    @Column(primaryKey = PrimaryKey.GENERAL)
    private int position;

    /**
     * 词条效果id
     */
    private int entryEffectId;

    /**
     * 词条实际效果
     */
    private String entryEffect;

    /**
     * 词条信息，entryEffect 转化过来的
     */
    @JSONField(serialize = false)
    private Entry entry;

    public long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(long instanceId) {
        this.instanceId = instanceId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getEntryEffectId() {
        return entryEffectId;
    }

    public void setEntryEffectId(int entryEffectId) {
        this.entryEffectId = entryEffectId;
    }

    public String getEntryEffect() {
        return entryEffect;
    }

    public void setEntryEffect(String entryEffect) {
        this.entryEffect = entryEffect;
    }

    public void updateEntry(Entry entry) {
        this.entry =entry;
        this.entryEffect = entry.convertToString();
    }

    @JSONField(serialize = false)
    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    @Override
    public String toString() {
        return "EquipEntryModel{" +
                "instanceId=" + instanceId +
                ", position=" + position +
                ", entryEffectId=" + entryEffectId +
                ", entryEffect='" + entryEffect + '\'' +
                ", entry=" + entry +
                '}';
    }
}