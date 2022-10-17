package com.dykj.rpg.game.module.rune.service.impl;

import com.dykj.rpg.common.config.dao.SkillCharacterBasicDao;
import com.dykj.rpg.common.config.dao.SkillRunesBasicDao;
import com.dykj.rpg.common.config.dao.SkillStarUseDao;
import com.dykj.rpg.common.config.model.SkillCharacterBasicModel;
import com.dykj.rpg.common.config.model.SkillRunesBasicModel;
import com.dykj.rpg.common.config.model.SkillRunesEffectModel;
import com.dykj.rpg.common.config.model.SkillStarUseModel;
import com.dykj.rpg.common.data.dao.PlayerRuneDao;
import com.dykj.rpg.common.data.model.PlayerRuneModel;
import com.dykj.rpg.common.data.model.PlayerSkillModel;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.module.cache.PlayerRuneCache;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.rune.consts.RuneTypeEnum;
import com.dykj.rpg.game.module.rune.service.RuneService;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.protocol.rune.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @Description
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/5/20
 */
@Service
public class RuneServiceImpl implements RuneService
{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private SkillCharacterBasicDao skillCharacterBasicDao;

    @Resource
    private SkillStarUseDao skillStarUseDao;

    @Resource
    private SkillRunesBasicDao skillRunesBasicDao;

    @Resource
    private PlayerRuneDao playerRuneDao;

    /**
     * 符文装配
     * @param player 玩家信息
     * @param runeAssemblyRq 协议
     */
    public void runeAssembly(Player player, RuneAssemblyRq runeAssemblyRq)
    {
        if (0 == runeAssemblyRq.getSkillId() || 0 == runeAssemblyRq.getRuneId() || runeAssemblyRq.getRunePos() <= 0)
        {
            logger.error("玩家id:{}, 符文装配时请求参数错误!", player.getPlayerId());
            CmdUtil.sendErrorMsg(player.getSession(), runeAssemblyRq.getCode(), ErrorCodeEnum.CLIENT_PRAMS_ERROR);
            return;
        }
        PlayerRuneCache playerRuneCache = player.cache().getPlayerRuneCache();
        if (null != playerRuneCache.getRuneInfo(runeAssemblyRq.getSkillId(), runeAssemblyRq.getRuneId()))
        {
            logger.error("玩家id:{}, 符文id:{}, 技能id:{}, 当前符文已装配在该技能上", player.getPlayerId(), runeAssemblyRq.getRuneId(), runeAssemblyRq.getSkillId());
            CmdUtil.sendErrorMsg(player.getSession(), runeAssemblyRq.getCode(), ErrorCodeEnum.RUNE_ALREADY_ASSEMBLED_SKILL);
            return;
        }
        //技能基础
        SkillCharacterBasicModel skillCharacterBasicModel = skillCharacterBasicDao.getConfigByKey(runeAssemblyRq.getSkillId());
        //玩家对应技能
        PlayerSkillModel playerSkillModel = player.cache().getPlayerSkillCache().getPlayerSkillModelMap().get(runeAssemblyRq.getSkillId());
        if (null == skillCharacterBasicModel || null == playerSkillModel)
        {
            logger.error("玩家id:{}, 技能id:{}, 查询当前技能或技能基础配置表数据为空!", player.getPlayerId(), runeAssemblyRq.getSkillId());
            CmdUtil.sendErrorMsg(player.getSession(), runeAssemblyRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
            return;
        }
        //根据技能品质和技能星级匹配技能升星消耗资源表数据
        Optional<SkillStarUseModel> skillUpgOpt = skillStarUseDao.getConfigs().stream()
                .filter(e -> e.getItemQualityType().equals(skillCharacterBasicModel.getItemQualityType()))
                .filter(k -> k.getSkillStarLevel() == playerSkillModel.getSkillStarLevel()).findFirst();
        if (!skillUpgOpt.isPresent())
        {
            logger.error("玩家id:{}, 技能id:{}, 未匹配到技能升级所需的资源数据!", player.getPlayerId(), playerSkillModel.getSkillId());
            CmdUtil.sendErrorMsg(player.getSession(), runeAssemblyRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
            return;
        }
        //判断符文槽数量是否满足
        List<PlayerRuneModel> assemblyRuneList = playerRuneCache.getRuneList(runeAssemblyRq.getSkillId());
        if (null != assemblyRuneList && !assemblyRuneList.isEmpty() && assemblyRuneList.size() == skillUpgOpt.get().getRuneNumber())
        {
            logger.error("玩家id:{}, 技能id:{}, 符文槽已满!", player.getPlayerId(), runeAssemblyRq.getSkillId());
            CmdUtil.sendErrorMsg(player.getSession(), runeAssemblyRq.getCode(), ErrorCodeEnum.RUNE_SLOT_FULL);
            return;
        }
        //判断技能是否适配当前符文
        SkillRunesBasicModel skillRunesBasicModel = skillRunesBasicDao.getConfigByKey(runeAssemblyRq.getRuneId());
        if (null == skillRunesBasicModel)
        {
            logger.error("玩家id:{}, 符文id:{}, 未匹配到符文基础配置数据!", player.getPlayerId(), runeAssemblyRq.getRuneId());
            CmdUtil.sendErrorMsg(player.getSession(), runeAssemblyRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
            return;
        }
        if (!this.checkAdaptationType(skillCharacterBasicModel, skillRunesBasicModel))
        {
            logger.error("玩家id:{}, 技能id:{}, 符文id:{}, 当前符文与该技能未适配!", player.getPlayerId(), runeAssemblyRq.getSkillId(), runeAssemblyRq.getRuneId());
            CmdUtil.sendErrorMsg(player.getSession(), runeAssemblyRq.getCode(), ErrorCodeEnum.RUNE_AND_SKILL_NOT_ADAPTED);
            return;
        }
        PlayerRuneModel playerRuneModel = this.assemblyModel(player.getPlayerId(), runeAssemblyRq.getSkillId(), runeAssemblyRq.getRuneId(), runeAssemblyRq.getRunePos());
        playerRuneDao.queueInsert(playerRuneModel);
        playerRuneCache.updateCache(playerRuneModel);
        CmdUtil.sendMsg(player, new RuneAssemblyRs(true));
        logger.debug("玩家id:{}, 技能id:{}, 符文id:{}, 装配符文操作执行完毕", player.getPlayerId(), runeAssemblyRq.getSkillId(), runeAssemblyRq.getRuneId());
    }

    /**
     * 符文卸载
     * @param player 玩家信息
     * @param runeUninstallRq 协议
     */
    public void runeUninstall(Player player, RuneUninstallRq runeUninstallRq)
    {
        if (0 == runeUninstallRq.getSkillId() || 0 == runeUninstallRq.getRuneId())
        {
            logger.error("玩家id:{}, 符文卸载时请求参数错误!", player.getPlayerId());
            CmdUtil.sendErrorMsg(player.getSession(), runeUninstallRq.getCode(), ErrorCodeEnum.CLIENT_PRAMS_ERROR);
            return;
        }
        PlayerRuneCache playerRuneCache = player.cache().getPlayerRuneCache();
        //判断当前符文处于装配在当前技能上才可以卸载
        PlayerRuneModel playerRuneModel = playerRuneCache.getRuneInfo(runeUninstallRq.getSkillId(), runeUninstallRq.getRuneId());
        if (null == playerRuneModel)
        {
            logger.error("玩家id:{}, 符文id:{}, 技能id:{}, 当前符文未装配在该技能上, 无法卸载", player.getPlayerId(), runeUninstallRq.getRuneId(), runeUninstallRq.getSkillId());
            CmdUtil.sendErrorMsg(player.getSession(), runeUninstallRq.getCode(), ErrorCodeEnum.RUNE_NOT_ALREADY_ASSEMBLED_SKILL);
            return;
        }
        playerRuneDao.queueDelete(playerRuneModel);
        playerRuneCache.deleteCache(playerRuneModel);
        CmdUtil.sendMsg(player, new RuneUninstallRs(true));
        logger.debug("玩家id:{}, 技能id:{}, 符文id:{}, 卸载符文操作执行完毕", player.getPlayerId(), runeUninstallRq.getSkillId(), runeUninstallRq.getRuneId());
    }

    /**
     * 符文替换
     * @param player 玩家信息
     * @param runeReplaceRq 协议
     */
    public void runeReplace(Player player, RuneReplaceRq runeReplaceRq)
    {
        if (0 == runeReplaceRq.getSkillId() || 0 == runeReplaceRq.getSourceRuneId() || 0 == runeReplaceRq.getNewRuneId() || runeReplaceRq.getRunePos() <= 0)
        {
            logger.error("玩家id:{}, 符文替换时请求参数错误!", player.getPlayerId());
            CmdUtil.sendErrorMsg(player.getSession(), runeReplaceRq.getCode(), ErrorCodeEnum.CLIENT_PRAMS_ERROR);
            return;
        }
        PlayerRuneCache playerRuneCache = player.cache().getPlayerRuneCache();
        //判断源符文处于装配在当前技能上才可以替换
        PlayerRuneModel sourcePlayerRuneModel = playerRuneCache.getRuneInfo(runeReplaceRq.getSkillId(), runeReplaceRq.getSourceRuneId());
        if (null == sourcePlayerRuneModel)
        {
            logger.error("玩家id:{}, 符文id:{}, 技能id:{}, 源符文未装配在该技能上, 无法替换", player.getPlayerId(), runeReplaceRq.getSourceRuneId(), runeReplaceRq.getSkillId());
            CmdUtil.sendErrorMsg(player.getSession(), runeReplaceRq.getCode(), ErrorCodeEnum.RUNE_NOT_ALREADY_ASSEMBLED_SKILL_REPLACE);
            return;
        }
        //判断新符文处于未装配的状态下才可以替换
        PlayerRuneModel newPlayerRuneModel = playerRuneCache.getCache((long) runeReplaceRq.getNewRuneId());
        if (null != newPlayerRuneModel)
        {
            logger.error("玩家id:{}, 符文id:{}, 新符文处于装配状态, 无法替换", player.getPlayerId(), runeReplaceRq.getNewRuneId());
            CmdUtil.sendErrorMsg(player.getSession(), runeReplaceRq.getCode(), ErrorCodeEnum.RUNE_IN_ASSEMBLY_STATUS);
            return;
        }
        //技能基础
        SkillCharacterBasicModel skillCharacterBasicModel = skillCharacterBasicDao.getConfigByKey(runeReplaceRq.getSkillId());
        if (null == skillCharacterBasicModel)
        {
            logger.error("玩家id:{}, 技能id:{}, 当前技能基础配置表数据为空!", player.getPlayerId(), runeReplaceRq.getSkillId());
            CmdUtil.sendErrorMsg(player.getSession(), runeReplaceRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
            return;
        }
        //判断技能是否适配新符文
        SkillRunesBasicModel newSkillRunesBasicModel = skillRunesBasicDao.getConfigByKey(runeReplaceRq.getNewRuneId());
        if (null == newSkillRunesBasicModel)
        {
            logger.error("玩家id:{}, 符文id:{}, 未匹配到符文基础配置数据!", player.getPlayerId(), runeReplaceRq.getNewRuneId());
            CmdUtil.sendErrorMsg(player.getSession(), runeReplaceRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
            return;
        }
        if (!this.checkAdaptationType(skillCharacterBasicModel, newSkillRunesBasicModel))
        {
            logger.error("玩家id:{}, 技能id:{}, 符文id:{}, 当前符文与该技能未适配!", player.getPlayerId(), runeReplaceRq.getSkillId(), runeReplaceRq.getNewRuneId());
            CmdUtil.sendErrorMsg(player.getSession(), runeReplaceRq.getCode(), ErrorCodeEnum.RUNE_AND_SKILL_NOT_ADAPTED);
            return;
        }
        playerRuneDao.queueDelete(sourcePlayerRuneModel);
        playerRuneCache.deleteCache(sourcePlayerRuneModel);
        newPlayerRuneModel = this.assemblyModel(player.getPlayerId(), runeReplaceRq.getSkillId(), runeReplaceRq.getNewRuneId(), runeReplaceRq.getRunePos());
        playerRuneDao.queueInsert(newPlayerRuneModel);
        playerRuneCache.updateCache(newPlayerRuneModel);
        CmdUtil.sendMsg(player, new RuneReplaceRs(true));
        logger.debug("玩家id:{}, 技能id:{}, 源符文id:{}, 新符文id:{}, 替换符文操作执行完毕", player.getPlayerId(), runeReplaceRq.getSkillId(),
                runeReplaceRq.getSourceRuneId(), runeReplaceRq.getNewRuneId());
    }

    /**
     * 校验适配类型
     */
    private boolean checkAdaptationType(SkillCharacterBasicModel skillCharacterBasicModel, SkillRunesBasicModel skillRunesBasicModel)
    {
        boolean flag = false;
        for(int i = 0; i < skillCharacterBasicModel.getRunesType().size(); i++)
        {
            if (skillCharacterBasicModel.getRunesType().get(i).equals(skillRunesBasicModel.getType()))
            {
                if (skillRunesBasicModel.getType() == RuneTypeEnum.EXCLUSIVE.getType())
                {
                    if (null != skillRunesBasicModel.getSkillId() && skillCharacterBasicModel.getSkillId().equals(skillRunesBasicModel.getSkillId()))
                    {
                        flag = true;
                        break;
                    }
                }
                else
                {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * 组装符文model
     * @param playerId 玩家id
     * @param skillId 技能id
     * @param runeId 符文id
     * @param runePos 符文位置
     */
    private PlayerRuneModel assemblyModel(int playerId, int skillId, int runeId, int runePos)
    {
        PlayerRuneModel playerRuneModel = new PlayerRuneModel();
        playerRuneModel.setPlayerId(playerId);
        playerRuneModel.setSkillId(skillId);
        playerRuneModel.setRuneId(runeId);
        playerRuneModel.setRunePos(runePos);
        return playerRuneModel;
    }

    /**
     * 封装协议
     * @param playerRuneModel 符文信息
     * @return 协议
     */
    @Override
    public RuneRs runeRs(PlayerRuneModel playerRuneModel)
    {
        RuneRs runeRs = new RuneRs();
        runeRs.setRuneId(playerRuneModel.getRuneId());
        runeRs.setRunePos(playerRuneModel.getRunePos());
        return runeRs;
    }
}