package com.dykj.rpg.common.log.temporary;

import com.dykj.rpg.common.log.BaseLog;

/**
 * @Description 关卡信息表
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/16
 */
public class StageData extends BaseLog
{
    private Integer id;//日副本id
    private Integer chaptersId;//章节id
    private String fightBase;//底座图片
    private String fightBasePos;//站位坐标
    private Integer consume;//每次消耗体力
    private Integer difficulty;//难度
    private Integer stageid;//关卡顺序ID
    private String type;//关卡类型
    private String stagename;//关卡名称说明
    private Integer timeOver;//通关时限
    private String description;//关卡描述
    private Integer bubbleDial;//剧情脚本
    private String encounterGroup;//怪物组(10000|10001)
    private Integer money;//奖励金钱
    private Integer exp;//奖励经验
    private Integer firstDrop;//首次掉落
    private Integer drop;//关卡掉落
    private Integer worldDrop;//世界掉落
    private Integer maxChallengeCount;//关卡可挑战次数
    private String fightMap;//战斗地图
    private Integer raidsDropExp;//扫荡经验
    private Integer reqiureLevel;//开放等级
    private Integer recommendPower;//推荐战力
    private String roleGroup;//玩家组
    private String awardIcon;//奖励图标
    private Integer startId;//起始剧情编号
    private Integer peopleNum;//关卡人数限制
    private String roleLimit;//进入角色限制
    private String roleAdv;//推荐角色
    private String enemyPre;//敌方阵容
    private String eventId;//事件组ID与执行规则
    private String mechanismGroup;//场景机关组

    public StageData(){};

    public StageData(Integer id, Integer chaptersId, String fightBase, String fightBasePos, Integer consume, Integer difficulty, Integer stageid, String type, String stagename, Integer timeOver, String description, Integer bubbleDial, String encounterGroup, Integer money, Integer exp, Integer firstDrop, Integer drop, Integer worldDrop, Integer maxChallengeCount, String fightMap, Integer raidsDropExp, Integer reqiureLevel, Integer recommendPower, String roleGroup, String awardIcon, Integer startId, Integer peopleNum, String roleLimit, String roleAdv, String enemyPre, String eventId, String mechanismGroup)
    {
        this.id = id;
        this.chaptersId = chaptersId;
        this.fightBase = fightBase;
        this.fightBasePos = fightBasePos;
        this.consume = consume;
        this.difficulty = difficulty;
        this.stageid = stageid;
        this.type = type;
        this.stagename = stagename;
        this.timeOver = timeOver;
        this.description = description;
        this.bubbleDial = bubbleDial;
        this.encounterGroup = encounterGroup;
        this.money = money;
        this.exp = exp;
        this.firstDrop = firstDrop;
        this.drop = drop;
        this.worldDrop = worldDrop;
        this.maxChallengeCount = maxChallengeCount;
        this.fightMap = fightMap;
        this.raidsDropExp = raidsDropExp;
        this.reqiureLevel = reqiureLevel;
        this.recommendPower = recommendPower;
        this.roleGroup = roleGroup;
        this.awardIcon = awardIcon;
        this.startId = startId;
        this.peopleNum = peopleNum;
        this.roleLimit = roleLimit;
        this.roleAdv = roleAdv;
        this.enemyPre = enemyPre;
        this.eventId = eventId;
        this.mechanismGroup = mechanismGroup;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getChaptersId()
    {
        return chaptersId;
    }

    public void setChaptersId(Integer chaptersId)
    {
        this.chaptersId = chaptersId;
    }

    public String getFightBase()
    {
        return fightBase;
    }

    public void setFightBase(String fightBase)
    {
        this.fightBase = fightBase;
    }

    public String getFightBasePos()
    {
        return fightBasePos;
    }

    public void setFightBasePos(String fightBasePos)
    {
        this.fightBasePos = fightBasePos;
    }

    public Integer getConsume()
    {
        return consume;
    }

    public void setConsume(Integer consume)
    {
        this.consume = consume;
    }

    public Integer getDifficulty()
    {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty)
    {
        this.difficulty = difficulty;
    }

    public Integer getStageid()
    {
        return stageid;
    }

    public void setStageid(Integer stageid)
    {
        this.stageid = stageid;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getStagename()
    {
        return stagename;
    }

    public void setStagename(String stagename)
    {
        this.stagename = stagename;
    }

    public Integer getTimeOver()
    {
        return timeOver;
    }

    public void setTimeOver(Integer timeOver)
    {
        this.timeOver = timeOver;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Integer getBubbleDial()
    {
        return bubbleDial;
    }

    public void setBubbleDial(Integer bubbleDial)
    {
        this.bubbleDial = bubbleDial;
    }

    public String getEncounterGroup()
    {
        return encounterGroup;
    }

    public void setEncounterGroup(String encounterGroup)
    {
        this.encounterGroup = encounterGroup;
    }

    public Integer getMoney()
    {
        return money;
    }

    public void setMoney(Integer money)
    {
        this.money = money;
    }

    public Integer getExp()
    {
        return exp;
    }

    public void setExp(Integer exp)
    {
        this.exp = exp;
    }

    public Integer getFirstDrop()
    {
        return firstDrop;
    }

    public void setFirstDrop(Integer firstDrop)
    {
        this.firstDrop = firstDrop;
    }

    public Integer getDrop()
    {
        return drop;
    }

    public void setDrop(Integer drop)
    {
        this.drop = drop;
    }

    public Integer getWorldDrop()
    {
        return worldDrop;
    }

    public void setWorldDrop(Integer worldDrop)
    {
        this.worldDrop = worldDrop;
    }

    public Integer getMaxChallengeCount()
    {
        return maxChallengeCount;
    }

    public void setMaxChallengeCount(Integer maxChallengeCount)
    {
        this.maxChallengeCount = maxChallengeCount;
    }

    public String getFightMap()
    {
        return fightMap;
    }

    public void setFightMap(String fightMap)
    {
        this.fightMap = fightMap;
    }

    public Integer getRaidsDropExp()
    {
        return raidsDropExp;
    }

    public void setRaidsDropExp(Integer raidsDropExp)
    {
        this.raidsDropExp = raidsDropExp;
    }

    public Integer getReqiureLevel()
    {
        return reqiureLevel;
    }

    public void setReqiureLevel(Integer reqiureLevel)
    {
        this.reqiureLevel = reqiureLevel;
    }

    public Integer getRecommendPower()
    {
        return recommendPower;
    }

    public void setRecommendPower(Integer recommendPower)
    {
        this.recommendPower = recommendPower;
    }

    public String getRoleGroup()
    {
        return roleGroup;
    }

    public void setRoleGroup(String roleGroup)
    {
        this.roleGroup = roleGroup;
    }

    public String getAwardIcon()
    {
        return awardIcon;
    }

    public void setAwardIcon(String awardIcon)
    {
        this.awardIcon = awardIcon;
    }

    public Integer getStartId()
    {
        return startId;
    }

    public void setStartId(Integer startId)
    {
        this.startId = startId;
    }

    public Integer getPeopleNum()
    {
        return peopleNum;
    }

    public void setPeopleNum(Integer peopleNum)
    {
        this.peopleNum = peopleNum;
    }

    public String getRoleLimit()
    {
        return roleLimit;
    }

    public void setRoleLimit(String roleLimit)
    {
        this.roleLimit = roleLimit;
    }

    public String getRoleAdv()
    {
        return roleAdv;
    }

    public void setRoleAdv(String roleAdv)
    {
        this.roleAdv = roleAdv;
    }

    public String getEnemyPre()
    {
        return enemyPre;
    }

    public void setEnemyPre(String enemyPre)
    {
        this.enemyPre = enemyPre;
    }

    public String getEventId()
    {
        return eventId;
    }

    public void setEventId(String eventId)
    {
        this.eventId = eventId;
    }

    public String getMechanismGroup()
    {
        return mechanismGroup;
    }

    public void setMechanismGroup(String mechanismGroup)
    {
        this.mechanismGroup = mechanismGroup;
    }

    @Override
    public String toString()
    {
        return "StageData{" + "id=" + id + ", chaptersId=" + chaptersId + ", fightBase='" + fightBase + '\'' + ", fightBasePos='" + fightBasePos + '\'' + ", consume=" + consume + ", difficulty=" + difficulty + ", stageid=" + stageid + ", type='" + type + '\'' + ", stagename='" + stagename + '\'' + ", timeOver=" + timeOver + ", description='" + description + '\'' + ", bubbleDial=" + bubbleDial + ", encounterGroup='" + encounterGroup + '\'' + ", money=" + money + ", exp=" + exp + ", firstDrop=" + firstDrop + ", drop=" + drop + ", worldDrop=" + worldDrop + ", maxChallengeCount=" + maxChallengeCount + ", fightMap='" + fightMap + '\'' + ", raidsDropExp=" + raidsDropExp + ", reqiureLevel=" + reqiureLevel + ", recommendPower=" + recommendPower + ", roleGroup='" + roleGroup + '\'' + ", awardIcon='" + awardIcon + '\'' + ", startId=" + startId + ", peopleNum=" + peopleNum + ", roleLimit='" + roleLimit + '\'' + ", roleAdv='" + roleAdv + '\'' + ", enemyPre='" + enemyPre + '\'' + ", eventId='" + eventId + '\'' + ", mechanismGroup='" + mechanismGroup + '\'' + '}';
    }
}