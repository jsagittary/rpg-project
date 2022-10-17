package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleRoleSyncData extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9024;
	//当场战斗当前服务器端的总运行帧数
	private int frameNum;
	//玩家，怪物，boss等的游戏数据
	private List<com.dykj.rpg.protocol.battle.BattleRoleInfo> roleInfos;
	public BattleRoleSyncData(){
		this.roleInfos = new ArrayList<>();
	}
	public BattleRoleSyncData(int frameNum,List<com.dykj.rpg.protocol.battle.BattleRoleInfo> roleInfos){
		this.frameNum = frameNum;
		this.roleInfos = roleInfos;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.frameNum = 0;
		for(com.dykj.rpg.protocol.battle.BattleRoleInfo value : roleInfos){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.roleInfos.clear();
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
	public List<com.dykj.rpg.protocol.battle.BattleRoleInfo> getRoleInfos(){
		return this.roleInfos;
	}
	public void setRoleInfos(List<com.dykj.rpg.protocol.battle.BattleRoleInfo> _roleInfos){
		this.roleInfos = _roleInfos;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		frameNum = (int)dch.bytesToParam("Integer");
		roleInfos = (List<com.dykj.rpg.protocol.battle.BattleRoleInfo>)dch.bytesToParam("List<com.dykj.rpg.protocol.battle.BattleRoleInfo>");
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
		if(!ech.paramToBytes(roleInfos)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleRoleSyncData [code=" + code +",frameNum=" + frameNum +",roleInfos=" + roleInfos +"]";
	}
}
