package com.dykj.rpg.common.data.model.logic;

/**
 * @author jyb
 * @date 2020/11/25 10:10
 * @Description 装备一条词条，由多个词条单元组成
 */
public class Entry {
    /**
     * 词条信息
     */
    private EntryUnit[] entryUnits;


    public EntryUnit[] getEntryUnits() {
        return entryUnits;
    }

    public void setEntryUnits(EntryUnit[] entryUnits) {
        this.entryUnits = entryUnits;
    }


    public String convertToString() {
        StringBuffer stringBuffer = new StringBuffer();
        if (entryUnits != null && entryUnits.length > 0) {
            int i = 0;
            for (EntryUnit entryUnit : entryUnits) {
                stringBuffer.append(entryUnit.convertToString());
                if (i < entryUnits.length - 1) {
                    stringBuffer.append(",");
                }
                i++;

            }
        }
        return stringBuffer.toString();
    }

    public Entry(EntryUnit[] entryUnits) {
        this.entryUnits = entryUnits;
    }

    /***
     *type:id:typeId:value,type:id:typeId:value 转化成EntryUnit
     * @param entryEffect
     */
    public Entry(String entryEffect) {
        String[] effects = entryEffect.split("\\,");
        entryUnits = new EntryUnit[effects.length];
        int i = 0;
        for (String effect : effects) {
            String[] rs = effect.split("\\:");
            entryUnits[i] = new EntryUnit(Integer.valueOf(rs[0]), Integer.valueOf(rs[1]), Integer.valueOf(rs[2]),Integer.valueOf(rs[3]), Integer.valueOf(rs[4]));
            i++;
        }
    }
}