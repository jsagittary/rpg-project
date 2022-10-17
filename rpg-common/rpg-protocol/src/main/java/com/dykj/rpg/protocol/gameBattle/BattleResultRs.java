package com.dykj.rpg.protocol.gameBattle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleResultRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1603;
	//关卡结算奖励
	private List<com.dykj.rpg.protocol.item.ItemRs> award;
	//战斗服的随机奖励
	private List<com.dykj.rpg.protocol.item.ItemRs> randomAward;
	private int battleType;
	//挑战boss关卡cd结束时间
	private long battleCdEndTime;
	//false=战斗失败，true=战斗胜利
	private boolean result;
	//解锁哪个关卡
	private int openMissionId;
	//通关条件
	private List<com.dykj.rpg.protocol.battle.SuccessConditionInfo> conditions;
	public BattleResultRs(){
		this.award = new ArrayList<>();
		this.randomAward = new ArrayList<>();
		this.conditions = new ArrayList<>();
	}
	public BattleResultRs(List<com.dykj.rpg.protocol.item.ItemRs> award,List<com.dykj.rpg.protocol.item.ItemRs> randomAward,int battleType,long battleCdEndTime,boolean result,int openMissionId,List<com.dykj.rpg.protocol.battle.SuccessConditionInfo> conditions){
		this.award = award;
		this.randomAward = randomAward;
		this.battleType = battleType;
		this.battleCdEndTime = battleCdEndTime;
		this.result = result;
		this.openMissionId = openMissionId;
		this.conditions = conditions;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		for(com.dykj.rpg.protocol.item.ItemRs value : award){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.award.clear();
		for(com.dykj.rpg.protocol.item.ItemRs value : randomAward){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.randomAward.clear();
		this.battleType = 0;
		this.battleCdEndTime = 0;
		this.result = false;
		this.openMissionId = 0;
		for(com.dykj.rpg.protocol.battle.SuccessConditionInfo value : conditions){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.conditions.clear();
	}
	public short getCode(){
		return this.code;
	}
	public List<com.dykj.rpg.protocol.item.ItemRs> getAward(){
		return this.award;
	}
	public void setAward(List<com.dykj.rpg.protocol.item.ItemRs> _award){
		this.award = _award;
	}
	public List<com.dykj.rpg.protocol.item.ItemRs> getRandomAward(){
		return this.randomAward;
	}
	public void setRandomAward(List<com.dykj.rpg.protocol.item.ItemRs> _randomAward){
		this.randomAward = _randomAward;
	}
	public int getBattleType(){
		return this.battleType;
	}
	public void setBattleType(int _battleType){
		this.battleType = _battleType;
	}
	public long getBattleCdEndTime(){
		return this.battleCdEndTime;
	}
	public void setBattleCdEndTime(long _battleCdEndTime){
		this.battleCdEndTime = _battleCdEndTime;
	}
	public boolean getResult(){
		return this.result;
	}
	public void setResult(boolean _result){
		this.result = _result;
	}
	public int getOpenMissionId(){
		return this.openMissionId;
	}
	public void setOpenMissionId(int _openMissionId){
		this.openMissionId = _openMissionId;
	}
	public List<com.dykj.rpg.protocol.battle.SuccessConditionInfo> getConditions(){
		return this.conditions;
	}
	public void setConditions(List<com.dykj.rpg.protocol.battle.SuccessConditionInfo> _conditions){
		this.conditions = _conditions;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		award = (List<com.dykj.rpg.protocol.item.ItemRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.item.ItemRs>");
		randomAward = (List<com.dykj.rpg.protocol.item.ItemRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.item.ItemRs>");
		battleType = (int)dch.bytesToParam("Integer");
		battleCdEndTime = (long)dch.bytesToParam("Long");
		result = (boolean)dch.bytesToParam("Boolean");
		openMissionId = (int)dch.bytesToParam("Integer");
		conditions = (List<com.dykj.rpg.protocol.battle.SuccessConditionInfo>)dch.bytesToParam("List<com.dykj.rpg.protocol.battle.SuccessConditionInfo>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(award)){
			return false;
		}
		if(!ech.paramToBytes(randomAward)){
			return false;
		}
		if(!ech.paramToBytes(battleType)){
			return false;
		}
		if(!ech.paramToBytes(battleCdEndTime)){
			return false;
		}
		if(!ech.paramToBytes(result)){
			return false;
		}
		if(!ech.paramToBytes(openMissionId)){
			return false;
		}
		if(!ech.paramToBytes(conditions)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleResultRs [code=" + code +",award=" + award +",randomAward=" + randomAward +",battleType=" + battleType +",battleCdEndTime=" + battleCdEndTime +",result=" + result +",openMissionId=" + openMissionId +",conditions=" + conditions +"]";
	}
}
