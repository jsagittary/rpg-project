package com.dykj.rpg.battle.role;

import com.dykj.rpg.battle.ai.AiTrigger;
import com.dykj.rpg.battle.attribute.RoleAttributes;
import com.dykj.rpg.battle.basic.BasicRoleSkill;
import com.dykj.rpg.battle.basic.BasicSkillCarrier;
import com.dykj.rpg.battle.config.TileNpcDetail;
import com.dykj.rpg.battle.constant.*;
import com.dykj.rpg.battle.dictionary.StaticDictionary;
import com.dykj.rpg.battle.logic.BattleContainer;
import com.dykj.rpg.battle.logic.BattleObject;
import com.dykj.rpg.battle.logic.MapLogic;
import com.dykj.rpg.battle.logic.ParseLoot;
import com.dykj.rpg.battle.manager.*;
import com.dykj.rpg.battle.pool.StaticPositionRingPool;
import com.dykj.rpg.battle.util.CalculationUtil;
import com.dykj.rpg.common.config.model.LootNpcModel;
import com.dykj.rpg.net.core.UdpSession;
import com.dykj.rpg.net.core.UdpSessionManager;
import com.dykj.rpg.net.protocol.Protocol;
import com.dykj.rpg.net.protocol.ProtocolPool;
import com.dykj.rpg.protocol.battle.BattlePosition;
import com.dykj.rpg.protocol.battle.BattleRoleHitInfo;
import com.dykj.rpg.protocol.battle.BattleRoleInfo;
import com.dykj.rpg.protocol.battle.BattleRoleLootInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BattleRole extends BattleObject{

    Logger logger = LoggerFactory.getLogger("BattleRole");
    /**
     *  玩家ID, 在BattlePlayer中有效
     */
    protected int playerId;
    /**
     *  每个战斗角色在战斗中的唯一ID
     */
    protected int modelId;
    /**
     * 角色在地图中对应的ID
     */
    protected int caIdx;
    /**
     * buff在角色身上的唯一ID
     */
    private int buffGuid;
    /**
     *
     */
    protected BattleContainer battleContainer;
    /**
     *
     */
    public MapLogic mapLogic;
    /**
     *
     */
    public byte mapIndex;

    /**
     * 怪物出生标签
     */
    public TileNpcDetail tileNpcDetail;

    public float[] currentPosition = new float[3];

    /**
     * 上一帧的坐标点，用于碰撞判断
     */
    protected float[] previousPosition = new float[3];

    protected short previousDirection = 0;

    protected float[] currentDir = new float[3];

    protected short direction = 0;

    protected float[] targetPosition  = new float[3];

    public BattleRole targetRole;

    private int clientShowTargetRoleId = 0;

    protected StaticPositionRingPool findPathResults = new StaticPositionRingPool(100);

    protected float[] directionSpeeds = new float[3];

    protected StaticPositionRingPool findCrowdResults = new StaticPositionRingPool(100);

    /**
     * 角色掉落物品信息
     */
    protected BattleRoleLootInfo roleLootInfo;

    /**
     * 角色类型
     */
    protected byte roleType ;
    /**
     * 角色技能消耗的资源类型
     */
    public int skillSourceType;
    /**
     * 模型id
     */
    protected int roleId;
    /**
     * 模型等级
     */
    protected int roleLevel;
    /**
     * 当前寻路类型
     */
    protected byte detourType = DetourTypeConstant.DETOUR_TYPE_NULL;
    /**
     * 当前角色的移动方式
     */
    public byte moveType = RoleMoveTypeConstant.ROLE_MOVE_TYPE_WAIT;
    /**
     * 角色行动限制集合
     * key 为限制类型
     */
    protected Map<Byte,List<Integer>> moveLimitMap = new ConcurrentHashMap<>();
    /**
     *  寻路是否结束
     */
    protected boolean runFinish = true;
    /**
     * 角色出生时的帧数
     */
    protected int createFrameNum = 0;
    /**
     * 当前游戏运行的帧数
     */
    protected int frameNum = 0;

    /**
     * 阵营
     */
    protected byte camp;

    /**
     * 最大产生硬直的受击次数
     */
    protected int maxHitTimesStun;

    /**
     * 产生硬直的受击次数
     */
    protected int curHitTimes;

    /**
     * 最大硬直时长
     */
    protected int maxStunTime;
    /**
     * 当前剩余硬直时间
     */
    protected int curStunTime;

    /**
     * 当前释放的技能ID
     */
    protected int skillId = 0;
    /**
     * 当前释放的技能动作时间
     */
    protected int skillAnimTime = 0;

    /**
     * 对象在当前帧的伤害列表
     */
    protected List<BattleRoleHitInfo> roleHitInfos = new ArrayList<>();

    /**
     * 属性管理器
     */
    public AttributeManager attributeManager;
    /**
     * 状态管理器
     */
    public StateManager stateManager;

    /**
     * buff管理器
     */
    public BuffManager buffManager;

    /**
     * 动作管理器
     */
    public AnimManager animManager;

    /**
     * 技能管理器
     */
    public SkillManager skillManager;

    /**
     * 技巧管理器
     */
    public AiManager aiManager;

    public BattleRole(){
        /**
         * 初始化角色属性管理器，注意：attributeManager初始化必须在buffManager之前
         */
        attributeManager = new AttributeManager(this);
        /**
         * 初始化状态管理器
         */
        stateManager = new StateManager(this);
        /**
         * 初始化buff管理器
         */
        buffManager = new BuffManager(this);
        /**
         * 初始化动作管理器
         */
        animManager = new AnimManager(this);
        /**
         * 初始化技能管理器
         */
        skillManager = new SkillManager(this);
        /**
         * 初始化技巧管理器
         */
        aiManager = new AiManager(this);
    }

    /**
     *
     * @param battleContainer
     * @param roleAttributes
     */
    public void init(BattleContainer battleContainer, TileNpcDetail tileNpcDetail,int playerId,int skillSourceType,byte roleType,byte camp,int roleId,int roleLevel,int modelId,RoleAttributes roleAttributes){
        this.battleContainer = battleContainer;
        this.tileNpcDetail = tileNpcDetail;

        this.playerId = playerId;
        this.roleType = roleType;
        this.skillSourceType = skillSourceType;
        this.camp = camp;
        this.roleId = roleId;
        this.roleLevel = roleLevel;
        this.modelId = modelId;

        //this.roleBattleModel = roleBattleModel;
        //this.rolePositionModel = rolePositionModel;

        /**
         * 初始化角色属性管理器，注意：attributeManager初始化必须在buffManager之前
         */
        attributeManager.init(roleAttributes);
        /**
         * 初始化状态管理器
         */
        stateManager.init();
        /**
         * 初始化buff管理器
         */
        buffManager.init(battleContainer);
        /**
         * 初始化动作管理器
         */
        animManager.init();
        /**
         * 初始化技能管理器
         */
        skillManager.init(battleContainer);
        /**
         * 初始化技巧管理器
         */
        aiManager.init(battleContainer);

    }

    @Override
    public void release() {

        stateManager.release();

        buffManager.release();

        skillManager.release();

        aiManager.release();


        playerId = 0;
        modelId = 0;
        caIdx = -1;
        buffGuid = 0;

        battleContainer = null;
        mapLogic = null;
        mapIndex = 0;

        tileNpcDetail = null;

        //currentPosition = new float[3];
        //previousPosition = new float[3];
        //currentDir = new float[3];
        direction = 0;
        //targetPosition  = new float[3];

        targetRole = null;

        clientShowTargetRoleId = 0;

        findPathResults.clear();

        //directionSpeeds = new float[3];

        findCrowdResults.clear();

        roleLootInfo = null;

        roleType = 0;
        roleId = 0;
        roleLevel = 0;
        detourType = DetourTypeConstant.DETOUR_TYPE_NULL;
        moveType = RoleMoveTypeConstant.ROLE_MOVE_TYPE_WAIT;
        moveLimitMap.clear();
        runFinish = true;

        createFrameNum = 0;
        frameNum = 0;
        camp = 0;

        maxHitTimesStun = 0;
        curHitTimes = 0;
        maxStunTime = 0;
        curStunTime = 0;

        skillId = 0;
        skillAnimTime = 0;

        roleHitInfos.clear();

    }

    @Override
    public void selfCheak() {
        if(targetRole != null && targetRole.getState() == RoleStateConstant.ROLE_STATE_DEATH){
            targetRole = null;
            setFreeState();
        }
    }

    /**
     *
     * @param mapLogic
     */
    public void setMapLogic(MapLogic mapLogic){
        this.mapLogic = mapLogic;
        this.mapIndex = mapLogic.getMapIndex();
        //this.roleBattleModel.setMapIndex(mapLogic.getMapIndex());
        //setCurrentPosition(rolePositionModel.getPosX()/100f,rolePositionModel.getPosZ()/100f);

        BattleRole player = mapLogic.getPlayer();
        if(player != null){
            float[] playerPos = player.getCurrentPosition();
            //setCurrentDir((short)(CalculationUtil.vectorToAngle(playerPos[0]-currentPosition[0],playerPos[2]-currentPosition[2])));
        }

        //为技能更新mapLogic，由于载体交给了mapLogic管理，载体的实时更新在mapLogic中进行
        skillManager.setMapLogic(mapLogic);

    }

    public MapLogic getMapLogic(){
        return this.mapLogic;
    }

    public int getModelId(){
        return this.modelId;
    }

    public void setCurrentPosition(float[] currentPosition){

        this.previousPosition[0] = this.currentPosition[0];
        this.previousPosition[2] = this.currentPosition[2];

        this.currentPosition[0] = currentPosition[0];
        this.currentPosition[2] = currentPosition[2];

    }

    public void setCurrentPosition(float posX,float posZ){

        this.previousPosition[0] = this.currentPosition[0];
        this.previousPosition[2] = this.currentPosition[2];

        this.currentPosition[0] = posX;
        this.currentPosition[2] = posZ;

    }

    public void setCurrentDir(float[] currentDir){
        this.currentDir[0] = currentDir[0];
        this.currentDir[1] = currentDir[1];
        this.currentDir[2] = currentDir[2];

        this.direction = (short)CalculationUtil.vectorToAngle(this.currentDir[0],this.currentDir[2]);

    }

    public void setCurrentDir(float dirX,float dirZ){
        this.currentDir[0] = dirX;
        this.currentDir[1] = 0;
        this.currentDir[2] = dirZ;

        this.direction = (short)CalculationUtil.vectorToAngle(this.currentDir[0],this.currentDir[2]);

    }

    public void setCurrentDir(short direction){
        this.direction = direction;

    }

    public void setTargetPosition(float[] targetPosition,byte targetType){
        this.targetPosition[0] = targetPosition[0];
        this.targetPosition[1] = targetPosition[1];
        this.targetPosition[2] = targetPosition[2];

//        this.roleBattleModel.setTargetType(targetType);
//        this.roleBattleModel.setTargetPosX((int)(this.targetPosition[0]*100));
//        this.roleBattleModel.setTargetPosY((int)(this.targetPosition[1]*100));
//        this.roleBattleModel.setTargetPosZ((int)(this.targetPosition[2]*100));

    }

    public void setTargetPosition(float posX,float posZ,byte targetType){
        this.targetPosition[0] = posX;
        this.targetPosition[1] = mapLogic.getPositionYOnMap(posX,posZ);
        this.targetPosition[2] = posZ;

//        this.roleBattleModel.setTargetType(targetType);
//        this.roleBattleModel.setTargetPosX((int)(this.targetPosition[0]*100));
//        this.roleBattleModel.setTargetPosY((int)(this.targetPosition[1]*100));
//        this.roleBattleModel.setTargetPosZ((int)(this.targetPosition[2]*100));

    }

    /**
     * 以移动的角色坐标为目标点，用于群体寻路（群体寻路时，边缘可能无法寻路，产生角色卡死不动）
     * @param targetRole
     */
    public void setTargetRole(BattleRole targetRole){
        //this.roleBattleModel.setTargetType(RoleTargetTypeConstant.ROLE_TARGET_TYPE_ROLE);
        //this.roleBattleModel.setTargetId(targetRole.modelId);
        this.targetRole = targetRole;
        clientShowTargetRoleId = targetRole.getModelId();
    }

    public void setDirection(short _direction){
        this.previousDirection = this.direction;
        this.direction = _direction;
    }

    public void clearClientShowTargetRoleId(int targetId){
        if(targetId == clientShowTargetRoleId){
            clientShowTargetRoleId = 0;
        }
    }

    /**
     * 设置离开地图的坐标点，用于线路寻路
     * @param posX
     * @param posZ
     */
    public void setExitTargetPosition(float posX,float posZ){
        //1.设置目的地
        setTargetPosition(posX,posZ,RoleTargetTypeConstant.ROLE_TARGET_TYPE_POSITION);
        //2.进行一次最短距离寻路
        mapLogic.findStraightPath(this);
        //3.将寻路方式修改为路径寻路
        detourType = DetourTypeConstant.DETOUR_TYPE_PATH_POSITION;
        stateManager.addPurpos(RolePurposConstant.ROLE_PURPOS_FIND_EXIT);
    }

    /**
     * 寻路方式一
     * 对目标坐标点做路径寻路
     * @param pos
     */
    public void setPathTargetPosition(float[] pos){
        //1.设置目的地
        setTargetPosition(pos[0],pos[2],RoleTargetTypeConstant.ROLE_TARGET_TYPE_POSITION);
        //2.进行一次最短距离寻路
        mapLogic.findStraightPath(this);
        //3.将寻路方式修改为路径寻路
        detourType = DetourTypeConstant.DETOUR_TYPE_PATH_POSITION;
        stateManager.addPurpos(RolePurposConstant.ROLE_PURPOS_FIND_POSITION);
    }

    /**
     * 寻路方式二
     * 对目标角色做路径寻路
     * @param role
     */
    public void setPathTargetRole(BattleRole role){
        //1.设置目的地
        setTargetRole(role);
        setTargetPosition(role.getCurrentPosition()[0],role.getCurrentPosition()[2],RoleTargetTypeConstant.ROLE_TARGET_TYPE_POSITION);
        //2.进行一次最短距离寻路
        mapLogic.findStraightPath(this);
        //2.将寻路方式修改为群体寻路
        detourType = DetourTypeConstant.DETOUR_TYPE_PATH_ROLE;
        stateManager.addPurpos(RolePurposConstant.ROLE_PURPOS_FIND_OBJECT);
    }

    /**
     * 寻路方式三
     * 对目标坐标点做群体寻路
     * @param pos
     */
    public void setCrowdTargetPosition(float[] pos){
        //1.清空缓存,设置目的地
        findCrowdResults.clear();
        setTargetPosition(pos[0],pos[2],RoleTargetTypeConstant.ROLE_TARGET_TYPE_POSITION);
        mapLogic.setAgentTarget(caIdx,targetPosition);
        detourType = DetourTypeConstant.DETOUR_TYPE_CROWD_POSITION;
        stateManager.addPurpos(RolePurposConstant.ROLE_PURPOS_FIND_POSITION);
    }

    /**
     * 寻路方式四
     * 对目标角色做群体寻路
     * @param role
     */
    public void setCrowdTargetRole(BattleRole role){
        //1.清空缓存，设置目标对象
        findCrowdResults.clear();
        setTargetRole(role);
        //2.将寻路方式修改为群体寻路
        detourType = DetourTypeConstant.DETOUR_TYPE_CROWD_ROLE;
        //System.out.println("setCrowdTargetRole "+Arrays.toString(currentPosition));
        mapLogic.setAgentPosition(this.caIdx,currentPosition);
        stateManager.addPurpos(RolePurposConstant.ROLE_PURPOS_FIND_OBJECT);
    }

    public void setTargetNull(){
        targetRole = null;
        detourType = DetourTypeConstant.DETOUR_TYPE_NULL;
    }

    public void setHitInfo(BattleRole hostRole, BasicSkillCarrier skillCarrier, int hitId, int hitType, int hurtNum){
        byte state = getState();
        if(state == RoleStateConstant.ROLE_STATE_BORN || state == RoleStateConstant.ROLE_STATE_DEATH){
            return;
        }

        animManager.addAnim(frameNum,RoleAnimConstant.ROLE_ANIM_HIT,0);

        //当前剩余硬直时间为0时，开始统计击打次数
        if(hurtNum < 0 && curStunTime <= 0){
            curHitTimes ++;
            if(curHitTimes >= maxHitTimesStun){
                curHitTimes = 0;
                if(animManager.addAnim(frameNum,RoleAnimConstant.ROLE_ANIM_STIFF,maxStunTime)){
                    setStillState(maxStunTime);
                }
            }
        }

        attributeManager.addBlood(hostRole,hurtNum);

        BattleRoleHitInfo hitInfo = (BattleRoleHitInfo)ProtocolPool.getInstance().borrowProtocol(BattleRoleHitInfo.code);
        hitInfo.setModelId(modelId);
        hitInfo.setBeHidId(hitId);
        hitInfo.setHurtType((byte)hitType);
        hitInfo.setChangeBlood(hurtNum);
        if(getState() == RoleStateConstant.ROLE_STATE_DEATH){
            short deadDirection = -1;
            if(skillCarrier != null){
                if(skillCarrier.getCarrierType() == BasicSkillCarrier.CARRIER_TYPE_DIRECTION_BULLET ||
                        skillCarrier.getCarrierType() == BasicSkillCarrier.CARRIER_TYPE_TARGET_BULLET ||
                        skillCarrier.getCarrierType() == BasicSkillCarrier.CARRIER_TYPE_DIRECTION_MOVE ||
                        skillCarrier.getCarrierType() == BasicSkillCarrier.CARRIER_TYPE_TARGET_MOVE
                ){
                    deadDirection = skillCarrier.direction;
                }
            }

            hitInfo.setState((byte)1);
            hitInfo.setCarrierId(skillCarrier.getCarrierId());
            hitInfo.setDirection(deadDirection);
        }else{
            hitInfo.setState((byte)0);
        }
        roleHitInfos.add(hitInfo);
    }

    public void clearLastHitInfo(){
        roleHitInfos.clear();
    }

    public byte getState(){
        return stateManager.getState();
    }

    public void setDeadState(BattleRole hostRole){

        stateManager.addBasicState(RoleStateConstant.ROLE_STATE_DEATH,0);

        animManager.addAnim(frameNum,RoleAnimConstant.ROLE_ANIM_DEAD,0);

        buffManager.stopAllBuff();

        //mapLogic.releaseDeadTargerRole(this);

        if(roleType == RoleTypeConstant.BATTLE_ROLE_MONSTER || roleType == RoleTypeConstant.BATTLE_ROLE_BOSS){
            //掉落奖励
            LootNpcModel lootNpcModel = StaticDictionary.getInstance().getLootNpcModelByNpcIdAndNpcLv(roleId,roleLevel);
            if(lootNpcModel != null){
                roleLootInfo = ParseLoot.parse(hostRole.roleId,lootNpcModel.getDroplist());
                BattlePosition position = (BattlePosition)ProtocolPool.getInstance().borrowProtocol(BattlePosition.code);
                position.setMapIndex(mapLogic.getMapIndex());
                position.setPosX((int)(currentPosition[0]*100));
                position.setPosZ((int)(currentPosition[2]*100));
                roleLootInfo.setLootPos(position);

                if(hostRole.getRoleType() == RoleTypeConstant.BATTLE_ROLE_PLAYER){
                    ((BattlePlayer)hostRole).addLootInfo(roleLootInfo);
                }
                mapLogic.addSyncLoot(roleLootInfo);
            }
        }

    }

    /**
     * 恢复自由身，此时会清除所有目标和行为状态
     */
    public void setFreeState(){
        stateManager.addBasicState(RoleStateConstant.ROLE_STATE_FREE,0);
    }

    public void setBornState(int stateTime){
        stateManager.addBasicState(RoleStateConstant.ROLE_STATE_BORN,stateTime);
    }

    public void setHideState(int stateTime){
        stateManager.addBasicState(RoleStateConstant.ROLE_STATE_HIDE,stateTime);
    }

    public void setStillState(int stateTime){
        stateManager.addBasicState(RoleStateConstant.ROLE_STATE_STILL,stateTime);
    }

    public byte getDetourType(){
        return this.detourType;
    }

    public float[] getCurrentPosition(){
        return this.currentPosition;
    }

    public float[] getPreviousPosition(){
        return this.previousPosition;
    }

    public float[] getCurrentDir(){
        return this.currentDir;
    }

    public float[] getTargetPosition(){
        return this.targetPosition;
    }

    public void setFindPathResult(float x,float y,float z){
        this.findPathResults.add(x,y,z);
        this.runFinish = false;
    }

    public void clearFindPathResults(){
        this.findPathResults.clear();
    }

    public void setFindCrowdResult(float x,float y,float z) {
        //this.findCrowdResults.clear();
        this.findCrowdResults.add(x,y,z);
        this.runFinish = false;
    }

    public void setFindCrowdResults(List<float[]> detourResults) {
        this.findCrowdResults.clear();
        this.findCrowdResults.addAll(detourResults);
        this.runFinish = false;
    }

    public void stop(){
        this.runFinish = true;
        this.moveType = RoleMoveTypeConstant.ROLE_MOVE_TYPE_WAIT;
    }

    public short getDirection(){
        return this.direction;
    }

    public int getCaIdx(){
        return this.caIdx;
    }

    public void setCaIdx(int caIdx){
        this.caIdx = caIdx;
    }

    public void setRoleType(byte roleType){
        this.roleType = roleType;
    }

    public byte getRoleType(){
        return roleType;
    }

    public int getRoleId(){
        return roleId;
    }

    public int getRoleLevel(){
        return roleLevel;
    }

    public void setPlayerId(int playerId){
        this.playerId = playerId;
    }

    public int getPlayerId(){
        return  playerId;
    }

    public void setCamp(byte camp){
        this.camp = camp;
    }

    public byte getCamp(){
        return camp;
    }

    public void setMoveType(byte moveType){
        this.moveType = moveType;
    }

    public void sendPlayerToEnterMapPosition(){
        float[] enterPos = mapLogic.getEnterPos();
        if(enterPos != null && enterPos.length == 2){
            setCurrentPosition(enterPos[0],enterPos[1]);
        }
    }

    public void carrierMoveLimitRole(int carrierId){
        List<Integer> carrierIds = moveLimitMap.get(RoleMoveLimitConstant.ROLE_MOVE_LIMIT_CARRIER);
        if(carrierIds == null){
            carrierIds = new ArrayList<>();
            moveLimitMap.put(RoleMoveLimitConstant.ROLE_MOVE_LIMIT_CARRIER,carrierIds);
        }
        carrierIds.add(carrierId);

        if(detourType == DetourTypeConstant.DETOUR_TYPE_CROWD_POSITION || detourType == DetourTypeConstant.DETOUR_TYPE_CROWD_ROLE){
            detourType = DetourTypeConstant.DETOUR_TYPE_NULL;
        }
    }

    /**
     * 开始释放技能动作
     */
    public void startReleaseSkillAnim(BasicRoleSkill roleSkill){
        //System.out.println("---------startReleaseSkill----------");
        if(roleSkill.getSkillType() != BasicRoleSkill.SKILL_RELEASE_TYPE_ELEMENT && roleSkill.getAnimatorTime() > 0){
            if(animManager.addAnim(frameNum,RoleAnimConstant.ROLE_ANIM_SKILL,roleSkill.getAnimatorTime())){
                skillId = roleSkill.getSkillId();
                skillAnimTime = roleSkill.getAnimatorTime();
            }
        }
    }

    /**
     * 结束释放技能动作（吟唱型和引导型技能可能会被打断）
     */
    public void endReleaseSkillAnim(BasicRoleSkill roleSkill){
        //System.out.println("---------endReleaseSkill----------");
        if(animManager.addAnim(frameNum,RoleAnimConstant.ROLE_ANIM_BREAK_SKILL,0)){
            skillId = roleSkill.getSkillId();
        }
    }

    public boolean update(int frameNum){
        this.frameNum = frameNum;

        /**
         * 清除上一帧角色的伤害信息
         */
        roleHitInfos.clear();

        /**
         * 角色属性更新
         */
        attributeManager.update(frameNum);

        /**
         * 技能更新
         */
        skillManager.update(frameNum);

        /**
         * 角色状态更新
         */
        stateManager.update(frameNum);

        /**
         * buff更新
         */
        buffManager.update(frameNum);

        /**
         * 动作更新
         */
        animManager.update();

        /**
         * 技巧cd更新
         */
        aiManager.update(frameNum);

        /**
         * 硬直时定住角色
         */
        if(curStunTime > 0){
            return false;
        }

        /**
         * ai全时触发
         */
        aiManager.matchAi(AiTrigger.AI_TRIGGER_ALL_TIME);

        return true;
    }

    public boolean run() {
        if(!this.runFinish){
            if(detourType == DetourTypeConstant.DETOUR_TYPE_PATH_POSITION || detourType == DetourTypeConstant.DETOUR_TYPE_PATH_ROLE){
                return pathRun();
            }
            if(detourType == DetourTypeConstant.DETOUR_TYPE_CROWD_POSITION || detourType == DetourTypeConstant.DETOUR_TYPE_CROWD_ROLE){
                return crowdRun();
            }
        }
        return true;
    }

    protected float[] positionOffset = new float[2];

    private float[] nextPosition = new float[3];

    public boolean pathRun(){
        float[] runNextPos = null;

        if((runNextPos = findPathResults.peek())==null){
            return false;
        }else{
            //while((runNextPos = findPathResults.peek())!=null){
                nextPosition[0] = currentPosition[0];
                nextPosition[1] = currentPosition[1];
                nextPosition[2] = currentPosition[2];

                float positionOffsetX = 0;
                float positionOffsetZ = 0;
                boolean arriveX = false;
                boolean arriveZ = false;
                //double dist = Math.sqrt(Math.pow(runNextPos[0]-currentPosition[0], 2)+Math.pow(runNextPos[1]-currentPosition[1], 2)+Math.pow(runNextPos[2]-currentPosition[2], 2));
                //double dist = Math.sqrt(Math.pow(runNextPos[0]-currentPosition[0], 2)+Math.pow(runNextPos[2]-currentPosition[2], 2));
                if(Math.abs(runNextPos[0]-currentPosition[0]) <= 0.01f && Math.abs(runNextPos[2]-currentPosition[2]) <= 0.01f){
                    arriveX = true;
                    arriveZ = true;
                }else{

                    float newDir = CalculationUtil.vectorToAngle(runNextPos[0]-currentPosition[0],runNextPos[2]-currentPosition[2]);
                    short curDirection = direction;
                    if(Math.abs(newDir - curDirection) > 180){
                        //角度差值
                        float diff = newDir - curDirection;
                        if(diff > 0){
                            newDir -= 360;
                        }
                        if(diff < 0){
                            newDir += 360;
                        }
                    }

                    //处理转身
                    if(Math.abs(newDir - curDirection) > 60){
                        //角度差值
                        float diff = newDir - curDirection;
                        if(diff > 0){
                            direction += 60;
                        }
                        if(diff < 0){
                            direction -= 60;
                        }
                    }else{
                        curDirection = (short)newDir;
                        while(curDirection < 0){
                            curDirection += 360;
                        }
                        curDirection %= 360;

                        setDirection(curDirection);

                        //double dist = Math.sqrt(Math.pow(runNextPos[0]-currentPosition[0], 2)+Math.pow(runNextPos[1]-currentPosition[1], 2)+Math.pow(runNextPos[2]-currentPosition[2], 2));
                        double dist = Math.sqrt(Math.pow(runNextPos[0]-currentPosition[0], 2)+Math.pow(runNextPos[2]-currentPosition[2], 2));
                        float runSpeed = stateManager.getMoveSpeed();
                        float moveX = (float)((runNextPos[0]-currentPosition[0]) * (runSpeed/ GameConstant.GAME_FRAME)/dist);
                        //float moveY = (float)((runNextPos[1]-currentPosition[1]) * (runSpeed/GameConstant.GAME_FRAME)/dist);
                        float moveZ = (float)((runNextPos[2]-currentPosition[2]) * (runSpeed/GameConstant.GAME_FRAME)/dist);
                        //System.out.println("moveX = "+moveX+"  moveZ = "+moveZ+"  moveDistance = "+Math.sqrt(moveX*moveX+moveZ*moveZ));
                        //float[] nextPosition = new float[]{currentPosition[0],currentPosition[1],currentPosition[2]};
                        //X方向移动
                        if(Math.abs(runNextPos[0] - currentPosition[0]) <= Math.abs(moveX)+0.01f) {
                            positionOffsetX = (currentPosition[0] + moveX - runNextPos[0]);
                            arriveX = true;
                        }
                        nextPosition[0] = currentPosition[0] + moveX;

                        //Z方向移动
                        if(Math.abs(runNextPos[2] - currentPosition[2]) <= Math.abs(moveZ)+0.01f) {
                            positionOffsetZ = (currentPosition[2] + moveZ - runNextPos[2]);
                            arriveZ = true;
                        }
                        nextPosition[2] = currentPosition[2] + moveZ;

                    }

                }

                if(arriveX && arriveZ){
                    //System.out.println("到达一个目标点");

                    positionOffset[0] = positionOffsetX;
                    positionOffset[1] = positionOffsetZ;
                    findPathResults.poll();
                    if(findPathResults.size() == 0){
                        //System.out.println("到达目的地！！！");
                        byte purpos = stateManager.getPurpos();
                        /**
                         * 走到目的地后，如果当前行为是寻找地图出口，进入下一个地图
                         */
                        if(purpos == RolePurposConstant.ROLE_PURPOS_FIND_EXIT){

                            mapLogic.finishMapLogic();
                            if(battleContainer.roleJumpToOtherMap(this,positionOffset)){
                                stateManager.addPurpos(RolePurposConstant.ROLE_PURPOS_NULL);
                                moveType = RoleMoveTypeConstant.ROLE_MOVE_TYPE_RUN;
                            }else{
                                logger.info("has no next map !!!");
                                battleContainer.finishBattle(BattleContainer.BATTLE_FINISH_TYPE_NORMAL,true,10);
                                runFinish = true;
                                moveType = RoleMoveTypeConstant.ROLE_MOVE_TYPE_WAIT;
                            }

                            return true;
                        }

                        if(purpos == RolePurposConstant.ROLE_PURPOS_FIND_POSITION){

                            setCurrentPosition(nextPosition);
                            stateManager.addPurpos(RolePurposConstant.ROLE_PURPOS_NULL);
                            moveType = RoleMoveTypeConstant.ROLE_MOVE_TYPE_RUN;
                            //runFinish = true;
                            return true;
                        }
                    }else{
                        setCurrentPosition(nextPosition);
                        moveType = RoleMoveTypeConstant.ROLE_MOVE_TYPE_RUN;
                    }
                }else{
                    setCurrentPosition(nextPosition);
                    moveType = RoleMoveTypeConstant.ROLE_MOVE_TYPE_RUN;
                }

            //}

            return true;
        }

        //System.out.println("modelId = "+modelId+" posX = "+currentPosition[0]+" posY = "+currentPosition[1]+" posZ = "+currentPosition[2]);

    }

    public boolean crowdRun(){
        if(findCrowdResults.size() != 0){
            float[] result = findCrowdResults.poll();
            if(result != null){
                //setCurrentDir(new float[]{result[3],result[4],result[5]});
                if(Math.abs(result[0]-currentPosition[0]) > 0.01f || Math.abs(result[2]-currentPosition[2]) > 0.01f){
                    setDirection((short)CalculationUtil.vectorToAngle(result[0]-currentPosition[0],result[2]-currentPosition[2]));
                }

                nextPosition[0] = result[0];
                nextPosition[1] = result[1];
                nextPosition[2] = result[2];

                setCurrentPosition(nextPosition);

                moveType = RoleMoveTypeConstant.ROLE_MOVE_TYPE_RUN;
            }else{
                //runFinish = true;
                moveType = RoleMoveTypeConstant.ROLE_MOVE_TYPE_WAIT;
                return false;
            }
        }
        return true;
    }

    public BattleRoleInfo getSyncData(){
        byte state = getState();
//        if(getState() == RoleStateConstant.ROLE_STATE_DEATH){
//            return null;
//        }

        BattleRoleInfo battleRoleInfo = (BattleRoleInfo)ProtocolPool.getInstance().borrowProtocol(BattleRoleInfo.code);
        battleRoleInfo.setId(modelId);

        byte anim = animManager.getSyncAnim();
        battleRoleInfo.setAnim(anim);
        battleRoleInfo.setMaxBlood(attributeManager.maxBlood);
        battleRoleInfo.setBlood(attributeManager.curBlood);

        if(anim == RoleAnimConstant.ROLE_ANIM_SKILL){
            battleRoleInfo.setSkillId(skillId);
            battleRoleInfo.setSkillTime(skillAnimTime);
        }

        //if(state != RoleStateConstant.ROLE_STATE_STILL){
        if(previousPosition[0] != currentPosition[0] || previousPosition[2] != currentPosition[2] || previousDirection != direction){
            BattlePosition pos = (BattlePosition)ProtocolPool.getInstance().borrowProtocol(BattlePosition.code);
            pos.setMapIndex(mapIndex);
            pos.setPosX((int)(currentPosition[0]*100));
            pos.setPosZ((int)(currentPosition[2]*100));
            pos.setDirection(direction);
            pos.setMoveType(moveType);

            battleRoleInfo.setPosition(pos);
        }

        battleRoleInfo.setShow(stateManager.getState()!=RoleStateConstant.ROLE_STATE_HIDE);

        battleRoleInfo.setTargetId(clientShowTargetRoleId);

        battleRoleInfo.setSoulEnergy(attributeManager.curSoulEnergy);

        battleRoleInfo.setSkillSource(attributeManager.getSkillSourceNumByType(skillSourceType));

        return battleRoleInfo;
    }

    public void sendBattleData(Protocol protocol){
        if(playerId != 0){
            UdpSession session = UdpSessionManager.getInstance().getSessionByPlayerId(playerId);
            if(session != null && session.kcpHandler != null) {
                //System.out.println("send msg "+protocol.toString());
                session.write(protocol);
                ProtocolPool.getInstance().restoreProtocol(protocol);
            }
        }
    }

    public List<BattleRoleHitInfo> getSyncHitInfo(){
        if(roleHitInfos!=null && roleHitInfos.size() >0){
            return roleHitInfos;
        }
        return null;
    }

    public BuffManager getBuffManager() {
        return buffManager;
    }

    public StateManager getStateManager(){
        return stateManager;
    }

    public AnimManager getAnimManager() {
        return animManager;
    }

    public int getBuffAttribute(int attributeId, int type){
        return buffManager.getAttributeFromBuff(attributeId,type);
    }

    public int createNewBuffGuid(){
        return ++buffGuid;
    }

    @Override
    public String toString() {
        return "BattleRole{" +
                "playerId=" + playerId +
                ", modelId=" + modelId +
                ", roleId=" + roleId +
                ", roleLevel=" + roleLevel +
                ", state=" + getState() +
                '}';
    }
}
