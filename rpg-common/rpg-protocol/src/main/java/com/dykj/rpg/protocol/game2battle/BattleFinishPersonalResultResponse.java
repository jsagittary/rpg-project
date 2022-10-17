package com.dykj.rpg.protocol.game2battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleFinishPersonalResultResponse extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 8909;
	//false=战斗失败，true=战斗胜利
	private boolean result;
	private int playerId;
	//消耗品，数量传负数
	private List<com.dykj.rpg.protocol.item.ItemRs> items;
	//战斗得到物品
	private List<com.dykj.rpg.protocol.item.ItemRs> awards;
	//通关条件
	private List<com.dykj.rpg.protocol.battle.SuccessConditionInfo> conditions;
	public BattleFinishPersonalResultResponse(){
		this.items = new ArrayList<>();
		this.awards = new ArrayList<>();
		this.conditions = new ArrayList<>();
	}
	public BattleFinishPersonalResultResponse(boolean result,int playerId,List<com.dykj.rpg.protocol.item.ItemRs> items,List<com.dykj.rpg.protocol.item.ItemRs> awards,List<com.dykj.rpg.protocol.battle.SuccessConditionInfo> conditions){
		this.result = result;
		this.playerId = playerId;
		this.items = items;
		this.awards = awards;
		this.conditions = conditions;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.result = false;
		this.playerId = 0;
		for(com.dykj.rpg.protocol.item.ItemRs value : items){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.items.clear();
		for(com.dykj.rpg.protocol.item.ItemRs value : awards){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.awards.clear();
		for(com.dykj.rpg.protocol.battle.SuccessConditionInfo value : conditions){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.conditions.clear();
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
	public int getPlayerId(){
		return this.playerId;
	}
	public void setPlayerId(int _playerId){
		this.playerId = _playerId;
	}
	public List<com.dykj.rpg.protocol.item.ItemRs> getItems(){
		return this.items;
	}
	public void setItems(List<com.dykj.rpg.protocol.item.ItemRs> _items){
		this.items = _items;
	}
	public List<com.dykj.rpg.protocol.item.ItemRs> getAwards(){
		return this.awards;
	}
	public void setAwards(List<com.dykj.rpg.protocol.item.ItemRs> _awards){
		this.awards = _awards;
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
		result = (boolean)dch.bytesToParam("Boolean");
		playerId = (int)dch.bytesToParam("Integer");
		items = (List<com.dykj.rpg.protocol.item.ItemRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.item.ItemRs>");
		awards = (List<com.dykj.rpg.protocol.item.ItemRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.item.ItemRs>");
		conditions = (List<com.dykj.rpg.protocol.battle.SuccessConditionInfo>)dch.bytesToParam("List<com.dykj.rpg.protocol.battle.SuccessConditionInfo>");
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
		if(!ech.paramToBytes(playerId)){
			return false;
		}
		if(!ech.paramToBytes(items)){
			return false;
		}
		if(!ech.paramToBytes(awards)){
			return false;
		}
		if(!ech.paramToBytes(conditions)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleFinishPersonalResultResponse [code=" + code +",result=" + result +",playerId=" + playerId +",items=" + items +",awards=" + awards +",conditions=" + conditions +"]";
	}
}
