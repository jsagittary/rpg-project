package com.dykj.rpg.battle.logic;

/**
 * 战斗逻辑处理类
 */

import com.dykj.rpg.battle.basic.BasicBattleLogic;
import com.dykj.rpg.battle.config.MapTileConfig;
import com.dykj.rpg.battle.constant.BattleStateConstant;
import com.dykj.rpg.battle.constant.BattleTypeConstant;
import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.battle.data.WaitUsePotionData;
import com.dykj.rpg.battle.detour.MapLoader;
import com.dykj.rpg.battle.ice.client.BattleCacheClient;
import com.dykj.rpg.battle.manager.BattlePoolManager;
import com.dykj.rpg.battle.manager.SuccessConditionManager;
import com.dykj.rpg.battle.manager.WaitHandlerDataManager;
import com.dykj.rpg.battle.role.BattlePlayer;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.battle.role.BattleRoleFactory;
import com.dykj.rpg.battle.util.RingBuffer;
import com.dykj.rpg.common.consts.ItemTypeEnum;
import com.dykj.rpg.net.core.UdpSession;
import com.dykj.rpg.net.core.UdpSessionManager;
import com.dykj.rpg.net.protocol.Protocol;
import com.dykj.rpg.protocol.battle.*;
import com.dykj.rpg.protocol.game2battle.*;
import com.dykj.rpg.protocol.item.ItemRs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class BattleContainer extends BattleObject{

    Logger logger = LoggerFactory.getLogger("BattleContainer");

    /**
     * 正常结束
     */
    public final static byte BATTLE_FINISH_TYPE_NORMAL = 1;
    /**
     * 手动退出
     */
    public final static byte BATTLE_FINISH_TYPE_MANUAL = 2;

    /**
     * 战斗对象池
     */
    public BattlePoolManager battlePoolManager = new BattlePoolManager();

    public byte battleType = 0;

    public int battleId = 0;

    public int maxNpcRounds = 0;

    public int curNpcRounds = 0;

    private int currentModelId = 0;

    public int monsterLevel = 0;
    /**
     * 战斗同步，先禁用
     */
    //public Battle battle;

    public List<Integer> playerIds = new ArrayList<>(10);

    public List<BattlePlayer> players = new ArrayList<>(10);

    public List<UdpSession> udpSessions = new ArrayList<>(10);

    /**
     * 关卡ID
     */
    public int misId = 0;

    /**
     * 地块数量
     */
    public int mapNum = 0;
    /**
     * 地图信息
     */
    public List<MapTileData> mapTileDataList = new ArrayList<>(10);
    /**
     * 寻路处理类 主角所在地图
     */
    private MapLogic[] mapLogics = new MapLogic[10];
    /**
     * 寻路处理类 主角所在地图的下一个地图
     */
    public byte centerMapIndex = 0;
    /**
     * 战斗状态
     */
    public byte state = BattleStateConstant.BATTLE_STATE_NULL;

    /**
     *
     */
    private long gameStartTime = 0;
    /**
     * 游戏进行的帧数
     */
    public int frameNum = 0;
    /**
     *
     */
    public int sycnFrameNum = 0;
    /**
     * 需要做同步的数据缓存
     */
    private RingBuffer<SyncFrameData> syncDataBuffer = new RingBuffer<>(10,SyncFrameData.class);

    /**
     * 战斗开始准备时间
     */
    public int startBattleDelayFrame = 0;

    public int finishBattleDelayFrame = -1;

    public byte finishType = 0;

    public boolean battleResult = false;

    /**
     * 战斗通关条件
     */
    public SuccessConditionManager successConditionManager = new SuccessConditionManager();

    /**
     * 战斗中途插入数据管理
     */
    public WaitHandlerDataManager waitHandlerDataManager = new WaitHandlerDataManager();

    public BasicBattleLogic battleLogic;

    public boolean isRobetBattle = false;

    public BattleContainer(){

    }

    @Override
    public void release(){
        battleType = 0;
        battleId = 0;
        maxNpcRounds = 0;
        curNpcRounds = 0;
        currentModelId = 0;

        monsterLevel = 0;

        playerIds.clear();
        players.clear();
        udpSessions.clear();

        mapNum = 0;
        mapTileDataList.clear();

        for(int i=0;i<mapLogics.length;i++){
            if(mapLogics[i] != null){
                MapLogicPool.restoreMapLogic(mapLogics[i]);
                mapLogics[i] = null;
            }
        }

        centerMapIndex = 0;
        state = BattleStateConstant.BATTLE_STATE_NULL;
        gameStartTime = 0;
        frameNum = 0;
        sycnFrameNum = 0;

        syncDataBuffer.clear();

        startBattleDelayFrame = 0;
        finishBattleDelayFrame = -1;
        finishType = 0;
        battleResult = false;
        isRobetBattle = false;

        successConditionManager.release();
        waitHandlerDataManager.release();

        battlePoolManager.release();

    }

    @Override
    public void selfCheak() {

    }

    public void initBattle(int battleId, int levelId){
        this.battleId = battleId;
        this.misId = levelId;
        /**
         * 初始化关卡地图信息
         */
        int[] params = MapLoader.getInstance().getBattleMapInfoByLevelId(levelId,mapTileDataList);

        if(params == null || params.length < 2){
            return ;
        }

        this.battleType = (byte)params[0];
        this.maxNpcRounds = params[1];

        this.monsterLevel = MapLoader.getInstance().getMonsterLevelByLevelId(levelId);

        /**
         * 初始化关卡胜利条件
         */
        this.successConditionManager.init(battleType,levelId);

        if(mapTileDataList != null && mapTileDataList.size() != 0){
            //加载地图网格和信息
            mapNum = mapTileDataList.size();
            for(int i=0;i<mapNum;i++){
                MapTileConfig tileConfig = MapLoader.getInstance().getTileConfigByLevelIdAndIndex(levelId,i);
                MapLogic mapLogic = MapLogicPool.borrowMapLogic(mapTileDataList.get(i).id);
                mapLogic.init(this,mapTileDataList.get(i),tileConfig);
                mapLogics[i] = mapLogic;
            }


            if(battleType == BattleTypeConstant.BATTLE_TYPE_GENERAL){
                this.battleLogic = new BattleGeneralLogic(this);
            }
            if(battleType == BattleTypeConstant.BATTLE_TYPE_BASE_GUARD){
                float[] startPos = mapLogics[0].getFirstMapPlayerStartPos();
                this.battleLogic = new BattleGuardLogic(this,startPos[0],startPos[1]);
            }

        }else{
            System.out.println("缺少关卡配置文件");
        }

        this.waitHandlerDataManager.init(this);

    }

    /**
     * 正式使用
     * @param requestList
     */
    public void initBattlePlayers(PlayerEnterBattleRequestList requestList){

        List<PlayerEnterBattleRequest> requests = requestList.getRequests();
        for(PlayerEnterBattleRequest request : requests){
            int playerId = request.getPlayerId();
            this.playerIds.add(playerId);

            UdpSession session = UdpSessionManager.getInstance().getSessionByPlayerId(playerId);
            this.udpSessions.add(session);

            EnterBattleRoleInfo roleInfo = request.getRoleInfo();
            if(roleInfo == null){
                return;
            }
            //roleId需要从数据库获取
            int roleId = roleInfo.getRoleId(); //对应characterId
            int roleLevel = roleInfo.getRoleLevel();

            BattlePlayer player = BattleRoleFactory.createBattlePlayer(this,playerId,(byte)0,roleId,roleLevel);
            player.soulId = request.getSoulId();
            player.setFreeState();

            //将角色装备，升级等对属性的修改覆盖默认属性
            List<AttributeInfo> attributeInfos = roleInfo.getAttributeInfos();
            for(AttributeInfo attributeInfo : attributeInfos){
                player.buffManager.addRoleAttribute(attributeInfo.getId(),attributeInfo.getAttributeType(),attributeInfo.getNum());
            }

            //初始化默认属性
            player.buffManager.initDefaultAttribute();

            //装配技能
            List<EnterBattleSkillInfo> skillInfos = request.getSkillInfos();
            List<Integer> skillIds = new ArrayList<>();
            String skillStr = "角色装配的技能列表 [";
            for(EnterBattleSkillInfo skillInfo : skillInfos){
                skillIds.add(skillInfo.getSkillId());
                player.skillManager.installRoleSkill(skillInfo);
                skillStr += skillInfo.getSkillId()+",";
            }
            skillStr += "]";
            System.out.println(skillStr);

            //装配药水
            List<ItemRs> potionList = request.getItems();
            if(potionList != null && potionList.size() > 0){
                for(ItemRs itemRs : potionList){
                    player.skillManager.installPotionSkill(itemRs);
                }
            }

            //装配AI
            List<AiBattleRs> aiList = request.getAiBattles();
            for(AiBattleRs aiBattleRs : aiList){
                player.aiManager.installAi(aiBattleRs.getAiId(),aiBattleRs.getAiConditionParam(),aiBattleRs.getAiActionParam());
            }

            players.add(player);
            //添加玩家待处理数据
            waitHandlerDataManager.addPlayer(playerId);

            //设置角色的初始位置
            float[] startPos = mapLogics[0].getFirstMapPlayerStartPos();
            mapLogics[0].playerEnterMap(player,startPos[0],startPos[1]);
            centerMapIndex = 0;
        }

    }

    /**
     * 测试使用
     * @param playerIds
     */
    public void initBattlePlayers(List<Integer> playerIds){

        for(int playerId : playerIds){

            this.playerIds.add(playerId);

            UdpSession session = UdpSessionManager.getInstance().getSessionByPlayerId(playerId);
            this.udpSessions.add(session);

            //roleId需要从数据库获取
            int roleId = 2; //对应characterId
            int roleLevel = 1;

            BattlePlayer player = BattleRoleFactory.createBattlePlayer(this,playerId,(byte)0,roleId,roleLevel);
            player.soulId = 2;
            player.setFreeState();
            player.setCurrentPosition(0,0);

            //初始化默认属性
            player.buffManager.initDefaultAttribute();

            //装配技能
            if(GameConstant.TEST_SKILLS!=null && !GameConstant.TEST_SKILLS.trim().equals("")){
                String[] skills = GameConstant.TEST_SKILLS.split(",");
                if(skills.length > 0){
                    for(String skillStr : skills){
                        String[] infos = skillStr.split(":");
                        if(infos.length == 2){
                            player.skillManager.installRoleSkill(Integer.parseInt(infos[0]),Integer.parseInt(infos[1]));
                        }
                    }
                }
            }

            //装配药水
            if(GameConstant.TEST_POTIONS!=null && !GameConstant.TEST_POTIONS.trim().equals("")){
                String[] potions = GameConstant.TEST_POTIONS.split(",");
                if(potions.length > 0){
                    for(String potionStr : potions){
                        String[] infos = potionStr.split(":");
                        if(infos.length == 2){
                            player.skillManager.installPotionSkill(Integer.parseInt(infos[0]),Integer.parseInt(infos[1]));
                        }
                    }
                }
            }

            //装配AI
            if(GameConstant.TEST_AIS!=null && !GameConstant.TEST_AIS.trim().equals("")){
                String[] ais = GameConstant.TEST_AIS.split(",");
                if(ais.length > 0){
                    for(String aiId : ais){
                        player.aiManager.installAi(Integer.parseInt(aiId));
                    }

                }
            }

            players.add(player);
            //添加玩家待处理数据
            waitHandlerDataManager.addPlayer(playerId);

            //设置角色的初始位置
            float[] startPos = mapLogics[0].getFirstMapPlayerStartPos();
            mapLogics[0].playerEnterMap(player,startPos[0],startPos[1]);
            centerMapIndex = 0;
        }
    }

    /**
     * 初始化随机战斗数据 随机地图+创建主角
     * @param battleType 战斗类型
     * @param playerIds 进入战斗的玩家角色信息 为后期的群战做准备
     */
    public List<MapTileData> initRandomBattle(byte battleType, int battleId, List<Integer> playerIds){
        this.battleType = battleType;
        this.battleId = battleId;
        this.playerIds = playerIds;

        //先进行随机地图
        mapNum = 5;
        if(createRandomMap(mapNum)){
            //加载地图网格和信息
            for(int i=0;i<mapNum;i++){
                MapLogic mapLogic = MapLogicPool.borrowMapLogic(mapTileDataList.get(i).id);
                mapLogic.init(this,mapTileDataList.get(i),null);
                mapLogics[i] = mapLogic;
            }
        }

        //Battle battle = new Battle();
        //this.battle = battle;

        for(int playerId : playerIds){

            /**
             * TODO 此处需要通过 playerId 从数据库获取角色的基本信息并赋值给 RoleBasicModel
             */
            int roleId = playerId; //roleId需要从数据库获取

            roleId = 1; //对应characterId
            int roleLevel = 1;

            BattlePlayer player = BattleRoleFactory.createBattlePlayer(this,playerId,(byte)0,roleId,roleLevel);
            player.setFreeState();
            player.setCurrentPosition(0,0);
            players.add(player);

        }
        BattlePlayer player = players.get(0);
        mapLogics[0].playerEnterMap(player,0,0);
        centerMapIndex = 0;

        return mapTileDataList;

    }

    /**
     * 将角色切换到新的地块中
     * @param battleRole
     * @param positionOffset
     * @return
     */
    public boolean roleJumpToOtherMap(BattleRole battleRole,float[] positionOffset){
        System.out.println("------roleJumpToOtherMap------");
        if(battleRole.mapIndex + 1 < mapTileDataList.size()){

            mapLogics[battleRole.mapIndex].playerExitMap();
            mapLogics[battleRole.mapIndex+1].playerEnterMap(battleRole,positionOffset[0],positionOffset[1]);

            /**
             * 单人战斗时，直接将主地图切换到当前角色所在地图
             */
            centerMapIndex = battleRole.mapIndex;

            return true;
        }else{
            return false;
        }
    }

    /**
     * 创建随机地图
     * @param mapNum
     * @return
     */
    public boolean createRandomMap(int mapNum){
//        battleMapInfos = MapLoader.getInstance().createRandomMap(mapNum);
//        if(battleMapInfos.size() == mapNum){
//            System.out.println(Arrays.toString(battleMapInfos.toArray()));
//            return true;
//        }else{
            return false;
//        }
    }

    /**
     * 战斗启动数据处理
     */
    public void startBattle(){
        System.out.println("---------------startBattle--------------");
        this.state = BattleStateConstant.BATTLE_STATE_INIT;
    }

    /**
     * 战斗暂停数据处理
     */
    public boolean pauseBattle(){
        if(battleType == BattleTypeConstant.BATTLE_TYPE_GENERAL){
            this.state = BattleStateConstant.BATTLE_STATE_PAUSE;
            return true;
        }
        return false;
    }

    /**
     * 战斗重启数据处理
     */
    public boolean resumeBattle(){
        if(battleType == BattleTypeConstant.BATTLE_TYPE_GENERAL){
            this.state = BattleStateConstant.BATTLE_STATE_RUNNING;
            return true;
        }
        return false;
    }

    /**
     * 战斗完成数据处理
     * finishType 结束类型 1=正常退出[需要给游戏服推送结算信息]  2=手动退出[不需给游戏服推送结算信息]
     * delayFrame 延迟关闭战斗数据通道时间，用于清空需要发送给客户端的缓存数据，防止客户端收不到结束响应
     */
    public void finishBattle(byte finishType,boolean battleResult,int delayFrame){
        logger.info("finishBattle finishType = "+finishType+" battleResult = "+battleResult+" delayFrame = "+delayFrame);
        if(this.state < BattleStateConstant.BATTLE_STATE_FINISH){
            //System.out.println("finishBattle !!! total time = "+(System.currentTimeMillis()-gameStartTime));
            //暂停所有地图
            for(int i=0;i<mapNum;i++){
                mapLogics[i].stopRunMap();
            }

            this.finishBattleDelayFrame = delayFrame;
            this.finishType = finishType;
            this.battleResult = battleResult;
            this.state = BattleStateConstant.BATTLE_STATE_FINISH;
        }
    }

    public int createNewModelId(){
        return ++currentModelId;
    }

    /**
     * 获取正在战斗的所有角色信息
     */
    public List<BattleRole> getValidBattleRoles(){
        List<BattleRole> battleRoles = new ArrayList<>();
        //战前准备时间
        //if(this.state == BattleStateConstant.BATTLE_STATE_INIT){
            for(BattleRole player : players){
                battleRoles.add(player);
            }
        //}

        if(this.state == BattleStateConstant.BATTLE_STATE_RUNNING) {

            for(int i= centerMapIndex-1;i<=centerMapIndex;i++){
                if(i>=0){
                    List<BattleRole> list = this.mapLogics[i].getBattleRoles();
                    if(list != null && list.size() > 0){
                        for(BattleRole role : list){
                            battleRoles.add(role);
                        }
                    }
                }
            }

        }

        return battleRoles;
    }

    /**
     * 服务器模拟跑帧，提前0.5帧做计算
     */
    public void runFrame(long runFrameTime){

        if(this.state == BattleStateConstant.BATTLE_STATE_REMOVE || this.state == BattleStateConstant.BATTLE_STATE_DESTROY){
            return;
        }

        if(this.state != BattleStateConstant.BATTLE_STATE_FINISH){
            //先做一次胜利条件判断
            byte result = successConditionManager.addTimeInfo();

            if(result == SuccessConditionManager.SUCCESS_CONDITION_RESULT_FAIL){
                logger.info("condition result fail !!!");
                finishBattle(BATTLE_FINISH_TYPE_NORMAL,false,10);
                //return;
            }

            if(result == SuccessConditionManager.SUCCESS_CONDITION_RESULT_SUCCESS){
                logger.info("condition result success !!!");
                finishBattle(BATTLE_FINISH_TYPE_NORMAL,true,10);
                //return;
            }
        }

        if(this.state == BattleStateConstant.BATTLE_STATE_FINISH){
            finishBattleDelayFrame --;
        }

        if((sycnFrameNum+2) < frameNum){ // 做两帧容错
            //同步数据卡顿，先做等待数据同步
            //logger.error("server synchronized data is stuck !!! battleId = "+battleId+"  sycnFrameNum = "+sycnFrameNum+"  frameNum = "+frameNum);
            return;
        }

        //战前准备时间
        if(this.state == BattleStateConstant.BATTLE_STATE_INIT){
            this.startBattleDelayFrame -= 1;
            if(this.startBattleDelayFrame <= 0){
                this.state = BattleStateConstant.BATTLE_STATE_RUNNING;
            }
        }
        if(this.state == BattleStateConstant.BATTLE_STATE_RUNNING) {
            for(int i= centerMapIndex;i>=centerMapIndex-1;i--){
                if(i>=0){
                    this.mapLogics[i].runFrame(frameNum+1);
                }
            }
            createSyncData(runFrameTime);

            frameNum ++ ;
        }

        //做一次缓存对象自查
        battlePoolManager.poolSelfCheck();

    }

    /**
     * 生产发往客户端的战斗数据
     */
    public void createSyncData(long runFrameTime){
        //System.out.println("createSyncData frameNum = "+frameNum+" time = "+System.currentTimeMillis());
        try{

            if(sycnFrameNum < frameNum && this.state == BattleStateConstant.BATTLE_STATE_RUNNING){

                SyncFrameData syncFrameData = new SyncFrameData();

                syncFrameData.frameNum = frameNum;
                syncFrameData.runFrameTime = runFrameTime;

                for(int i= centerMapIndex-1;i<=centerMapIndex;i++){
                    if(i >= 0){
                        this.mapLogics[i].getNewRoleBasics(syncFrameData.roleBasicList);
                        this.mapLogics[i].getSyncData(syncFrameData.roleInfoList);
                        this.mapLogics[i].getSyncEffect(syncFrameData.skillEffectList);
                        this.mapLogics[i].getSyncCarrier(syncFrameData.skillCarrierList);
                        this.mapLogics[i].getSyncLoot(syncFrameData.roleLootInfoList);
                        this.mapLogics[i].getSyncHitInfo(syncFrameData.roleHitInfoList);
                        this.mapLogics[i].getSyncSkillBuff(syncFrameData.roleSkillBuffList);
                        this.mapLogics[i].getSyncAiInfo(syncFrameData.roleAiInfoList);
                    }
                }

                syncDataBuffer.add(syncFrameData);
            }
            //System.out.println("createSyncDate finish frameNum = "+frameNum+" time = "+System.currentTimeMillis());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 战斗数据同步到客户端
     */
    public void update(long updateTime){
        if(frameNum == 0){
            return;
        }

        try{
            SyncFrameData syncFrameData = syncDataBuffer.peek();
            if(syncFrameData != null && updateTime-syncFrameData.runFrameTime >= GameConstant.FRAME_TIME){

                //long now = System.currentTimeMillis();
                //System.out.println("BattleContainer update interval time = "+(now-preUpdateTime));
                //preUpdateTime = now;

                syncDataBuffer.poll();

                for(UdpSession udpSession : udpSessions){
                    syncFrameData.sendSyncData(udpSession);
                }
                sycnFrameNum = syncFrameData.frameNum;
            }

            if(this.state == BattleStateConstant.BATTLE_STATE_FINISH){
                //logger.info("finishBattleDelayFrame = "+finishBattleDelayFrame);
                if(this.finishBattleDelayFrame <= 0){
                    BattleFinishResultRs response = new BattleFinishResultRs();
                    response.setResult(battleResult);
                    //response.setConditions(successConditionManager.getShowConditions());
                    logger.info(response.toString());
                    sendBattleData(response);

                    if(!isRobetBattle && finishType == BATTLE_FINISH_TYPE_NORMAL){
                        /**
                         * 将战斗结果回传给游戏服务器
                         */
                        BattleFinishResultResponse finishResultResponse = new BattleFinishResultResponse();
                        finishResultResponse.setBattleId(battleId);
                        List<BattleFinishPersonalResultResponse> personalResultResponses = finishResultResponse.getResults();
                        for(int playerId : playerIds){
                            BattleFinishPersonalResultResponse personalResultResponse = new BattleFinishPersonalResultResponse();
                            personalResultResponse.setResult(battleResult);
                            personalResultResponse.setPlayerId(playerId);

                            BattlePlayer player = getPlayerById(playerId);
                            List<ItemRs> awardRsList = player.getTotalLootInfoList();
                            if(battleResult){
                                //获得的掉落物品信息
                                personalResultResponse.setAwards(player.getTotalLootInfoList());

                            }

                            List<ItemRs> itemRsList = personalResultResponse.getItems();
                            //消耗药水的信息
                            player.skillManager.sendPotionInfoForGameServer(itemRsList);

                            personalResultResponse.setConditions(successConditionManager.getShowConditions());

                            personalResultResponses.add(personalResultResponse);

                        }

                        logger.info(finishResultResponse.toString());

                        BattleCacheClient.getInstance().battleFinishResult(battleId,finishResultResponse);
                    }

                    this.state = BattleStateConstant.BATTLE_STATE_REMOVE;
                }
            }
            //System.out.println("update finish frameNum = "+sycnFrameNum+" time = "+System.currentTimeMillis());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendBattleData(Protocol protocol){
        for(UdpSession udpSession : udpSessions){
            if(udpSession != null && udpSession.kcpHandler != null) {
                udpSession.write(protocol);
            }
        }
    }

    public byte getCenterMapIndex() {
        return centerMapIndex;
    }

    public BattlePlayer getPlayerById(int playerId){
        for(BattlePlayer player : players){
            if(player.getPlayerId() == playerId){
                return player;
            }
        }
        return null;
    }
}
