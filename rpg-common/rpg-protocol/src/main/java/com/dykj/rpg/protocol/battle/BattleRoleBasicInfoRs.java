package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleRoleBasicInfoRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9011;
	//游戏角色信息
	private List<com.dykj.rpg.protocol.battle.BattleRoleBasic> roleBasics;
	public BattleRoleBasicInfoRs(){
		this.roleBasics = new ArrayList<>();
	}
	public BattleRoleBasicInfoRs(List<com.dykj.rpg.protocol.battle.BattleRoleBasic> roleBasics){
		this.roleBasics = roleBasics;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		for(com.dykj.rpg.protocol.battle.BattleRoleBasic value : roleBasics){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.roleBasics.clear();
	}
	public short getCode(){
		return this.code;
	}
	public List<com.dykj.rpg.protocol.battle.BattleRoleBasic> getRoleBasics(){
		return this.roleBasics;
	}
	public void setRoleBasics(List<com.dykj.rpg.protocol.battle.BattleRoleBasic> _roleBasics){
		this.roleBasics = _roleBasics;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		roleBasics = (List<com.dykj.rpg.protocol.battle.BattleRoleBasic>)dch.bytesToParam("List<com.dykj.rpg.protocol.battle.BattleRoleBasic>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(roleBasics)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleRoleBasicInfoRs [code=" + code +",roleBasics=" + roleBasics +"]";
	}
}
