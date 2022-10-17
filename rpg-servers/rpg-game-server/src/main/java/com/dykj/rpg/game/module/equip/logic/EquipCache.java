package com.dykj.rpg.game.module.equip.logic;

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
    private Map<Integer, EquipEntryModel> equipEntryModelList = new ConcurrentHashMap<>();

    public EquipCache(PlayerEquipModel playerEquipModel, Map<Integer, EquipEntryModel> equipEntryModelList) {
        this.playerEquipModel = playerEquipModel;
        this.equipEntryModelList = equipEntryModelList;
    }

    public PlayerEquipModel getPlayerEquipModel() {
        return playerEquipModel;
    }

    public void setPlayerEquipModel(PlayerEquipModel playerEquipModel) {
        this.playerEquipModel = playerEquipModel;
    }

    public Map<Integer, EquipEntryModel> getEquipEntryModelList() {
        return equipEntryModelList;
    }

    public void setEquipEntryModelList(Map<Integer, EquipEntryModel> equipEntryModelList) {
        this.equipEntryModelList = equipEntryModelList;
    }

    public long getEquipId() {
        return playerEquipModel.getInstanceId();
    }
}