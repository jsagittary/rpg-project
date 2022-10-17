package com.dykj.rpg.battle.cache.manager;

import com.dykj.rpg.battle.cache.constant.BattleTypeEnum;
import com.dykj.rpg.battle.cache.util.RingBuffer;
import com.dykj.rpg.net.protocol.BitArray;
import com.dykj.rpg.net.protocol.ProtocolByteArrayManager;
import com.dykj.rpg.protocol.game2battle.PlayerEnterBattleRequest;
import com.dykj.rpg.protocol.game2battle.PlayerEnterBattleRequestList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TableManager {

    Logger logger = LoggerFactory.getLogger("TableManager");

    private static TableManager instance;
    /**
     * key=战斗类型
     */
    private Map<Byte, RingBuffer<PlayerEnterBattleRequest>> enterBattleRequestMap;

    private TableManager(){
        enterBattleRequestMap = new ConcurrentHashMap<>();
    }

    public static TableManager getInstance(){
        if(instance == null){
            instance = new TableManager();
        }
        return instance;
    }

    public void addEnterBattleRequest(PlayerEnterBattleRequest request){
        byte battleType = request.getBattleType();
        RingBuffer<PlayerEnterBattleRequest> ringBuffer = enterBattleRequestMap.get(battleType);
        if(ringBuffer == null){
            ringBuffer = new RingBuffer<PlayerEnterBattleRequest>(10000);
            enterBattleRequestMap.put(battleType,ringBuffer);
        }
        ringBuffer.add(request);

        testTableResult(battleType);
    }

    /**
     * 测试是否有组队成功的战斗
     */
    private void testTableResult(byte battleType){
        RingBuffer<PlayerEnterBattleRequest> ringBuffer = enterBattleRequestMap.get(battleType);
        if(ringBuffer != null){
            BattleTypeEnum battleTypeEnum = BattleTypeEnum.getEnumByBattleType(battleType);
            if(battleTypeEnum != null && ringBuffer.size() >= battleTypeEnum.playerNum){
                PlayerEnterBattleRequestList requests = new PlayerEnterBattleRequestList();
                List<PlayerEnterBattleRequest> list = requests.getRequests();
                for(int i=0;i<battleTypeEnum.playerNum;i++){
                    list.add(ringBuffer.poll());
                }
                System.out.println("battle table success !!!");
                BitArray bitArray = ProtocolByteArrayManager.getInstance().getProtocolBitArray();
                requests.encode(bitArray);
                ClientManager.getInstance().sendTableInfoToBattleServer(bitArray.getWriteByteArray());
            }
        }else{
            logger.error("enter battle fail ! battleType["+battleType+"] is not exist !!!");
        }
    }

}
