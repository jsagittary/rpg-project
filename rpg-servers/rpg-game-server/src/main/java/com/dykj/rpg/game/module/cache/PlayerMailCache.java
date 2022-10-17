package com.dykj.rpg.game.module.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.fastjson.annotation.JSONField;
import com.dykj.rpg.common.data.model.PlayerMailModel;

/**
 * @author CaoBing
 * @date 2021年4月20日
 * @Description:
 */
public class PlayerMailCache {
	
	private Map<Long, PlayerMailModel> playerMailModelMap = new ConcurrentHashMap<>();
	
    public Map<Long, PlayerMailModel> getPlayerMailModelMap() {
		return playerMailModelMap;
	}

	public void setPlayerMailModelMap(Map<Long, PlayerMailModel> playerMailModelMap) {
		this.playerMailModelMap = playerMailModelMap;
	}

	@JSONField(serialize = false)
    private AtomicInteger sequence;

    /**
     * 计算序号，序号全局唯一自加，用于计算邮件的唯一id，全球唯一
     */
    public void calculateSequence() {
        if (playerMailModelMap.values().isEmpty()) {
            sequence = new AtomicInteger(0);
            return;
        }
        int max = 0;
        for (PlayerMailModel playerMailModel : playerMailModelMap.values()) {
            int seq = (int) (playerMailModel.getInstanceId() & 0x0FFFFF);
            max = Math.max(max, seq);
        }
        sequence = new AtomicInteger(max);
    }

    /**
     * 获取队友唯一id
     *
     * @return 队友唯一id
     */
    public long generateInstanceId(int id) {
        return (long) id << 20 | nextSequence();
    }
    
    /**
     * 获取下一个序号
     *
     * @return 序号
     */
    public int nextSequence() {
        return sequence.incrementAndGet();
    }

}
