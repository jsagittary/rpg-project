package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleCarrierSyncData extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9026;
	//当场战斗当前服务器端的总运行帧数
	private int frameNum;
	private List<com.dykj.rpg.protocol.battle.BattleSkillCarrierInfo> skillCarriers;
	public BattleCarrierSyncData(){
		this.skillCarriers = new ArrayList<>();
	}
	public BattleCarrierSyncData(int frameNum,List<com.dykj.rpg.protocol.battle.BattleSkillCarrierInfo> skillCarriers){
		this.frameNum = frameNum;
		this.skillCarriers = skillCarriers;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.frameNum = 0;
		for(com.dykj.rpg.protocol.battle.BattleSkillCarrierInfo value : skillCarriers){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.skillCarriers.clear();
	}
	public short getCode(){
		return this.code;
	}
	public int getFrameNum(){
		return this.frameNum;
	}
	public void setFrameNum(int _frameNum){
		this.frameNum = _frameNum;
	}
	public List<com.dykj.rpg.protocol.battle.BattleSkillCarrierInfo> getSkillCarriers(){
		return this.skillCarriers;
	}
	public void setSkillCarriers(List<com.dykj.rpg.protocol.battle.BattleSkillCarrierInfo> _skillCarriers){
		this.skillCarriers = _skillCarriers;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		frameNum = (int)dch.bytesToParam("Integer");
		skillCarriers = (List<com.dykj.rpg.protocol.battle.BattleSkillCarrierInfo>)dch.bytesToParam("List<com.dykj.rpg.protocol.battle.BattleSkillCarrierInfo>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(frameNum)){
			return false;
		}
		if(!ech.paramToBytes(skillCarriers)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleCarrierSyncData [code=" + code +",frameNum=" + frameNum +",skillCarriers=" + skillCarriers +"]";
	}
}
