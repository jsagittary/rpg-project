package com.dykj.rpg.battle.dictionary;

import com.dykj.rpg.battle.manager.BuffManager;
import com.dykj.rpg.common.config.dao.*;
import com.dykj.rpg.common.config.model.*;
import com.dykj.rpg.util.spring.BeanFactory;

import java.util.*;

public class StaticDictionary {

    private static StaticDictionary instance;

    Map<Integer,CharacterAttributesModel> characterAttributesModelMap;

    Map<Integer,CharacterBasicModel> characterBasicModelMap;

    Map<Integer,SkillCharacterBasicModel> skillCharacterBasicModelMap;

    Map<Integer,SkillCharacterCarrierModel> skillCharacterCarrierModelMap;

    Map<Integer,SkillCharacterEffectModel> skillCharacterEffectModelMap;

    Map<Integer,SkillCharacterStateModel> skillCharacterStateModelMap;

    Map<Integer, NpcAttribModel> npcAttribModelMap;

    Map<Integer, NpcBasicModel> npcBasicModelMap;

    Map<Integer, NpcAiModel> npcAiModelMap;

    Map<Integer,LootNpcModel> lootNpcModelMap;

    Map<Integer,LootDropteamModel> lootDropteamModelMap;

    Map<Integer,LootMiniobjBasicModel> lootMiniobjBasicModelMap;

    Map<Integer,ItemModel> itemModelMap;

    Map<Integer,MisBasicModel> misBasicModelMap;

    Map<Integer,SoulSkinModel> soulSkinMap;

    Map<Integer,AiCharacterBasicModel> aiBasicModelMap;

    private StaticDictionary(){

        loadCharacterAttributesModels();

        loadCharacterBasicModels();

        loadSkillCharacterBasicModels();

        loadSkillCharacterCarrierModels();

        loadSkillCharacterEffectModels();

        loadSkillCharacterStateModels();

        loadNpcAttribModels();

        loadNpcBasicModels();

        loadNpcAiModels();

        loadLootNpcModels();

        loadLootDropteamModels();

        loadLootMiniobjBasicModels();

        loadItemModels();

        loadMisBasicModels();

        loadSkillSoulModels();

        loadAiBasicModels();

        System.out.println("load static data finish !!!");

    }

    public static StaticDictionary getInstance(){
        if(instance == null){
            instance = new StaticDictionary();
        }
        return instance;
    }

    private void loadCharacterAttributesModels(){
        CharacterAttributesDao characterAttributesDao = (CharacterAttributesDao) BeanFactory.getBean("characterAttributesDao");
        characterAttributesDao.load();
        Collection<CharacterAttributesModel> models = characterAttributesDao.getConfigs();
        characterAttributesModelMap = new HashMap<>();
        for(CharacterAttributesModel model : models){
            characterAttributesModelMap.put(model.getId(),model);
        }
    }

    public CharacterAttributesModel getCharacterAttributesModelById(int id){
        return characterAttributesModelMap.get(id);
    }

    public CharacterAttributesModel getCharacterAttributesModelByGrowClassAndLevel(int growClass,int level){
        Set<Integer> set = characterAttributesModelMap.keySet();
        for(int key : set){
            CharacterAttributesModel attribModel = characterAttributesModelMap.get(key);
            if(attribModel.getGrowClass() == growClass && level >= attribModel.getLevel().get(0) && level <= attribModel.getLevel().get(1)){
                return attribModel;
            }
        }

        return null;
    }

    private void loadCharacterBasicModels(){
        CharacterBasicDao characterBasicDao = (CharacterBasicDao) BeanFactory.getBean("characterBasicDao");
        characterBasicDao.load();
        Collection<CharacterBasicModel> models = characterBasicDao.getConfigs();
        characterBasicModelMap = new HashMap<>();
        for(CharacterBasicModel model : models){
            characterBasicModelMap.put(model.getCharacterId(),model);
        }
    }

    public CharacterBasicModel getCharacterBasicModelById(int id){
        return characterBasicModelMap.get(id);
    }

    private void loadSkillCharacterBasicModels(){
        SkillCharacterBasicDao skillCharacterBasicDao = (SkillCharacterBasicDao) BeanFactory.getBean("skillCharacterBasicDao");
        skillCharacterBasicDao.load();
        Collection<SkillCharacterBasicModel> models = skillCharacterBasicDao.getConfigs();
        skillCharacterBasicModelMap = new HashMap<>();
        for(SkillCharacterBasicModel model : models){
            skillCharacterBasicModelMap.put(model.getSkillId(),model);
        }
    }

    public SkillCharacterBasicModel getSkillCharacterBasicModelById(int skillId){
        return skillCharacterBasicModelMap.get(skillId);
    }

    public List<SkillCharacterBasicModel> getSkillCharacterBasicModelsByReleaseType(int releaseType){
        List<SkillCharacterBasicModel> list = new ArrayList<>();
        for(SkillCharacterBasicModel model : skillCharacterBasicModelMap.values()){
            if(model.getSkillType() == releaseType){
                list.add(model);
            }
        }
        return list;
    }

    private void loadSkillCharacterCarrierModels(){
        SkillCharacterCarrierDao skillCharacterCarrierDao = (SkillCharacterCarrierDao) BeanFactory.getBean("skillCharacterCarrierDao");
        skillCharacterCarrierDao.load();
        Collection<SkillCharacterCarrierModel> models = skillCharacterCarrierDao.getConfigs();
        skillCharacterCarrierModelMap = new HashMap<>();
        for(SkillCharacterCarrierModel model : models){
            skillCharacterCarrierModelMap.put(model.getCarrierId(),model);
        }
    }

    public SkillCharacterCarrierModel getSkillCharacterCarrierModelById(int carrierId){
        return skillCharacterCarrierModelMap.get(carrierId);
    }

    private void loadSkillCharacterEffectModels(){
        SkillCharacterEffectDao skillCharacterEffectDao = (SkillCharacterEffectDao) BeanFactory.getBean("skillCharacterEffectDao");
        skillCharacterEffectDao.load();
        Collection<SkillCharacterEffectModel> models = skillCharacterEffectDao.getConfigs();
        skillCharacterEffectModelMap = new HashMap<>();
        for(SkillCharacterEffectModel model : models){
            skillCharacterEffectModelMap.put(model.getEffectId(),model);
        }
    }

    public SkillCharacterEffectModel getSkillCharacterEffectModelById(int effectId){
        return skillCharacterEffectModelMap.get(effectId);
    }

    private void loadSkillCharacterStateModels(){
        SkillCharacterStateDao skillCharacterStateDao = (SkillCharacterStateDao) BeanFactory.getBean("skillCharacterStateDao");
        skillCharacterStateDao.load();
        Collection<SkillCharacterStateModel> models = skillCharacterStateDao.getConfigs();
        skillCharacterStateModelMap = new HashMap<>();
        for(SkillCharacterStateModel model : models){
            skillCharacterStateModelMap.put(model.getStateId(),model);
        }
    }

    public SkillCharacterStateModel getSkillCharacterStateModelById(int stateId){
        return skillCharacterStateModelMap.get(stateId);
    }

    public void getElementSkillCharacterStateModel(Map<Integer,SkillCharacterStateModel> elementBuffMap,Map<Integer,SkillCharacterStateModel> elementDeBuffMap,Map<Integer,SkillCharacterStateModel> elementProficientMap){

        for(SkillCharacterStateModel stateModel : skillCharacterStateModelMap.values()){
            if(stateModel.getStateNature() == BuffManager.STATE_TYPE_ELEMENT_BUFF){
                elementBuffMap.put(stateModel.getElementType(),stateModel);
            }
            if(stateModel.getStateNature() == BuffManager.STATE_TYPE_ELEMENT_DEBUFF){
                elementDeBuffMap.put(stateModel.getElementType(),stateModel);
            }
            if(stateModel.getStateNature() == BuffManager.STATE_TYPE_ELEMENT_PROFICIENT){
                elementProficientMap.put(stateModel.getElementType(),stateModel);
            }
        }
    }

    private void loadNpcAttribModels(){
        NpcAttribDao npcAttribDao = (NpcAttribDao) BeanFactory.getBean("npcAttribDao");
        npcAttribDao.load();
        Collection<NpcAttribModel> models = npcAttribDao.getConfigs();
        npcAttribModelMap = new HashMap<>();
        for(NpcAttribModel model : models){
            if(model.getNpcLv().size() != 2){
                System.err.println("load npc_attrib fail ! npc_lv should have two element !!!");
                continue;
            }
            npcAttribModelMap.put(model.getNpcAttribId(),model);
        }
    }

    public NpcAttribModel getNpcAttributeModelById(int id){
        return npcAttribModelMap.get(id);
    }

    public NpcAttribModel getNpcAttributeModelByGrowClassAndNpcLv(int growClass,int npcLv){
        Set<Integer> set = npcAttribModelMap.keySet();
        for(int key : set){
            NpcAttribModel attribModel = npcAttribModelMap.get(key);
            if(attribModel.getGrowClass() == growClass && npcLv >= attribModel.getNpcLv().get(0) && npcLv <= attribModel.getNpcLv().get(1)){
                return attribModel;
            }
        }

        return null;
    }

    private void loadNpcBasicModels(){
        NpcBasicDao npcBasicDao = (NpcBasicDao) BeanFactory.getBean("npcBasicDao");
        npcBasicDao.load();
        Collection<NpcBasicModel> models = npcBasicDao.getConfigs();
        npcBasicModelMap = new HashMap<>();
        for(NpcBasicModel model : models){
            npcBasicModelMap.put(model.getNpcId(),model);
        }
    }

    public NpcBasicModel getNpcBasicModelById(int id){
        return npcBasicModelMap.get(id);
    }

    private void loadNpcAiModels(){
        NpcAiDao npcAiDao = (NpcAiDao) BeanFactory.getBean("npcAiDao");
        npcAiDao.load();
        Collection<NpcAiModel> models = npcAiDao.getConfigs();
        npcAiModelMap = new HashMap<>();
        for(NpcAiModel model : models){
            npcAiModelMap.put(model.getAiId(),model);
        }
    }

    public NpcAiModel getNpcAiModelById(int id){
        return npcAiModelMap.get(id);
    }

    private void loadLootNpcModels(){
        LootNpcDao lootNpcDao = (LootNpcDao) BeanFactory.getBean("lootNpcDao");
        lootNpcDao.load();
        Collection<LootNpcModel> models = lootNpcDao.getConfigs();
        lootNpcModelMap = new HashMap<>();
        for(LootNpcModel model : models){
            lootNpcModelMap.put(model.getId(),model);
        }
    }

    public LootNpcModel getLootNpcModelByNpcIdAndNpcLv(int npcId,int npcLv){
        Set<Integer> set = lootNpcModelMap.keySet();
        for(int id : set){
            LootNpcModel model = lootNpcModelMap.get(id);
            if(model.getNpcId() == npcId){
                if(model.getNpcLv().size() != 2){
                    continue;
                }
                if(model.getNpcLv().get(0) <= npcLv && model.getNpcLv().get(1) >= npcLv){
                    return model;
                }
            }
        }
        return null;
    }

    private void loadLootDropteamModels(){
        LootDropteamDao lootDropteamDao = (LootDropteamDao) BeanFactory.getBean("lootDropteamDao");
        lootDropteamDao.load();
        Collection<LootDropteamModel> models = lootDropteamDao.getConfigs();
        lootDropteamModelMap = new HashMap<>();
        for(LootDropteamModel model : models){
            lootDropteamModelMap.put(model.getId(),model);
        }
    }

    public List<LootDropteamModel> getLootDropteamModelByDropteamIdAndCharacter(int dropteamId, int characterType){
        List<LootDropteamModel> list = new ArrayList<>();
        Set<Integer> set = lootDropteamModelMap.keySet();
        for(int id : set){
            LootDropteamModel model = lootDropteamModelMap.get(id);
            if(model.getDropteamId() == dropteamId){
                List<Integer> characters = model.getDropteamCharacter();
                for(int character : characters){
                    //0=全职业
                    if(character == 0 || character == characterType){
                        list.add(model);
                    }
                }
            }
        }
        return list;
    }

    private void loadLootMiniobjBasicModels(){
        LootMiniobjBasicDao lootMiniobjBasicDao = (LootMiniobjBasicDao) BeanFactory.getBean("lootMiniobjBasicDao");
        lootMiniobjBasicDao.load();
        Collection<LootMiniobjBasicModel> models = lootMiniobjBasicDao.getConfigs();
        lootMiniobjBasicModelMap = new HashMap<>();
        for(LootMiniobjBasicModel model : models){
            lootMiniobjBasicModelMap.put(model.getMiniobjId(),model);
        }
    }

    public LootMiniobjBasicModel getLootMiniobjBasicModelById(int id){
        return lootMiniobjBasicModelMap.get(id);
    }

    private void loadItemModels(){
        ItemDao itemDao = (ItemDao) BeanFactory.getBean("itemDao");
        itemDao.load();
        Collection<ItemModel> models = itemDao.getConfigs();
        itemModelMap = new HashMap<>();
        for(ItemModel model : models){
            itemModelMap.put(model.getItemId(),model);
        }
    }

    public ItemModel getItemModelById(int id){
        return itemModelMap.get(id);
    }

    private void loadMisBasicModels(){
        MisBasicDao misBasicDao = (MisBasicDao) BeanFactory.getBean("misBasicDao");
        misBasicDao.load();
        Collection<MisBasicModel> models = misBasicDao.getConfigs();
        misBasicModelMap = new HashMap<>();
        for(MisBasicModel model : models){
            misBasicModelMap.put(model.getMisId(),model);
        }
    }
    public MisBasicModel getMisBasicModelById(int id){
        return misBasicModelMap.get(id);
    }

    public List<MisBasicModel> getAllMisBasicModel(){
        return new ArrayList<>(misBasicModelMap.values());
    }

    private void loadSkillSoulModels(){
        SoulSkinDao soulSkinDao = (SoulSkinDao) BeanFactory.getBean("soulSkinDao");
        soulSkinDao.load();
        Collection<SoulSkinModel> models = soulSkinDao.getConfigs();
        soulSkinMap= new HashMap<>();
        models.forEach((soulSkinModel -> soulSkinMap.put(soulSkinModel.getKey(),soulSkinModel)));
    }

    public  SoulSkinModel getSkillSoulSkinModel(int id){return soulSkinMap.get(id);}


    private void loadAiBasicModels(){
        AiCharacterBasicDao aiCharacterBasicDao = (AiCharacterBasicDao) BeanFactory.getBean("aiCharacterBasicDao");
        aiCharacterBasicDao.load();
        Collection<AiCharacterBasicModel> models = aiCharacterBasicDao.getConfigs();
        aiBasicModelMap = new HashMap<>();
        for(AiCharacterBasicModel model : models){
            aiBasicModelMap.put(model.getAiId(),model);
        }
    }

    public AiCharacterBasicModel getAiCharacterBasicModel(int aiId){
        return aiBasicModelMap.get(aiId);
    }

}
