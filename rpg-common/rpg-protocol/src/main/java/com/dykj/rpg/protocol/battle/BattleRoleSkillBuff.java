package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleRoleSkillBuff extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9037;
	//角色的modelId
	private int modelId;
	//buff集合
	private List<com.dykj.rpg.protocol.battle.BattleSkillBuffInfo> buffInfos;
	public BattleRoleSkillBuff(){
		this.buffInfos = new ArrayList<>();
	}
	public BattleRoleSkillBuff(int modelId,List<com.dykj.rpg.protocol.battle.BattleSkillBuffInfo> buffInfos){
		this.modelId = modelId;
		this.buffInfos = buffInfos;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.modelId = 0;
		for(com.dykj.rpg.protocol.battle.BattleSkillBuffInfo value : buffInfos){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.buffInfos.clear();
	}
	public short getCode(){
		return this.code;
	}
	public int getModelId(){
		return this.modelId;
	}
	public void setModelId(int _modelId){
		this.modelId = _modelId;
	}
	public List<com.dykj.rpg.protocol.battle.BattleSkillBuffInfo> getBuffInfos(){
		return this.buffInfos;
	}
	public void setBuffInfos(List<com.dykj.rpg.protocol.battle.BattleSkillBuffInfo> _buffInfos){
		this.buffInfos = _buffInfos;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		modelId = (int)dch.bytesToParam("Integer");
		buffInfos = (List<com.dykj.rpg.protocol.battle.BattleSkillBuffInfo>)dch.bytesToParam("List<com.dykj.rpg.protocol.battle.BattleSkillBuffInfo>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(modelId)){
			return false;
		}
		if(!ech.paramToBytes(buffInfos)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleRoleSkillBuff [code=" + code +",modelId=" + modelId +",buffInfos=" + buffInfos +"]";
	}
}
