package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleFinishResultRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9014;
	//战斗结果 false=失败，true=成功
	private boolean result;
	//通关条件
	private List<com.dykj.rpg.protocol.battle.SuccessConditionInfo> conditions;
	public BattleFinishResultRs(){
		this.conditions = new ArrayList<>();
	}
	public BattleFinishResultRs(boolean result,List<com.dykj.rpg.protocol.battle.SuccessConditionInfo> conditions){
		this.result = result;
		this.conditions = conditions;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.result = false;
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
		if(!ech.paramToBytes(conditions)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleFinishResultRs [code=" + code +",result=" + result +",conditions=" + conditions +"]";
	}
}
