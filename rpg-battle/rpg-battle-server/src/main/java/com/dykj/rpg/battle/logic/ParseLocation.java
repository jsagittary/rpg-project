package com.dykj.rpg.battle.logic;

import com.dykj.rpg.battle.basic.BasicRoleSkill;
import com.dykj.rpg.battle.basic.BasicSkillCarrier;
import com.dykj.rpg.battle.basic.BasicSkillEffect;
import com.dykj.rpg.battle.constant.BattleCampConstant;
import com.dykj.rpg.battle.constant.SkillHitShapConstant;
import com.dykj.rpg.battle.logic.MapLogic;
import com.dykj.rpg.battle.pool.TemporaryPoolManager;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.battle.util.CalculationUtil;
import com.dykj.rpg.protocol.battle.BattlePosition;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析地点数据
 */
public class ParseLocation {


    /**
     *
     *  return float[] 大小为1时代表由效果控制方向
     *
     *  第一个参数：目标类型
     *      1=单位
     *      2=地点
     *      3=方向
     *
     *  第二个参数：基准点
     *      1=施法者
     *      2=触发者
     *      3=上一级载体
     *      4=指示器
     *      5=锁定目标
     *      6=无
     *      7=继承上一级效果
     *
     *  第三个参数：操作类型
     *      0=当前基准点目标
     *      1=偏移类型（面前距离）
     *      2=距离范围
     *      3=角度范围
     *
     */


    /**
     * 寻找目标地点
     * @param preCarrier
     * @param location
     * @param triggerRole
     * @return
     */
    public static float[] parsePosition(BasicSkillCarrier preCarrier,List<List<Integer>> location, BattleRole triggerRole){

        if(preCarrier == null){
            return null;
        }

        SkillReleaseData skillReleaseData = preCarrier.skillReleaseData;

        for(List<Integer> list :location) {
            int paramA = list.get(0); //目标类型
            if(paramA == 2){ //地点
                int paramB = list.get(1);
                if(paramB == 1){ //施法者
                    BattleRole releaseSkillRole = skillReleaseData.hostRole;
                    if(releaseSkillRole == null){
                        return null;
                    }
                    int paramC = list.get(2);
                    if(paramC == 0) { //当前基准点作为目标

                        if(skillReleaseData.roleSkill.skillSeat >= 5){ //灵魂之影
                            return TemporaryPoolManager.getPositionArray(skillReleaseData.roleSkill.soulRolePos[0],0,skillReleaseData.roleSkill.soulRolePos[2]);
                        }

                        return TemporaryPoolManager.getPositionArray(releaseSkillRole.getCurrentPosition()[0],0,releaseSkillRole.getCurrentPosition()[2]);

                    }
                    else{
                        if(skillReleaseData.roleSkill.skillSeat >= 5){ //灵魂之影
                            return parsePosition(skillReleaseData.roleSkill.soulRolePos[0],skillReleaseData.roleSkill.soulRolePos[1],skillReleaseData.roleSkill.soulRolePos[2],(short)skillReleaseData.roleSkill.soulRolePos[3],list);
                        }else{
                            return parsePosition(releaseSkillRole.getCurrentPosition()[0],releaseSkillRole.getCurrentPosition()[1],releaseSkillRole.getCurrentPosition()[2],releaseSkillRole.getDirection(),list);
                        }
                    }
                }
                if(paramB == 2){ //触发者
                    if(triggerRole == null){
                        return null;
                    }
                    if(list.size() == 2){
                        return TemporaryPoolManager.getPositionArray(triggerRole.getCurrentPosition()[0],triggerRole.getCurrentPosition()[1],triggerRole.getCurrentPosition()[2]);
                    }
                    int paramC = list.get(2);
                    if(paramC == 0) { //当前基准点作为目标
                        return TemporaryPoolManager.getPositionArray(triggerRole.getCurrentPosition()[0],triggerRole.getCurrentPosition()[1],triggerRole.getCurrentPosition()[2]);
                    }else{
                        return parsePosition(triggerRole.getCurrentPosition()[0],triggerRole.getCurrentPosition()[1],triggerRole.getCurrentPosition()[2],triggerRole.getDirection(),list);
                    }

                }
                if(paramB == 3){ //上一级载体
                    if(preCarrier == null){
                        return null;
                    }
                    int paramC = list.get(2);
                    if(paramC == 0) { //当前基准点作为目标
                        return TemporaryPoolManager.getPositionArray(preCarrier.getCurrentPosition()[0],preCarrier.getCurrentPosition()[1],preCarrier.getCurrentPosition()[2]);
                    }else{
                        return parsePosition(preCarrier.getCurrentPosition()[0],preCarrier.getCurrentPosition()[1],preCarrier.getCurrentPosition()[2],preCarrier.getDirection(),list);
                    }
                }
                if(paramB == 4){ //指示器
                    BattlePosition skillIndicatorPos = preCarrier.getSkillIndicatorPos();
                    if(skillIndicatorPos == null){
                        BattleRole releaseSkillRole = skillReleaseData.hostRole;

                        if(releaseSkillRole == null){
                            return null;
                        }

                        return TemporaryPoolManager.getPositionArray(releaseSkillRole.getCurrentPosition()[0],releaseSkillRole.getCurrentPosition()[1],releaseSkillRole.getCurrentPosition()[2]);

                    }

                    //指示器直接返回
                    return TemporaryPoolManager.getPositionArray(skillIndicatorPos.getPosX()/100f,0,skillIndicatorPos.getPosZ()/100f);

                }
                if(paramB == 5){ //锁定目标
                    BattleRole targetRole = skillReleaseData.targetRole;

                    if(targetRole == null){
                        return null;
                    }

                    int paramC = list.get(2);
                    if(paramC == 0) { //当前基准点作为目标
                        return TemporaryPoolManager.getPositionArray(targetRole.getCurrentPosition()[0],targetRole.getCurrentPosition()[1],targetRole.getCurrentPosition()[2]);
                    }else{
                        return parsePosition(targetRole.getCurrentPosition()[0],targetRole.getCurrentPosition()[1],targetRole.getCurrentPosition()[2],targetRole.getDirection(),list);
                    }
                }
                if(paramB == 6){ //上一级载体对象

                    if(preCarrier == null){
                        return null;
                    }

                    int paramC = list.get(2);
                    if(paramC == 0) { //当前基准点作为目标
                        return TemporaryPoolManager.getPositionArray(preCarrier.getCurrentPosition()[0],preCarrier.getCurrentPosition()[1],preCarrier.getCurrentPosition()[2]);
                    }else{
                        return parsePosition(preCarrier.getCurrentPosition()[0],preCarrier.getCurrentPosition()[1],preCarrier.getCurrentPosition()[2],preCarrier.getDirection(),list);
                    }

                }
                if(paramB == 7){ //继承上一级效果
                    return null;
                }
                if(paramB == 8){ //无

                    if(preCarrier == null){
                        return null;
                    }
                    return TemporaryPoolManager.getPositionArray(preCarrier.getCurrentPosition()[0],preCarrier.getCurrentPosition()[1],preCarrier.getCurrentPosition()[2]);

                }
            }
        }

        return null;

    }

    private static float[] parsePosition(float x,float y,float z,int direction ,List<Integer> list){
        float[] results = null;
        if(list.size()>2){
            int paramC = list.get(2);
            if(paramC == 1){ //正前方偏移,偏移距离以cm为单位

                if(list.size()>3){
                    float paramD = list.get(3)/100f;
                    double a = direction*Math.PI/180f;

                    results = TemporaryPoolManager.getPositionArray(x+(float)(Math.cos(a)*paramD),y,z+(float)(Math.sin(a)*paramD));
                }
            }
        }

        if(results == null){

            results = TemporaryPoolManager.getPositionArray(x,y,z);
        }

        return results;
    }

    /**
     * 寻找目标对象
     * @param preCarrier
     * @param location
     * @param triggerRole
     * @return
     */
    public static Object parseTargetRole(BasicSkillCarrier preCarrier,List<List<Integer>> location,BattleRole triggerRole){

        if(preCarrier == null){
            return null;
        }

        SkillReleaseData skillReleaseData = preCarrier.skillReleaseData;

        List<BattleRole> targetRoles = new ArrayList<>();
        for(List<Integer> list :location) {
            int paramA = list.get(0); //目标类型

            if(paramA == 1){ //单位
                int paramB = list.get(1);
                if(paramB == 1){ //施法者
                    BattleRole releaseSkillRole = skillReleaseData.hostRole;
                    if(releaseSkillRole == null){
                        continue;
                    }

                    int paramC = list.get(2);
                    if(paramC == 0) { //当前基准点作为目标
                        if(releaseSkillRole != null){
                            targetRoles.add(releaseSkillRole);
                        }
                    }else{
                        if(paramC == 2) { //距离范围
                            MapLogic mapLogic = releaseSkillRole.getMapLogic();
                            if(triggerRole != null){

                                List<BattleRole> roles = getDistanceRangeRoles(mapLogic,TemporaryPoolManager.getPositionArray(releaseSkillRole.getCurrentPosition()[0],0,releaseSkillRole.getCurrentPosition()[2]),list);

                                if(roles != null){
                                    for(BattleRole battleRole : roles){
                                        if (battleRole != null){
                                            targetRoles.add(battleRole);
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
                if(paramB == 2){ //触发者

                    if(triggerRole == null){
                        continue;
                    }

                    int paramC = list.get(2);
                    if(paramC == 0) { //当前基准点作为目标
                        if(triggerRole != null){
                            targetRoles.add(triggerRole);
                        }
                    }else{
                        if(paramC == 2) { //距离范围
                            MapLogic mapLogic = triggerRole.getMapLogic();
                            if(triggerRole != null){

                                List<BattleRole> roles = getDistanceRangeRoles(mapLogic,TemporaryPoolManager.getPositionArray(triggerRole.getCurrentPosition()[0],0,triggerRole.getCurrentPosition()[2]),list);

                                if(roles != null){
                                    for(BattleRole battleRole : roles){
                                        if(battleRole != null){
                                            targetRoles.add(battleRole);
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
                if(paramB == 3){ //上一级载体区域的所有对象

                    int paramC = list.get(2);
                    if(paramC == 0) { //当前基准点作为目标
                        List<BattleRole> roles = preCarrier.getShapCollisionRoles(BattleCampConstant.BATTLE_CAMP_ALL);
                        for(BattleRole battleRole : roles){
                            if(battleRole != null){
                                targetRoles.add(battleRole);
                            }
                        }
                    }else{
                        if(paramC == 2){ //距离范围内搜索敌人

                            MapLogic mapLogic = preCarrier.mapLogic;

                            List<BattleRole> roles = getDistanceRangeRoles(mapLogic,TemporaryPoolManager.getPositionArray(preCarrier.getCurrentPosition()[0],0,preCarrier.getCurrentPosition()[2]),list);

                            if(roles != null){
                                for(BattleRole battleRole : roles){
                                    if(battleRole != null){
                                        targetRoles.add(battleRole);
                                    }
                                }
                            }
                        }
                    }

                }
                if(paramB == 4){ //指示器
                    continue;
                }
                if(paramB == 5){ //锁定目标

                    BattleRole targetRole = skillReleaseData.targetRole;

                    if(targetRole == null){
                        continue;
                    }

                    int paramC = list.get(2);
                    if(paramC == 0) { //当前基准点作为目标
                        targetRoles.add(targetRole);
                    }else{
                        if(paramC == 2){ //距离范围内搜索敌人

                            MapLogic mapLogic = targetRole.getMapLogic();

                            List<BattleRole> roles = getDistanceRangeRoles(mapLogic,TemporaryPoolManager.getPositionArray(targetRole.getCurrentPosition()[0],0,targetRole.getCurrentPosition()[2]),list);

                            if(roles != null){
                                for(BattleRole battleRole : roles){
                                    if(battleRole != null){
                                        targetRoles.add(battleRole);
                                    }
                                }

                            }
                        }
                    }
                }
                if(paramB == 6){ //上一级载体对象

                    int paramC = list.get(2);
                    if(paramC == 0) { //当前基准点作为目标
                        return preCarrier;
                    }else{
                        if(paramC == 2){ //距离范围内搜索敌人

                            MapLogic mapLogic = preCarrier.mapLogic;

                            List<BattleRole> roles = getDistanceRangeRoles(mapLogic,TemporaryPoolManager.getPositionArray(preCarrier.getCurrentPosition()[0],0,preCarrier.getCurrentPosition()[2]),list);

                            if(roles != null){
                                for(BattleRole battleRole : roles){
                                    if(battleRole != null){
                                        targetRoles.add(battleRole);
                                    }
                                }
                            }
                        }
                    }
                }
                if(paramB == 7){ //继承上一级效果

                }
                if(paramB == 8){ //无

                }
            }

        }

        return targetRoles;

    }

    /**
     * 获取距离范围内的角色
     */
    private static List<BattleRole> getDistanceRangeRoles(MapLogic mapLogic,float[] startPos,List<Integer> params){
        float paramD = 0;
        float paramE = 0;
        if(params.size() >= 4){
            paramD = params.get(3)/100f;
        }
        if(params.size() >= 5){
            paramE = params.get(4)/100f;
        }
        List<BattleRole> roles = null;
        if(paramE != 0){
            roles = getTargetRoleByCircle(mapLogic,startPos,paramD,paramE);
        }else{
            if(paramD > 0){
                roles = getTargetRoleByCircle(mapLogic,startPos,0,paramD);
            }
        }
        return roles;
    }

    private static List<BattleRole> getTargetRoleByCircle(MapLogic mapLogic,float[] startPos,float minRadius,float maxRadius){
        List<BattleRole> battleRoleList = mapLogic.getInShapRole(startPos, SkillHitShapConstant.SKILL_HIT_SHAP_RING,minRadius,maxRadius);
        return battleRoleList;
    }

    public static short parseDirection(BasicSkillEffect effect,List<List<Integer>> location,BattleRole triggerRole){
        for(List<Integer> list :location) {
            int paramA = list.get(0); //目标类型

            if (paramA == 3) { //方向
                int paramB = list.get(1);
                if(paramB == 1){ //施法者
                    BasicRoleSkill roleSkill = effect.getHostSkill();
                    if(roleSkill.skillSeat >= 5){ //灵魂之影
                        return (short)roleSkill.soulRolePos[3];
                    }else{
                        BattleRole releaseSkillRole = effect.getHostSkill().getHostRole();
                        if(releaseSkillRole == null){
                            return -1;
                        }
                        return releaseSkillRole.getDirection();
                    }
                }
                if(paramB == 2){ //触发者
                    if(triggerRole == null){
                        return -1;
                    }
                    return triggerRole.getDirection();

                }
                if(paramB == 3){ //上一级载体
                    BasicSkillCarrier preCarrier = effect.getPreSkillCarrier();
                    if(preCarrier == null){
                        return -1;
                    }
                    return preCarrier.getDirection();
                }
                if(paramB == 4){ //指示器
                    return -1;
                }
                if(paramB == 5){ //锁定目标
                    BattleRole releaseSkillRole = effect.getHostSkill().getHostRole();
                    if(releaseSkillRole == null){
                        return  -1;
                    }
                    BattleRole targetRole = effect.getHostSkill().getHostRole().targetRole;
                    if(targetRole == null){
                        return -1;
                    }
                    return targetRole.getDirection();

                }
                if(paramB == 6){ //上一级载体对象
                    BasicSkillCarrier preCarrier = effect.getPreSkillCarrier();
                    if(preCarrier == null){
                        return -1;
                    }
                    return preCarrier.getDirection();
                }
                if(paramB == 7){ //继承上一级效果
                    return -1;
                }
                if(paramB == 8){ //无
                    return -1;
                }

                if(paramB > 100){
                    if(triggerRole == null){
                        return -1;
                    }
                    float[] startPos = getDirectionTargetPos(effect,triggerRole,paramB/100);
                    float[] endPos = getDirectionTargetPos(effect,triggerRole,paramB%100);
                    if(startPos != null && endPos != null){
                        return (short)CalculationUtil.vectorToAngle(endPos[0]-startPos[0],endPos[2]-startPos[2]);
                    }
                }
            }
        }
        return -1;
    }

    private static float[] getDirectionTargetPos(BasicSkillEffect effect,BattleRole triggerRole,int type){
        if(type == 1){//施法者
            BattleRole releaseSkillRole = effect.getHostSkill().getHostRole();

            if(releaseSkillRole != null){
                return TemporaryPoolManager.getPositionArray(
                        releaseSkillRole.getCurrentPosition()[0],
                        releaseSkillRole.getCurrentPosition()[1],
                        releaseSkillRole.getCurrentPosition()[2]
                );
            }

        }

        if(type == 2) {//触发者
            if(triggerRole != null){
                return TemporaryPoolManager.getPositionArray(
                        triggerRole.getCurrentPosition()[0],
                        triggerRole.getCurrentPosition()[1],
                        triggerRole.getCurrentPosition()[2]
                );
            }
        }

        if(type == 3) {//上一级载体

        }

        if(type == 4) {//指示器

        }

        if(type == 5) {//锁定目标
            BattleRole releaseSkillRole = effect.getHostSkill().getHostRole();
            BattleRole targetRole = effect.getHostSkill().getHostRole().targetRole;
            if(targetRole != null){
                return TemporaryPoolManager.getPositionArray(
                        targetRole.getCurrentPosition()[0],
                        targetRole.getCurrentPosition()[1],
                        targetRole.getCurrentPosition()[2]
                );
            }
        }

        if(type == 6) {//上一级载体对象
            BasicSkillCarrier preCarrier = effect.getPreSkillCarrier();
            if(preCarrier != null){
                return TemporaryPoolManager.getPositionArray(
                        preCarrier.getCurrentPosition()[0],
                        preCarrier.getCurrentPosition()[1],
                        preCarrier.getCurrentPosition()[2]
                );
            }
        }



        return null;
    }

}
