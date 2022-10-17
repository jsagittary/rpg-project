package com.dykj.rpg.game.module.task.logic.service.strategy;

import com.dykj.rpg.common.config.dao.EquipBasicDao;
import com.dykj.rpg.common.config.dao.SkillCharacterBasicDao;
import com.dykj.rpg.common.config.model.EquipBasicModel;
import com.dykj.rpg.common.config.model.SkillCharacterBasicModel;
import com.dykj.rpg.common.data.dao.PlayerMissionDao;
import com.dykj.rpg.common.data.model.*;
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
 * @Description 任务完成条件 - 2=【激活期间】
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/19
 */
@Component
public class DuringActivationRealize extends TaskFinishStrategy
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private EquipBasicDao equipBasicDao;

    @Resource
    private SkillCharacterBasicDao skillCharacterBasicDao;

    @Resource
    private PlayerMissionDao playerMissionDao;

    @Override
    public void afterPropertiesSet() throws Exception
    {
        TaskFinishFactory.register(TaskFinishConditionEnum.DURING_ACTIVATION, this);
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
        TaskFinishConditionEnum.DuringActivationEnum activationEnum = (TaskFinishConditionEnum.DuringActivationEnum) conditionsModel.getSubclassCategory();
        int activateSubclassType = taskFinish.get(1);//激活期间类型下子类
        //这样判断是因为TaskServiceImpl实现是遍历的玩家所有任务判断传入的子类型避免同是激活期间的类型多次执行
        if (activateSubclassType == activationEnum.getSubclassType())
        {
            //【激活期间】提升法术等级X次
            if (activateSubclassType == TaskFinishConditionEnum.DuringActivationEnum.SPELLS_LEVEL.getSubclassType())
            {
                this.updProgress(playerTaskModel, taskFinish, 1);
            }
            //【激活期间】获得X件Y品质（见下文，品质相关）或以上装备
            else if (activateSubclassType == TaskFinishConditionEnum.DuringActivationEnum.QUALITY_EQUIP.getSubclassType())
            {
                PlayerEquipModel playerEquipModel = (PlayerEquipModel) conditionsModel.getParam()[0];
                EquipBasicModel equipBasicModel = equipBasicDao.getConfigByKey(playerEquipModel.getEquipId());
                if (null == equipBasicModel)
                {
                    logger.error("玩家id:{}, 装备id:{}, 获取装备基础信息为空!", player.getPlayerId(), playerEquipModel.getEquipId());
                    return null;
                }
                //如果当前添加的装备品质大于或等于配置项品质
                if (equipBasicModel.getItemQualityType() >= taskFinish.get(3))
                {
                    this.updProgress(playerTaskModel, taskFinish, 1);
                }
            }
            //【激活期间】学会X个Y品质（见下文，品质相关）或以上法术
            else if (activateSubclassType == TaskFinishConditionEnum.DuringActivationEnum.LEARN_SPELLS.getSubclassType())
            {
                PlayerSkillModel playerSkillModel = (PlayerSkillModel) conditionsModel.getParam()[0];
                SkillCharacterBasicModel skillCharacterBasicModel = skillCharacterBasicDao.getConfigByKey(playerSkillModel.getSkillId());
                if (null == skillCharacterBasicModel)
                {
                    logger.error("玩家id:{}, 技能id:{}, 获取技能基础信息为空!", player.getPlayerId(), playerSkillModel.getSkillId());
                    return null;
                }
                //如果当前添加的技能品质大于或等于配置项品质
                if (skillCharacterBasicModel.getItemQualityType() >= taskFinish.get(3))
                {
                    this.updProgress(playerTaskModel, taskFinish, 1);
                }
            }
            //【激活期间】挑战X次任意关卡
            else if (activateSubclassType == TaskFinishConditionEnum.DuringActivationEnum.CHALLENGE_MISSION.getSubclassType())
            {
                PlayerMissionModel playerMissionModel = player.cache().getPlayerMissionModel();
                if (null == playerMissionModel)
                {
                    logger.error("玩家id:{}, 获取玩家关卡为空!", player.getPlayerId());
                    return null;
                }
                //如果上一次战斗获胜的时间不为空
                if (null != playerMissionModel.getLastBattleTime())
                {
                    this.updProgress(playerTaskModel, taskFinish, 1);
                }
            }
            //【激活期间】完成灵魂法术淬炼X次
            else if (activateSubclassType == TaskFinishConditionEnum.DuringActivationEnum.SPELLS_TEMPER.getSubclassType())
            {
                this.updProgress(playerTaskModel, taskFinish, 1);
            }
            //【激活期间】使用Y道具X次
            else if (activateSubclassType == TaskFinishConditionEnum.DuringActivationEnum.USE_ITEM.getSubclassType())
            {
                PlayerItemModel playerItemModel = (PlayerItemModel) conditionsModel.getParam()[0];
                if(playerItemModel.getItemType() != taskFinish.get(2) || playerItemModel.getItemId() != taskFinish.get(3))
                {
                    return null;
                }
                int schedule = playerTaskModel.getTaskSchedule() + 1;
                if (schedule >= taskFinish.get(4))
                {
                    playerTaskModel.setTaskSchedule(taskFinish.get(4));
                    playerTaskModel.setTaskStatus(2);
                }
                else
                {
                    playerTaskModel.setTaskSchedule(schedule);
                }
            }
            //【激活期间】花费X金币
            else if (activateSubclassType == TaskFinishConditionEnum.DuringActivationEnum.SPEND_GOLD.getSubclassType())
            {
                int number = (int) conditionsModel.getParam()[0];//消耗金币数量
                this.updProgress(playerTaskModel, taskFinish, number < 0 ? Math.abs(number) : number);
            }
            //【激活期间】花费X钻石
            else if (activateSubclassType == TaskFinishConditionEnum.DuringActivationEnum.SPEND_DIAMOND.getSubclassType())
            {
                int number = (int) conditionsModel.getParam()[0];//消耗钻石数量
                this.updProgress(playerTaskModel, taskFinish, number < 0 ? Math.abs(number) : number);
            }
            logger.debug("玩家id:{}, 任务完成条件大类:{}, 子类:{}, 刷新任务状态完毕, playerTaskModel:{}", player.getPlayerId(), activationEnum.getSubclassType(), activateSubclassType, playerTaskModel.toString());
            return playerTaskModel;
        }
        return null;
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