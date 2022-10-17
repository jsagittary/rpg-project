package com.dykj.rpg.battle.data;

import com.dykj.rpg.protocol.battle.BattlePosition;

public class WaitReleaseSkillData {

    public int frameNum;

    public int skillId;

    public BattlePosition pos;

    public WaitReleaseSkillData(){

    }

    public WaitReleaseSkillData(int frameNum,int skillId,BattlePosition pos){
        this.frameNum = frameNum;
        this.skillId = skillId;
        this.pos = pos;
    }

}
