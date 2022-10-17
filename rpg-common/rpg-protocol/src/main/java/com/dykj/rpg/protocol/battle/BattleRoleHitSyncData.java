package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleRoleHitSyncData extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9031;
	//当场战斗当前服务器端的总运行帧数
	private int frameNum;
	private List<com.dykj.rpg.protocol.battle.BattleRoleHitInfo> hitInfos;
	public BattleRoleHitSyncData(){
		this.hitInfos = new ArrayList<>();
	}
	public BattleRoleHitSyncData(int frameNum,List<com.dykj.rpg.protocol.battle.BattleRoleHitInfo> hitInfos){
		this.frameNum = frameNum;
		this.hitInfos = hitInfos;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.frameNum = 0;
		for(com.dykj.rpg.protocol.battle.BattleRoleHitInfo value : hitInfos){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.hitInfos.clear();
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
	public List<com.dykj.rpg.protocol.battle.BattleRoleHitInfo> getHitInfos(){
		return this.hitInfos;
	}
	public void setHitInfos(List<com.dykj.rpg.protocol.battle.BattleRoleHitInfo> _hitInfos){
		this.hitInfos = _hitInfos;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		frameNum = (int)dch.bytesToParam("Integer");
		hitInfos = (List<com.dykj.rpg.protocol.battle.BattleRoleHitInfo>)dch.bytesToParam("List<com.dykj.rpg.protocol.battle.BattleRoleHitInfo>");
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
		if(!ech.paramToBytes(hitInfos)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleRoleHitSyncData [code=" + code +",frameNum=" + frameNum +",hitInfos=" + hitInfos +"]";
	}
}
