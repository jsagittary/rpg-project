package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleEffectSyncData extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9025;
	//当场战斗当前服务器端的总运行帧数
	private int frameNum;
	private List<com.dykj.rpg.protocol.battle.BattleSkillEffectInfo> skillEffects;
	public BattleEffectSyncData(){
		this.skillEffects = new ArrayList<>();
	}
	public BattleEffectSyncData(int frameNum,List<com.dykj.rpg.protocol.battle.BattleSkillEffectInfo> skillEffects){
		this.frameNum = frameNum;
		this.skillEffects = skillEffects;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.frameNum = 0;
		for(com.dykj.rpg.protocol.battle.BattleSkillEffectInfo value : skillEffects){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.skillEffects.clear();
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
	public List<com.dykj.rpg.protocol.battle.BattleSkillEffectInfo> getSkillEffects(){
		return this.skillEffects;
	}
	public void setSkillEffects(List<com.dykj.rpg.protocol.battle.BattleSkillEffectInfo> _skillEffects){
		this.skillEffects = _skillEffects;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		frameNum = (int)dch.bytesToParam("Integer");
		skillEffects = (List<com.dykj.rpg.protocol.battle.BattleSkillEffectInfo>)dch.bytesToParam("List<com.dykj.rpg.protocol.battle.BattleSkillEffectInfo>");
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
		if(!ech.paramToBytes(skillEffects)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleEffectSyncData [code=" + code +",frameNum=" + frameNum +",skillEffects=" + skillEffects +"]";
	}
}
