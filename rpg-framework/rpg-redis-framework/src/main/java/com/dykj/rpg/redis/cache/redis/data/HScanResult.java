package com.dykj.rpg.redis.cache.redis.data;

import java.util.Map;

/**
 * @author jyb
 * @date 2019/5/21 16:18
 */
public class HScanResult<T> {

    private int index;
    private Map<String, T> map;

    public HScanResult(int index, Map<String, T> map) {
        this.index = index;
        this.map = map;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Map<String, T> getMap() {
        return map;
    }

    public void setMap(Map<String, T> map) {
        this.map = map;
    }
}
