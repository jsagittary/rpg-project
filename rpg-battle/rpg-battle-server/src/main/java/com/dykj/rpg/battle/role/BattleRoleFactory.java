package com.dykj.rpg.battle.role;

import com.dykj.rpg.battle.attribute.RoleAttributes;
import com.dykj.rpg.battle.config.TileNpcDetail;
import com.dykj.rpg.battle.constant.*;
import com.dykj.rpg.battle.dictionary.StaticDictionary;
import com.dykj.rpg.battle.logic.BattleContainer;
import com.dykj.rpg.common.config.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 战斗对象工厂类
 */
public class BattleRoleFactory {

    private static Logger logger = LoggerFactory.getLogger("BattleRoleFactory");

    public static BattlePlayer createBattlePlayer(BattleContainer battleContainer,int playerId, byte mapIndex, int roleId,int roleLevel){
        BattlePlayer player = battleContainer.battlePoolManager.borrowBattlePlayer();
        if(player != null){
            int modelId = battleContainer.createNewModelId();
            CharacterBasicModel basicModel = StaticDictionary.getInstance().getCharacterBasicModelById(roleId);
            if(basicModel == null){
                logger.error("createBattlePlayer error !!! CharacterBasicModel[roleId="+roleId+"] not found !!!");
                return null;
            }
            int characterId = basicModel.getCharacterId();
            int skillSourceType = basicModel.getEnergyType();
            CharacterAttributesModel attributesModel = StaticDictionary.getInstance().getCharacterAttributesModelByGrowClassAndLevel(basicModel.getGrowClass(), roleLevel);
            if(attributesModel == null){
                logger.error("createBattlePlayer error !!! CharacterAttributesModel[growClass="+basicModel.getGrowClass()+",roleLevel="+roleLevel+"] not found !!!");
                return null;
            }

            RoleAttributes roleAttributes = new RoleAttributes();
            roleAttributes.addAttributes(basicModel.getInitializeAttributes());
            roleAttributes.addAttributes(attributesModel.getMainattributes());
            roleAttributes.addAttributes(attributesModel.getGeneralattributes());

            //RoleBattleModel roleBattleModel = createRoleBattleModel(mapIndex,modelId);

            //RolePositionModel rolePositionModel = createRolePositionModel(modelId);

            player.init(battleContainer,playerId,skillSourceType,roleId,roleLevel,modelId,basicModel,roleAttributes);
        }
        return player;
    }

    public static BattleMonster createBattleMonster(BattleContainer battleContainer, byte mapIndex, TileNpcDetail tileNpcDetail){

        BattleMonster monster = battleContainer.battlePoolManager.borrowBattleMonster();
        if(monster != null){
            int modelId = battleContainer.createNewModelId();

            NpcBasicModel basicModel = StaticDictionary.getInstance().getNpcBasicModelById(tileNpcDetail.npcId);
            if(basicModel == null){
                logger.error("createBattleMonster error !!! NpcBasicModel[npcId="+tileNpcDetail.npcId+"] not found !!!");
                return null;
            }

            NpcAttribModel attribModel = StaticDictionary.getInstance().getNpcAttributeModelByGrowClassAndNpcLv(basicModel.getGrowClass(), battleContainer.monsterLevel);
            if(attribModel == null){
                logger.error("createBattleMonster error !!! NpcAttributeModel[growClass="+basicModel.getGrowClass()+",npcLevel="+battleContainer.monsterLevel+"] not found !!!");
                return null;
            }

            NpcAiModel npcAiModel = StaticDictionary.getInstance().getNpcAiModelById(basicModel.getAiId());
            if(npcAiModel == null){
                logger.error("createBattleMonster error !!! NpcAiModel[npcId="+tileNpcDetail.npcId+",aiId="+basicModel.getAiId()+"] not found !!!");
                return null;
            }

            RoleAttributes roleAttributes = new RoleAttributes();
            roleAttributes.addAttributes(attribModel.getMainAttrib());
            roleAttributes.addAttributes(attribModel.getGeneralAttrib());

            //RoleBattleModel roleBattleModel = createRoleBattleModel(mapIndex,modelId);

            //RolePositionModel rolePositionModel = createRolePositionModel(modelId);

            monster.init(battleContainer,tileNpcDetail,0,battleContainer.monsterLevel,modelId,basicModel,npcAiModel,roleAttributes);

            //初始化默认属性
            monster.buffManager.initDefaultAttribute();
        }else{
            logger.error("battle role number over limit !!!");
        }

        return monster;
    }

}
