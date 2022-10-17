package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleBuffSyncData extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9038;
	//当场战斗当前服务器端的总运行帧数
	private int frameNum;
	//buff集合
	private List<com.dykj.rpg.protocol.battle.BattleRoleSkillBuff> skillBuffs;
	public BattleBuffSyncData(){
		this.skillBuffs = new ArrayList<>();
	}
	public BattleBuffSyncData(int frameNum,List<com.dykj.rpg.protocol.battle.BattleRoleSkillBuff> skillBuffs){
		this.frameNum = frameNum;
		this.skillBuffs = skillBuffs;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.frameNum = 0;
		for(com.dykj.rpg.protocol.battle.BattleRoleSkillBuff value : skillBuffs){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.skillBuffs.clear();
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
	public List<com.dykj.rpg.protocol.battle.BattleRoleSkillBuff> getSkillBuffs(){
		return this.skillBuffs;
	}
	public void setSkillBuffs(List<com.dykj.rpg.protocol.battle.BattleRoleSkillBuff> _skillBuffs){
		this.skillBuffs = _skillBuffs;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		frameNum = (int)dch.bytesToParam("Integer");
		skillBuffs = (List<com.dykj.rpg.protocol.battle.BattleRoleSkillBuff>)dch.bytesToParam("List<com.dykj.rpg.protocol.battle.BattleRoleSkillBuff>");
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
		if(!ech.paramToBytes(skillBuffs)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleBuffSyncData [code=" + code +",frameNum=" + frameNum +",skillBuffs=" + skillBuffs +"]";
	}
}
