package com.dykj.rpg.game.module.task.logic.service.strategy;

import com.dykj.rpg.common.config.dao.EquipBasicDao;
import com.dykj.rpg.common.config.dao.SkillCharacterBasicDao;
import com.dykj.rpg.common.config.model.EquipBasicModel;
import com.dykj.rpg.common.config.model.SkillCharacterBasicModel;
import com.dykj.rpg.common.data.model.PlayerEquipModel;
import com.dykj.rpg.common.data.model.PlayerSkillModel;
import com.dykj.rpg.common.data.model.PlayerTaskModel;
import com.dykj.rpg.game.module.cache.PlayerEquipCache;
import com.dykj.rpg.game.module.cache.logic.EquipCache;
import com.dykj.rpg.game.module.cache.PlayerSkillCache;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.task.consts.TaskFinishConditionEnum;
import com.dykj.rpg.game.module.task.logic.response.TaskConditions;
import com.dykj.rpg.game.module.task.logic.service.strategy.core.TaskFinishFactory;
import com.dykj.rpg.game.module.task.logic.service.strategy.core.TaskFinishStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 任务完成条件 - 3=【累计期间】
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/19
 */
@Component
public class AccumulationPeriodRealize extends TaskFinishStrategy
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private EquipBasicDao equipBasicDao;

    @Resource
    private SkillCharacterBasicDao skillCharacterBasicDao;

    @Override
    public void afterPropertiesSet() throws Exception
    {
        TaskFinishFactory.register(TaskFinishConditionEnum.ACCUMULATION_PERIOD, this);
    }

    /**
     * 策略方法
     * @param player 玩家信息
     * @param playerTaskModel 玩家任务
     * @param taskFinish 任务完成条件配置
     * @param conditionsModel 参数集
     * @return 玩家任务
     */
    @Override
    public PlayerTaskModel realize(Player player, PlayerTaskModel playerTaskModel, List<Integer> taskFinish, TaskConditions conditionsModel)
    {
        TaskFinishConditionEnum.GrandTotalEnum accumulationEnum = (TaskFinishConditionEnum.GrandTotalEnum) conditionsModel.getSubclassCategory();
        int activateSubclassType = taskFinish.get(1);//累计期间类型下子类
        //这样判断是因为TaskServiceImpl实现是遍历的玩家所有任务判断传入的子类型避免同是激活期间的类型多次执行
        if (activateSubclassType == accumulationEnum.getSubclassType())
        {
            //累计成功提升任意法术等级X次
            if (activateSubclassType == TaskFinishConditionEnum.GrandTotalEnum.SPELLS_LEVEL.getSubclassType())
            {
                this.updProgress(playerTaskModel, taskFinish, 1);
            }
            //【累计期间】获得X件Y品质（见下文，品质相关）或以上装备
            else if (activateSubclassType == TaskFinishConditionEnum.GrandTotalEnum.QUALITY_EQUIP.getSubclassType())
            {
                return this.grandEquipLogic(player, taskFinish, playerTaskModel);
            }
            //【累计期间】学会X个Y品质（见下文，品质相关）或以上法术
            else if (activateSubclassType == TaskFinishConditionEnum.GrandTotalEnum.LEARN_SPELLS.getSubclassType())
            {
                return this.grandSpellsLogic(player, taskFinish, playerTaskModel);
            }
            //【累计期间】完成灵魂法术淬炼X次
            else if (activateSubclassType == TaskFinishConditionEnum.GrandTotalEnum.SPELLS_TEMPER.getSubclassType())
            {
                this.updProgress(playerTaskModel, taskFinish, 1);
            }
            //【累计期间】花费X金币
            else if (activateSubclassType == TaskFinishConditionEnum.GrandTotalEnum.SPEND_GOLD.getSubclassType())
            {
                int number = (int) conditionsModel.getParam()[0];;//消耗金币数量
                this.updProgress(playerTaskModel, taskFinish, number < 0 ? Math.abs(number) : number);
            }
            //【累计期间】花费X钻石
            else if (activateSubclassType == TaskFinishConditionEnum.GrandTotalEnum.SPEND_DIAMOND.getSubclassType())
            {
                int number = (int) conditionsModel.getParam()[0];//消耗钻石数量
                this.updProgress(playerTaskModel, taskFinish, number < 0 ? Math.abs(number) : number);
            }
            logger.debug("玩家id:{}, 任务完成条件大类:{}, 子类:{}, 刷新任务状态完毕, playerTaskModel:{}", player.getPlayerId(), accumulationEnum.getSubclassType(), activateSubclassType, playerTaskModel.toString());
            return playerTaskModel;
        }
        return null;
    }

    /**
     * 【累计期间】获得X件Y品质（见下文，品质相关）或以上装备
     * @param player 玩家信息
     * @param taskFinish 任务完成条件配置
     * @param playerTaskModel 玩家任务
     */
    private PlayerTaskModel grandEquipLogic(Player player, List<Integer> taskFinish, PlayerTaskModel playerTaskModel)
    {
        PlayerEquipCache playerEquipCache = player.cache().getPlayerEquipCache();
        if (null == playerEquipCache)
        {
            logger.error("玩家id:{}, 获取玩家背包装备列表为空!", player.getPlayerId());
            return null;
        }
        int cumulativeCount = 0;
        for (EquipCache equipCache : playerEquipCache.getEquipCacheMap().values())
        {
            PlayerEquipModel playerEquipModel = equipCache.getPlayerEquipModel();
            EquipBasicModel equipBasicModel = equipBasicDao.getConfigByKey(playerEquipModel.getEquipId());
            if (null == equipBasicModel)
            {
                logger.error("玩家id:{}, 装备id:{}, 获取装备基础信息为空!", player.getPlayerId(), playerEquipModel.getEquipId());
                return null;
            }
            if (equipBasicModel.getItemQualityType() >= taskFinish.get(3))
            {
                cumulativeCount++;
            }
        }
        if (cumulativeCount >= taskFinish.get(2))
        {
            playerTaskModel.setTaskSchedule(taskFinish.get(2));
            playerTaskModel.setTaskStatus(2);
        }
        else
        {
            if (cumulativeCount == 0)
            {
                return null;
            }
            playerTaskModel.setTaskSchedule(playerTaskModel.getTaskSchedule() + 1);
        }
        return playerTaskModel;
    }

    /**
     * 【累计期间】学会X个Y品质（见下文，品质相关）或以上法术
     * @param player 玩家信息
     * @param taskFinish 任务完成条件配置
     * @param playerTaskModel 玩家任务
     */
    private PlayerTaskModel grandSpellsLogic(Player player, List<Integer> taskFinish, PlayerTaskModel playerTaskModel)
    {
        PlayerSkillCache playerSkillCache = player.cache().getPlayerSkillCache();
        if (null == playerSkillCache)
        {
            logger.error("玩家id:{}, 获取玩家技能列表为空!", player.getPlayerId());
            return null;
        }
        int cumulativeCount = 0;
        for (PlayerSkillModel playerSkillModel : playerSkillCache.getPlayerSkillModelMap().values())
        {
            SkillCharacterBasicModel skillCharacterBasicModel = skillCharacterBasicDao.getConfigByKey(playerSkillModel.getSkillId());
            if (null == skillCharacterBasicModel)
            {
                logger.error("玩家id:{}, 技能id:{}, 获取技能基础信息为空!", player.getPlayerId(), skillCharacterBasicModel.getSkillId());
                return null;
            }
            if (skillCharacterBasicModel.getItemQualityType() >= taskFinish.get(3))
            {
                cumulativeCount++;
            }
        }
        if (cumulativeCount >= taskFinish.get(2))
        {
            playerTaskModel.setTaskSchedule(taskFinish.get(2));
            playerTaskModel.setTaskStatus(2);
        }
        else
        {
            if (cumulativeCount == 0)
            {
                return null;
            }
            playerTaskModel.setTaskSchedule(playerTaskModel.getTaskSchedule() + 1);
        }
        return playerTaskModel;
    }

    /**
     * 更新进度
     * @param playerTaskModel 玩家任务
     * @param taskFinish 任务完成条件
     * @param quantity 进度数量
     */
    private void updProgress(PlayerTaskModel playerTaskModel, List<Integer> taskFinish, int quantity)
    {
        int schedule = playerTaskModel.getTaskSchedule() + quantity;
        if (schedule >= taskFinish.get(2))
        {
            playerTaskModel.setTaskSchedule(taskFinish.get(2));
            playerTaskModel.setTaskStatus(2);
        }
        else
        {
            playerTaskModel.setTaskSchedule(schedule);
        }
    }
}