package com.dykj.rpg.common.cache;

import com.dykj.rpg.db.data.BaseModel;

/**
 * @Author: jyb
 * @Date: 2020/9/25 10:01
 * @Description:
 */
@Deprecated
public abstract class Db2Model extends BaseModel implements ICache2PrimaryKey {

    @Override
    public int KeyNum() {
        return 2;
    }
}
