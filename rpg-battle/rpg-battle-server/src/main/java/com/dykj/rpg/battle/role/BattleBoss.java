package com.dykj.rpg.battle.role;

/**
 * Boss行为和数据处理类
 */

import com.dykj.rpg.battle.attribute.RoleAttributes;
import com.dykj.rpg.battle.constant.BattleCampConstant;
import com.dykj.rpg.battle.constant.RoleTypeConstant;
import com.dykj.rpg.battle.logic.BattleContainer;
import org.recast4j.detour.StraightPathItem;

import java.util.ArrayList;
import java.util.List;

public class BattleBoss extends BattleRole{

    public int monsterId;

    private float[] currentPosition = new float[3];

    private float[] targetPosition  = new float[3];

    List<StraightPathItem> detourResults = new ArrayList<>();

    private RunNextPos runNextPos = new RunNextPos();

    //是否需要等待别人先路过的帧数
    //private boolean needAvoidOrther = false;

    private int needAvoidFrame = 0;

    private boolean runFinish = false;

    public void init(BattleContainer battleContainer, byte skillSourceType,int roleId, int roleLevel, int modelId,RoleAttributes roleAttributes){
        super.init(battleContainer,null,0, skillSourceType,RoleTypeConstant.BATTLE_ROLE_MONSTER, BattleCampConstant.BATTLE_CAMP_ENEMY,roleId,roleLevel,modelId,roleAttributes);

    }

    public float[] getCurrentPosition(){
        return this.currentPosition;
    }

    public float[] getTargetPosition(){
        return this.targetPosition;
    }

    public void setDetourResults(List<StraightPathItem> detourResults) {
        this.detourResults = detourResults;
        detourResults.remove(0);
        float[] detourResultPos = detourResults.get(0).getPos();
        runNextPos.state = 1;
        runNextPos.nextPos[0] = detourResultPos[0];
        runNextPos.nextPos[1] = detourResultPos[1];
        runNextPos.nextPos[2] = detourResultPos[2];
    }

    public boolean run() {
        return false;

    }

    @Override
    public void release() {

    }

    class RunNextPos{
        byte state = 0; //0=无效，1=正常路径，2=避让路径
        float[] nextPos = new float[3];
    }
}
