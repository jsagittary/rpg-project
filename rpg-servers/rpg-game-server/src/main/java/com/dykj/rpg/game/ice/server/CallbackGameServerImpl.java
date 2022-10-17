package com.dykj.rpg.game.ice.server;

import Ice.Current;
import com.dykj.rpg.battle.ice.service._CallbackGameServerDisp;
import com.dykj.rpg.game.module.battle.service.BattleService;
import com.dykj.rpg.net.protocol.Serializer;
import com.dykj.rpg.protocol.game2battle.BattleFinishResultResponse;
import com.dykj.rpg.protocol.game2battle.EnterBattleSuccessResponse;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 接收从战斗服返回的数据
 */
public class CallbackGameServerImpl extends _CallbackGameServerDisp {

    private Logger logger = LoggerFactory.getLogger("game");
    /**
     * 进入战斗服返回，此时只是返回进入组队结果
     *
     * @param data
     * @param __current
     */
    @Override
    public void enterToBattleCacheResult(byte[] data, Current __current) {
        logger.info("CallbackGameServerImpl enterToBattleCacheResult success ");
    }

    /**
     * 组队成功后战斗服主动推送进入战斗信息
     *
     * @param data
     * @param __current
     */
    @Override
    public void enterToBattleServerSuccess(byte[] data, Current __current) {
        try {

            EnterBattleSuccessResponse enterBattleSuccessResponse = (EnterBattleSuccessResponse) Serializer.deserialize(data, EnterBattleSuccessResponse.class);
            logger.info("CallbackGameServerImpl enterToBattleServerSuccess success {}",enterBattleSuccessResponse.toString());
            BeanFactory.getBean(BattleService.class).enterToBattleServerSuccess(enterBattleSuccessResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 战斗结束后战斗服返回给游戏服战斗信息
     *
     * @param data
     * @param __current
     */
    @Override
    public void battleFinishResult(byte[] data, Current __current) {
        try {
           BattleFinishResultResponse battleFinishResultResponse = (BattleFinishResultResponse) Serializer.deserialize(data, BattleFinishResultResponse.class);
            logger.info("CallbackGameServerImpl battleFinishResult success {}",battleFinishResultResponse.toString());
            BeanFactory.getBean(BattleService.class).battleResult(battleFinishResultResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
       logger.info("------------battleFinishResult-------------");
    }
}
