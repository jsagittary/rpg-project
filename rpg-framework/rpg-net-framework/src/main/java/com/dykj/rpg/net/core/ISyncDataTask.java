package com.dykj.rpg.net.core;

/**
 * 数据安全基类
 */
public interface ISyncDataTask extends Runnable {
    /**
     * 任务id 一般为玩家id,公会id 等等
     * @return
     */
    public int  getTaskId();
}
