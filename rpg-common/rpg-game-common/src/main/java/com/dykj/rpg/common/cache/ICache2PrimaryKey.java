package com.dykj.rpg.common.cache;

public interface ICache2PrimaryKey {
    /**
     * map的键 ，一般都是玩家id
     * @return
     */
    long primaryKey();

    /**
     *
     * @return
     */
    Long primary2Key();

}
