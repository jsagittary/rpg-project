package com.dykj.rpg.battle.manager;

import com.dykj.rpg.battle.basic.*;
import com.dykj.rpg.battle.logic.BattleObjectPool;
import com.dykj.rpg.battle.logic.SkillReleaseData;
import com.dykj.rpg.battle.role.BattleMonster;
import com.dykj.rpg.battle.role.BattlePlayer;
import com.dykj.rpg.battle.role.BattleRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BattlePoolManager {

    Logger logger = LoggerFactory.getLogger("BattlePoolManager");

    private BattleObjectPool<BattlePlayer> battlePlayerPool;

    private BattleObjectPool<BattleMonster> battleMonsterPool;

    private int borrowRoleSkillNum = 0;
    private int restoreRoleSkillNum = 0;
    private BattleObjectPool<BasicRoleSkill> roleSkillPool;

    private int borrowSkillEffectNum = 0;
    private int restoreSkillEffectNum = 0;
    private BattleObjectPool<BasicSkillEffect> skillEffectPool;

    private int borrowSkillCarrierNum = 0;
    private int restoreSkillCarrierNum = 0;
    private BattleObjectPool<BasicSkillCarrier> skillCarrierPool;

    private int borrowSkillBuffNum = 0;
    private int restoreSkillBuffNum = 0;
    private BattleObjectPool<BasicSkillBuff> skillBuffPool;

    private int borrowSkillReleaseDataNum = 0;
    private int restoreSkillReleaseDataNum = 0;
    private BattleObjectPool<SkillReleaseData> skillReleaseDataPool;


    public BattlePoolManager(){
        battlePlayerPool = new BattleObjectPool<>(BattlePlayer.class,10,1200000);
        battleMonsterPool = new BattleObjectPool<>(BattleMonster.class,50,1200000);
        roleSkillPool = new BattleObjectPool<>(BasicRoleSkill.class,50*5,1200000);
        skillEffectPool = new BattleObjectPool<>(BasicSkillEffect.class,50*50,1200000);
        skillCarrierPool = new BattleObjectPool<>(BasicSkillCarrier.class,50*50,60000);
        skillBuffPool = new BattleObjectPool<>(BasicSkillBuff.class,50*10,120000);
        skillReleaseDataPool = new BattleObjectPool<>(SkillReleaseData.class,50*10,60000);
    }

    public void release(){
        battlePlayerPool.releaseAll();
        battleMonsterPool.releaseAll();

        borrowRoleSkillNum = 0;
        restoreRoleSkillNum = 0;
        roleSkillPool.releaseAll();

        borrowSkillEffectNum = 0;
        restoreSkillEffectNum = 0;
        skillEffectPool.releaseAll();

        borrowSkillCarrierNum = 0;
        restoreSkillCarrierNum = 0;
        skillCarrierPool.releaseAll();

        borrowSkillBuffNum = 0;
        restoreSkillBuffNum = 0;
        skillBuffPool.releaseAll();

        borrowSkillReleaseDataNum = 0;
        restoreSkillReleaseDataNum = 0;
        skillReleaseDataPool.releaseAll();
    }

    public BattlePlayer borrowBattlePlayer(){
        BattlePlayer battlePlayer = battlePlayerPool.borrow();
        if(battlePlayer == null){
            logger.error("borrow battle player from pool over limit !!!");
        }else{

        }
        return battlePlayer;
    }

    public void restoreBattlePlayer(BattlePlayer battlePlayer){
        battlePlayerPool.restore(battlePlayer);
    }

    public BattleMonster borrowBattleMonster(){
        BattleMonster battleMonster = battleMonsterPool.borrow();
        if(battleMonster == null){
            logger.error("borrow battle monster from pool over limit !!!");
        }
        return battleMonster;
    }

    public void restoreBattleMonster(BattleMonster battleMonster){
        battleMonsterPool.restore(battleMonster);
    }

    public BasicRoleSkill borrowRoleSkill(){
        BasicRoleSkill roleSkill = roleSkillPool.borrow();
        if(roleSkill == null){
            logger.error("borrow battle role skill from pool over limit !!!");
        }
        return roleSkill;
    }

    public void restoreRoleSkill(BasicRoleSkill roleSkill){
        roleSkillPool.restore(roleSkill);
    }

    public BasicSkillEffect borrowSkillEffect(){
        BasicSkillEffect skillEffect = skillEffectPool.borrow();
        if(skillEffect == null){
            logger.error("borrow battle skill effect from pool over limit !!! size = "+skillEffectPool.getSize()+" borrowSkillEffectNum = "+borrowSkillEffectNum+" restoreSkillEffectNum = "+restoreSkillEffectNum);
        }else{
            borrowSkillEffectNum ++;
        }
        //System.out.println("borrowSkillEffect borrowSkillEffectNum = "+borrowSkillEffectNum+" restoreSkillEffectNum = "+restoreSkillEffectNum);
        return skillEffect;
    }

    public void restoreSkillEffect(BasicSkillEffect skillEffect){
        if(skillEffectPool.restore(skillEffect)){
            restoreSkillEffectNum ++;
        }
        //System.out.println("restoreSkillEffect borrowSkillEffectNum = "+borrowSkillEffectNum+" restoreSkillEffectNum = "+restoreSkillEffectNum);
    }

    public BasicSkillCarrier borrowSkillCarrier(){
        BasicSkillCarrier skillCarrier = skillCarrierPool.borrow();
        if(skillCarrier == null){
            logger.error("borrow battle skill carrier from pool over limit !!!  borrowSkillCarrierNum = "+borrowSkillCarrierNum+" restoreSkillCarrierNum = "+restoreSkillCarrierNum);
        }else{
            borrowSkillCarrierNum ++;
        }

        //System.out.println("borrowSkillCarrier borrowSkillCarrierNum = "+borrowSkillCarrierNum+" restoreSkillCarrierNum = "+restoreSkillCarrierNum);

        return skillCarrier;
    }

    public void restoreSkillCarrier(BasicSkillCarrier skillCarrier){
        //System.out.println("restoreSkillCarrier  carrierId = "+skillCarrier.getCarrierId());
        if(skillCarrierPool.restore(skillCarrier)){
            restoreSkillCarrierNum ++;
        }
        //System.out.println("restoreSkillCarrier borrowSkillCarrierNum = "+borrowSkillCarrierNum+" restoreSkillCarrierNum = "+restoreSkillCarrierNum);
    }

    public BasicSkillBuff borrowSkillBuff(){
        BasicSkillBuff skillBuff = skillBuffPool.borrow();
        if(skillBuff == null){
            logger.error("borrow battle skill buff from pool over limit !!!  borrowSkillBuffNum = "+borrowSkillBuffNum+" restoreSkillBuffNum = "+restoreSkillBuffNum);
        }else{
            borrowSkillBuffNum ++;
        }

        //System.out.println("borrowSkillCarrier borrowSkillCarrierNum = "+borrowSkillCarrierNum+" restoreSkillCarrierNum = "+restoreSkillCarrierNum);

        return skillBuff;
    }

    public void restoreSkillBuff(BasicSkillBuff skillbuff){
        //System.out.println("restoreSkillCarrier  carrierId = "+skillCarrier.getCarrierId());
        if(skillBuffPool.restore(skillbuff)){
            restoreSkillBuffNum ++;
        }
        //System.out.println("restoreSkillCarrier borrowSkillCarrierNum = "+borrowSkillCarrierNum+" restoreSkillCarrierNum = "+restoreSkillCarrierNum);
    }

    public SkillReleaseData borrowSkillReleaseData(){
        SkillReleaseData releaseData = skillReleaseDataPool.borrow();
        if(releaseData == null){
            logger.error("borrow battle skill release data from pool over limit !!!  borrowSkillReleaseDataNum = "+borrowSkillReleaseDataNum+" restoreSkillReleaseDataNum = "+restoreSkillReleaseDataNum);
        }else{
            borrowSkillReleaseDataNum ++;
        }
        return releaseData;
    }

    public void restoreSkillReleaseData(SkillReleaseData releaseData){
        if(skillReleaseDataPool.restore(releaseData)){
            restoreSkillReleaseDataNum ++;
        }
    }

    public void poolSelfCheck(){
        battlePlayerPool.selfCheck();
        battleMonsterPool.selfCheck();
        skillCarrierPool.selfCheck();
    }

    public void scanBattlePool(){
        skillEffectPool.printOverTimeObject(System.currentTimeMillis());
        skillCarrierPool.printOverTimeObject(System.currentTimeMillis());
        skillBuffPool.printOverTimeObject(System.currentTimeMillis());
    }


}
