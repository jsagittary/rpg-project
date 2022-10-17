package com.dykj.rpg.battle.logic;

import com.dykj.rpg.battle.attribute.AttributeConfig;
import com.dykj.rpg.battle.attribute.RoleAttributes;
import com.dykj.rpg.battle.basic.BasicRoleSkill;
import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.battle.logic.BattleContainer;
import com.dykj.rpg.battle.manager.BattleManager;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.common.attribute.consts.AttributeBasicConstant;
import com.dykj.rpg.common.attribute.consts.ElementTypeConstant;
import com.dykj.rpg.protocol.battle.BattleHitLogData;
import com.dykj.rpg.protocol.battle.RoleBasicAttributeLog;

import java.util.List;
import java.util.Random;

/**
 * 伤害计算
 */
public class HurtCalculation {

    public final static int HURT_STATE_NORMAL = 1;

    public final static int HURT_STATE_SHANBI = 2;

    public final static int HURT_STATE_BAOJI = 3;

    public final static int HURT_STATE_GEDANG = 4;

    public final static int HURT_STATE_ZHILIAO = 5;

    public final static int HURT_STATE_BUFF = 6;

    private final static float LV_ZHI = 10000f;

    /**
     * （（全攻击值+元素攻击值）*技能加成率+技能附加值）*
     * （全破防值+元素破防值）/（全防御值+元素防御值）*
     * （1+全元素效果强化系数+元素效果强化系数）/（1+全伤害减比+元素伤害减比）*（1+条件伤害增比）/（1+条件伤害减比）*（1+特殊伤害增比）/（1+特殊伤害减比）*
     * 闪避修正 * 暴击修正 * 格挡修正
     *
     * @param skillRole
     * @param elementType 元素类型
     * @param damageProp  技能加成率
     * @param damageValue 技能附加值
     * @param targetRole
     */
    public static Random random = new Random(System.currentTimeMillis());


    public static int[] getHurt(BattleRole skillRole, int skillId,int elementType, int damageProp,int damageValue ,BattleRole targetRole){

        int hurtState = HURT_STATE_NORMAL;
        float hurtNum = 0;

        /**
         * 基础值计算
         */
        float jichugongjizhi = 0f;
        float pofangzhi = 0f;
        float fangyuzhi = 0f;
        float yuansuxishu = 1f;
        float tiaojianxishu = 1f;
        float teshuxishu = 1f;
        float shanghaifudonglv = 1f;
        float shanghaifudongzhi = 0f;

        float baoji = 1f;
        float gedang = 1f;

        /**
         * 通过buff进行基础值计算
         */
        //damageProp = skillRole.getBuffAttribute(AttributeBasicConstant.SHANG_HAI_FU_DONG_LV,0,damageProp);
        //damageValue = skillRole.getBuffAttribute(AttributeBasicConstant.SHANG_HAI_FU_DONG_ZHI,0,damageValue);
        //攻方
        int _quanGongJiZhi = skillRole.getBuffAttribute(AttributeBasicConstant.GONG_JI_ZHI,ElementTypeConstant.QUAN);
        int _yuanSuGongJiZhi = skillRole.getBuffAttribute(AttributeBasicConstant.GONG_JI_ZHI,elementType);
        //攻方
        int _quanPofangZhi = skillRole.getBuffAttribute(AttributeBasicConstant.PO_FANG_ZHI,ElementTypeConstant.QUAN);
        int _yuanSuPoFangZhi = skillRole.getBuffAttribute(AttributeBasicConstant.PO_FANG_ZHI,elementType);
        //守方
        int _quanFangYuZhi = targetRole.getBuffAttribute(AttributeBasicConstant.FANG_YU_ZHI,ElementTypeConstant.QUAN);
        int _yuanSuFangYuZhi = targetRole.getBuffAttribute(AttributeBasicConstant.FANG_YU_ZHI,elementType);
        //攻方
        int _shangHaiFuDongLv = skillRole.getBuffAttribute(AttributeBasicConstant.SHANG_HAI_FU_DONG_LV,0);
        int _shangHaiFuDongZhi = skillRole.getBuffAttribute(AttributeBasicConstant.SHANG_HAI_FU_DONG_ZHI,0);

        /**
         * 基础值计算
         */
        jichugongjizhi = (_quanGongJiZhi+_yuanSuGongJiZhi)*damageProp/LV_ZHI+damageValue;
        pofangzhi = _quanPofangZhi+_yuanSuPoFangZhi;
        fangyuzhi = _quanFangYuZhi+_yuanSuFangYuZhi;
        shanghaifudonglv = 1f+(random.nextInt(2*_shangHaiFuDongLv+1)-_shangHaiFuDongLv)/LV_ZHI;
        shanghaifudongzhi = (random.nextInt(2*_shangHaiFuDongZhi+1)-_shangHaiFuDongZhi);

        /**
         *---------------------暴击判断---------------------
         * 基础暴击率 = (暴击值 / 免暴值 -1)  值域【0，1】
         * 最终暴击率 = 基础暴击率 + 附加暴击率  值域【0，1】
         */

        int _baoJiZhi = skillRole.getBuffAttribute(AttributeBasicConstant.BAO_JI_ZHI,0);
        int _mianBaoZhi = targetRole.getBuffAttribute(AttributeBasicConstant.MIAN_BAO_ZHI,0);
        int _fuJiaBaoJiLv = skillRole.getBuffAttribute(AttributeBasicConstant.FU_JIA_BAO_JI_LV,0);

        float jichubaojilv = (float)_baoJiZhi/(float)_mianBaoZhi-1f;
        jichubaojilv = jichubaojilv < 0 ? 0 : jichubaojilv;
        jichubaojilv = jichubaojilv > 1 ? 1 : jichubaojilv;
        float zuizhongbaojilv = jichubaojilv + _fuJiaBaoJiLv/LV_ZHI;
        zuizhongbaojilv = zuizhongbaojilv < 0 ? 0 : zuizhongbaojilv;
        zuizhongbaojilv = zuizhongbaojilv > 1 ? 1 : zuizhongbaojilv;

        float baojishanghaibeilv = 1f;
        if(random.nextInt(10000) < zuizhongbaojilv*10000){
            hurtState = HURT_STATE_BAOJI;

            /** ---------------------暴击倍率---------------------
            * 基础暴击伤害倍率 = 暴伤值 / 韧性值  值域【0，+∞】
            * 最终暴击伤害倍率 = 基础暴击伤害倍率 + 附加暴击伤害倍率  值域【0，+∞】
            */
            int _baoShangZhi = skillRole.getBuffAttribute(AttributeBasicConstant.BAO_SHANG_ZHI,0);
            int _renXingZhi = targetRole.getBuffAttribute(AttributeBasicConstant.REN_XING_ZHI,0);
            int _fuJiaBaoJiShangHaiBeiLv = skillRole.getBuffAttribute(AttributeBasicConstant.FU_JIA_BAO_JI_SHANG_HAI_BEI_LV,0);

            baojishanghaibeilv = (float)_baoShangZhi/(float)_renXingZhi + _fuJiaBaoJiShangHaiBeiLv/LV_ZHI + 1f;
        }

        /**
         * ---------------------格挡判断---------------------
         * 基础格挡率 =（ 1- 破击值 / 格挡值）  值域【0，1】
         * 最终格挡率 = 基础格挡率 + 附加格挡率  值域【0，1】
         */

        int _poJiZhi = skillRole.getBuffAttribute(AttributeBasicConstant.PO_JI_ZHI,0);
        int _geDangZhi = targetRole.getBuffAttribute(AttributeBasicConstant.GE_DANG_ZHI,0);
        int _fuJiaGeDangLv = skillRole.getBuffAttribute(AttributeBasicConstant.FU_JIA_GE_DANG_LV,0);

        float jichugedanglv = 1 - (float)_poJiZhi/(float)_geDangZhi;
        jichugedanglv = jichugedanglv < 0 ? 0 : jichugedanglv;
        jichugedanglv = jichugedanglv > 1 ? 1 : jichugedanglv;
        float zuizhonggedanglv = jichugedanglv + _fuJiaGeDangLv/LV_ZHI;
        zuizhonggedanglv = zuizhonggedanglv < 0 ? 0 : zuizhonggedanglv;
        zuizhonggedanglv = zuizhonggedanglv > 1 ? 1 : zuizhonggedanglv;

        float zuizhonggedangshanghaibeilv = 1f;
        if(random.nextInt(10000) < zuizhonggedanglv*10000){
            hurtState = HURT_STATE_GEDANG;

            /** ---------------------格挡倍率---------------------
            * 基础格挡伤害倍率 = 破伤值 / 档伤值  值域【0，1】
            * 最终格挡伤害倍率 = 基础格挡伤害倍率 + 附加格挡伤害倍率  值域【0，1】
            */
            int _poShangZhi = skillRole.getBuffAttribute(AttributeBasicConstant.PO_SHANG_ZHI,0);
            int _dangShangZhi = targetRole.getBuffAttribute(AttributeBasicConstant.DANG_SHANG_ZHI,0);
            int _fuJiaGeDangShangHaiBeiLv = skillRole.getBuffAttribute(AttributeBasicConstant.FU_JIA_GE_DANG_SHANG_HAI_BEI_LV,0);

            float jichugedangshanghaibeilv = (float)_poShangZhi/(float)_dangShangZhi;
            jichugedangshanghaibeilv = jichugedangshanghaibeilv < 0 ? 0 : jichugedangshanghaibeilv;
            jichugedangshanghaibeilv = jichugedangshanghaibeilv >1 ? 1 : jichugedangshanghaibeilv;
            zuizhonggedangshanghaibeilv = jichugedangshanghaibeilv + _fuJiaGeDangShangHaiBeiLv/LV_ZHI;
            zuizhonggedangshanghaibeilv = zuizhonggedangshanghaibeilv < 0 ? 0 : zuizhonggedangshanghaibeilv;
            zuizhonggedangshanghaibeilv = zuizhonggedangshanghaibeilv >1 ? 1 : zuizhonggedangshanghaibeilv;

        }


        /**
         * 特殊计算
         */
        if(elementType == ElementTypeConstant.ZHEN_SHI){

            hurtState = HURT_STATE_NORMAL;

            hurtNum = jichugongjizhi*shanghaifudonglv + shanghaifudongzhi;

        }else if(elementType == ElementTypeConstant.ZHI_LIAO){

            hurtState = HURT_STATE_ZHILIAO;

            int _yuanSuXiaoGuoQiangHuaXiShu = skillRole.getBuffAttribute(AttributeBasicConstant.YUAN_SU_XIAO_GUO_QIANG_HUA_XI_SHU,elementType);
            int _yuanSuXiaoGuoRuoHuaXiShu = targetRole.getBuffAttribute(AttributeBasicConstant.YUAN_SU_XIAO_GUO_RUO_HUA_XI_SHU,elementType);
            int _tiaoJianXiaoGuoQiangHuaXiShu = skillRole.getBuffAttribute(AttributeBasicConstant.TIAO_JIAN_XIAO_GUO_QIANG_HUA_XI_SHU,0);
            int _tiaoJianXiaoGuoRuoHuaXiShu = targetRole.getBuffAttribute(AttributeBasicConstant.TIAO_JIAN_XIAO_GUO_QIANG_HUA_XI_SHU,0);
            int _teShuXiaoGuoQiangHuaXiShu = skillRole.getBuffAttribute(AttributeBasicConstant.TE_SHU_XIAO_GUO_QIANG_HUA_XI_SHU,0);
            int _teShuXiaoGuoRuoHuaXiShu = targetRole.getBuffAttribute(AttributeBasicConstant.TE_SHU_XIAO_GUO_RUO_HUA_XI_SHU,0);

            yuansuxishu = (1+_yuanSuXiaoGuoQiangHuaXiShu/LV_ZHI)/(1+_yuanSuXiaoGuoRuoHuaXiShu/LV_ZHI);
            tiaojianxishu = (1+_tiaoJianXiaoGuoQiangHuaXiShu/LV_ZHI)/(1+_tiaoJianXiaoGuoRuoHuaXiShu/LV_ZHI);
            teshuxishu = (1+_teShuXiaoGuoQiangHuaXiShu/LV_ZHI)/(1+_teShuXiaoGuoRuoHuaXiShu/LV_ZHI);

            hurtNum = jichugongjizhi*pofangzhi/fangyuzhi*yuansuxishu*tiaojianxishu*teshuxishu*shanghaifudonglv+shanghaifudongzhi;

        }else{

            int _quanYuanSuXiaoGuoQiangHuaXiShu = skillRole.getBuffAttribute(AttributeBasicConstant.YUAN_SU_XIAO_GUO_QIANG_HUA_XI_SHU,ElementTypeConstant.QUAN);
            int _quanYuanSuXiaoGuoRuoHuaXiShu = targetRole.getBuffAttribute(AttributeBasicConstant.YUAN_SU_XIAO_GUO_RUO_HUA_XI_SHU,ElementTypeConstant.QUAN);
            int _yuanSuXiaoGuoQiangHuaXiShu = skillRole.getBuffAttribute(AttributeBasicConstant.YUAN_SU_XIAO_GUO_QIANG_HUA_XI_SHU,elementType);
            int _yuanSuXiaoGuoRuoHuaXiShu = targetRole.getBuffAttribute(AttributeBasicConstant.YUAN_SU_XIAO_GUO_RUO_HUA_XI_SHU,elementType);

            int _tiaoJianXiaoGuoQiangHuaXiShu = skillRole.getBuffAttribute(AttributeBasicConstant.TIAO_JIAN_XIAO_GUO_QIANG_HUA_XI_SHU,0);
            int _tiaoJianXiaoGuoRuoHuaXiShu = targetRole.getBuffAttribute(AttributeBasicConstant.TIAO_JIAN_XIAO_GUO_QIANG_HUA_XI_SHU,0);
            int _teShuXiaoGuoQiangHuaXiShu = skillRole.getBuffAttribute(AttributeBasicConstant.TE_SHU_XIAO_GUO_QIANG_HUA_XI_SHU,0);
            int _teShuXiaoGuoRuoHuaXiShu = targetRole.getBuffAttribute(AttributeBasicConstant.TE_SHU_XIAO_GUO_RUO_HUA_XI_SHU,0);

            yuansuxishu = (1+_quanYuanSuXiaoGuoQiangHuaXiShu/LV_ZHI+_yuanSuXiaoGuoQiangHuaXiShu/LV_ZHI)/(1+_quanYuanSuXiaoGuoRuoHuaXiShu/LV_ZHI+_yuanSuXiaoGuoRuoHuaXiShu/LV_ZHI);

            tiaojianxishu = (1+_tiaoJianXiaoGuoQiangHuaXiShu/LV_ZHI)/(1+_tiaoJianXiaoGuoRuoHuaXiShu/LV_ZHI);
            teshuxishu = (1+_teShuXiaoGuoQiangHuaXiShu/LV_ZHI)/(1+_teShuXiaoGuoRuoHuaXiShu/LV_ZHI);

            hurtNum = jichugongjizhi*pofangzhi/fangyuzhi*yuansuxishu*tiaojianxishu*teshuxishu*baojishanghaibeilv*zuizhonggedangshanghaibeilv*shanghaifudonglv+shanghaifudongzhi;

        }

        showCalculationLog(skillRole,skillId,elementType,damageProp,damageValue,targetRole,hurtState,(int)Math.floor(hurtNum));

        return new int[]{hurtState,(int)Math.floor(hurtNum)};
    }

    public static void showCalculationLog(BattleRole skillRole, int skillId,int elementType, int damageProp,int damageValue ,BattleRole targetRole,int hurtState,int hurtNum){

        if(GameConstant.LOG_HURT_CALCULATION){
            int playerId = skillRole.getPlayerId();
            if(playerId == 0){
                playerId = targetRole.getPlayerId();
            }
            if(playerId != 0){
                BattleContainer battleContainer = BattleManager.getInstance().getBattleContainerByPlayerId(playerId);

                BattleHitLogData logData = new BattleHitLogData();
                logData.setFrameNum(battleContainer.frameNum);
                logData.setReleaseModelId(skillRole.getModelId());
                List<RoleBasicAttributeLog> releaseAtrributes = logData.getReleaseAtrributes();
                for(AttributeConfig config : skillRole.attributeManager.getAllAttributeConfigs()){
                    int num = skillRole.getBuffAttribute((short)config.id, (byte)config.type1);
                    releaseAtrributes.add(new RoleBasicAttributeLog((short)config.id, (byte)config.type1, num));
                }
                logData.setTargetModelId(targetRole.getModelId());
                List<RoleBasicAttributeLog> targetAtrributes = logData.getTargetAtrributes();
                for(AttributeConfig config : targetRole.attributeManager.getAllAttributeConfigs()){
                    int num = skillRole.getBuffAttribute((short)config.id, (byte)config.type1);
                    targetAtrributes.add(new RoleBasicAttributeLog((short)config.id, (byte)config.type1, num));
                }
                logData.setSkillId(skillId);
                logData.setElementType((byte)elementType);
                logData.setShanbi(hurtState==HURT_STATE_SHANBI);
                logData.setBaoji(hurtState==HURT_STATE_BAOJI);
                logData.setGedang(hurtState==HURT_STATE_GEDANG);
                logData.setZhiliao(hurtState==HURT_STATE_ZHILIAO);
                logData.setHurtNum(hurtNum);

                int blood = targetRole.attributeManager.curBlood;
                if(hurtState == HURT_STATE_ZHILIAO){
                    blood += hurtNum;
                }else{
                    blood -= hurtNum;
                }
                if(blood < 0){
                    blood = 0;
                }
                if(blood > targetRole.attributeManager.maxBlood){
                    blood = targetRole.attributeManager.maxBlood;
                }
                logData.setTargetCurBlood(blood);

                BasicRoleSkill basicRoleSkill = skillRole.skillManager.getRoleSkillBySkillId(skillId);
                logData.setSkillSourceType((byte)basicRoleSkill.getSkillSourceType());
                logData.setSkillSourceNum(basicRoleSkill.getSkillSourceNum());
                logData.setReleaseCurskillSourceNum(skillRole.attributeManager.getSkillSourceNumByType(basicRoleSkill.getSkillSourceType()));

                //System.out.println("logData size = "+logData.getEncodeSize());
                System.out.println(logData.toString());

                battleContainer.sendBattleData(logData);
            }
        }
    }

}
