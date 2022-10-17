package com.dykj.rpg.game.module.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dykj.rpg.common.data.model.EquipPosModel;

/**
 * @author jyb
 * @date 2021/4/6 10:41
 * @Description
 */
public class EquipPosCache{
    /**
     * 装备栏位
     */
    private Map<Integer, EquipPosModel> equipPosModelMap = new ConcurrentHashMap<>();

	public Map<Integer, EquipPosModel> getEquipPosModelMap() {
		return equipPosModelMap;
	}

	public void setEquipPosModelMap(Map<Integer, EquipPosModel> equipPosModelMap) {
		this.equipPosModelMap = equipPosModelMap;
	}
    
}