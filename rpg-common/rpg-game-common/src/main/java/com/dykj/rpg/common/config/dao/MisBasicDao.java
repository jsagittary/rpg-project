package com.dykj.rpg.common.config.dao;

import com.dykj.rpg.db.dao.AbstractConfigDao;
import com.dykj.rpg.common.config.model.MisBasicModel;
import org.springframework.stereotype.Repository;

/**
 * @Description 关卡章节
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/22
 */
@Repository
public class MisBasicDao extends AbstractConfigDao<Integer, MisBasicModel> {

    public MisBasicModel getNextOpenMission(int missionId) {
        for (MisBasicModel misBasicModel : getConfigs()) {
            if (misBasicModel.getPreId().equals(missionId)) {
                return misBasicModel;
            }
        }
        return null;
    }
}