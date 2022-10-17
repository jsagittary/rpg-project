package com.dykj.rpg.common.data.dao;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.dykj.rpg.common.data.model.EquipEntryModel;
import com.dykj.rpg.common.data.model.logic.Entry;
import com.dykj.rpg.db.dao.BaseDao;

/**
 * @author jyb
 * @date 2020/11/25 11:45
 * @Description
 */
@Repository	
public class EquipEntryDao extends BaseDao<EquipEntryModel> {
    @Override
    public RowMapper<EquipEntryModel> rowMapper() {
        RowMapper<EquipEntryModel> rowMapper = (resultSet, i) -> {
            EquipEntryModel equipEntryModel = new EquipEntryModel();
            equipEntryModel.setInstanceId(resultSet.getLong("instance_id"));
            equipEntryModel.setPosition(resultSet.getInt("position"));
            equipEntryModel.setEntryEffectId(resultSet.getInt("entry_effect_id"));
            equipEntryModel.setEntryEffect(resultSet.getString("entry_effect"));
            Entry entry = new Entry(equipEntryModel.getEntryEffect());
            equipEntryModel.setEntry(entry);
            return equipEntryModel;
        };
        return rowMapper;
    }


    /**
     * 查找一个装备的所有词条
     * @param instanceId
     * @return
     */
    public List<EquipEntryModel> getEquipEntryModels(long instanceId) {
        String sql = "select * from  equip_entry  where instance_id = ?";
        return queryForList(sql, instanceId);
    }
}