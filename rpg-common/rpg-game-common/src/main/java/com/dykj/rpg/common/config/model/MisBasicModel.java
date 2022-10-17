package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;
import java.util.Map;

/**
 * @Description 关卡章节
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/03/10
*/
public class MisBasicModel extends BaseConfig<Integer>
{
    private Integer misId;//关卡ID
    private Integer misType;//关卡类型
    private List<List<Integer>> conditionType;//通关条件与显示
    private Integer preId;//上一关卡ID
    private Integer order;//关卡序列
    private Integer chapterId;//所属章节
    private Integer worldId;//所属地图
    private String misMap;//关联主线地图
    private Integer monstLevel;//怪物等级
    private List<List<Integer>> reward;//通关奖励
    private Integer enterLv;//进入等级
    private Integer enterComat;//进入战力
    private Integer waitTime;//进入等待时间（S）
    private Integer idleMisMap;//关联挂机地图
    private List<List<Integer>> idleReward;//每分钟挂机奖励
    private Map<Integer, Integer> difficultyCorrect;//属性难度修正

    public Integer getMisId()
    {
        return this.misId;
    }

    public void setMisId(Integer misId)
    {
        this.misId = misId;
    }

    public Integer getMisType()
    {
        return this.misType;
    }

    public void setMisType(Integer misType)
    {
        this.misType = misType;
    }

    public List<List<Integer>> getConditionType()
    {
        return this.conditionType;
    }

    public void setConditionType(List<List<Integer>> conditionType)
    {
        this.conditionType = conditionType;
    }

    public Integer getPreId()
    {
        return this.preId;
    }

    public void setPreId(Integer preId)
    {
        this.preId = preId;
    }

    public Integer getOrder()
    {
        return this.order;
    }

    public void setOrder(Integer order)
    {
        this.order = order;
    }

    public Integer getChapterId()
    {
        return this.chapterId;
    }

    public void setChapterId(Integer chapterId)
    {
        this.chapterId = chapterId;
    }

    public Integer getWorldId()
    {
        return this.worldId;
    }

    public void setWorldId(Integer worldId)
    {
        this.worldId = worldId;
    }

    public String getMisMap()
    {
        return this.misMap;
    }

    public void setMisMap(String misMap)
    {
        this.misMap = misMap;
    }

    public Integer getMonstLevel()
    {
        return this.monstLevel;
    }

    public void setMonstLevel(Integer monstLevel)
    {
        this.monstLevel = monstLevel;
    }

    public List<List<Integer>> getReward()
    {
        return this.reward;
    }

    public void setReward(List<List<Integer>> reward)
    {
        this.reward = reward;
    }

    public Integer getEnterLv()
    {
        return this.enterLv;
    }

    public void setEnterLv(Integer enterLv)
    {
        this.enterLv = enterLv;
    }

    public Integer getEnterComat()
    {
        return this.enterComat;
    }

    public void setEnterComat(Integer enterComat)
    {
        this.enterComat = enterComat;
    }

    public Integer getWaitTime()
    {
        return this.waitTime;
    }

    public void setWaitTime(Integer waitTime)
    {
        this.waitTime = waitTime;
    }

    public Integer getIdleMisMap()
    {
        return this.idleMisMap;
    }

    public void setIdleMisMap(Integer idleMisMap)
    {
        this.idleMisMap = idleMisMap;
    }

    public List<List<Integer>> getIdleReward()
    {
        return this.idleReward;
    }

    public void setIdleReward(List<List<Integer>> idleReward)
    {
        this.idleReward = idleReward;
    }

    public Map<Integer, Integer> getDifficultyCorrect()
    {
        return this.difficultyCorrect;
    }

    public void setDifficultyCorrect(Map<Integer, Integer> difficultyCorrect)
    {
        this.difficultyCorrect = difficultyCorrect;
    }

    @Override
    public Integer getKey()
    {
        return this.misId;
    }
}