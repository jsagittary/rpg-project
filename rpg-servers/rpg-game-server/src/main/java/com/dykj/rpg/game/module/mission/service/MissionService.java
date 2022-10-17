package com.dykj.rpg.game.module.mission.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dykj.rpg.common.config.constant.ConfigEnum;
import com.dykj.rpg.common.config.dao.ConfigDao;
import com.dykj.rpg.common.config.dao.MisBasicDao;
import com.dykj.rpg.common.config.dao.VipDao;
import com.dykj.rpg.common.config.model.MisBasicModel;
import com.dykj.rpg.common.config.model.VipModel;
import com.dykj.rpg.common.consts.CalculationEnum;
import com.dykj.rpg.common.consts.CommonConsts;
import com.dykj.rpg.common.consts.ItemOperateEnum;
import com.dykj.rpg.common.data.dao.PlayerMissionDao;
import com.dykj.rpg.common.data.model.PlayerMissionModel;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.module.item.consts.ItemPromp;
import com.dykj.rpg.game.module.item.response.ItemJoinModel;
import com.dykj.rpg.game.module.item.response.ItemResponse;
import com.dykj.rpg.game.module.item.service.ItemService;
import com.dykj.rpg.game.module.item.service.RandomItemService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.protocol.mission.GetHandUpAwardRs;
import com.dykj.rpg.protocol.mission.GetQuickHandUpAwardRs;
import com.dykj.rpg.protocol.mission.HandUpAwardRs;
import com.dykj.rpg.protocol.mission.MissionLoginRs;
import com.dykj.rpg.protocol.mission.MissionRs;
import com.dykj.rpg.protocol.mission.QuickHandUpRs;
import com.dykj.rpg.util.JsonUtil;
import com.dykj.rpg.util.date.DateUtils;

import cn.hutool.core.date.DateUtil;

/**
 * @author jyb
 * @date 2020/12/21 14:36
 * @Description
 */
@Service
public class MissionService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private PlayerMissionDao playerMissionDao;

    @Resource
    private ConfigDao configDao;

    @Resource
    private MisBasicDao misBasicDao;

    @Resource
    private RandomItemService randomItemService;
    @Resource
    private ItemService itemService;
    @Resource
    private VipDao vipDao;

    public PlayerMissionModel initMission(Player player) {
        PlayerMissionModel playerMissionModel = new PlayerMissionModel();
        playerMissionModel.setPlayerId(player.getPlayerId());
        playerMissionModel.setMissionId(CommonConsts.FIRST_MISSION);
        playerMissionModel.setHandUpTime(new Date());
        playerMissionModel.setLastSettleAwardTime(new Date());
        playerMissionDao.queueInsert(playerMissionModel);
        return playerMissionModel;
    }


    public MissionLoginRs missionLoginRs(Player player) {
        PlayerMissionModel playerMissionModel = player.cache().getPlayerMissionModel();
        MisBasicModel misBasicModel = misBasicDao.getConfigByKey(playerMissionModel.getMissionId());
        MissionLoginRs missionLoginRs = new MissionLoginRs();
        MissionRs missionRs = new MissionRs();
        missionRs.setMissionId(playerMissionModel.getMissionId());
        missionRs.setHandUpTime(playerMissionModel.getHandUpTime().getTime());

        if (playerMissionModel.getLastBattleTime() != null) {
            missionRs.setBattleCdEndTime(playerMissionModel.getLastBattleTime().getTime() + misBasicModel.getWaitTime() * 1000);
        } else {
            missionRs.setBattleCdEndTime(0L);
        }

        missionLoginRs.setMissionRs(missionRs);
        return missionLoginRs;
    }

    /**
     * 打开挂机奖励界面
     *
     * @param player
     */
    public void openHandUpAward(Player player) {
        PlayerMissionModel playerMissionModel = player.cache().getPlayerMissionModel();
        List<ItemJoinModel> itemJoinModels = refreshHangUpAward(player);
        HandUpAwardRs handUpAwardRs = new HandUpAwardRs();
        for (ItemJoinModel itemJoinModel : itemJoinModels) {
            handUpAwardRs.getAwards().add(itemJoinModel.covertItemRs());
        }
        handUpAwardRs.setHandUpTime(playerMissionModel.getHandUpTime().getTime());
        CmdUtil.sendMsg(player, handUpAwardRs);

    }

    /**
     * 领取挂机奖励
     *
     * @param player
     */
    public ErrorCodeEnum getOpenHandUpAward(Player player) {
        PlayerMissionModel playerMissionModel = player.cache().getPlayerMissionModel();
        List<ItemJoinModel> itemJoinModels = refreshHangUpAward(player);
        if (itemJoinModels == null || itemJoinModels.size() < 1) {
            return ErrorCodeEnum.HAND_UP_AWARD_ERROR;
        }
        //添加物品
        ItemResponse response= itemService.batchUpdateItemPush(player, itemJoinModels, ItemOperateEnum.HAND_UP_AWARD, ItemPromp.BULLET_FRAME);
        if(!response.getCodeEnum().equals(ErrorCodeEnum.SUCCESS)){
            logger.error("MissionService getOpenHandUpAward error : addItem error ");
        }
        //刷新挂机时间，只有领取了 才会刷新这个时间
        playerMissionModel.setHandUpTime(new Date());
        //清空已经发的奖励
        playerMissionModel.setHangUpAward(null);
        playerMissionDao.queueUpdate(playerMissionModel);
        GetHandUpAwardRs quickHandUpRs = new GetHandUpAwardRs(playerMissionModel.getHandUpTime().getTime());
        //推送成功协议
        CmdUtil.sendMsg(player, quickHandUpRs);

        return ErrorCodeEnum.SUCCESS;

    }


    /**
     * 刷新挂机奖励
     *
     * @param player
     * @return
     */
    public List<ItemJoinModel> refreshHangUpAward(Player player) {
        PlayerMissionModel playerMissionModel = player.cache().getPlayerMissionModel();
        MisBasicModel misBasicModel = misBasicDao.getConfigByKey(playerMissionModel.getMissionId());
        long now = System.currentTimeMillis();
        long time = now - playerMissionModel.getLastSettleAwardTime().getTime();
        int maxTime = Integer.valueOf(configDao.getConfigByKey(ConfigEnum.IDLEREWARDUPPERLIMIT.getConfigType()).getValue()) * 1000;
        time = time >= maxTime ? maxTime : time;
        List<ItemJoinModel> itemJoinModels = getHangUpAward(time, misBasicModel, player.cache().getPlayerInfoModel().getProfession());
        List<ItemJoinModel> result = new ArrayList<>();
        if (playerMissionModel.getHangUpAward() != null && !playerMissionModel.getHangUpAward().equals("")) {
            randomItemService.addItemJoinModel(result, JsonUtil.toList(playerMissionModel.getHangUpAward(), ItemJoinModel.class));
        }
        if (itemJoinModels != null) {
            randomItemService.addItemJoinModel(result, itemJoinModels);
            //跟新数据库
            playerMissionModel.setLastSettleAwardTime(new Date(now));
            playerMissionModel.setHangUpAward(JsonUtil.toJsonString(itemJoinModels));
            playerMissionDao.queueUpdate(playerMissionModel);
        }
        return result;
    }


    /**
     * 计算挂机奖励
     *
     * @param time毫秒
     * @param misBasicModel
     * @param profession
     * @return
     */
    
    public List<ItemJoinModel> getHangUpAward(long time, MisBasicModel misBasicModel, int profession) {
        //一分钟结算一次奖励，先计算需要结算多少次奖励
        int num = (int) time / (60 * 1000);
        if (num >= 1) {
            List<ItemJoinModel> result = new ArrayList<>();
            for (int i = 0; i < num; i++) {
                List<ItemJoinModel> itemJoinModels = randomItemService.randomItem(misBasicModel.getIdleReward(), profession);
                if (itemJoinModels != null && itemJoinModels.size() > 0) {
                    randomItemService.addItemJoinModel(result, itemJoinModels);
                } else {
                    logger.error("MissionService refreshHangUpAward error :award is null  missionId  {} ", misBasicModel.getMisId());
                }
            }
            return result;
        }
        return null;
    }

    /**
     * 获得快速挂机的界面信息
     *
     * @param player
     */
    public void quickHandUp(Player player) {
        PlayerMissionModel playerMissionModel = player.cache().getPlayerMissionModel();
        refreshHandUpNum(playerMissionModel);
        QuickHandUpRs quickHandUpRs = new QuickHandUpRs();
        quickHandUpRs.setQuickHangUpNum(playerMissionModel.getQuickHangUpNum());
        //几点重置
        int hour = Integer.valueOf(configDao.getConfigByKey(ConfigEnum.FASTIDLERESETPOINTTIME.getConfigType()).getValue());
        if (playerMissionModel.getQuickHangUpTime() == null) {
            quickHandUpRs.setQuickHangUpTime(0L);
        } else {
            Date refreshDate = DateUtils.getRefreshTime(playerMissionModel.getQuickHangUpTime(), hour);
            quickHandUpRs.setQuickHangUpTime(refreshDate.getTime());
        }

        String s = DateUtil.format(new Date(quickHandUpRs.getQuickHangUpTime()), "yyyy-MM-dd HH:mm:ss");
        System.out.println(s);
        CmdUtil.sendMsg(player,quickHandUpRs);
    }

    /**
     * 刷新快速挂机次数
     *
     * @param playerMissionModel
     */
    public void refreshHandUpNum(PlayerMissionModel playerMissionModel) {
        if (playerMissionModel.getQuickHangUpTime() == null) {
            return;
        }
        //几点重置
        int hour = Integer.valueOf(configDao.getConfigByKey(ConfigEnum.FASTIDLERESETPOINTTIME.getConfigType()).getValue());
        if (DateUtils.isPass(playerMissionModel.getQuickHangUpTime(), hour)) {
            playerMissionModel.setQuickHangUpNum(0);
            playerMissionModel.setQuickHangUpTime(null);
            playerMissionDao.queueUpdate(playerMissionModel);
        }
    }


    /**
     * 领取快速挂机奖励
     *
     * @param player
     * @return
     */
    public ErrorCodeEnum getHandUpAwardHandler(Player player) {
        PlayerMissionModel playerMissionModel = player.cache().getPlayerMissionModel();
        refreshHandUpNum(playerMissionModel);
        VipModel vipModel = vipDao.getConfigByKey(player.cache().getPlayerInfoModel().getVipLv());
        if (playerMissionModel.getQuickHangUpNum() >= vipModel.getFastIdleFreeNumber() + vipModel.getFastIdlePayNumber()) {
            return ErrorCodeEnum.QUICK_HAND_UP_NUM_ERROR;
        }
        if (playerMissionModel.getQuickHangUpNum() > 0) {
            String valueStr = configDao.getConfigByKey(ConfigEnum.FASTIDLECONSUME.getConfigType()).getValue();
            String value[] = valueStr.split("\\,");
            String costStr = value[playerMissionModel.getQuickHangUpNum() - 1];
            ItemJoinModel itemJoinModel = new ItemJoinModel(costStr, CalculationEnum.CALCULATION);
            if (!itemService.isQuantityEnough(player, itemJoinModel.getItemId(), -itemJoinModel.getItemNum())) {
                return ErrorCodeEnum.HAND_UP_COST_NOT_ENOUGH;
            }
            itemService.updateItemPush(player, itemJoinModel, ItemOperateEnum.GET_HAND_UP_AWARD_COST, ItemPromp.GENERIC);
        }
        MisBasicModel misBasicModel = misBasicDao.getConfigByKey(playerMissionModel.getMissionId());
        int time = Integer.valueOf(configDao.getConfigByKey(ConfigEnum.FASTIDLEREWARDTIME.getConfigType()).getValue()) * 1000;
        //获得奖励
        List<ItemJoinModel> itemJoinModels = getHangUpAward(time, misBasicModel, player.cache().getPlayerInfoModel().getProfession());
        //添加物品
        itemService.batchUpdateItemPush(player, itemJoinModels, ItemOperateEnum.HAND_UP_AWARD, ItemPromp.BULLET_FRAME);
        playerMissionModel.setQuickHangUpNum(playerMissionModel.getQuickHangUpNum() + 1);
        playerMissionModel.setQuickHangUpTime(new Date());
        playerMissionDao.queueUpdate(playerMissionModel);
        GetQuickHandUpAwardRs getQuickHandUpAwardRs = new GetQuickHandUpAwardRs();
        getQuickHandUpAwardRs.setQuickHangUpNum(playerMissionModel.getQuickHangUpNum());
        //几点重置
        int hour = Integer.valueOf(configDao.getConfigByKey(ConfigEnum.FASTIDLERESETPOINTTIME.getConfigType()).getValue());
        Date refreshDate = DateUtils.getRefreshTime(playerMissionModel.getQuickHangUpTime(), hour);
        //添加物品
        getQuickHandUpAwardRs.setQuickHangUpTime(refreshDate.getTime());
        CmdUtil.sendMsg(player, getQuickHandUpAwardRs);
        return ErrorCodeEnum.SUCCESS;
    }
}