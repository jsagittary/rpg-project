package com.dykj.rpg.battle.logic;


import com.dykj.rpg.battle.dictionary.StaticDictionary;
import com.dykj.rpg.common.config.model.LootDropteamModel;
import com.dykj.rpg.common.config.model.LootMiniobjBasicModel;
import com.dykj.rpg.net.protocol.ProtocolPool;
import com.dykj.rpg.protocol.battle.BattleRoleLootInfo;
import com.dykj.rpg.protocol.battle.LootDetailInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParseLoot {

    private static Random random = new Random(System.currentTimeMillis());

    /**
     * 独立计算随机
     */
    private static byte RANDOM_TYPE_SIGLE = 1;

    /**
     * 合并权重随机
     */
    private static byte RANDOM_TYPE_GROUP = 2;

    public static BattleRoleLootInfo parse(int characterType,List<List<Integer>> lootInfos){
        BattleRoleLootInfo battleRoleLootInfo = (BattleRoleLootInfo) ProtocolPool.getInstance().borrowProtocol(BattleRoleLootInfo.code);
        List<LootDetailInfo> lootDetailInfoList = battleRoleLootInfo.getLootDetails();
        for(List<Integer> lootInfo : lootInfos){
            parseLoot(characterType,lootDetailInfoList,lootInfo);
        }
        return battleRoleLootInfo;
    }

    private static void parseLoot(int characterType,List<LootDetailInfo> lootDetailInfoList,List<Integer> lootInfo) {
        if (lootInfo.size() > 0) {

            if (lootInfo.get(0) == 1) { //道具物品
                if (lootInfo.size() >= 5) {
                    int goodsType = lootInfo.get(1);
                    int goodsId = lootInfo.get(2);
                    int goodsNum = lootInfo.get(3);
                    int rate = lootInfo.get(4);

                    if (random.nextInt(10000) < rate) {
                        LootDetailInfo lootDetailInfo = (LootDetailInfo)ProtocolPool.getInstance().borrowProtocol(LootDetailInfo.code);
                        lootDetailInfo.setType((byte) 1);
                        lootDetailInfo.setDetailType(goodsType);
                        lootDetailInfo.setDetailId(goodsId);
                        lootDetailInfo.setNum(goodsNum);

                        lootDetailInfoList.add(lootDetailInfo);

                    }
                } else {
                    System.err.println("loot config error !!! ");
                }

            }
            if (lootInfo.get(0) == 2) { //小物件
                if (lootInfo.size() >= 3) {
                    int miniobjId = lootInfo.get(1);
                    int miniobjNum = lootInfo.get(2);
                    LootMiniobjBasicModel model = StaticDictionary.getInstance().getLootMiniobjBasicModelById(miniobjId);
                    List<Integer> itemThing = model.getItemThing();
                    List<Integer> itemNumber = model.getMiniobjNumber();
                    if(itemThing != null && itemThing.size() == 2 && itemNumber != null && itemNumber.size() == 2){
                        int detailType = itemThing.get(0);
                        int detailId = itemThing.get(1);

                        int goodsNum = 0;
                        if(itemNumber.get(1)==itemNumber.get(0)){
                            goodsNum = itemNumber.get(1);
                        }
                        if(itemNumber.get(1)<itemNumber.get(0)){
                            goodsNum = random.nextInt(itemNumber.get(1)-itemNumber.get(0))+itemNumber.get(0);
                        }

                        if(goodsNum != 0){
                            LootDetailInfo lootDetailInfo = (LootDetailInfo)ProtocolPool.getInstance().borrowProtocol(LootDetailInfo.code);
                            lootDetailInfo.setType((byte) 2);
                            lootDetailInfo.setDetailType(detailType);
                            lootDetailInfo.setDetailId(detailId);
                            lootDetailInfo.setNum(goodsNum*miniobjNum);
                            lootDetailInfo.setMiniObjId(miniobjId);

                            lootDetailInfoList.add(lootDetailInfo);
                        }
                    }
                }


            }
            if (lootInfo.get(0) == 3) { //掉落组
                if (lootInfo.size() >= 3) {
                    int dropteamId = lootInfo.get(1);
                    int rate = lootInfo.get(2);
                    if (random.nextInt(10000) < rate) {
                        List<LootDropteamModel> lootDropteamModelList = StaticDictionary.getInstance().getLootDropteamModelByDropteamIdAndCharacter(dropteamId, characterType);

                        for (LootDropteamModel lootDropteamModel : lootDropteamModelList) {
                            List<List<Integer>> dropThingList = lootDropteamModel.getDropThing();

                            int totalRate = 0;
                            int dropThingSize = dropThingList.size();
                            List<Integer> dropThingRateList = new ArrayList<>();
                            for (List<Integer> dropThing : dropThingList) {
                                if (dropThing.size() > 0) {
                                    int dropThingRate = dropThing.get(dropThing.size() - 1);
                                    dropThingRateList.add(dropThingRate);
                                    totalRate += dropThingRate;
                                } else {
                                    //TODO error
                                }

                            }

                            int randomNum = random.nextInt(totalRate);
                            int testRate = 0;
                            for (int i = 0; i < dropThingSize; i++) {
                                testRate += dropThingRateList.get(i);
                                if (randomNum < testRate) { //权重计算
                                    List<Integer> newList = new ArrayList<>();
                                    int copySize = dropThingList.get(i).size();
                                    for (int j = 0; j < copySize; j++) {
                                        if (j == copySize - 1) {
                                            newList.add(10000);
                                        } else {
                                            newList.add(dropThingList.get(i).get(j));
                                        }
                                    }
                                    parseLoot(characterType, lootDetailInfoList, newList);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
