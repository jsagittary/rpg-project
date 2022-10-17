package com.dykj.rpg.battle.logic;

public abstract class BattleObject {

    public byte camp;

    public long borrowTime;

    public long restoreTime;

    /**
     * 使用状况
     */
    private boolean using;

    /**
     * 唯一ID
     */
    private int uid;

    public abstract void release();

    /**
     * 战斗对象自查,去除对象的死亡引用
     */
    public abstract void selfCheak();

    public boolean isUsing(){
        return using;
    }

    public void setUsing(boolean using){
        this.using = using;
    }

    public void setUID(int uid){
        this.uid = uid;
    }

}
