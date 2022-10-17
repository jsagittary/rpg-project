package com.dykj.rpg.common.config.dao;

import com.dykj.rpg.db.dao.AbstractConfigDao;
import com.dykj.rpg.common.config.model.EqEntryBoxModel;
import com.dykj.rpg.util.random.ItemRandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description 装备词条类型库
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/18
 */
@Repository
public class EqEntryBoxDao extends AbstractConfigDao<Integer, EqEntryBoxModel> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public int randomEntryEffectId(int effect_box_id, int equip_character_id) {
        for (EqEntryBoxModel e : getConfigs()) {
            if (e.getEquipEntryEffectBoxId() == effect_box_id) {
                if (e.getEquipCharacterId().contains(0) || e.getEquipCharacterId().contains(equip_character_id)) {
                    return ItemRandomUtil.getBoxItem(e.getEqEntryBoxItem()).getMarkId();
                }
            }
        }
        logger.error("EqEntryBoxDao randomEntryEffectId error effect_box_id  {}   equip_character_id {} ", effect_box_id, equip_character_id);
        return 0;
    }
}