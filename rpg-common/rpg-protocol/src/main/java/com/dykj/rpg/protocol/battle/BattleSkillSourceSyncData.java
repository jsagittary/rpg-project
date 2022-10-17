package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleSkillSourceSyncData extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9033;
	//当场战斗当前服务器端的总运行帧数
	private int frameNum;
	private List<com.dykj.rpg.protocol.battle.BattleSkillSourceInfo> sourceInfos;
	public BattleSkillSourceSyncData(){
		this.sourceInfos = new ArrayList<>();
	}
	public BattleSkillSourceSyncData(int frameNum,List<com.dykj.rpg.protocol.battle.BattleSkillSourceInfo> sourceInfos){
		this.frameNum = frameNum;
		this.sourceInfos = sourceInfos;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.frameNum = 0;
		for(com.dykj.rpg.protocol.battle.BattleSkillSourceInfo value : sourceInfos){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.sourceInfos.clear();
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
	public List<com.dykj.rpg.protocol.battle.BattleSkillSourceInfo> getSourceInfos(){
		return this.sourceInfos;
	}
	public void setSourceInfos(List<com.dykj.rpg.protocol.battle.BattleSkillSourceInfo> _sourceInfos){
		this.sourceInfos = _sourceInfos;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		frameNum = (int)dch.bytesToParam("Integer");
		sourceInfos = (List<com.dykj.rpg.protocol.battle.BattleSkillSourceInfo>)dch.bytesToParam("List<com.dykj.rpg.protocol.battle.BattleSkillSourceInfo>");
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
		if(!ech.paramToBytes(sourceInfos)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleSkillSourceSyncData [code=" + code +",frameNum=" + frameNum +",sourceInfos=" + sourceInfos +"]";
	}
}
