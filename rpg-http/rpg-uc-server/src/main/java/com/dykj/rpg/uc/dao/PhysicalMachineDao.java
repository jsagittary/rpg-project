package com.dykj.rpg.uc.dao;

import com.dykj.rpg.common.module.uc.model.PhysicalMachineModel;
import com.dykj.rpg.db.dao.AbstractBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jyb
 * @date 2020/12/29 19:45
 * @Description
 */
@Repository
public class PhysicalMachineDao extends AbstractBaseDao<PhysicalMachineModel> {

    /**
     * 查找所有的物理机信息
     * @return
     */
    public List<PhysicalMachineModel> queryAll() {
        return queryForList("select * from physical_machine");
    }
}