package com.dykj.rpg.battle.manager;

import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.battle.constant.RoleAnimConstant;
import com.dykj.rpg.battle.role.BattleRole;

/**
 * 动作管理器
 * 方法调用顺序  update --> addAnim --> getSyncAnim
 */
public class AnimManager {

    private BattleRole battleRole;

    private byte anim = RoleAnimConstant.ROLE_ANIM_WAIT;

    private boolean needSyncAnim = false;

    private int animTime = 0;

    private int animCreateFrame = 0;

    public AnimManager(BattleRole battleRole){
        this.battleRole = battleRole;
    }

    public void init(){
        anim = RoleAnimConstant.ROLE_ANIM_WAIT;
        needSyncAnim = false;
        animTime = 0;
        animCreateFrame = 0;
    }

    public void update(){
        if(this.animTime > 0){
            this.animTime -= GameConstant.FRAME_TIME;
        }
        if(this.animTime <= 0 && this.anim != RoleAnimConstant.ROLE_ANIM_WAIT){
            this.anim = RoleAnimConstant.ROLE_ANIM_WAIT;
        }
    }

    public boolean addAnim(int frameNum,byte _anim,int _animTime){
//        if(battleRole.getModelId() == 2){
//            System.out.println("addAnim _anim = "+_anim+"  _animTime = "+_animTime);
//        }
        //死亡优先级最高
        if(this.anim == RoleAnimConstant.ROLE_ANIM_DEAD){
            return false;
        }

        if(_anim == RoleAnimConstant.ROLE_ANIM_DEAD){
            this.anim = _anim;
            this.animTime = _animTime;
            this.animCreateFrame = frameNum;
            needSyncAnim = true;

            return true;
        }

        //硬直级别最高
        if(this.anim == RoleAnimConstant.ROLE_ANIM_STIFF){
            return false;
        }

        if(this.anim == RoleAnimConstant.ROLE_ANIM_SKILL){
            if(_anim == RoleAnimConstant.ROLE_ANIM_HIT){
                return false;
            }
        }

        if(this.anim != _anim){
            needSyncAnim = true;
        }

        this.anim = _anim;
        this.animTime = _animTime;
        this.animCreateFrame = frameNum;

        return true;

    }

    public byte getAnim(){
        return anim;
    }

    public byte getSyncAnim(){
        byte resultAnim = 0;
        if(needSyncAnim){
            needSyncAnim = false;
            resultAnim = anim;
        }else{
            if(anim == RoleAnimConstant.ROLE_ANIM_WAIT){
                resultAnim = anim;
            }else{
                resultAnim = RoleAnimConstant.ROLE_ANIM_CONTINUE;
            }
        }

//        if(battleRole.getModelId() == 2){
//            System.out.println("AnimManager getSyncAnim anim = "+resultAnim);
//        }
        return resultAnim;
    }

}
