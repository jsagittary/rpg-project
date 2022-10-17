package com.dykj.rpg.game.module.cache;

import com.dykj.rpg.common.data.model.PlayerCardRecordModel;
import com.dykj.rpg.common.data.model.PlayerCardResultModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 玩家抽卡缓存
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/30
 */
public class PlayerCardCache
{

    /**
     * 玩家抽卡记录缓存key1-卡池id key2-卡池按钮id
     */
    private final Map<Integer, Map<Integer, PlayerCardRecordModel>> cardRecordCacheMap = new ConcurrentHashMap<>();

    /**
     * 抽卡结果缓存
     */
    private final Map<Long, PlayerCardResultModel> cardResultCacheMap = new ConcurrentHashMap<>();

    /**
     * 更新玩家抽卡记录缓存
     * @param model 玩家抽卡记录model
     */
    public void updateCache(PlayerCardRecordModel model)
    {
        Map<Integer, PlayerCardRecordModel> playerCardMap = cardRecordCacheMap.get(model.primary2Key());
        if (null == playerCardMap || playerCardMap.isEmpty())
        {
            Map<Integer, PlayerCardRecordModel> buttonMap = new ConcurrentHashMap<>();
            buttonMap.put(model.primary3Key(), model);
            cardRecordCacheMap.put(model.primary2Key(), buttonMap);
        }
        else
        {
            playerCardMap.put(model.primary3Key(), model);
        }
    }

    /**
     * 查询当前玩家所有卡池抽卡记录缓存
     * @return 抽卡记录缓存列表
     */
    public List<PlayerCardRecordModel> getAllCardRecordList()
    {
        List<PlayerCardRecordModel> list = new ArrayList<>();
        for (Map<Integer, PlayerCardRecordModel> entry : cardRecordCacheMap.values())
        {
            list.addAll(entry.values());
        }
        return list;
    }

    /**
     * 根据卡池id查询当前卡池下所有抽卡记录缓存
     * @param key2 卡池id
     * @return 当前卡池下所有玩家抽卡记录缓存列表
     */
    public List<PlayerCardRecordModel> getCardRecordList(Integer key2)
    {
        Map<Integer, PlayerCardRecordModel> cardCache = cardRecordCacheMap.get(key2);
        if (null != cardCache && !cardCache.isEmpty())
        {
            return new ArrayList<>(cardCache.values());
        }
        return null;
    }

    /**
     *  根据卡池id、卡池按钮id获取指定抽卡记录信息
     * @param key2 卡池id
     * @param key3 卡池按钮id
     * @return 指定抽卡记录信息
     */
    public PlayerCardRecordModel getCardRecord(Integer key2, Integer key3)
    {
        Map<Integer, PlayerCardRecordModel> cardCache = cardRecordCacheMap.get(key2);
        if (null != cardCache && !cardCache.isEmpty())
        {
            return cardCache.get(key3);
        }
        return null;
    }

    /**
     * 更新玩家抽卡结果缓存
     * @param model 抽卡结果
     */
    public void updateCache(PlayerCardResultModel model)
    {
        cardResultCacheMap.put(model.primary2Key(), model);
    }

    /**
     * 根据卡池id查询当前卡池下所有抽卡结果缓存
     * @param cardId 卡池id
     * @return 抽卡结果
     */
    public PlayerCardResultModel getCardResult(Long cardId)
    {
        return cardResultCacheMap.get(cardId);
    }

    /**
     * 获取当前玩家所有抽卡结果
     * @return 抽卡结果列表
     */
    public Collection<PlayerCardResultModel> cardResultValues()
    {
        return cardResultCacheMap.values();
    }
}