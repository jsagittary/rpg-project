package com.dykj.rpg.battle.handler;

import com.dykj.rpg.battle.logic.BattleContainer;
import com.dykj.rpg.battle.manager.BattleManager;
import com.dykj.rpg.battle.role.BattlePlayer;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.core.UdpSession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.net.protocol.ProtocolPool;
import com.dykj.rpg.protocol.battle.CurrentTotalLootInfo;
import com.dykj.rpg.protocol.battle.ExitBattleRq;
import com.dykj.rpg.protocol.item.ItemRs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ExitBattleHandler extends AbstractClientHandler<ExitBattleRq> {
    Logger logger = LoggerFactory.getLogger("ExitBattleHandler");
    @Override
    protected void doHandler(ExitBattleRq exitBattleRq, ISession session) {
        logger.info(exitBattleRq.toString());
        int playerId = ((UdpSession)session).playerId;

        BattleContainer battleContainer = BattleManager.getInstance().getBattleContainerByPlayerId(playerId);
        if(battleContainer != null){
            //if(battleLogic.pauseBattle()){
                BattlePlayer player = battleContainer.getPlayerById(playerId);
                if(player != null){

                    List<ItemRs> awardRsList = player.getTotalLootInfoList();

                    CurrentTotalLootInfo currentTotalLootInfo = (CurrentTotalLootInfo) ProtocolPool.getInstance().borrowProtocol(CurrentTotalLootInfo.code);
                    List<ItemRs> lootInfos = currentTotalLootInfo.getLootInfos();
                    for(ItemRs itemRs : awardRsList){
                        ItemRs itemRs1 = (ItemRs)ProtocolPool.getInstance().borrowProtocol(ItemRs.code);
                        itemRs1.setItemType(itemRs.getItemType());
                        itemRs1.setItemId(itemRs.getItemId());
                        itemRs1.setItemNum(itemRs.getItemNum());
                        itemRs1.setInstId(itemRs.getInstId());
                        itemRs1.setEquip(itemRs.getEquip());
                        itemRs1.setExpiration(itemRs.getExpiration());
                        itemRs1.setIsLock(itemRs.getIsLock());
                        lootInfos.add(itemRs1);
                    }

                    logger.info(currentTotalLootInfo.toString());

                    sendMsg(session,currentTotalLootInfo);
                }
            //}
        }

    }
}
