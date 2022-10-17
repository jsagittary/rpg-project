package com.dykj.rpg.common.config.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.dykj.rpg.common.config.BaseManager;
import com.dykj.rpg.common.consts.CommonConsts;
import com.dykj.rpg.db.dao.AbstractConfigDao;
import com.dykj.rpg.util.PackageUtil;

/**
 * @Description
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/9/29
 */
@Repository
public class ConfigManage extends BaseManager{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 初始化配置表数据
     */
    public void load() {
        List<String> pathList = PackageUtil.getClassName(this.getClass().getPackage().getName());//拿到comm包下的所有dao对象进行load操作
        for (String path : pathList) {
            if (path.contains(this.getClass().getSimpleName()))
                continue;

            try {
                Class<?> clazz = Class.forName(path);
                AbstractConfigDao configDao = (AbstractConfigDao) app.getBean(clazz);
                configDao.load();
            } catch (Exception e) {
                logger.error("初始化{}异常!", path, e);
                System.exit(-1);
            }
        }
    }

    /**
     * 刷新对象数据
     *
     * @param model
     * @return
     */
    public boolean refreshDataAll(Class model) {
        boolean flag = true;
        List<String> pathList = PackageUtil.getClassName(this.getClass().getPackage().getName());//拿到comm包下的所有dao对象进行load操作
        for (String path : pathList) {
            if (!path.contains(this.getClass().getSimpleName()) && path.contains(model.getSimpleName().replaceAll(CommonConsts.STR_MODEL, ""))) {
                try {
                    Class<?> clazz = Class.forName(path);
                    AbstractConfigDao configDao = (AbstractConfigDao) app.getBean(clazz);
                    configDao.clear();
                    configDao.load();
                } catch (Exception e) {
                    logger.error("初始化{}异常!", path, e);
                    flag = false;
                }
                break;
            }
        }
        return flag;
    }
}