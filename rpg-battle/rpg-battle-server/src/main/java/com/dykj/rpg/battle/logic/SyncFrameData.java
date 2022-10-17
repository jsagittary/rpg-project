package com.dykj.rpg.battle.logic;

import com.dykj.rpg.net.core.UdpSession;
import com.dykj.rpg.net.protocol.Protocol;
import com.dykj.rpg.net.protocol.ProtocolPool;
import com.dykj.rpg.protocol.battle.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SyncFrameData {

    private Logger logger = LoggerFactory.getLogger("SyncFrameData");

    private final static byte SYNC_DATA_TYPE_ROLE_BASIC = 1;

    private final static byte SYNC_DATA_TYPE_ROLE_INFO = 2;

    private final static byte SYNC_DATA_TYPE_SKILL_EFFECT = 3;

    private final static byte SYNC_DATA_TYPE_SKILL_CARRIER = 4;

    private final static byte SYNC_DATA_TYPE_LOOT_INFO = 5;

    private final static byte SYNC_DATA_TYPE_HIT_INFO = 6;

    private final static byte SYNC_DATA_TYPE_SKILL_BUFF = 7;

    private final static byte SYNC_DATA_TYPE_AI_INFO = 8;

    public int frameNum;
    public long runFrameTime;

    public List<BattleRoleBasic> roleBasicList;
    public List<BattleRoleInfo> roleInfoList;
    public List<BattleSkillEffectInfo> skillEffectList;
    public List<BattleSkillCarrierInfo> skillCarrierList;
    public List<BattleRoleLootInfo> roleLootInfoList;
    public List<BattleRoleHitInfo> roleHitInfoList;
    public List<BattleRoleSkillBuff> roleSkillBuffList;
    public List<BattleAiInfo> roleAiInfoList;

    public SyncFrameData(){
        roleBasicList = new ArrayList<>();
        roleInfoList = new ArrayList<>();
        skillEffectList = new ArrayList<>();
        skillCarrierList = new ArrayList<>();
        roleLootInfoList = new ArrayList<>();
        roleHitInfoList = new ArrayList<>();
        roleSkillBuffList = new ArrayList<>();
        roleAiInfoList = new ArrayList<>();
    }

    private void clear(){
        roleBasicList.clear();
        roleInfoList.clear();
        skillEffectList.clear();
        skillCarrierList.clear();
        roleLootInfoList.clear();
        roleHitInfoList.clear();
        roleSkillBuffList.clear();
        roleAiInfoList.clear();
    }

    public void sendSyncData(UdpSession udpSession){

        if(frameNum == 0){
            return;
        }

//        if(udpSession != null && udpSession.kcpHandler != null){
//            System.out.println("sendSyncData time = "+System.currentTimeMillis());
//        }

        sendBattleSyncData(udpSession,SYNC_DATA_TYPE_ROLE_BASIC,roleBasicList,false);
        sendBattleSyncData(udpSession,SYNC_DATA_TYPE_ROLE_INFO,roleInfoList,false);
        sendBattleSyncData(udpSession,SYNC_DATA_TYPE_SKILL_EFFECT,skillEffectList,false);
        sendBattleSyncData(udpSession,SYNC_DATA_TYPE_SKILL_CARRIER,skillCarrierList,false);
        sendBattleSyncData(udpSession,SYNC_DATA_TYPE_LOOT_INFO,roleLootInfoList,false);
        sendBattleSyncData(udpSession,SYNC_DATA_TYPE_HIT_INFO,roleHitInfoList,false);
        sendBattleSyncData(udpSession,SYNC_DATA_TYPE_SKILL_BUFF,roleSkillBuffList,false);
        sendBattleSyncData(udpSession,SYNC_DATA_TYPE_AI_INFO,roleAiInfoList,false);
    }

    private void sendBattleSyncData(UdpSession udpSession,byte dataType, List<? extends Protocol> dataList, boolean needPrint){

        if(dataList.size() == 0){
            return;
        }

        if(dataType == SYNC_DATA_TYPE_ROLE_BASIC){
            BattleRoleBasicInfoRs syncDataProtocol = (BattleRoleBasicInfoRs) ProtocolPool.getInstance().borrowProtocol(BattleRoleBasicInfoRs.code);
            syncDataProtocol.getRoleBasics().addAll((List<BattleRoleBasic>)dataList);

            if(udpSession != null && udpSession.kcpHandler != null){
                udpSession.write(syncDataProtocol);
            }
            if(needPrint){
                logger.info(syncDataProtocol.toString());
            }
            ProtocolPool.getInstance().restoreProtocol(syncDataProtocol);
        }

        if(dataType == SYNC_DATA_TYPE_ROLE_INFO){
            BattleRoleSyncData syncDataProtocol = (BattleRoleSyncData) ProtocolPool.getInstance().borrowProtocol(BattleRoleSyncData.code);
            syncDataProtocol.setFrameNum(frameNum);
            syncDataProtocol.getRoleInfos().addAll((List<BattleRoleInfo>)dataList);

            if(udpSession != null && udpSession.kcpHandler != null){
                udpSession.write(syncDataProtocol);
            }
            if(needPrint){
                logger.info(syncDataProtocol.toString());
            }
            ProtocolPool.getInstance().restoreProtocol(syncDataProtocol);

        }

        if(dataType == SYNC_DATA_TYPE_SKILL_EFFECT){
            BattleEffectSyncData syncDataProtocol = (BattleEffectSyncData) ProtocolPool.getInstance().borrowProtocol(BattleEffectSyncData.code);
            syncDataProtocol.setFrameNum(frameNum);
            syncDataProtocol.getSkillEffects().addAll((List<BattleSkillEffectInfo>)dataList);

            if(udpSession != null && udpSession.kcpHandler != null){
                udpSession.write(syncDataProtocol);
            }
            if(needPrint){
                logger.info(syncDataProtocol.toString());
            }
            ProtocolPool.getInstance().restoreProtocol(syncDataProtocol);
        }

        if(dataType == SYNC_DATA_TYPE_SKILL_CARRIER){
            BattleCarrierSyncData syncDataProtocol = (BattleCarrierSyncData) ProtocolPool.getInstance().borrowProtocol(BattleCarrierSyncData.code);
            syncDataProtocol.setFrameNum(frameNum);
            syncDataProtocol.getSkillCarriers().addAll((List<BattleSkillCarrierInfo>)dataList);

            if(udpSession != null && udpSession.kcpHandler != null){
                udpSession.write(syncDataProtocol);
            }
            if(needPrint){
                logger.info(syncDataProtocol.toString());
            }
            ProtocolPool.getInstance().restoreProtocol(syncDataProtocol);
        }

        if(dataType == SYNC_DATA_TYPE_LOOT_INFO){
            BattleLootSyncData syncDataProtocol = (BattleLootSyncData) ProtocolPool.getInstance().borrowProtocol(BattleLootSyncData.code);
            syncDataProtocol.setFrameNum(frameNum);
            syncDataProtocol.getRoleLoots().addAll((List<BattleRoleLootInfo>)dataList);

            if(udpSession != null && udpSession.kcpHandler != null){
                udpSession.write(syncDataProtocol);
            }
            if(needPrint){
                logger.info(syncDataProtocol.toString());
            }
            ProtocolPool.getInstance().restoreProtocol(syncDataProtocol);
        }

        if(dataType == SYNC_DATA_TYPE_HIT_INFO){
            BattleRoleHitSyncData syncDataProtocol = (BattleRoleHitSyncData) ProtocolPool.getInstance().borrowProtocol(BattleRoleHitSyncData.code);
            syncDataProtocol.setFrameNum(frameNum);
            syncDataProtocol.getHitInfos().addAll((List<BattleRoleHitInfo>)dataList);

            if(udpSession != null && udpSession.kcpHandler != null){
                udpSession.write(syncDataProtocol);
            }
            if(needPrint){
                logger.info(syncDataProtocol.toString());
            }
            ProtocolPool.getInstance().restoreProtocol(syncDataProtocol);
        }

        if(dataType == SYNC_DATA_TYPE_SKILL_BUFF){
            BattleBuffSyncData syncDataProtocol = (BattleBuffSyncData) ProtocolPool.getInstance().borrowProtocol(BattleBuffSyncData.code);
            syncDataProtocol.setFrameNum(frameNum);
            syncDataProtocol.getSkillBuffs().addAll((List<BattleRoleSkillBuff>)dataList);

            if(udpSession != null && udpSession.kcpHandler != null){
                udpSession.write(syncDataProtocol);
            }
            if(needPrint){
                logger.info(syncDataProtocol.toString());
            }
            ProtocolPool.getInstance().restoreProtocol(syncDataProtocol);
        }

        if(dataType == SYNC_DATA_TYPE_AI_INFO){
            BattleAiSyncData syncDataProtocol = (BattleAiSyncData) ProtocolPool.getInstance().borrowProtocol(BattleAiSyncData.code);
            syncDataProtocol.setFrameNum(frameNum);
            syncDataProtocol.getAiInfos().addAll((List<BattleAiInfo>)dataList);

            if(udpSession != null && udpSession.kcpHandler != null){
                udpSession.write(syncDataProtocol);
            }
            if(needPrint){
                logger.info(syncDataProtocol.toString());
            }
            ProtocolPool.getInstance().restoreProtocol(syncDataProtocol);
        }
    }

    /**
     * 用于对过长的数据做切分，使其长度符合udp的最大数据容量
     */
    private void testSendBattleSyncData(UdpSession udpSession,byte dataType, List<? extends Protocol> dataList, boolean needPrint){
        //long startTime = System.currentTimeMillis();

        int size = 0;
        int maxSize = 10000;
        int index = 0;
        int listSize = dataList.size();
        boolean finish = false;
        a:while(listSize != 0){

            Protocol syncDataProtocol = null;
            List protocolDatas = null;

            if(dataType == SYNC_DATA_TYPE_ROLE_BASIC){
                //maxSize = SYNC_LIST_SIZE_ROLE_BASIC;
                syncDataProtocol = new BattleRoleBasicInfoRs();
                protocolDatas = ((BattleRoleBasicInfoRs) syncDataProtocol).getRoleBasics();
            }

            if(dataType == SYNC_DATA_TYPE_ROLE_INFO){
                //maxSize = SYNC_LIST_SIZE_ROLE_INFO;
                syncDataProtocol = new BattleRoleSyncData();
                ((BattleRoleSyncData) syncDataProtocol).setFrameNum(frameNum);
                protocolDatas = ((BattleRoleSyncData) syncDataProtocol).getRoleInfos();
            }

            if(dataType == SYNC_DATA_TYPE_SKILL_EFFECT){
                //maxSize = SYNC_LIST_SIZE_SKILL_EFFECT;
                syncDataProtocol = new BattleEffectSyncData();
                ((BattleEffectSyncData) syncDataProtocol).setFrameNum(frameNum);
                protocolDatas = ((BattleEffectSyncData) syncDataProtocol).getSkillEffects();
            }

            if(dataType == SYNC_DATA_TYPE_SKILL_CARRIER){
                //maxSize = SYNC_LIST_SIZE_SKILL_CARRIER;
                syncDataProtocol = new BattleCarrierSyncData();
                ((BattleCarrierSyncData) syncDataProtocol).setFrameNum(frameNum);
                protocolDatas = ((BattleCarrierSyncData) syncDataProtocol).getSkillCarriers();
            }

            if(dataType == SYNC_DATA_TYPE_LOOT_INFO){
                //maxSize = SYNC_LIST_SIZE_LOOT_INFO;
                syncDataProtocol = new BattleLootSyncData();
                ((BattleLootSyncData) syncDataProtocol).setFrameNum(frameNum);
                protocolDatas = ((BattleLootSyncData) syncDataProtocol).getRoleLoots();
            }

            if(dataType == SYNC_DATA_TYPE_HIT_INFO){
                //maxSize = SYNC_LIST_SIZE_HIT_INFO;
                syncDataProtocol = new BattleRoleHitSyncData();
                ((BattleRoleHitSyncData) syncDataProtocol).setFrameNum(frameNum);
                protocolDatas = ((BattleRoleHitSyncData) syncDataProtocol).getHitInfos();
            }

            if(dataType == SYNC_DATA_TYPE_SKILL_BUFF){
                //maxSize = SYNC_LIST_SIZE_SKILL_BUFF;
                syncDataProtocol = new BattleBuffSyncData();
                ((BattleBuffSyncData) syncDataProtocol).setFrameNum(frameNum);
                protocolDatas = ((BattleBuffSyncData) syncDataProtocol).getSkillBuffs();
            }

            size = 0;

            b:while(true){
                size ++;

                //if(size >= NetConfig.maxUdpLength){
                if(size > maxSize){
                    if(needPrint){
                        System.out.println(syncDataProtocol.toString());
                    }
                    if(udpSession != null && udpSession.kcpHandler != null){
                        udpSession.write(syncDataProtocol);
                    }
                    break b;
                }

                protocolDatas.add(dataList.get(index));
                index ++ ;

                if(index == listSize){
                    if(needPrint){
                        System.out.println(syncDataProtocol.toString());
                    }
                    if(udpSession != null && udpSession.kcpHandler != null){
                        udpSession.write(syncDataProtocol);
                    }
                    finish = true;
                    break b;
                }
            }

            if(finish){
                break a;
            }

        }

        //long endTime = System.currentTimeMillis();
        //System.out.println("sendBattleSyncData handle time = "+(endTime - startTime));
    }

}
