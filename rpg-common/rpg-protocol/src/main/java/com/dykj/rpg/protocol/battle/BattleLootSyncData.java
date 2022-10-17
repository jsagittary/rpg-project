package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleLootSyncData extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9029;
	//当场战斗当前服务器端的总运行帧数
	private int frameNum;
	private List<com.dykj.rpg.protocol.battle.BattleRoleLootInfo> roleLoots;
	public BattleLootSyncData(){
		this.roleLoots = new ArrayList<>();
	}
	public BattleLootSyncData(int frameNum,List<com.dykj.rpg.protocol.battle.BattleRoleLootInfo> roleLoots){
		this.frameNum = frameNum;
		this.roleLoots = roleLoots;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.frameNum = 0;
		for(com.dykj.rpg.protocol.battle.BattleRoleLootInfo value : roleLoots){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.roleLoots.clear();
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
	public List<com.dykj.rpg.protocol.battle.BattleRoleLootInfo> getRoleLoots(){
		return this.roleLoots;
	}
	public void setRoleLoots(List<com.dykj.rpg.protocol.battle.BattleRoleLootInfo> _roleLoots){
		this.roleLoots = _roleLoots;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		frameNum = (int)dch.bytesToParam("Integer");
		roleLoots = (List<com.dykj.rpg.protocol.battle.BattleRoleLootInfo>)dch.bytesToParam("List<com.dykj.rpg.protocol.battle.BattleRoleLootInfo>");
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
		if(!ech.paramToBytes(roleLoots)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleLootSyncData [code=" + code +",frameNum=" + frameNum +",roleLoots=" + roleLoots +"]";
	}
}
