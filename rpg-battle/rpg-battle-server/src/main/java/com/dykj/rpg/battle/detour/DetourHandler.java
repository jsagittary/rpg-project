package com.dykj.rpg.battle.detour;

/**
 * 寻路处理类
 */

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.dykj.rpg.battle.constant.DetourTypeConstant;
import com.dykj.rpg.battle.pool.TemporaryPoolManager;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.common.attribute.consts.AttributeBasicConstant;
import org.recast4j.detour.*;
import org.recast4j.detour.crowd.Crowd;
import org.recast4j.detour.crowd.CrowdAgent;
import org.recast4j.detour.crowd.CrowdAgentParams;
import org.recast4j.detour.crowd.ObstacleAvoidanceQuery;
import org.recast4j.detour.crowd.debug.CrowdAgentDebugInfo;

import static org.recast4j.detour.DetourCommon.*;

public class DetourHandler {
	public static void main(String[] args) {
		DetourHandler test = new DetourHandler(401001);
	}

	//不合格地址
	public static final int invalidPos = -1000;
	
    private final float[] m_polyPickExt = new float[] { 2, 4, 2 };

	protected NavMeshQuery query;
    protected NavMesh navmesh;
    protected GameInputGeomProvider geom;
    protected Crowd crowd;
    protected QueryFilter filter;
    //网格对坐标点的查询范围
    float[] ext = new float[]{1,20,1};

    Map<Integer,BattleRole> roleMap = new ConcurrentHashMap<>();

    /**
     * 向地图添加角色与运行地图角色寻路有冲突
     */
    private ReentrantLock crowdLock = new ReentrantLock();

    public DetourHandler(int mapId){
        setUp(mapId);
    }

    private void setUp(int mapId) {
        navmesh = MapLoader.getInstance().getNavMeshById(mapId);
        geom = MapLoader.getInstance().getGeomById(mapId);
        query = new NavMeshQuery(navmesh);
        crowd = new Crowd(GameConstant.MAX_DETOUR_AGENT*2, 0.6f, navmesh);
        filter = crowd.getFilter(0);
        // Setup local avoidance params to different qualities.
        // Use mostly default settings, copy from dtCrowd.
        ObstacleAvoidanceQuery.ObstacleAvoidanceParams params = new ObstacleAvoidanceQuery.ObstacleAvoidanceParams(crowd.getObstacleAvoidanceParams(0));

        // Low (11)
        params.velBias = 0.5f;
        params.adaptiveDivs = 5;
        params.adaptiveRings = 2;
        params.adaptiveDepth = 1;
        crowd.setObstacleAvoidanceParams(0, params);

        // Medium (22)
        params.velBias = 0.5f;
        params.adaptiveDivs = 5;
        params.adaptiveRings = 2;
        params.adaptiveDepth = 2;
        crowd.setObstacleAvoidanceParams(1, params);

        // Good (45)
        params.velBias = 0.5f;
        params.adaptiveDivs = 7;
        params.adaptiveRings = 2;
        params.adaptiveDepth = 3;
        crowd.setObstacleAvoidanceParams(2, params);

        // High (66)
        params.velBias = 0.5f;
        params.adaptiveDivs = 7;
        params.adaptiveRings = 3;
        params.adaptiveDepth = 3;

        crowd.setObstacleAvoidanceParams(3, params);

    }

    public void release(){
        List<CrowdAgent> agents = crowd.getActiveAgents();
        for(CrowdAgent agent : agents){
            crowd.removeAgent(agent.idx);
        }
        roleMap.clear();
    }

    QueryFilter findStraightPathFilter = new DefaultQueryFilter();
	public void findStraightPath(BattleRole battleRole) {

        float[] currentPos = battleRole.getCurrentPosition();
        float[] targetPos = battleRole.getTargetPosition();

        float[] startPos = TemporaryPoolManager.getPositionArray(-currentPos[0], 0, currentPos[2]);
        float[] endPos = TemporaryPoolManager.getPositionArray(-targetPos[0], 0, targetPos[2]);

		//System.out.println("startPos x = "+startPos[0]+"  y = "+startPos[1]+"  z = "+startPos[2]);
        //System.out.println("endPos x = "+endPos[0]+"  y = "+endPos[1]+"  z = "+endPos[2]);

        long startRef = query.findNearestPoly(startPos, m_polyPickExt, findStraightPathFilter).result.getNearestRef();
        long endRef = query.findNearestPoly(endPos, m_polyPickExt, findStraightPathFilter).result.getNearestRef();

        //System.out.println("startRef = " +startRef);
        //System.out.println("endRef = " +endRef);

        Result<List<Long>> path = query.findPath(startRef, endRef, startPos, endPos, findStraightPathFilter);
            
        Result<List<StraightPathItem>> result = query.findStraightPath(startPos, endPos, path.result,Integer.MAX_VALUE, 0);
        List<StraightPathItem> straightPath = result.result;
        if(straightPath != null) {
            float[] resultPos = null;
            for (int j = 1; j < straightPath.size(); j++) {
                resultPos = straightPath.get(j).getPos();
                if(j == straightPath.size()-1 && (resultPos[0] != endPos[0] || resultPos[2] != endPos[2])){
                    battleRole.setFindPathResult(-endPos[0],endPos[1],endPos[2]);
                }else{
                    battleRole.setFindPathResult(-resultPos[0],resultPos[1],resultPos[2]);
                }
            }
        }

    }

    float[] pointOnMeshPos = new float[]{0,0,0};
    float[] pointOnMeshHalfExtents = new float[]{0,1,0};
    QueryFilter pointOnMeshFilter = new DefaultQueryFilter();
    public boolean isPointOnMesh(float x,float z){

        pointOnMeshPos[0] = -x;
        pointOnMeshPos[1] = 0;
        pointOnMeshPos[2] = z;

        while(true){
            if(query.findNearestPoly(pointOnMeshPos, pointOnMeshHalfExtents, pointOnMeshFilter).result.getNearestRef() != 0){
                return true;
            }
            if(pointOnMeshPos[1] > 10){
                break;
            }else{
                if(pointOnMeshPos[1] > 0){
                    pointOnMeshPos[1] *= -1;
                }else{
                    pointOnMeshPos[1] *= -1;
                    pointOnMeshPos[1] += 2;
                }
            }
        }
        return false;
    }
    public float getPositionYOnMesh2(float x,float z){
        return geom.getPositionYOnMesh(x,z);
    }

    float[] getYPos = new float[]{0,0,0};
    float[] getYHalfExtents = new float[]{0,1,0};
    QueryFilter getYFilter = new DefaultQueryFilter();
    public float getPositionYOnMesh(float x,float z){
        getYPos[0] = -x;
        getYPos[1] = 0;
        getYPos[2] = z;
        while(true){
            if(query.findNearestPoly(getYPos, getYHalfExtents, getYFilter).result.getNearestRef() != 0){
                return getYPos[1];
            }
            if(getYPos[1] > 10){
                break;
            }else{
                if(getYPos[1] > 0){
                    getYPos[1] *= -1;
                }else{
                    getYPos[1] *= -1;
                    getYPos[1] += 2;
                }
            }
        }
        return invalidPos;
    }

    private float[] findPolyHalfExtents = new float[]{0,0,0};
    private float[][] findPolyResultPos = new float[][]{new float[]{0,0,0},new float[]{0,0,0},new float[]{0,0,0},new float[]{0,0,0}};
    public float[][] findSoulNearestPoly(float[] pos,float radius){

        findPolyHalfExtents[0] = radius;
        findPolyHalfExtents[1] = 10;
        findPolyHalfExtents[2] = radius;

        pos[0] *= -1;

        Result<FindNearestPolyResult> result;
        pos[0] -= radius/2;
        pos[2] -= radius/2;
        result = query.findNearestPoly(pos, findPolyHalfExtents, filter);
        if(result.result.getNearestRef() != 0){
            float[] resultPos = result.result.getNearestPos();
            findPolyResultPos[0][0] = -resultPos[0];
            findPolyResultPos[0][1] = 1;
            findPolyResultPos[0][2] = resultPos[2];
        }else{
            findPolyResultPos[0][1] = 0;
        }

        pos[0] += radius/2;
        result = query.findNearestPoly(pos, findPolyHalfExtents, filter);
        if(result.result.getNearestRef() != 0){
            float[] resultPos = result.result.getNearestPos();
            findPolyResultPos[1][0] = -resultPos[0];
            findPolyResultPos[1][1] = 1;
            findPolyResultPos[1][2] = resultPos[2];
        }else{
            findPolyResultPos[1][1] = 0;
        }

        pos[2] += radius/2;
        result = query.findNearestPoly(pos, findPolyHalfExtents, filter);
        if(result.result.getNearestRef() != 0){
            float[] resultPos = result.result.getNearestPos();
            findPolyResultPos[2][0] = -resultPos[0];
            findPolyResultPos[2][1] = 1;
            findPolyResultPos[2][2] = resultPos[2];
        }else{
            findPolyResultPos[2][1] = 0;
        }

        pos[0] -= radius/2;
        result = query.findNearestPoly(pos, findPolyHalfExtents, filter);
        if(result.result.getNearestRef() != 0){
            float[] resultPos = result.result.getNearestPos();
            findPolyResultPos[3][0] = -resultPos[0];
            findPolyResultPos[3][1] = 1;
            findPolyResultPos[3][2] = resultPos[2];
        }else{
            findPolyResultPos[3][1] = 0;
        }

        return findPolyResultPos;
    }

    /**
     * 用于多对象走向同一目标
     * 地图中障碍物过多时可能会出现堵死
     */
    public void findCrowdPath(){
        //long startTime = System.nanoTime();
        if(!crowdLock.isLocked()){
            try{
                crowdLock.lock();
                //设置角色速度
                for(CrowdAgent agent: crowd.getActiveAgents()){
                    BattleRole battleRole = roleMap.get(agent.idx);
                    float speed = battleRole.getStateManager().getDetourMoveSpeed();
                    agent.params.maxSpeed = speed;
                    //System.out.println("battle role id = "+battleRole.getModelId()+"  1 position = "+ Arrays.toString(agent.npos));
                    //速度为0时，重新设置位置
                    if(speed == 0){
                        float[] currentPos = battleRole.getCurrentPosition();
                        Result<FindNearestPolyResult> nearest = query.findNearestPoly(TemporaryPoolManager.getPositionArray(-currentPos[0],0,currentPos[2]), ext, filter);
                        if(nearest.result.getNearestRef() != 0){
                            float[] nearestPos = nearest.result.getNearestPos();
                            agent.npos[0] = nearestPos[0];
                            agent.npos[1] = nearestPos[1];
                            agent.npos[2] = nearestPos[2];
                        }else{
                            agent.npos[0] = -currentPos[0];
                            agent.npos[1] = currentPos[1];
                            agent.npos[2] = currentPos[2];
                        }
                    }

                }
                crowd.update(1f/ GameConstant.GAME_FRAME * GameConstant.CROWN_ADVANCE_FRAME_NUM, null);
                for(CrowdAgent agent: crowd.getActiveAgents()){
                    BattleRole battleRole = roleMap.get(agent.idx);
                    if(battleRole != null){
                        if(battleRole.getDetourType() == DetourTypeConstant.DETOUR_TYPE_CROWD_POSITION || battleRole.getDetourType() == DetourTypeConstant.DETOUR_TYPE_CROWD_ROLE){
                            float[] currentPos = battleRole.getCurrentPosition();

                            float resultX = -agent.npos[0];
                            float resultY = agent.npos[1];
                            float resultZ = agent.npos[2];

                            for(int i=1;i<=GameConstant.CROWN_ADVANCE_FRAME_NUM;i++){
                                battleRole.setFindCrowdResult(currentPos[0]+(resultX-currentPos[0])*i/GameConstant.CROWN_ADVANCE_FRAME_NUM,0,currentPos[2]+(resultZ-currentPos[2])*i/GameConstant.CROWN_ADVANCE_FRAME_NUM);
                            }
                            //battleRole.setFindCrowdResults(results);
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                crowdLock.unlock();
            }
        }
        //long endTime = System.nanoTime();
        //System.out.println("DetourHandler findCrowdPath 计算耗时 "+((endTime - startTime)/1000)+"μs");
    }

    public void setAgentPosition(int idx,float[] currentPos){
        CrowdAgent ag = crowd.getAgent(idx);
        Result<FindNearestPolyResult> nearest = query.findNearestPoly(TemporaryPoolManager.getPositionArray(-currentPos[0],0,currentPos[2]), ext, filter);
        if(nearest.result.getNearestRef() != 0){
            float[] nearestPos = nearest.result.getNearestPos();
            ag.npos[0] = nearestPos[0];
            ag.npos[1] = nearestPos[1];
            ag.npos[2] = nearestPos[2];
        }else{
            ag.npos[0] = -currentPos[0];
            ag.npos[1] = currentPos[1];
            ag.npos[2] = currentPos[2];
        }
    }

    public void setAgentTarget(int idx,float[] targetPos){
        //long startTime = System.nanoTime();
//        float[] ext = crowd.getQueryExtents();
//        QueryFilter filter = crowd.getFilter(0);

//        CrowdAgent ag = crowd.getAgent(idx);
//        float[] vel = calcVel(ag.npos, new float[]{-targetPos[0],targetPos[1],targetPos[2]}, ag.params.maxSpeed);
//        crowd.requestMoveVelocity(idx, vel);

        CrowdAgent ag = crowd.getAgent(idx);

        Result<FindNearestPolyResult> nearest = query.findNearestPoly(TemporaryPoolManager.getPositionArray(-targetPos[0],targetPos[1],targetPos[2]), ext, filter);
        Result<FindNearestPolyResult> current = query.findNearestPoly(ag.npos, ext, filter);
        if (ag.isActive()) {
            if(ag.targetState == CrowdAgent.MoveRequestState.DT_CROWDAGENT_TARGET_NONE ||
                    ag.targetRef != nearest.result.getNearestRef() ||
                    current.result.getNearestRef() == nearest.result.getNearestRef()){
                crowd.requestMoveTarget(idx, nearest.result.getNearestRef(), nearest.result.getNearestPos());
            }
        }
        //long endTime = System.nanoTime();
        //System.out.println("DetourHandler setAgentTarget 计算耗时 "+((endTime - startTime)/1000)+"μs");
    }


    public void removeAgentGrid(BattleRole battleRole){
        crowd.removeAgent(battleRole.getCaIdx());
        roleMap.remove(battleRole.getCaIdx());
    }

    public int addAgentGrid(BattleRole battleRole, boolean crowdPath,int updateFlags, int obstacleAvoidanceType) {
        CrowdAgentParams ap = getAgentParams(battleRole, updateFlags, obstacleAvoidanceType);
        float[] startPos = battleRole.getCurrentPosition();
        Result<FindNearestPolyResult> nearest = query.findNearestPoly(TemporaryPoolManager.getPositionArray(-startPos[0],0,startPos[2]), ext, filter);
        if(nearest.result.getNearestRef() != 0){
            float[] nearestPos = nearest.result.getNearestPos();
            int idx = -1;
            if(!crowdLock.isLocked()) {
                try {
                    //CrowdAgentParams ap = getAgentParams(radius, updateFlags, obstacleAvoidanceType);
                    crowdLock.lock();
                    idx = crowd.addAgent(nearestPos, ap);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    crowdLock.unlock();
                }
            }
            if(crowdPath){
                roleMap.put(idx,battleRole);
            }
            return idx;
        }else{
            return -1;
        }

    }

    protected CrowdAgentParams getAgentParams(float radius, int updateFlags, int obstacleAvoidanceType) {
        CrowdAgentParams ap = new CrowdAgentParams();
        ap.radius = radius;
        ap.height = 4f;
        ap.maxAcceleration = 8.0f;
        ap.maxSpeed = 10f;
        ap.collisionQueryRange = ap.radius * 12f;
        ap.pathOptimizationRange = ap.radius * 30f;
        ap.updateFlags = updateFlags;
        ap.obstacleAvoidanceType = obstacleAvoidanceType;
        ap.separationWeight = 2f;
        return ap;
    }

    protected CrowdAgentParams getAgentParams(BattleRole battleRole, int updateFlags, int obstacleAvoidanceType) {
        CrowdAgentParams ap = new CrowdAgentParams();
        ap.radius = battleRole.attributeManager.radius;
        ap.height = 2;
        ap.maxAcceleration = 1000;
        ap.maxSpeed = battleRole.buffManager.getAttributeFromBuff(AttributeBasicConstant.YI_DONG_SU_DU_PAO_ZHI,0);
        ap.collisionQueryRange = ap.radius * 12f;
        ap.pathOptimizationRange = ap.radius * 30f;
        ap.updateFlags = updateFlags;
        ap.obstacleAvoidanceType = obstacleAvoidanceType;
        ap.separationWeight = 2f;
        return ap;
    }

    protected void setMoveTarget(float[] pos, boolean adjust) {
        float[] ext = crowd.getQueryExtents();
        QueryFilter filter = crowd.getFilter(0);
        if (adjust) {
            for (int i = 0; i < crowd.getAgentCount(); i++) {
                CrowdAgent ag = crowd.getAgent(i);
                if (!ag.isActive()) {
                    continue;
                }
                float[] vel = calcVel(ag.npos, pos, ag.params.maxSpeed);
                crowd.requestMoveVelocity(i, vel);
            }
        } else {
            Result<FindNearestPolyResult> nearest = query.findNearestPoly(pos, ext, filter);
            for (int i = 0; i < crowd.getAgentCount(); i++) {
                CrowdAgent ag = crowd.getAgent(i);
                if (!ag.isActive()) {
                    continue;
                }
                crowd.requestMoveTarget(i, nearest.result.getNearestRef(), nearest.result.getNearestPos());
            }
        }
    }

    protected float[] calcVel(float[] pos, float[] tgt, float speed) {
        float[] vel = vSub(tgt, pos);
        vel[1] = 0.0f;
        vNormalize(vel);
        vel = vScale(vel, speed);
        return vel;
    }
}
