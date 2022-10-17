package com.dykj.rpg.game.module.player.service;

import com.dykj.rpg.common.config.dao.ConfigManage;
import com.dykj.rpg.common.consts.BaseStatusCodeConstants;
import com.dykj.rpg.common.consts.CommonConsts;
import com.dykj.rpg.common.module.uc.logic.UcMsg;
import com.dykj.rpg.util.PackageUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/9/29
 */
@Service
public class PlayerConfigService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ConfigManage configManageDao;

    /**
     * rpg-game-common工程下model对应目录
     */
    private static final String MODEL_PATH = "com.dykj.rpg.common.config.model";

    /**
     * 刷新表数据
     * @param tableNames 配置表集合
     * @return
     */
    public UcMsg refreshTableData(String tableNames)
    {
        UcMsg ucMsg = new UcMsg();
        ucMsg.setCode(BaseStatusCodeConstants.UNKNOW_EXCEPTION);
        List<String> descList = new ArrayList<>();
        //tableNames为空则刷新全部表
        if (StringUtils.isBlank(tableNames))
        {
            List<String> stringList = PackageUtil.getClassName(MODEL_PATH);
            for (String tableName : stringList)
            {
                this.refresh(tableName, ucMsg, descList);
            }
        }
        else
        {
            String[] arr = tableNames.split(CommonConsts.STR_COMMA);
            for (String tableName : arr)
            {
                String finalTableName = this.parseTableName(tableName);
                Optional<String> first = PackageUtil.getClassName(MODEL_PATH).stream().filter(e -> e.contains(finalTableName)).findFirst();
                if (first.isPresent())
                {
                    this.refresh(first.get(), ucMsg, descList);
                }
                else
                {
                    descList.add(String.format("没有匹配到配置表: \"%s\"!", tableName));
                }
            }
        }
        ucMsg.setDesc(descList.toString());
        return ucMsg;
    }

    /**
     * 判断表名是否包含下划线包含则按驼峰命名规则解析
     * @param tableName 表名
     * @return
     */
    private String parseTableName(String tableName)
    {
        if (tableName.contains(CommonConsts.STR_SYMBOL_UNDERSCORE))
        {
            Optional<String> reduceTableName = Arrays.stream(tableName.split(CommonConsts.STR_SYMBOL_UNDERSCORE))
                    .map(ele -> ele.substring(0, 1).toUpperCase().concat(ele.substring(1).toLowerCase()))
                    .reduce(String::concat);
            if (reduceTableName.isPresent())
                tableName = reduceTableName.get() + CommonConsts.STR_MODEL;
        }
        else
        {
            tableName = tableName.substring(0, 1).toUpperCase().concat(tableName.substring(1).toLowerCase()) + CommonConsts.STR_MODEL;
        }
        return tableName;
    }

    /**
     * 刷新配置表
     */
    private void refresh(String tableName, UcMsg ucMsg, List<String> descList)
    {
        try
        {
            Class<?> classz = Class.forName(tableName);
            if (configManageDao.refreshDataAll(classz))
            {
                ucMsg.setCode(BaseStatusCodeConstants.OK);
                descList.add(String.format("刷新配置表: \"%s\" 成功!!", tableName));
            }
            else
            {
                descList.add(String.format("刷新配置表: \"%s\" 失败!", tableName));
            }
        }
        catch (ClassNotFoundException e)
        {
            logger.error("表名: {}, 类型转换异常!", tableName, e);
            descList.add(String.format("配置表: \"%s\" 类型转换失败!", tableName));
        }
        catch (Exception e)
        {
            logger.error("表名: {} 刷新内存数据异常!", tableName, e);
            descList.add(String.format("配置表: \"%s\" 刷新内存数据异常!", tableName));
        }
    }
}