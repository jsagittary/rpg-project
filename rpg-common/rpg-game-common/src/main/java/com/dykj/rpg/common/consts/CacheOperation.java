package com.dykj.rpg.common.consts;

/**
 * @Author: jyb
 * @Date: 2020/9/25 17:41
 * @Description:
 */
public interface CacheOperation {
    /**
     * 三个操作的 prefix：insert、update、delete
     * REMOVE 移除缓存
     */
    int ADD = 100;
    int UPDATE = 200;
    int REMOVE = 300;
}
