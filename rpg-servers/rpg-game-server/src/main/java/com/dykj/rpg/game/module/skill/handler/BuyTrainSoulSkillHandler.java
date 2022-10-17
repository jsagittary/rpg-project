package com.dykj.rpg.game.module.skill.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dykj.rpg.common.attribute.consts.SkillTypeConsts;
import com.dykj.rpg.common.config.constant.ConfigEnum;
import com.dykj.rpg.common.config.dao.ConfigDao;
import com.dykj.rpg.common.consts.ItemOperateEnum;
import com.dykj.rpg.common.data.dao.PlayerSkillDao;
import com.dykj.rpg.common.data.model.PlayerSkillModel;
import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.cache.PlayerSkillCache;
import com.dykj.rpg.game.module.item.consts.ItemPromp;
import com.dykj.rpg.game.module.item.response.ItemJoinModel;
import com.dykj.rpg.game.module.item.service.ItemService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.protocol.skill.BuyTrainSoulSkillRq;
import com.dykj.rpg.protocol.skill.BuyTrainSoulSkillRs;
import com.dykj.rpg.util.date.DateUtils;
import com.dykj.rpg.util.spring.BeanFactory;

/**
 * @author jyb
 * @date 2021/2/23 15:51
 * @Description
 */
public class BuyTrainSoulSkillHandler extends GameHandler<BuyTrainSoulSkillRq> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doHandler(BuyTrainSoulSkillRq buyTrainSoulSkillRq, Player player) {
    	PlayerSkillCache playerSkillCache = player.cache().getPlayerSkillCache();
        if (playerSkillCache == null) {
            logger.error("BuyTrainSoulSkillHandler error playerSkillCache is not exist {}  ", player.getPlayerId());
            return;
        }
        PlayerSkillModel playerSkillModel = playerSkillCache.getPlayerSkillModelMap().get(buyTrainSoulSkillRq.getSkillId());
        if (playerSkillModel == null) {
            logger.error("BuyTrainSoulSkillHandler error skill is not exist playerId {}  ,skillId {}  ", player.getPlayerId(), buyTrainSoulSkillRq.getSkillId());
            return;
        }
        if (playerSkillModel.getSkillType() == SkillTypeConsts.SOUL_SKILL) {
            logger.error("BuyTrainSoulSkillHandler error skill is  SOUL_SKILL playerId {}  ,skillId {}  ", player.getPlayerId(), buyTrainSoulSkillRq.getSkillId());
            return;
        }
        if (playerSkillModel.getSoulChangeTime() == null) {
            logger.error("BuyTrainSoulSkillHandler error :skill  already train playerId {}  ,skillId {}  ", player.getPlayerId(), buyTrainSoulSkillRq.getSkillId());
            return;
        }
        //毫秒
        long time = Math.max(System.currentTimeMillis() - playerSkillModel.getSoulChangeTime().getTime(), 0);
        //快速淬炼参数2结算时间,秒
        int qickQuenchingTime = Integer.valueOf(BeanFactory.getBean(ConfigDao.class).getConfigByKey(ConfigEnum.QUICKQUENCHINGTIME.getConfigType()).getValue());
        String quickQuenchingCost = BeanFactory.getBean(ConfigDao.class).getConfigByKey(ConfigEnum.QUICKQUENCHINGCOST.getConfigType()).getValue();
        ItemJoinModel itemJoinModel = new ItemJoinModel(quickQuenchingCost);
        int cost = (int) Math.floor(time / 1000 / qickQuenchingTime * itemJoinModel.getItemNum());
        if (cost > 0) {
            itemJoinModel.setItemNum(-cost);
            BeanFactory.getBean(ItemService.class).updateItemPush(player, itemJoinModel, ItemOperateEnum.TRAIN_SOUL_SKILL_COST, ItemPromp.GENERIC);
        }
        playerSkillModel.setSoulChangeTime(DateUtils.getGMTTime());
        BeanFactory.getBean(PlayerSkillDao.class).queueUpdate(playerSkillModel);
        sendMsg(player, new BuyTrainSoulSkillRs(playerSkillModel.getSoulChangeTime().getTime()));
    }
}