package com.dykj.rpg.battle.logic;

import com.dykj.rpg.battle.config.*;
import com.dykj.rpg.battle.constant.*;
import com.dykj.rpg.battle.basic.BasicRoleSkill;
import com.dykj.rpg.battle.basic.BasicSkillCarrier;
import com.dykj.rpg.battle.role.BattlePlayer;
import com.dykj.rpg.battle.util.CalculationUtil;
import com.dykj.rpg.battle.detour.DetourHandler;
import com.dykj.rpg.battle.detour.MapLoader;
import com.dykj.rpg.battle.role.BattleMonster;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.battle.role.BattleRoleFactory;
import com.dykj.rpg.net.protocol.ProtocolPool;
import com.dykj.rpg.protocol.battle.*;
import com.dykj.rpg.util.random.RandomUtil;
import org.recast4j.detour.crowd.CrowdAgentParams;

import java.util.*;

/**
 * 主角进入地图的处理顺序
 * 1，将主角添加入地图
 *
 *
 */
public class MapLogic {

    private final static int MAP_MAX_ROLE_NUM = 200;

    private BattleContainer battleContainer;

    private DetourHandler detourHandler;

    private TilePortalConfig portalConfig;

    private MapTileConfig tileConfig;

    private MapTileData mapTileData;

    private BattleRole player;

    private List<BattleRole> roles;

    private List<TileNpcDetail> waitBornNpcs;

    /**
     * 用于标记需要发送给客户端的角色的基础信息，默认只发送一次
     */
    private List<BattleRole> newRoles;

    /**
     * 用于标记需要发送给客户端的效果的基础信息，默认只发送一次
     */
    private List<BattleSkillEffectInfo> newEffects;

    private List<SkillReleaseData> skillReleaseDatas;


    /**
     * 掉落信息
     */
    private List<BattleRoleLootInfo> newLootInfos;

    private int hitModelId = 0;

    private int carrierModelId = 0;

    private byte state = MapLogicStateConstant.MAPLOGIC_STATE_STOP;

    private byte mapIndex = 0;

    public boolean using = false;

    public int tileId = 0;
    public MapLogic(int tileId){
        this.tileId = tileId;
        detourHandler = new DetourHandler(tileId);
        portalConfig = MapLoader.getInstance().getTilePortalById(tileId);
        roles = Collections.synchronizedList(new ArrayList<>());
        waitBornNpcs = new ArrayList<>();
        newRoles = new ArrayList<>();
        newEffects = new ArrayList<>();
        skillReleaseDatas = Collections.synchronizedList(new ArrayList<>());
        newLootInfos = Collections.synchronizedList(new ArrayList<>());
    }

    public void init(BattleContainer battleContainer,MapTileData mapTileData,MapTileConfig tileConfig){
        this.battleContainer = battleContainer;
        this.mapTileData = mapTileData;
        this.tileConfig = tileConfig.copy();
        this.mapIndex = mapTileData.index;
    }

    public void release(){
        battleContainer = null;

        detourHandler.release();

        //portalConfig = null;

        tileConfig = null;

        mapTileData = null;

        player = null;

        roles.clear();

        waitBornNpcs.clear();

        newRoles.clear();

        newEffects.clear();

        skillReleaseDatas.clear();

        newLootInfos.clear();

        hitModelId = 0;

        carrierModelId = 0;

        state = MapLogicStateConstant.MAPLOGIC_STATE_STOP;

        mapIndex = 0;

        using = false;
    }

    public TilePortalConfig getTilePortalConfig(){
        return portalConfig;
    }

    public void setAgentPosition(int idx,float[] currentPos){
        detourHandler.setAgentPosition(idx, currentPos);
    }

    public float[] getFirstMapPlayerStartPos(){
        return new float[]{tileConfig.startPosX,tileConfig.startPosZ};
        //return pollPlayerNextRoadPoint();
    }

    public float[] getEnterPos(){
        return MapLoader.getInstance().getEnterPosById(mapTileData.id);
    }

    public float[] getExitPos(){
        byte exit = mapTileData.exit;
        if(exit == 0){
            return null;
        }
        return MapLoader.getInstance().getExitPosById(mapTileData.id,exit);
    }

    public byte getMapIndex(){
        return mapIndex;
    }

    /**
     * 主角进入地图
     */
    public void playerEnterMap(BattleRole player,float positionOffsetX,float positionOffsetZ){
        this.player = player;

        //1.将对象指向其需要进入的地图 注意：一定要先 setMapLogic ，然后 setCurrentPosition ， addAgentToMap
        player.setMapLogic(this);
        //2.切换坐标点到新的地图中
        float[] enterPos = getEnterPos();
        player.setCurrentPosition(enterPos[0]+positionOffsetX,enterPos[1]+positionOffsetZ);
        //3.将对象加入新的地图
        addAgentToMap(player,true);
        //4.启动运行地图块
        startRunMap();

    }

    /**
     * 主角退出地图
     */
    public void playerExitMap(){

        //1.先从原地图里删除对象
        removeRoleFromMap(player);
        //2.将主角设为空
        this.player = null;
        //3.停止运行地图块
        stopRunMap();
    }

    public int createNewMonster(byte mapIndex,TileNpcDetail tileNpcDetail, short dir){
        /**
         * TODO 此处需要通过 roleId 从获取角色的基本信息并赋值给 RoleBasicModel
         */

//        RoleBasicModel roleBasicModel = new RoleBasicModel();
//        roleBasicModel.setType(RoleTypeConstant.BATTLE_ROLE_MONSTER);
//        roleBasicModel.setRoleId(tileNpcDetail.npcId);
//        roleBasicModel.setRadius(40);
//        roleBasicModel.setHeight(400);
//        roleBasicModel.setMaxBlood(100);
//        roleBasicModel.setWalkSpeed(400);
//        roleBasicModel.setRunSpeed(200);
//        roleBasicModel.setJumpSpeed(200);
//        roleBasicModel.setMaxAcceleration(800);
//        float[] position = new float[]{tileNpcDetail.x,0,tileNpcDetail.z,dir};

        /**
         * 以下为正式代码,创建一个怪物对象
         */
        //roleId = 10001;

        BattleMonster monster = BattleRoleFactory.createBattleMonster(battleContainer,mapIndex,tileNpcDetail);
        monster.setCurrentPosition(tileNpcDetail.x,tileNpcDetail.z);
        monster.setCurrentDir(dir);
        addRoleToBattle(monster);

        newRoles.add(monster);

        return monster.getModelId();

    }

    /**
     * 将角色添加到战斗中
     */
    public boolean addRoleToBattle(BattleRole battleRole){
        /**
         * 以下两行为添加数据到缓存系统，做主从数据备份
         */
        //battleLogic.battle.getRoleBattleModels().add(battleRole.getRoleBattleModel());
        //battleLogic.battle.getRolePositionModels().add(battleRole.getRolePositionModel());

        /**
         * 将角色添加到战斗逻辑层
         */
        roles.add(battleRole);
        /**
         * 将角色添加到地图系统，用于寻路
         */
        battleRole.setMapLogic(this);
        addAgentToMap(battleRole,true);

        return false;
    }

    /**
     * 添加一个角色到战斗地图中
     * @param battleRole
     * @param crowdPath 是否需要进行群体寻路
     */
    public void addAgentToMap(BattleRole battleRole, boolean crowdPath){
        //int updateFlags = CrowdAgentParams.DT_CROWD_ANTICIPATE_TURNS | CrowdAgentParams.DT_CROWD_OPTIMIZE_VIS
        //        | CrowdAgentParams.DT_CROWD_OPTIMIZE_TOPO | CrowdAgentParams.DT_CROWD_OBSTACLE_AVOIDANCE;

        int updateFlags = CrowdAgentParams.DT_CROWD_ANTICIPATE_TURNS | CrowdAgentParams.DT_CROWD_OBSTACLE_AVOIDANCE;
        int idx = detourHandler.addAgentGrid(battleRole, crowdPath, updateFlags, 3);
        battleRole.setCaIdx(idx);
    }

    public void removeRoleFromMap(BattleRole battleRole){
        /**
         * 以下两行为添加数据到缓存系统，做主从数据备份
         */
        //battleLogic.battle.getRoleBattleModels().remove(battleRole.getRoleBattleModel());
        //battleLogic.battle.getRolePositionModels().remove(battleRole.getRolePositionModel());

        /**
         * 将角色从地图系统删除
         */
        roles.remove(battleRole);
        detourHandler.removeAgentGrid(battleRole);

        if(battleRole instanceof BattleMonster){
            battleContainer.battlePoolManager.restoreBattleMonster((BattleMonster)battleRole);
        }
//        if(battleRole instanceof BattlePlayer){
//            battleContainer.battlePoolManager.restoreBattlePlayer((BattlePlayer)battleRole);
//        }

    }

    private int getHitModelId(){
        return hitModelId++;
    }

    public int createCarrierModelId(){
        return ++carrierModelId;
    }

    public void startRunMap(){
        state = MapLogicStateConstant.MAPLOGIC_STATE_RUN;
    }

    public void stopRunMap(){
        state = MapLogicStateConstant.MAPLOGIC_STATE_STOP;

        if(player != null){
            player.setMoveType(RoleMoveTypeConstant.ROLE_MOVE_TYPE_WAIT);
        }

        if(roles.size() > 0){
            for(BattleRole role : roles){
                role.setMoveType(RoleMoveTypeConstant.ROLE_MOVE_TYPE_WAIT);
            }
        }
    }

    public byte getState(){
        return this.state;
    }

    public void finishMapLogic(){
        state = MapLogicStateConstant.MAPLOGIC_STATE_FINISH;
    }

    /**
     * 进行战斗群体寻路
     */
    public void findCrowdPath(){
        if(state == MapLogicStateConstant.MAPLOGIC_STATE_RUN && battleContainer.frameNum%GameConstant.CROWN_ADVANCE_FRAME_NUM==0){
            for(int i=0;i<roles.size()+1;i++){
                BattleRole role;
                if(i==roles.size()){
                    role = player;
                }else{
                    role = roles.get(i);
                }
                //BattleManager.getInstance().getDetourHandler().setAgentPosition(monster.caIdx,monster.getCurrentPosition(), monster.getCurrentDir());
                //monster.detourResults.clear();
                if(role.getDetourType() == DetourTypeConstant.DETOUR_TYPE_CROWD_ROLE){
                    if(role.targetRole != null){
                        role.setTargetPosition(role.targetRole.getCurrentPosition(),RoleTargetTypeConstant.ROLE_TARGET_TYPE_ROLE);
                        detourHandler.setAgentTarget(role.getCaIdx(),role.getTargetPosition());
                    }else{
                        role.setStillState(100);
                    }
                }
                if(role.getDetourType() == DetourTypeConstant.DETOUR_TYPE_CROWD_POSITION) {
                    if(role.getTargetPosition() != null){
                        detourHandler.setAgentTarget(role.getCaIdx(),role.getTargetPosition());
                    }else{
                        role.setStillState(100);
                    }
                }

            }
            detourHandler.findCrowdPath();
        }

    }

    public void findStraightPath(BattleRole battleRole){
        battleRole.clearFindPathResults();
        if(player == battleRole){
            detourHandler.findStraightPath(battleRole);
            return;
        }

        if(roles.contains(battleRole)){
            detourHandler.findStraightPath(battleRole);
        }
    }

    public List<BattleRole> getBattleRoles(){
        List<BattleRole> battleRoles = new ArrayList<>();
//        if(player != null){
//            battleRoles.add(player);
//        }
        for(BattleRole role : roles){
            battleRoles.add(role);
        }
        return battleRoles;
    }

    /**
     * 对于地块的更新，角色行为位置，子弹飞行位置等
     * 方法里的更新顺序不能随意改变
     * 顺序 玩家-->其他角色-->子弹
     * @param frameNum
     */
    public void runFrame(int frameNum){
        //0.先进行一次地图寻路
        findCrowdPath();
        //1.更新玩家信息
        if(state!=MapLogicStateConstant.MAPLOGIC_STATE_STOP && player != null){
            player.update(frameNum);
        }
        //2.怪物的出生控制
        if(state!=MapLogicStateConstant.MAPLOGIC_STATE_STOP && player != null){
            battleContainer.battleLogic.refreshMonsterLogic(this,tileConfig,(BattlePlayer) player);
        }
        //boolean hasNewMonster = false;
        //怪物数量超过40时需要做出生限制
        if(roles.size() < 40){
            if(waitBornNpcs != null && waitBornNpcs.size() > 0){
                for(int i=waitBornNpcs.size()-1;i>=0;i--){
                    TileNpcDetail npcDetail = waitBornNpcs.get(i);
                    if(npcDetail.getCurDelayTime() <= 0){
                        short dir = (short)CalculationUtil.vectorToAngle(player.getCurrentPosition()[0]-npcDetail.getX(),player.getCurrentPosition()[2]-npcDetail.getZ());
                        int modelId = createNewMonster(mapIndex, npcDetail,dir);
                        waitBornNpcs.remove(i);
                        //hasNewMonster = true;
                    }else{
                        npcDetail.setCurDelayTime(npcDetail.getCurDelayTime()-GameConstant.FRAME_TIME);
                    }
                }
            }
        }
        //放弃路点进行寻怪
        //if(hasNewMonster){
        //    ((BattlePlayer)player).newMonsterCreate();
        //}

        //3.更新怪物信息
        if(state!=MapLogicStateConstant.MAPLOGIC_STATE_STOP && roles.size() > 0){
            int size = roles.size();
            BattleRole battleRole;
            for(int i=0;i<size;){
                battleRole = roles.get(i);
                if(battleRole.getState() == RoleStateConstant.ROLE_STATE_DEATH){
                    if(player != null){
                        player.clearClientShowTargetRoleId(battleRole.getModelId());
                    }

                    //击杀类胜利条件添加信息
                    battleContainer.successConditionManager.addKillInfo(battleRole.getRoleId());
                    //删除标签，用于刷怪器进行监听
                    tileConfig.removeTileNpcDetail(battleRole.tileNpcDetail);
                    removeRoleFromMap(battleRole);
                    size--;
                }else{
                    battleRole.update(frameNum);
                    i++;
                }
            }
        }
        //4.更新技能载体信息
        if(state!=MapLogicStateConstant.MAPLOGIC_STATE_STOP && skillReleaseDatas.size() > 0){
            int index = 0;
            SkillReleaseData skillReleaseData;
            //注意： 由于skillReleaseData.update可能会增加SkillReleaseData数量，所以此处需要动态获取skillReleaseDatas的大小
            while(index<skillReleaseDatas.size()){
                skillReleaseData = skillReleaseDatas.get(index);
                if(skillReleaseData.getCarrierSize() == 0){
                    battleContainer.battlePoolManager.restoreSkillReleaseData(skillReleaseData);
                    skillReleaseDatas.remove(skillReleaseData);
                }else{
                    //注意：update可能会增加SkillReleaseData数量
                    skillReleaseData.update(frameNum);
                    index++;
                }
            }
        }

        /**
         * 测试用
         */
        for(SkillReleaseData skillReleaseData : skillReleaseDatas){
            for(BasicSkillCarrier skillCarrier : skillReleaseData.skillCarriers){
                if(skillCarrier.triggerRole!= null && !skillCarrier.triggerRole.isUsing()){
                    int i = 1;
                }
            }
        }
    }

    /**
     * 获取新角色的基础信息
     * @return
     */
    public void getNewRoleBasics(List<BattleRoleBasic> roleBasicList){
        for(BattleRole battleRole : newRoles){
            //BattleRoleBasic roleBasic = new BattleRoleBasic();
            BattleRoleBasic roleBasic = (BattleRoleBasic) ProtocolPool.getInstance().borrowProtocol(BattleRoleBasic.code);
            roleBasic.setId(battleRole.getModelId());
            roleBasic.setType(battleRole.getRoleType());
            roleBasic.setRoleId(battleRole.getRoleId());
            roleBasic.setLevel(battleRole.getRoleLevel());
            roleBasic.setMaxBlood(battleRole.attributeManager.maxBlood);
            roleBasic.setMaxSoulEnergy(battleRole.attributeManager.maxSoulEnergy);

            roleBasicList.add(roleBasic);
        }
        newRoles.clear();
    }

    public void getSyncData( List<BattleRoleInfo> roleInfoList){
        if(player != null){
            BattleRoleInfo roleInfo = player.getSyncData();
            if(roleInfo != null){
                roleInfoList.add(roleInfo);
            }
        }
        if(roles.size() > 0){
            for(BattleRole role : roles){
                BattleRoleInfo roleInfo = role.getSyncData();
                if(roleInfo != null){
                    roleInfoList.add(roleInfo);
                }
            }
        }
    }

    public void getSyncHitInfo(List<BattleRoleHitInfo> hitInfoList){
        if(player != null){
            List<BattleRoleHitInfo> list = player.getSyncHitInfo();
            if(list != null){
                hitInfoList.addAll(list);
            }
        }
        if(roles.size() > 0){
            for(BattleRole role : roles){
                List<BattleRoleHitInfo> list = role.getSyncHitInfo();
                if(list != null) {
                    hitInfoList.addAll(list);
                }
            }
        }
    }

    public void getSyncCarrier(List<BattleSkillCarrierInfo> carrierInfoList){
        if(skillReleaseDatas.size() > 0){
            for(SkillReleaseData releaseData : skillReleaseDatas){
                releaseData.getSyncCarrier(carrierInfoList);
            }
        }

    }

    public void addSyncEffect(BattleSkillEffectInfo effectInfo){
        newEffects.add(effectInfo);
    }

    public void getSyncEffect(List<BattleSkillEffectInfo> effectInfoList){
        for(BattleSkillEffectInfo effectInfo : newEffects){
            effectInfoList.add(effectInfo);
        }
        newEffects.clear();
    }

    /**
     * 添加奖励
     * @param lootInfo
     */
    public void addSyncLoot(BattleRoleLootInfo lootInfo){
        newLootInfos.add(lootInfo);
    }

    /**
     * 收取奖励后需要消毁
     * @return
     */
    public void getSyncLoot(List<BattleRoleLootInfo> lootInfoList){

        if(newLootInfos.size() > 0){
            for(BattleRoleLootInfo lootInfo : newLootInfos){
                lootInfoList.add(lootInfo);
            }
        }
        newLootInfos.clear();
    }


    public void getSyncSkillBuff(List<BattleRoleSkillBuff> skillBuffList){

        if(player != null){
            List<BattleSkillBuffInfo> buffInfos = player.getBuffManager().getAllBuffInfo();
            if(buffInfos.size() > 0){
                BattleRoleSkillBuff roleSkillBuff = (BattleRoleSkillBuff)ProtocolPool.getInstance().borrowProtocol(BattleRoleSkillBuff.code);
                roleSkillBuff.setModelId(player.getModelId());
                roleSkillBuff.setBuffInfos(buffInfos);
                skillBuffList.add(roleSkillBuff);
            }
        }

        if(roles.size() > 0){
            for(BattleRole role : roles){
                List<BattleSkillBuffInfo> buffInfos = role.getBuffManager().getAllBuffInfo();
                if(buffInfos.size() > 0){
                    BattleRoleSkillBuff roleSkillBuff = (BattleRoleSkillBuff)ProtocolPool.getInstance().borrowProtocol(BattleRoleSkillBuff.code);
                    roleSkillBuff.setModelId(role.getModelId());
                    roleSkillBuff.setBuffInfos(buffInfos);
                    skillBuffList.add(roleSkillBuff);
                }
            }
        }

    }

    public void getSyncAiInfo(List<BattleAiInfo> aiInfoList) {

        if (player != null) {
            player.aiManager.getSyncAiInfo(aiInfoList);
        }

    }

    public void setAgentTarget(int idx,float[] targetPos){
        detourHandler.setAgentTarget(idx, targetPos);
    }

    public float getPositionYOnMap(float x,float z){
        return detourHandler.getPositionYOnMesh(x,z);
    }

    public boolean isPointOnMesh(float x,float z){
        return detourHandler.isPointOnMesh(x,z);
    }

    float[] soulCenterPos = new float[]{0,0,0};
    public float[] findSoulPosition(float[] startPos, float[] endPos, float skillDistance) {
        float distance = (float) Math.sqrt(Math.pow(startPos[0] - endPos[0], 2) + Math.pow(startPos[1] - endPos[1], 2));
        soulCenterPos[0] = endPos[0] - (endPos[0] - startPos[0]) * skillDistance * 2f / 3f / distance;
        soulCenterPos[2] = endPos[2] - (endPos[2] - startPos[2]) * skillDistance * 2f / 3f / distance;
        float[][] results = detourHandler.findSoulNearestPoly(soulCenterPos, skillDistance / 3f);
        int index = -1;
        int indexMin = -1;
        float maxDistance = 0;
        float minDistance = 0;
        for (int i = 0; i < 4; i++) {
            float[] result = results[i];
            if (result[1] == 1) {
                float startDistance = (float) Math.sqrt(Math.pow(startPos[0] - result[0], 2) + Math.pow(startPos[2] - result[2], 2));
                float endDistance = (float) Math.sqrt(Math.pow(endPos[0] - result[0], 2) + Math.pow(endPos[2] - result[2], 2));
                if (startDistance + endDistance > maxDistance) {
                    maxDistance = startDistance + endDistance;
                    index = i;
                }
                minDistance = minDistance == 0 ? startDistance + endDistance : maxDistance;
                if (startDistance + endDistance < minDistance) {
                    minDistance = startDistance + endDistance;
                    indexMin = i;
                }
            }
        }
        List<Integer> _Indexes = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (i != index && i != indexMin) {
                _Indexes.add(i);
            }
        }
        if (_Indexes.size() > 0) {
            return results[_Indexes.get(RandomUtil.randomBetween(0, _Indexes.size()))];
        }

        return null;
    }

    /**
     * 寻找玩家的下一个目标点，只有当地图中没有任何怪物存在时，调用此方法
     * 如果找不到下一个目标点，直接判断通过本地图，走向地图出口
     * @return
     */
    public void peekPlayerNextRoadPoint(float[] nextRoadPoint){
        if(tileConfig == null || tileConfig.roadDetailList == null || tileConfig.roadDetailList.size() == 0){
            nextRoadPoint[0] = 0;
            nextRoadPoint[1] = 0;
            nextRoadPoint[2] = 0;
            nextRoadPoint[3] = -1;
            return;
        }
        TileRoadDetail tileRoadDetail = tileConfig.roadDetailList.get(0);

        if(Math.abs(tileRoadDetail.getX()-player.currentPosition[0])<=0.5f && Math.abs(tileRoadDetail.getZ()-player.currentPosition[2])<=0.5f){
            tileConfig.roadDetailList.remove(0);
            if(tileConfig.roadDetailList.size() == 0){
                nextRoadPoint[0] = 0;
                nextRoadPoint[1] = 0;
                nextRoadPoint[2] = 0;
                nextRoadPoint[3] = -1;
                return;
            }
            tileRoadDetail = tileConfig.roadDetailList.get(0);
        }


        nextRoadPoint[0] = tileRoadDetail.getX();
        nextRoadPoint[1] = 0;
        nextRoadPoint[2] = tileRoadDetail.getZ();
        nextRoadPoint[3] = tileRoadDetail.getIndex();

    }

    /**
     * 寻找玩家的下一个目标点，只有当地图中没有任何怪物存在时，调用此方法
     * 如果找不到下一个目标点，直接判断通过本地图，走向地图出口
     * @return
     */
    public void pollPlayerNextRoadPoint(float[] nextRoadPoint){
        if(tileConfig == null || tileConfig.roadDetailList == null || tileConfig.roadDetailList.size() == 0){
            nextRoadPoint[0] = 0;
            nextRoadPoint[1] = 0;
            nextRoadPoint[2] = 0;
            nextRoadPoint[3] = -1;
            return;
        }
        TileRoadDetail tileRoadDetail = tileConfig.roadDetailList.get(0);
        tileConfig.roadDetailList.remove(0);

        nextRoadPoint[0] = tileRoadDetail.getX();
        nextRoadPoint[1] = 0;
        nextRoadPoint[2] = tileRoadDetail.getZ();
        nextRoadPoint[3] = tileRoadDetail.getIndex();

    }

    /**
     * 删除目标点
     * @param index
     */
    public void removePlayerNextRoadPointByIndex(int index){
        if(tileConfig == null || tileConfig.roadDetailList == null || tileConfig.roadDetailList.size() == 0){
            return;
        }
        for(TileRoadDetail tileRoadDetail : tileConfig.roadDetailList){
            if(tileRoadDetail.getIndex() == index){
                tileConfig.roadDetailList.remove(tileRoadDetail);
                return;
            }
        }
    }

    /**
     * 寻找离角色最近的其他角色
     * @param monster
     * @return
     */
    public BattleRole findNearestRole(BattleMonster monster){
        double targetDistance = 0;
        BattleRole target = null;
        for(BattleRole br : roles){
            if(br != monster){
                double distance = Math.sqrt(Math.pow(monster.getCurrentPosition()[0]-br.getCurrentPosition()[0],2)+Math.pow(monster.getCurrentPosition()[2]-br.getCurrentPosition()[2],2));
                if(distance <= (br.attributeManager.radius+monster.attributeManager.radius)){
                    if(targetDistance == 0 || distance < targetDistance){
                        targetDistance = distance;
                        target = br;
                    }
                    return br;
                }
            }
        }
        return target;
    }

    /**
     * 寻找离玩家最近的敌人
     * @return
     */
    public BattleRole findNearestEnemy(){
        double targetDistance = 0;
        BattleRole target = null;
        for(BattleRole battleRole : roles){
            byte state = battleRole.getState();
            if(battleRole != player && state != RoleStateConstant.ROLE_STATE_DEATH){
                double distance = getTwoRoleDistance(player,battleRole);
                if(targetDistance == 0 || distance < targetDistance){
                    targetDistance = distance;
                    target = battleRole;
                }
            }
        }
        return target;
    }

    /**
     * 寻找离指定敌人最近的其他敌人
     * @param enemyRole 指定敌人
     * @param arroundDistance 以指定敌人为圆心的搜索范围半径
     * @return
     */
    public BattleRole findNearestEnemyAroundEnemy(BattleRole enemyRole,float arroundDistance){
        double targetDistance = 0;
        BattleRole target = null;
        for(BattleRole battleRole : roles){
            if(battleRole != enemyRole && battleRole.getState() != RoleStateConstant.ROLE_STATE_DEATH){
                double distance = getTwoRoleDistance(enemyRole,battleRole);
                if(targetDistance == 0 || distance < targetDistance){
                    if(arroundDistance == 0 || distance < arroundDistance){
                        targetDistance = distance;
                        target = battleRole;
                    }
                }
            }
        }
        return target;
    }


    /**
     * 寻找血量最低的敌人
     * @return
     */
    public BattleRole findBloodLeastEnemy(){
        int blood = 0;
        BattleRole target = null;
        for(BattleRole battleRole : roles){
            byte state = battleRole.getState();
            if(battleRole != player && state != RoleStateConstant.ROLE_STATE_DEATH){
                if(blood == 0 || blood > battleRole.attributeManager.curBlood){
                    blood = battleRole.attributeManager.curBlood;
                    target = battleRole;
                }
            }
        }
        return target;
    }


    private int splitNum = 5;
    /**
     * 用于计算快速子弹是否击中角色
     * 该方法只是将getInPositionEnemy进行多次拆分，从而提升精确度
     * 当子弹超过一定速度还是会出现子弹穿过角色而被判没有击中的情况出现
     * 假设每帧时间为0.1s，拆分为5份，每份时间为0.02s，角色直径为0.6m,子弹速度就不宜大于30m/s
     * @param prePos
     * @param curPos
     * @return 忽略
     */
    public List<BattleRole> getCollisionRole(BasicSkillCarrier carrier,BattleRole ignoreRole,byte camp,byte shap, List<Integer> shapParam,float[] prePos,float[] curPos){
        List<BattleRole> hitRoles = new ArrayList<>();
        for(int i=1;i<=splitNum;i++){
            //己方子弹，只寻找敌方角色
            if(camp == BattleCampConstant.BATTLE_CAMP_OWN || camp == BattleCampConstant.BATTLE_CAMP_ALL){
                float[] rolePos = new float[2];
                float[] bulletPos = new float[2];
                for(BattleRole battleRole : roles){
                    if(ignoreRole != battleRole && battleRole.getState() != RoleStateConstant.ROLE_STATE_BORN && battleRole.getState() != RoleStateConstant.ROLE_STATE_DEATH && !hitRoles.contains(battleRole)){

                        float roleRadius = battleRole.attributeManager.radius;

                        float[] previousPos = battleRole.getPreviousPosition();
                        float[] currentPos = battleRole.getCurrentPosition();

                        rolePos[0] = previousPos[0] + (currentPos[0]-previousPos[0])*i/splitNum;
                        rolePos[1] = previousPos[2] + (currentPos[2]-previousPos[2])*i/splitNum;

                        bulletPos[0] = prePos[0] + (curPos[0]-prePos[0])*i/splitNum;
                        bulletPos[1] = prePos[2] + (curPos[2]-prePos[2])*i/splitNum;

                        if(isRoleInShap(shap,shapParam,bulletPos,rolePos,roleRadius,carrier.getDirection())){
                            hitRoles.add(battleRole);
                        }

                    }
                }
            }
            //敌方子弹，只寻找己方角色
            if(camp == BattleCampConstant.BATTLE_CAMP_ENEMY || camp == BattleCampConstant.BATTLE_CAMP_ALL){
                float[] rolePos = new float[2];
                float[] bulletPos = new float[2];

                if(!hitRoles.contains(player)){
                    float roleRadius = player.attributeManager.radius;

                    float[] previousPos = player.getPreviousPosition();
                    float[] currentPos = player.getCurrentPosition();

                    rolePos[0] = previousPos[0] + (currentPos[0]-previousPos[0])*i/splitNum;
                    rolePos[1] = previousPos[2] + (currentPos[2]-previousPos[2])*i/splitNum;

                    bulletPos[0] = prePos[0] + (curPos[0]-prePos[0])*i/splitNum;
                    bulletPos[1] = prePos[2] + (curPos[2]-prePos[2])*i/splitNum;

                    if(isRoleInShap(shap,shapParam,bulletPos,rolePos,roleRadius,carrier.getDirection())){
                        hitRoles.add(player);
                    }
                }

            }
        }

        return hitRoles;
    }

    public boolean isRoleInShap(byte shap, List<Integer> shapParam,float[] bulletPos,float[] rolePos,float roleRadius,short direction){

        if(shap == SkillHitShapConstant.SKILL_HIT_SHAP_POINT){
            float distance = getTwoPositionDistance(rolePos,bulletPos);
            if(distance < roleRadius){
                return true;
            }
        }
        if(shap == SkillHitShapConstant.SKILL_HIT_SHAP_CIRCLE){
            float distance = getTwoPositionDistance(rolePos,bulletPos);
            if(distance < roleRadius + shapParam.get(0)/100f){
                return true;
            }
        }
        if(shap == SkillHitShapConstant.SKILL_HIT_SHAP_RECTANGLE){
            float length = shapParam.get(0)/100f;
            float width = shapParam.get(1)/100f;
            float[] point = new float[]{rolePos[0]-bulletPos[0],rolePos[1]-bulletPos[1]};
            if(CalculationUtil.isPointInRectangle(width,length,direction,point)){
                return true;
            }
        }
        return false;
    }

    public List<BattleRole> getInShapRole(float[] shapPos,byte shap,float param1,float param2){
        List<BattleRole> results = new ArrayList<>();

        for(BattleRole battleRole : roles){
            float[] rolePos = battleRole.getCurrentPosition();
            if(shap == SkillHitShapConstant.SKILL_HIT_SHAP_CIRCLE) {
                if(CalculationUtil.isPointInCircle(param1, new float[]{rolePos[0] - shapPos[0], rolePos[2] - shapPos[2]})){
                    results.add(battleRole);
                }
            }
            if(shap == SkillHitShapConstant.SKILL_HIT_SHAP_RING) {
                float[] dir = new float[]{0f,0f};
                float[] point = new float[]{rolePos[0] - shapPos[0], rolePos[2] - shapPos[2]};
                if(CalculationUtil.isPointInSector(param1, param2,360f,dir,point)){
                    results.add(battleRole);
                }
            }
        }

        return results;
    }

    /**
     * 判断是否是合理的落座位置
     * 使用： 魂技选位
     */
    public boolean isValidSeatPoint(float x,float z){
        if(detourHandler.isPointOnMesh(x,z)){
            for(BattleRole role : roles){
                float distance = (float)Math.sqrt(Math.pow(role.currentPosition[0]-x,2)+Math.pow(role.currentPosition[2]-z,2));
                if(distance <= role.attributeManager.radius){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public float getTwoRoleDistance(BattleRole role1,BattleRole role2){
        return (float)Math.sqrt(Math.pow(role1.getCurrentPosition()[0]-role2.getCurrentPosition()[0],2)+Math.pow(role1.getCurrentPosition()[2]-role2.getCurrentPosition()[2],2));
    }

    public float getTwoPositionDistance(float[] pos1,float[] pos2){
        return (float)Math.sqrt(Math.pow(pos1[0]-pos2[0],2)+Math.pow(pos1[1]-pos2[1],2));
    }

    public float getRoleToPlayerDistance(BattleRole role){
        return getTwoRoleDistance(role,player);
    }

    public BattleRole getPlayer(){
        return player;
    }

    public Collection<BattleRole> getRoles(){
        return this.roles;
    }

    public void addWaitBornNpc(TileNpcDetail npcDetail){
        waitBornNpcs.add(npcDetail);
    }

    public boolean hasWaitBornNpc(){
        if(waitBornNpcs != null && waitBornNpcs.size() > 0){
            return true;
        }
        return false;
    }

    public boolean hasNpcOnMap(){
        if(tileConfig.groupConfigList.size() != 0 || (waitBornNpcs != null && waitBornNpcs.size() > 0)){
            return true;
        }
        return false;
    }

    public void addSkillReleaseData(SkillReleaseData skillReleaseData){
        if(skillReleaseData != null){
            skillReleaseDatas.add(skillReleaseData);
        }
    }

    public void removeDestroySkill(BasicRoleSkill roleSkill){
        if(skillReleaseDatas.size() > 0){
            for(SkillReleaseData releaseData : skillReleaseDatas){
                if(releaseData.roleSkill == roleSkill){
                    battleContainer.battlePoolManager.restoreSkillReleaseData(releaseData);
                    skillReleaseDatas.remove(roleSkill);
                    return;
                }
            }
        }
    }

    /**
     * 清空地图 均摊
     */
    public void clearMap(){

        while(roles.size() > 0){
            removeRoleFromMap(roles.get(0));
        }

    }
}
