package com.dykj.rpg.game.module.cache.logic;

import com.dykj.rpg.common.data.model.EquipEntryModel;
import com.dykj.rpg.common.data.model.PlayerEquipModel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jyb
 * @date 2021/4/20 16:34
 * @Description
 */
public class EquipCache {
    /**
     * 装备类
     */
    private PlayerEquipModel playerEquipModel;
    /**
     * 词条类
     */
    private Map<Integer, EquipEntryModel> equipEntryModelMap = new ConcurrentHashMap<>();


    public PlayerEquipModel getPlayerEquipModel() {
        return playerEquipModel;
    }

    public void setPlayerEquipModel(PlayerEquipModel playerEquipModel) {
        this.playerEquipModel = playerEquipModel;
    }

    public Map<Integer, EquipEntryModel> getEquipEntryModelMap() {
        return equipEntryModelMap;
    }

    public void setEquipEntryModelMap(Map<Integer, EquipEntryModel> equipEntryModelList) {
        this.equipEntryModelMap = equipEntryModelList;
    }

    public long getInstanceId() {
        return playerEquipModel.getInstanceId();
    }

    public EquipEntryModel getEquipEntryModel(int index) {
        return equipEntryModelMap.get(index);
    }

    public EquipCache(PlayerEquipModel playerEquipModel) {
        this.playerEquipModel = playerEquipModel;
    }

    public boolean isDress() {
        return playerEquipModel.getEquipPos() > 0;
    }
}