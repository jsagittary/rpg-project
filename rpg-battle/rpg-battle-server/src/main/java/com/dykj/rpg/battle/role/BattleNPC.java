package com.dykj.rpg.battle.role;

import com.dykj.rpg.battle.attribute.RoleAttributes;
import com.dykj.rpg.battle.constant.BattleCampConstant;
import com.dykj.rpg.battle.constant.RoleTypeConstant;
import com.dykj.rpg.battle.logic.BattleContainer;

public class BattleNPC extends BattleRole{

    /**
     *
     * @param battleContainer
     * @param roleAttributes
     */
    public void init(BattleContainer battleContainer, byte skillSourceType,int roleId, int roleLevel,int modelId,RoleAttributes roleAttributes) {
        super.init(battleContainer,null,0,skillSourceType, RoleTypeConstant.BATTLE_ROLE_MONSTER, BattleCampConstant.BATTLE_CAMP_ENEMY,roleId,roleLevel,modelId,roleAttributes);
    }

    @Override
    public void release() {

    }
}
