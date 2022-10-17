package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleAiSyncData extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9043;
	//当场战斗当前服务器端的总运行帧数
	private int frameNum;
	//技巧集合
	private List<com.dykj.rpg.protocol.battle.BattleAiInfo> aiInfos;
	public BattleAiSyncData(){
		this.aiInfos = new ArrayList<>();
	}
	public BattleAiSyncData(int frameNum,List<com.dykj.rpg.protocol.battle.BattleAiInfo> aiInfos){
		this.frameNum = frameNum;
		this.aiInfos = aiInfos;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.frameNum = 0;
		for(com.dykj.rpg.protocol.battle.BattleAiInfo value : aiInfos){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.aiInfos.clear();
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
	public List<com.dykj.rpg.protocol.battle.BattleAiInfo> getAiInfos(){
		return this.aiInfos;
	}
	public void setAiInfos(List<com.dykj.rpg.protocol.battle.BattleAiInfo> _aiInfos){
		this.aiInfos = _aiInfos;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		frameNum = (int)dch.bytesToParam("Integer");
		aiInfos = (List<com.dykj.rpg.protocol.battle.BattleAiInfo>)dch.bytesToParam("List<com.dykj.rpg.protocol.battle.BattleAiInfo>");
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
		if(!ech.paramToBytes(aiInfos)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleAiSyncData [code=" + code +",frameNum=" + frameNum +",aiInfos=" + aiInfos +"]";
	}
}
