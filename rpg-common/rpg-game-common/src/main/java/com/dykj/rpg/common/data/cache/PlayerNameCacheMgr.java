package com.dykj.rpg.common.data.cache;

import com.dykj.rpg.common.core.AbstractListCacheMgr;
import org.springframework.stereotype.Component;

/**
 * @author jyb
 * @date 2020/11/9 18:33
 * @Description
 */
@Component
public class PlayerNameCacheMgr extends AbstractListCacheMgr<String> {

    /**
     * 是否重复
     *
     * @param name
     * @return
     */
    public synchronized boolean addName(String name, boolean flag) {
        if (values().contains(name)) {
            return false;
        }
        if (flag) {
            addCache(name);
        }
        return true;
    }
}