package com.dykj.rpg.game.module.gm.service;

import com.dykj.rpg.common.config.dao.MisBasicDao;
import com.dykj.rpg.common.config.model.MisBasicModel;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.ice.client.BattleClient;
import com.dykj.rpg.game.module.battle.service.BattleService;
import com.dykj.rpg.game.module.gm.core.GmStrategy;
import com.dykj.rpg.game.module.gm.response.GmResponse;
import com.dykj.rpg.game.module.item.response.ItemJoinModel;
import com.dykj.rpg.game.module.item.service.RandomItemService;
import com.dykj.rpg.game.module.server.logic.GameServerConfig;
import com.dykj.rpg.game.module.skill.service.SkillService;
import com.dykj.rpg.protocol.game2battle.EnterBattleSkillInfo;
import com.dykj.rpg.protocol.game2battle.PlayerEnterBattleRequest;
import com.dykj.rpg.protocol.item.ItemRs;
import com.dykj.rpg.util.JsonUtil;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author jyb
 * @date 2020/12/5 15:21
 * @Description
 */
@Component
public class BattleGmService extends GmStrategy {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String serviceName() {
        return "battle";
    }

    @Resource
    private GameServerConfig gameServerConfig;
    @Resource
    private BattleService battleService;
    @Resource
    private SkillService skillService;
    @Resource
    private MisBasicDao misBasicDao;
    @Resource
    private RandomItemService randomItemService;

    public void equip() {
        List<EnterBattleSkillInfo> enterBattleSkillInfos = skillService.createEnterBattleSkills(player);
        System.out.println(JsonUtil.toJsonString(enterBattleSkillInfos));
    }


    public void item(String missionId) {
        Integer misId = Integer.valueOf(missionId);
        MisBasicModel misBasicModel = misBasicDao.getConfigByKey(misId);
        List<ItemJoinModel> itemJoinModels = randomItemService.randomItem(misBasicModel.getReward(), player.cache().getPlayerInfoModel().getProfession());

        logger.error("=============================start====================================");
        for (ItemJoinModel i : itemJoinModels) {
            logger.error(i.toString());
        }
        logger.error("=============================end====================================");
        // logger.info("item {} ", Arrays.toString(itemJoinModels.toArray()));
    }


    public GmResponse mission(String missionId) {
        GmResponse gmResponse = new GmResponse();
        gmResponse.setCodeEnum(ErrorCodeEnum.SUCCESS);
        MisBasicModel misBasicModel = BeanFactory.getBean(MisBasicDao.class).getConfigByKey(Integer.valueOf(missionId));
        if (misBasicModel == null) {
            logger.error("mission error  misBasicModel is not exist  missionId: {}", missionId);
            gmResponse.setCodeEnum(ErrorCodeEnum.DATA_ERROR);
            return gmResponse;
        }
        boolean flag = BeanFactory.getBean(BattleService.class).enterBattleServer(player, misBasicModel.getMisId(), (byte) 1);
        if (!flag) {
            logger.error("mission doHandler flag {} ", false);
            gmResponse.setCodeEnum(ErrorCodeEnum.ENTER_BATTLE_SERVER_ERROR);
        }
        return gmResponse;
    }

}