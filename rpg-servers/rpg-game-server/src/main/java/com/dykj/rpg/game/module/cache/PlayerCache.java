package com.dykj.rpg.game.module.cache;

import com.dykj.rpg.common.data.model.PlayerInfoAttachedModel;
import com.dykj.rpg.common.data.model.PlayerInfoModel;
import com.dykj.rpg.common.data.model.PlayerMissionModel;
import com.dykj.rpg.game.module.attribute.logic.AttributeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * @author jyb
 * @date 2021/4/20 15:30
 * @Description 玩家基础数据缓存
 * 缓存的属性名跟类名要一致，否则会报错
 */
public class PlayerCache {
    /**
     * 玩家缓存选举角色
     */
    private PlayerInfoModel playerInfoModel;
    /**
     * 玩家关卡
     */
    private PlayerMissionModel playerMissionModel;
    /**
     * 玩家装备缓存
     */
    private PlayerEquipCache playerEquipCache;
    /**
     * 玩家装备栏位的缓存
     */
    private EquipPosCache equipPosCache;
    /**
     * 属性集合
     */
    private AttributeCache attributeCache;
    /**
     * 玩家技能缓存
     */
    private PlayerSkillCache playerSkillCache;
    /**
     * 玩家雕纹缓存
     */
    private PlayerAiCache playerAiCache;

    /**
     * 玩家附属信息
     */
    private PlayerInfoAttachedModel playerInfoAttachedModel;

    /**
     * 玩家背包
     */
    private PlayerItemCache playerItemCache;

    /**
     * 玩家任务
     */
    private PlayerTaskCache playerTaskCache;

    /**
     * 玩家抽卡
     */
    private PlayerCardCache playerCardCache;

    /**
     * 玩家邮件
     */
    private PlayerMailCache playerMailCache;

    /**
     * 玩家灵魂之影缓存
     */
    private PlayerSoulSkinCache playerSoulSkinCache;

    /**
     * 玩家符文缓存
     */
    private PlayerRuneCache playerRuneCache;


    public PlayerMissionModel getPlayerMissionModel() {
        return playerMissionModel;
    }


    private Logger logger = LoggerFactory.getLogger(getClass());

    public void setPlayerMissionModel(PlayerMissionModel playerMissionModel) {
        this.playerMissionModel = playerMissionModel;
    }

    public PlayerCache(PlayerInfoModel playerInfoModel) {
        this.playerInfoModel = playerInfoModel;
    }

    public PlayerInfoModel getPlayerInfoModel() {
        return playerInfoModel;
    }

    public void setPlayerInfoModel(PlayerInfoModel playerInfoModel) {
        this.playerInfoModel = playerInfoModel;
    }

    public PlayerEquipCache getPlayerEquipCache() {
        return playerEquipCache;
    }

    public void setPlayerEquipCache(PlayerEquipCache playerEquipCache) {
        this.playerEquipCache = playerEquipCache;
    }

    public int getPlayerId() {
        return playerInfoModel.getPlayerId();
    }

    public AttributeCache getAttributeCache() {
        return attributeCache;
    }

    public PlayerInfoAttachedModel getPlayerInfoAttachedModel() {
        return playerInfoAttachedModel;
    }

    public void setPlayerInfoAttachedModel(PlayerInfoAttachedModel playerInfoAttachedModel) {
        this.playerInfoAttachedModel = playerInfoAttachedModel;
    }

    public PlayerSkillCache getPlayerSkillCache() {
        return playerSkillCache;
    }

    public void setPlayerSkillCache(PlayerSkillCache playerSkillCache) {
        this.playerSkillCache = playerSkillCache;
    }

    public PlayerAiCache getPlayerAiCache() {
        return playerAiCache;
    }

    public void setPlayerAiCache(PlayerAiCache playerAiCache) {
        this.playerAiCache = playerAiCache;
    }

    public EquipPosCache getEquipPosCache() {
        return equipPosCache;
    }

    public void setEquipPosCache(EquipPosCache equipPosCache) {
        this.equipPosCache = equipPosCache;
    }

    public PlayerItemCache getPlayerItemCache() {
        return playerItemCache;
    }

    public void setPlayerItemCache(PlayerItemCache playerItemCache) {
        this.playerItemCache = playerItemCache;
    }

    public PlayerTaskCache getPlayerTaskCache() {
        return playerTaskCache;
    }

    public void setPlayerTaskCache(PlayerTaskCache playerTaskCache) {
        this.playerTaskCache = playerTaskCache;
    }

    public PlayerCardCache getPlayerCardCache() {
        return playerCardCache;
    }

    public void setPlayerCardCache(PlayerCardCache playerCardCache) {
        this.playerCardCache = playerCardCache;
    }

    public void setAttributeCache(AttributeCache attributeCache) {
        this.attributeCache = attributeCache;
    }

    public PlayerMailCache getPlayerMailCache() {
        return playerMailCache;
    }

    public void setPlayerMailCache(PlayerMailCache playerMailCache) {
        this.playerMailCache = playerMailCache;
    }

    public PlayerSoulSkinCache getPlayerSoulSkinCache() {
        return playerSoulSkinCache;
    }

    public void setPlayerSoulSkinCache(PlayerSoulSkinCache playerSoulSkinCache) {
        this.playerSoulSkinCache = playerSoulSkinCache;
    }

    public PlayerRuneCache getPlayerRuneCache()
    {
        return playerRuneCache;
    }

    public void setPlayerRuneCache(PlayerRuneCache playerRuneCache)
    {
        this.playerRuneCache = playerRuneCache;
    }

    /**
     * 反射设置值
     *
     * @param clazz
     */
    public void setCache(Class clazz) {
        try {
            String name = clazz.getSimpleName();
            char[] charFirst = new char[]{name.charAt(0)};
            String strFirst = new String(charFirst);
            Field f = this.getClass().getDeclaredField(name.replaceFirst(strFirst, strFirst.toLowerCase()));
            if (f == null) {
                logger.error("attribute  {} is not exist ", clazz.getSimpleName());
            }
            f.setAccessible(true);
            f.set(this, clazz.newInstance());
        } catch (Exception e) {
            logger.error("player  setCache error {} ", e);
        }
    }

    /**
     * 反射获取对象
     *
     * @param clazz
     * @return
     */
    public Object getCache(Class clazz) {
        Object o = null;
        try {
            String name = clazz.getSimpleName();
            char[] charFirst = new char[]{name.charAt(0)};
            String strFirst = new String(charFirst);
            Field f = this.getClass().getDeclaredField(name.replaceFirst(strFirst, strFirst.toLowerCase()));
            if (f == null) {
                logger.error("attribute  {} is not exist ", clazz.getSimpleName());
            }
            f.setAccessible(true);
            o = f.get(this);
        } catch (Exception e) {
            logger.error("player  setCache error {} ", e);
        }
        return o;
    }

}