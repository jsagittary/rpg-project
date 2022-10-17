package com.dykj.rpg.game.module.cache;

import com.dykj.rpg.game.module.cache.logic.EquipCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jyb
 * @date 2021/4/20 17:55
 * @Description
 */
public class PlayerEquipCache {

    private AtomicInteger sequence;

    /**
     * 装备缓存
     */
    private Map<Long, EquipCache> equipCacheMap = new ConcurrentHashMap<>();

    /**
     * 计算序号，序号全局唯一自加，用于计算装备的唯一id，全球唯一
     */
    public void calculateSequence() {
        if (equipCacheMap.isEmpty()) {
            sequence = new AtomicInteger(0);
            return;
        }
        int max = 0;
        for (EquipCache equipCache : equipCacheMap.values()) {
            int seq = (int) (equipCache.getPlayerEquipModel().getInstanceId() & 0x0FFFFF);
            max = Math.max(max, seq);
        }
        sequence = new AtomicInteger(max);
    }

    /**
     * 获取下一个序号
     *
     * @return 序号
     */
    public int nextSequence() {
        return sequence.incrementAndGet();
    }

    /**
     * 获取队友唯一id
     *
     * @return 队友唯一id
     */
    public long generateInstanceId(int id) {
        return (long) id << 20 | nextSequence();
    }



    public Map<Long, EquipCache> getEquipCacheMap() {
        return equipCacheMap;
    }

    public EquipCache getByEquipPos(int equipPos) {
        for (EquipCache p : equipCacheMap.values()) {
            if (p.getPlayerEquipModel().getEquipPos() == equipPos) {
                return p;
            }
        }
        return null;
    }

    public List<EquipCache> getDressEquips() {
        List<EquipCache> equipCaches = new ArrayList<>();
        for (EquipCache p : equipCacheMap.values()) {
            if (p.isDress()) {
                equipCaches.add(p);
            }
        }
        return equipCaches;
    }

}