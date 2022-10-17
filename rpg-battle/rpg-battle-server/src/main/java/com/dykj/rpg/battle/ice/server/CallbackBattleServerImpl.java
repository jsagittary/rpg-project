package com.dykj.rpg.battle.ice.server;

import Ice.Current;
import com.dykj.rpg.battle.ice.client.BattleCacheClient;
import com.dykj.rpg.battle.ice.service._CallbackBattleServerDisp;
import com.dykj.rpg.battle.logic.BattleContainer;
import com.dykj.rpg.battle.manager.BattleManager;
import com.dykj.rpg.net.protocol.BitArray;
import com.dykj.rpg.net.protocol.ProtocolByteArrayManager;
import com.dykj.rpg.protocol.game2battle.PlayerEnterBattleRequest;
import com.dykj.rpg.protocol.game2battle.PlayerEnterBattleRequestList;

import java.util.List;

/**
 * 接收从战斗服返回的数据
 */
public class CallbackBattleServerImpl extends _CallbackBattleServerDisp {

    @Override
    public boolean enterToBattle(byte[] data, Current __current) {

        BitArray bitArray = ProtocolByteArrayManager.getInstance().getProtocolBitArray();
        bitArray.initBytes(data,data.length);
        PlayerEnterBattleRequestList requestList = new PlayerEnterBattleRequestList();
        requestList.decode(bitArray);
        bitArray.release();

        int playerId = requestList.getRequests().get(0).getPlayerId();
        byte battleType = requestList.getRequests().get(0).getBattleType();
        int missionId = requestList.getRequests().get(0).getMissionId();
        System.out.println("-------------------enterToBattle from gameServer------------------");
        System.out.println("playerId = "+playerId + "  battleType = "+battleType);
        BattleContainer container = BattleManager.getInstance().createOneBattle(requestList);

        //设置测试的数据
        //logic.startBattle();

        List<PlayerEnterBattleRequest> requests = requestList.getRequests();
        int roleSize = requests.size();
        int[] playerIds = new int[roleSize];
        for(int i=0;i<roleSize;i++){
            playerIds[i] = requests.get(i).getPlayerId();
        }
        //logic.startBattle();
        BattleCacheClient.getInstance().enterToBattleServerSuccess(container.battleId, battleType,missionId,playerIds);

        return false;
    }

    @Override
    public int getRunningBattleCount(Current __current) {
        return 0;
    }
}
