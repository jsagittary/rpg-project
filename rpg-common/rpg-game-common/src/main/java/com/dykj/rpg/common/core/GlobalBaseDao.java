package com.dykj.rpg.common.core;

import com.dykj.rpg.db.consts.DataSourceKey;
import com.dykj.rpg.db.dao.BaseDao;
import com.dykj.rpg.db.data.BaseModel;

/**
 * @Author: jyb
 * @Date: 2020/10/9 13:45
 * @Description:
 */
public abstract class GlobalBaseDao<T extends BaseModel> extends BaseDao<T> {

    @Override
    protected String dataSourceKey() {
        return DataSourceKey.GLOBAL_DATASOURCE;
    }
}
