package com.dykj.rpg.battle.logic;

import com.dykj.rpg.battle.attribute.RoleAttributes;
import com.dykj.rpg.battle.constant.RoleTypeConstant;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.common.attribute.consts.AttributeBasicConstant;
import com.dykj.rpg.common.attribute.consts.ElementTypeConstant;

import java.util.Random;

/**
 * 伤害计算
 */
public class DodgeCalculation {

    private final static float LV_ZHI = 10000f;

    /**
     * 基础命中率 = 命中值 / 闪避值          值域【0，1】
     * 最终命中率 = 基础命中率 + 附加命中率   值域【0，1】
     *
     * @param skillRole
     * @param targetRole
     */

    public static boolean isDodge(BattleRole skillRole,BattleRole targetRole){
        Random random = new Random(System.currentTimeMillis());

        /**
         * 闪避计算
         */
        boolean shanbi = false;
        float jichumingzhonglv = (float)skillRole.buffManager.getAttributeFromBuff(AttributeBasicConstant.MING_ZHONG_ZHI,0)/targetRole.buffManager.getAttributeFromBuff(AttributeBasicConstant.SHAN_BI_ZHI,0);
        jichumingzhonglv = jichumingzhonglv > 1 ? 1f : jichumingzhonglv;
        float zuizhongmingzhonglv = jichumingzhonglv + skillRole.buffManager.getAttributeFromBuff(AttributeBasicConstant.FU_JIA_MING_ZHONG_LV,0)/LV_ZHI;
        zuizhongmingzhonglv = zuizhongmingzhonglv > 1 ? 1f : zuizhongmingzhonglv;
        if(random.nextInt(10000) > zuizhongmingzhonglv*10000){
            shanbi = true;
        }

        return shanbi;

    }

}
