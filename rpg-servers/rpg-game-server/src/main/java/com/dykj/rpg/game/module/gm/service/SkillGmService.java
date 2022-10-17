package com.dykj.rpg.game.module.gm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.dykj.rpg.game.module.task.logic.refresh.TaskScheduleRefreshUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dykj.rpg.common.config.dao.SkillCharacterBasicDao;
import com.dykj.rpg.common.config.model.SkillCharacterBasicModel;
import com.dykj.rpg.common.data.dao.PlayerSkillDao;
import com.dykj.rpg.common.data.model.PlayerSkillModel;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.module.cache.PlayerSkillCache;
import com.dykj.rpg.game.module.gm.core.GmStrategy;
import com.dykj.rpg.game.module.gm.response.GmResponse;
import com.dykj.rpg.protocol.skill.PlayerSkillListGmRs;
import com.dykj.rpg.protocol.skill.SkillLevelListGmRs;
import com.dykj.rpg.protocol.skill.SkillRs;

/**
 * @Description 技能gm指令实现
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/23
 */
@Service
public class SkillGmService extends GmStrategy
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SkillCharacterBasicDao skillCharacterBasicDao;// 技能基础

    @Resource
    private PlayerSkillDao playerSkillDao;// 玩家技能

    @Override
    public String serviceName()
    {
        return "skill";
    }

    /**
     * 根据gm指令修改技能等级
     *
     * @param paramsMap 参数(技能id:技能等级)
     * @return
     */
    public GmResponse set(Map<String, String> paramsMap)
    {
        GmResponse gmResponse = new GmResponse();
        gmResponse.setCodeEnum(ErrorCodeEnum.SUCCESS);
        if (null == paramsMap || paramsMap.isEmpty())
        {
            logger.error("使用GM指令修改技能等级时参数为空!");
            gmResponse.setCodeEnum(ErrorCodeEnum.CLIENT_PRAMS_ERROR);
            return gmResponse;
        }
        List<PlayerSkillModel> insPlayerSkill = new ArrayList<>();
        List<PlayerSkillModel> updPlayerSkill = new ArrayList<>();
        List<SkillRs> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : paramsMap.entrySet())
        {
            SkillCharacterBasicModel skillCharacterBasicModel = skillCharacterBasicDao.getConfigByKey(Integer.valueOf(entry.getKey()));
            if (null == skillCharacterBasicModel)
            {
                logger.error("技能id:{}, 技能基础表不存在!", entry.getKey());
                gmResponse.setCodeEnum(ErrorCodeEnum.SKILL_NOT_EXISTED);
                return gmResponse;
            }

            PlayerSkillCache playerSkillCache = player.cache().getPlayerSkillCache();
            if (null == playerSkillCache)
            {
                gmResponse.setCodeEnum(ErrorCodeEnum.DATA_ERROR);
                return gmResponse;
            }

            PlayerSkillModel playerSkillModel = playerSkillCache.getPlayerSkillModelMap().get(Integer.valueOf(entry.getKey()).longValue());
            if (null == playerSkillModel)
            {
                playerSkillModel = new PlayerSkillModel();
                playerSkillModel.setPlayerId(player.getPlayerId());
                playerSkillModel.setSkillId(Integer.parseInt(entry.getKey()));
                playerSkillModel.setSkillLevel(1);
                playerSkillModel.setSkillExp(0);
                playerSkillModel.setPosition(-1);
                // TODO 注意临时添加技能类型后续要修改
                playerSkillModel.setSkillType(1);// 临时添加后续要修改
                playerSkillModel.setSoulChangePos(0);
                insPlayerSkill.add(playerSkillModel);
            }
            else
            {
                int level = Integer.parseInt(entry.getValue());
                // 如果传入的技能等级大于技能升级的最大等级则进行值修正
                if (level > skillCharacterBasicModel.getSkillLimitnum())
                {
                    level = skillCharacterBasicModel.getSkillLimitnum();
                }
                playerSkillModel.setSkillLevel(level);
                updPlayerSkill.add(playerSkillModel);
            }
        }
        if (!insPlayerSkill.isEmpty())
        {
            for (PlayerSkillModel playerSkillModel : insPlayerSkill)
            {
                player.cache().getPlayerSkillCache().updateCache(playerSkillModel);
                playerSkillDao.queueInsert(playerSkillModel);
                list.add(this.skillRs(playerSkillModel));

                // 【激活期间】或【累计期间】学会X个Y品质（见下文，品质相关）或以上法术
                TaskScheduleRefreshUtil.learnSpellsSchedule(player, playerSkillModel);
            }
        }
        if (!updPlayerSkill.isEmpty())
        {
            for (PlayerSkillModel playerSkillModel : updPlayerSkill)
            {
                player.cache().getPlayerSkillCache().updateCache(playerSkillModel);
                playerSkillDao.queueUpdate(playerSkillModel);
                list.add(this.skillRs(playerSkillModel));
                // 法术升级触发【激活期间】或【累计期间】提升法术等级X次任务
                TaskScheduleRefreshUtil.spellsLevelSchedule(player);
            }
        }
        gmResponse.setProtocol(new SkillLevelListGmRs(list, true));
        logger.debug("根据玩家id:{}, 参数列表:{} 执行技能等级修改GM指令操作后返回:{}", super.getPlayer().getPlayerId(), paramsMap.toString(), gmResponse.toString());
        return gmResponse;
    }

    /**
     * 组装技能协议
     *
     * @param playerSkillModel 玩家技能
     * @return 协议
     */
    private SkillRs skillRs(PlayerSkillModel playerSkillModel)
    {
        SkillRs skillRs = new SkillRs();
        skillRs.setSkillId(playerSkillModel.getSkillId());
        skillRs.setSkillLevel(playerSkillModel.getSkillLevel());
        skillRs.setSkillExp(playerSkillModel.getSkillExp());
        skillRs.setPosition(playerSkillModel.getPosition());
        skillRs.setSkillType(playerSkillModel.getSkillType());
        return skillRs;
    }

    /**
     * 查询玩家技能列表
     *
     * @return 响应
     */
    public GmResponse list()
    {
        GmResponse gmResponse = new GmResponse();
        gmResponse.setCodeEnum(ErrorCodeEnum.SKILL_LIST_IS_NULL);
        PlayerSkillCache playerSkillCache = player.cache().getPlayerSkillCache();
        if (null != playerSkillCache)
        {
            List<SkillRs> skills = new ArrayList<>();
            for (PlayerSkillModel playerSkillModel : playerSkillCache.getPlayerSkillModelMap().values())
            {
                SkillRs skillRs = new SkillRs();
                skillRs.setSkillId(playerSkillModel.getSkillId());
                skillRs.setSkillLevel(playerSkillModel.getSkillLevel());
                skillRs.setSkillExp(playerSkillModel.getSkillExp());
                skillRs.setPosition(playerSkillModel.getPosition());
                skillRs.setSkillType(playerSkillModel.getSkillType());
                skills.add(skillRs);
            }
            gmResponse.setCodeEnum(ErrorCodeEnum.SUCCESS);
            gmResponse.setProtocol(new PlayerSkillListGmRs(skills));
        }
        logger.debug("根据玩家id:{}, 执行玩家技能列表查询GM指令操作后返回:{}", super.getPlayer().getPlayerId(), gmResponse.toString());
        return gmResponse;
    }
}