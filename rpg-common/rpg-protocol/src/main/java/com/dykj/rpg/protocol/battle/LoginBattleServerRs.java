package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class LoginBattleServerRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9002;
	//登录结果，成功返回true
	private boolean result;
	//关卡ID
	private int misId;
	//当前玩家角色所在的地图编号
	private byte mapIndex;
	//地图信息
	private List<com.dykj.rpg.protocol.battle.BattleMapInfo> mapInfos;
	//玩家角色技能列表
	private List<com.dykj.rpg.protocol.battle.PlayerSkillInfo> skillInfos;
	//技巧列表
	private List<com.dykj.rpg.protocol.battle.BattleAiInfo> aiInfos;
	//玩家药水列表
	private List<com.dykj.rpg.protocol.battle.BattlePotionInfo> potionInfos;
	//战斗类型 1=普通关卡（boss通关），7=守护基地
	private byte battleType;
	//关卡最大时长
	private int battleTime;
	//怪物总波数
	private int totalRounds;
	//通关条件
	private List<com.dykj.rpg.protocol.battle.SuccessConditionInfo> conditions;
	//灵魂之影id 为 0表示没有装载灵魂之影
	private int soulId;
	public LoginBattleServerRs(){
		this.mapInfos = new ArrayList<>();
		this.skillInfos = new ArrayList<>();
		this.aiInfos = new ArrayList<>();
		this.potionInfos = new ArrayList<>();
		this.conditions = new ArrayList<>();
	}
	public LoginBattleServerRs(boolean result,int misId,byte mapIndex,List<com.dykj.rpg.protocol.battle.BattleMapInfo> mapInfos,List<com.dykj.rpg.protocol.battle.PlayerSkillInfo> skillInfos,List<com.dykj.rpg.protocol.battle.BattleAiInfo> aiInfos,List<com.dykj.rpg.protocol.battle.BattlePotionInfo> potionInfos,byte battleType,int battleTime,int totalRounds,List<com.dykj.rpg.protocol.battle.SuccessConditionInfo> conditions,int soulId){
		this.result = result;
		this.misId = misId;
		this.mapIndex = mapIndex;
		this.mapInfos = mapInfos;
		this.skillInfos = skillInfos;
		this.aiInfos = aiInfos;
		this.potionInfos = potionInfos;
		this.battleType = battleType;
		this.battleTime = battleTime;
		this.totalRounds = totalRounds;
		this.conditions = conditions;
		this.soulId = soulId;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.result = false;
		this.misId = 0;
		this.mapIndex = 0;
		for(com.dykj.rpg.protocol.battle.BattleMapInfo value : mapInfos){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.mapInfos.clear();
		for(com.dykj.rpg.protocol.battle.PlayerSkillInfo value : skillInfos){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.skillInfos.clear();
		for(com.dykj.rpg.protocol.battle.BattleAiInfo value : aiInfos){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.aiInfos.clear();
		for(com.dykj.rpg.protocol.battle.BattlePotionInfo value : potionInfos){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.potionInfos.clear();
		this.battleType = 0;
		this.battleTime = 0;
		this.totalRounds = 0;
		for(com.dykj.rpg.protocol.battle.SuccessConditionInfo value : conditions){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.conditions.clear();
		this.soulId = 0;
	}
	public short getCode(){
		return this.code;
	}
	public boolean getResult(){
		return this.result;
	}
	public void setResult(boolean _result){
		this.result = _result;
	}
	public int getMisId(){
		return this.misId;
	}
	public void setMisId(int _misId){
		this.misId = _misId;
	}
	public byte getMapIndex(){
		return this.mapIndex;
	}
	public void setMapIndex(byte _mapIndex){
		this.mapIndex = _mapIndex;
	}
	public List<com.dykj.rpg.protocol.battle.BattleMapInfo> getMapInfos(){
		return this.mapInfos;
	}
	public void setMapInfos(List<com.dykj.rpg.protocol.battle.BattleMapInfo> _mapInfos){
		this.mapInfos = _mapInfos;
	}
	public List<com.dykj.rpg.protocol.battle.PlayerSkillInfo> getSkillInfos(){
		return this.skillInfos;
	}
	public void setSkillInfos(List<com.dykj.rpg.protocol.battle.PlayerSkillInfo> _skillInfos){
		this.skillInfos = _skillInfos;
	}
	public List<com.dykj.rpg.protocol.battle.BattleAiInfo> getAiInfos(){
		return this.aiInfos;
	}
	public void setAiInfos(List<com.dykj.rpg.protocol.battle.BattleAiInfo> _aiInfos){
		this.aiInfos = _aiInfos;
	}
	public List<com.dykj.rpg.protocol.battle.BattlePotionInfo> getPotionInfos(){
		return this.potionInfos;
	}
	public void setPotionInfos(List<com.dykj.rpg.protocol.battle.BattlePotionInfo> _potionInfos){
		this.potionInfos = _potionInfos;
	}
	public byte getBattleType(){
		return this.battleType;
	}
	public void setBattleType(byte _battleType){
		this.battleType = _battleType;
	}
	public int getBattleTime(){
		return this.battleTime;
	}
	public void setBattleTime(int _battleTime){
		this.battleTime = _battleTime;
	}
	public int getTotalRounds(){
		return this.totalRounds;
	}
	public void setTotalRounds(int _totalRounds){
		this.totalRounds = _totalRounds;
	}
	public List<com.dykj.rpg.protocol.battle.SuccessConditionInfo> getConditions(){
		return this.conditions;
	}
	public void setConditions(List<com.dykj.rpg.protocol.battle.SuccessConditionInfo> _conditions){
		this.conditions = _conditions;
	}
	public int getSoulId(){
		return this.soulId;
	}
	public void setSoulId(int _soulId){
		this.soulId = _soulId;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		result = (boolean)dch.bytesToParam("Boolean");
		misId = (int)dch.bytesToParam("Integer");
		mapIndex = (byte)dch.bytesToParam("Byte");
		mapInfos = (List<com.dykj.rpg.protocol.battle.BattleMapInfo>)dch.bytesToParam("List<com.dykj.rpg.protocol.battle.BattleMapInfo>");
		skillInfos = (List<com.dykj.rpg.protocol.battle.PlayerSkillInfo>)dch.bytesToParam("List<com.dykj.rpg.protocol.battle.PlayerSkillInfo>");
		aiInfos = (List<com.dykj.rpg.protocol.battle.BattleAiInfo>)dch.bytesToParam("List<com.dykj.rpg.protocol.battle.BattleAiInfo>");
		potionInfos = (List<com.dykj.rpg.protocol.battle.BattlePotionInfo>)dch.bytesToParam("List<com.dykj.rpg.protocol.battle.BattlePotionInfo>");
		battleType = (byte)dch.bytesToParam("Byte");
		battleTime = (int)dch.bytesToParam("Integer");
		totalRounds = (int)dch.bytesToParam("Integer");
		conditions = (List<com.dykj.rpg.protocol.battle.SuccessConditionInfo>)dch.bytesToParam("List<com.dykj.rpg.protocol.battle.SuccessConditionInfo>");
		soulId = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(result)){
			return false;
		}
		if(!ech.paramToBytes(misId)){
			return false;
		}
		if(!ech.paramToBytes(mapIndex)){
			return false;
		}
		if(!ech.paramToBytes(mapInfos)){
			return false;
		}
		if(!ech.paramToBytes(skillInfos)){
			return false;
		}
		if(!ech.paramToBytes(aiInfos)){
			return false;
		}
		if(!ech.paramToBytes(potionInfos)){
			return false;
		}
		if(!ech.paramToBytes(battleType)){
			return false;
		}
		if(!ech.paramToBytes(battleTime)){
			return false;
		}
		if(!ech.paramToBytes(totalRounds)){
			return false;
		}
		if(!ech.paramToBytes(conditions)){
			return false;
		}
		if(!ech.paramToBytes(soulId)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "LoginBattleServerRs [code=" + code +",result=" + result +",misId=" + misId +",mapIndex=" + mapIndex +",mapInfos=" + mapInfos +",skillInfos=" + skillInfos +",aiInfos=" + aiInfos +",potionInfos=" + potionInfos +",battleType=" + battleType +",battleTime=" + battleTime +",totalRounds=" + totalRounds +",conditions=" + conditions +",soulId=" + soulId +"]";
	}
}
