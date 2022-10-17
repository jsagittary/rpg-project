package com.dykj.rpg.game.module.gm.service;

import com.dykj.rpg.common.config.constant.ConfigEnum;
import com.dykj.rpg.common.config.dao.ConfigDao;
import com.dykj.rpg.common.config.model.ConfigModel;
import com.dykj.rpg.common.data.dao.PlayerInfoAttachedDao;
import com.dykj.rpg.common.data.dao.PlayerInfoDao;
import com.dykj.rpg.common.data.model.PlayerInfoAttachedModel;
import com.dykj.rpg.common.data.model.PlayerInfoModel;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.module.attribute.service.AttributeService;
import com.dykj.rpg.game.module.event.level.LevelEventManager;
import com.dykj.rpg.game.module.gm.core.GmStrategy;
import com.dykj.rpg.game.module.task.logic.service.TaskService;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.protocol.player.RoleUpgradeRs;
import com.dykj.rpg.protocol.player.UpdPlayerInfoRs;
import com.dykj.rpg.util.spring.BeanFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Description 玩家gm指令实现
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/23
 */
@Service
public class PlayerGmService extends GmStrategy
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String serviceName()
    {
        return "player";
    }

    @Resource
    private PlayerInfoDao playerInfoDao;

    @Resource
    private PlayerInfoAttachedDao playerInfoAttachedDao;

    @Resource
    private ConfigDao configDao;

    @Resource
    private AttributeService attributeService;

    @Resource
    private TaskService taskService;

    /**
     * 根据gm指令触发角色升级
     * @param lv 参数(角色等级)
     * @return
     */
    public void upgrade(String lv)
    {
        RoleUpgradeRs roleUpgrade = new RoleUpgradeRs();
        if (StringUtils.isBlank(lv))
        {
            logger.error("使用GM指令触发角色升级时参数为空!");
            CmdUtil.sendErrorMsg(this.getPlayer().getSession(), roleUpgrade.getCode(), ErrorCodeEnum.CLIENT_PRAMS_ERROR);
            return;
        }
        if (!StringUtils.isNumeric(lv))
        {
            logger.error("使用GM指令触发角色升级时参数:{} 类型错误!", lv);
            CmdUtil.sendErrorMsg(this.getPlayer().getSession(), roleUpgrade.getCode(), ErrorCodeEnum.CLIENT_PRAMS_ERROR);
            return;
        }
        PlayerInfoModel playerInfoModel = player.cache().getPlayerInfoModel();//玩家信息
        if (null == playerInfoModel)
        {
            logger.error("使用GM指令触发角色升级时, 根据玩家id:{} 查询玩家信息为空!", this.getPlayer().getPlayerId());
            CmdUtil.sendErrorMsg(this.getPlayer().getSession(), roleUpgrade.getCode(), ErrorCodeEnum.DATA_ERROR);
            return;
        }
        ConfigModel configModel = configDao.getConfigByKey(ConfigEnum.LEVELLIMIT.getConfigType());
        if (null == configModel)
        {
            logger.error("使用GM指令触发角色升级时, 查询config常量配置表\"玩家等级限制\"阈值为空!");
            CmdUtil.sendErrorMsg(this.getPlayer().getSession(), roleUpgrade.getCode(), ErrorCodeEnum.CONFIG_ERROR);
            return;
        }
        if (Integer.parseInt(lv) > Integer.parseInt(configModel.getValue()))
        {
            logger.error("使用GM指令触发角色升级时, 当前输入等级:{}, 已达到角色升级配置:{} 上限!", lv, configModel.getValue());
            CmdUtil.sendErrorMsg(this.getPlayer().getSession(), roleUpgrade.getCode(), ErrorCodeEnum.ROLES_LEVEL_UPPER_LIMIT);
            return;
        }
        playerInfoModel.setExp(0);
        playerInfoModel.setLv(Integer.parseInt(lv));
        player.cache().setPlayerInfoModel(playerInfoModel);
        playerInfoDao.queueUpdate(playerInfoModel);

        roleUpgrade.setLevel(playerInfoModel.getLv());
        roleUpgrade.setExp(playerInfoModel.getExp());
        CmdUtil.sendMsg(player, roleUpgrade);
        logger.debug("玩家id:{}, 角色等级:{} 执行GM指令设置角色等级操作后返回:{}", super.getPlayer().getPlayerId(), lv, roleUpgrade.toString());
        attributeService.refresh(player);
        logger.debug("玩家id:{}, 角色升级完成后刷新属性值: {}", player.getPlayerId(), attributeService.attributeString(player.cache().getAttributeCache().getAttributes()));
        //等级提升后触发事件监听
        BeanFactory.getBean(LevelEventManager.class).doEvents(player);
    }

    /**
     * 设置vip等级Gm指令(临时方案)
     * @param vipLv vip等级
     */
    public void vipgrade(String vipLv)
    {
        UpdPlayerInfoRs updPlayerInfoRs = new UpdPlayerInfoRs();
        if (StringUtils.isBlank(vipLv))
        {
            logger.error("使用GM指令设置vip等级时参数为空!");
            CmdUtil.sendErrorMsg(this.getPlayer().getSession(), updPlayerInfoRs.getCode(), ErrorCodeEnum.CLIENT_PRAMS_ERROR);
            return;
        }
        if (!StringUtils.isNumeric(vipLv))
        {
            logger.error("使用GM指令设置vip等级时参数:{} 类型错误!", vipLv);
            CmdUtil.sendErrorMsg(this.getPlayer().getSession(), updPlayerInfoRs.getCode(), ErrorCodeEnum.CLIENT_PRAMS_ERROR);
            return;
        }
        PlayerInfoModel playerInfoModel = player.cache().getPlayerInfoModel();//玩家信息
        if (null == playerInfoModel)
        {
            logger.error("使用GM指令设置vip等级时, 根据玩家id:{} 查询玩家信息为空!", this.getPlayer().getPlayerId());
            CmdUtil.sendErrorMsg(this.getPlayer().getSession(), updPlayerInfoRs.getCode(), ErrorCodeEnum.DATA_ERROR);
            return;
        }
        playerInfoModel.setVipLv(Integer.parseInt(vipLv));
        player.cache().setPlayerInfoModel(playerInfoModel);
        playerInfoDao.queueUpdate(playerInfoModel);
        updPlayerInfoRs.setLevel(playerInfoModel.getLv());
        updPlayerInfoRs.setExp(playerInfoModel.getExp());
        updPlayerInfoRs.setVipLv(playerInfoModel.getVipLv());
        CmdUtil.sendMsg(player, updPlayerInfoRs);
        logger.debug("玩家id:{}, 执行GM指令设置vip等级操作后返回:{}", super.getPlayer().getPlayerId(), updPlayerInfoRs.toString());
    }

    /**
     * 增加活跃度
     * @param paramsMap 参数：活跃度类型:活跃度数量
     */
    public void addActivity(Map<String, String> paramsMap)
    {
        UpdPlayerInfoRs updPlayerInfoRs = new UpdPlayerInfoRs();
        if (null == paramsMap || paramsMap.isEmpty())
        {
            logger.error("使用GM指令添加道具时参数为空!");
            CmdUtil.sendErrorMsg(player.getSession(), updPlayerInfoRs.getCode(), ErrorCodeEnum.CLIENT_PRAMS_ERROR);
            return;
        }
        PlayerInfoAttachedModel playerInfoAttachedModel = taskService.getPlayerInfoAttached(player);
        if (null == playerInfoAttachedModel)
        {
            logger.error("玩家id:{}, 查询玩家附属信息为空!", player.getPlayerId());
            CmdUtil.sendErrorMsg(player.getSession(), updPlayerInfoRs.getCode(), ErrorCodeEnum.CLIENT_PRAMS_ERROR);
            return;
        }
        for (Map.Entry<String, String> entry : paramsMap.entrySet())
        {
            int activityType = Integer.parseInt(entry.getKey());
            int activity = Integer.parseInt(entry.getValue());
            if (activityType == 1)//日常
            {
                playerInfoAttachedModel.setDailyActivity(playerInfoAttachedModel.getDailyActivity() + activity);
            }
            else if (activityType == 2)//周常
            {
                playerInfoAttachedModel.setWeekActivity(playerInfoAttachedModel.getWeekActivity() + activity);
            }
            else
            {
                logger.error("玩家id:{}, 活跃度类型:{}, 错误!", player.getPlayerId(), activityType);
                CmdUtil.sendErrorMsg(player.getSession(), updPlayerInfoRs.getCode(), ErrorCodeEnum.CLIENT_PRAMS_ERROR);
                return;
            }
        }
        player.cache().setPlayerInfoAttachedModel(playerInfoAttachedModel);
        playerInfoAttachedDao.queueUpdate(playerInfoAttachedModel);
        updPlayerInfoRs.setDailyActivity(playerInfoAttachedModel.getDailyActivity());
        updPlayerInfoRs.setWeekActivity(playerInfoAttachedModel.getWeekActivity());
        CmdUtil.sendMsg(player, updPlayerInfoRs);
        logger.error("玩家id:{}, 执行更新活跃度GM指令完毕, 返回协议:{}", player.getPlayerId(), updPlayerInfoRs.toString());
    }
}