package com.dykj.rpg.common.cache;

import com.dykj.rpg.db.data.BaseModel;
import com.dykj.rpg.redis.cache.base.ICacheMapPrimaryKey;

import java.io.Serializable;

/**
 * @author jyb
 * @date 2019/1/5 15:10
 */
@Deprecated
public abstract class DB1Model extends BaseModel implements Serializable, ICachePrimaryKey {
    @Override
    public int KeyNum() {
        return 1;
    }

    @Override
    public void primaryKey(long key) {
        //doNothing
    }
}