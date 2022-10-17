package com.dykj.rpg.battle.handler;

import com.dykj.rpg.battle.logic.BattleContainer;
import com.dykj.rpg.battle.logic.MapTileData;
import com.dykj.rpg.battle.manager.BattleManager;
import com.dykj.rpg.battle.role.BattlePlayer;
import com.dykj.rpg.battle.basic.BasicRoleSkill;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.core.UdpSession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.net.protocol.ProtocolPool;
import com.dykj.rpg.protocol.battle.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoginBattleServerHandler extends AbstractClientHandler<LoginBattleServerRq> {
    Logger logger = LoggerFactory.getLogger("LoginBattleServerHandler");
    @Override
    protected void doHandler(LoginBattleServerRq enterBattleServerRq, ISession session) {
        logger.info(enterBattleServerRq.toString());
        int playerId = enterBattleServerRq.getUserId();

        if(playerId == 0){
            logger.info("登录游戏的playerId不能为空！！！");
        }

        BattleContainer battleContainer = BattleManager.getInstance().getBattleContainerByPlayerId(playerId);

        if(battleContainer == null){
            //battleContainer = BattleManager.getInstance().createOneBattle(((UdpSession)session).playerId);
            battleContainer = BattleManager.getInstance().createOneBattle(((UdpSession)session).playerId);
        }

        if(battleContainer == null){
            BattleErrorMessage battleErrorMessage = (BattleErrorMessage)ProtocolPool.getInstance().borrowProtocol(BattleErrorMessage.code);
            battleErrorMessage.setErrorCode(1);
            battleErrorMessage.setHandlerId(1);
            battleErrorMessage.setErrMsg("没有已存在的战斗，并创建新战斗失败！");

            sendMsg(session,battleErrorMessage);
            return ;
        }

        //返回地图信息
        //LoginBattleServerRs response = new LoginBattleServerRs();
        LoginBattleServerRs response = (LoginBattleServerRs)ProtocolPool.getInstance().borrowProtocol(LoginBattleServerRs.code);
        if(playerId == ((UdpSession)session).playerId){
            response.setResult(true);
        }else{
            response.setResult(false);
        }

        if(battleContainer.mapTileDataList != null){
            BattlePlayer player = battleContainer.getPlayerById(playerId);
            response.setSoulId(player.soulId);
            response.setMisId(battleContainer.misId);
            response.setMapIndex(battleContainer.getCenterMapIndex());

            List<BattleMapInfo> mapInfos = response.getMapInfos();
            for(MapTileData mapTileData : battleContainer.mapTileDataList){
                BattleMapInfo battleMapInfo = (BattleMapInfo)ProtocolPool.getInstance().borrowProtocol(BattleMapInfo.code);
                battleMapInfo.setId(mapTileData.id);
                battleMapInfo.setIndex(mapTileData.index);
                battleMapInfo.setExit(mapTileData.exit);

                List<MapMonsterInfo> monsterInfoList = battleMapInfo.getMonsterInfos();
                for(Map.Entry<Integer, Integer> entry : mapTileData.npcRecordInfoMap.entrySet()){
                    MapMonsterInfo mapMonsterInfo = (MapMonsterInfo)ProtocolPool.getInstance().borrowProtocol(MapMonsterInfo.code);
                    mapMonsterInfo.setMonsterId(entry.getKey());
                    mapMonsterInfo.setMonsterNum(entry.getValue());
                    monsterInfoList.add(mapMonsterInfo);
                }

                mapInfos.add(battleMapInfo);
            }

            List<PlayerSkillInfo> skillInfos = response.getSkillInfos();
            player.skillManager.sendBasicSkillForClient(skillInfos);

            List<BattleAiInfo> aiInfos = response.getAiInfos();
            player.aiManager.sendBattleAiInfoForClient(aiInfos);

            List<BattlePotionInfo> potionInfos = response.getPotionInfos();
            player.skillManager.sendPotionInfoForClient(potionInfos);

        }

        response.setBattleType(battleContainer.battleType);
        response.setTotalRounds(battleContainer.maxNpcRounds);
        response.setBattleTime(battleContainer.successConditionManager.getLastBattleTime());
        response.setConditions(battleContainer.successConditionManager.getShowConditions());

        logger.info(response.toString());

        sendMsg(session,response);

        //返回主角在地图的坐标
        if(battleContainer.mapTileDataList != null){
            BattlePlayer player = battleContainer.getPlayerById(playerId);
            float[] pos = player.getCurrentPosition();

            BattleRoleBasicInfoRs basicInfoRs = (BattleRoleBasicInfoRs)ProtocolPool.getInstance().borrowProtocol(BattleRoleBasicInfoRs.code);
            List<BattleRoleBasic> roleBasicList = basicInfoRs.getRoleBasics();

            BattleRoleBasic roleBasic = (BattleRoleBasic)ProtocolPool.getInstance().borrowProtocol(BattleRoleBasic.code);
            roleBasic.setId(player.getModelId());
            roleBasic.setType(player.getRoleType());
            roleBasic.setRoleId(player.getRoleId());
            roleBasic.setLevel(player.getRoleLevel());
            roleBasic.setMaxBlood(player.attributeManager.maxBlood);
            roleBasic.setMaxSoulEnergy(player.attributeManager.maxSoulEnergy);
            roleBasic.setSkillSourceType(player.skillSourceType);
            roleBasic.setMaxSkillSourceNum(player.attributeManager.getMaxSkillSourceNumByType(player.skillSourceType));

            roleBasicList.add(roleBasic);


            logger.info(basicInfoRs.toString());

            sendMsg(session,basicInfoRs);


            BattleRoleSyncData battleRoleSyncData = (BattleRoleSyncData)ProtocolPool.getInstance().borrowProtocol(BattleRoleSyncData.code);
            battleRoleSyncData.setFrameNum(0);
            List<BattleRoleInfo> list = battleRoleSyncData.getRoleInfos();
            BattleRoleInfo roleInfo = new BattleRoleInfo();
            roleInfo.setId(player.getModelId());
            roleInfo.setMaxBlood(player.attributeManager.maxBlood);
            roleInfo.setBlood(player.attributeManager.curBlood);
            roleInfo.setTargetId(0);
            roleInfo.setAnim((byte)0);
            roleInfo.setShow(true);
            BattlePosition battlePosition = new BattlePosition();
            battlePosition.setPosX((int)(pos[0]*100));
            battlePosition.setPosZ((int)(pos[2]*100));
            battlePosition.setMapIndex((byte)0);
            battlePosition.setMoveType((byte)0);
            battlePosition.setDirection(player.getDirection());
            roleInfo.setPosition(battlePosition);
            list.add(roleInfo);

            logger.info(battleRoleSyncData.toString());

            sendMsg(session,battleRoleSyncData);
        }


    }

}
