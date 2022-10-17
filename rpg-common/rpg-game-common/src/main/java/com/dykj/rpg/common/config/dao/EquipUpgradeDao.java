package com.dykj.rpg.common.config.dao;

import com.dykj.rpg.common.data.model.EquipPosModel;
import com.dykj.rpg.db.dao.AbstractConfigDao;
import com.dykj.rpg.common.config.model.EquipUpgradeModel;
import org.springframework.stereotype.Repository;

/**
 * @Description 技能升级
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/04/06
 */
@Repository
public class EquipUpgradeDao extends AbstractConfigDao<Integer, EquipUpgradeModel> {

    public EquipUpgradeModel getEquipUpgrade(int pos, int lv, int profession) {
        for (EquipUpgradeModel equipUpgradeModel : getConfigs()) {
            if (equipUpgradeModel.getPartType() == pos && equipUpgradeModel.getPartLevel() == lv && equipUpgradeModel.getClassType() == profession) {
                return equipUpgradeModel;
            }
        }
        return null;
    }
}