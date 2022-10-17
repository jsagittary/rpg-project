package com.dykj.rpg.protocol.game2battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class EnterBattleRoleInfo extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 8902;
	//玩家角色ID
	private int roleId;
	//玩家角色等级
	private int roleLevel;
	//角色修改的属性信息
	private List<com.dykj.rpg.protocol.game2battle.AttributeInfo> attributeInfos;
	public EnterBattleRoleInfo(){
		this.attributeInfos = new ArrayList<>();
	}
	public EnterBattleRoleInfo(int roleId,int roleLevel,List<com.dykj.rpg.protocol.game2battle.AttributeInfo> attributeInfos){
		this.roleId = roleId;
		this.roleLevel = roleLevel;
		this.attributeInfos = attributeInfos;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.roleId = 0;
		this.roleLevel = 0;
		for(com.dykj.rpg.protocol.game2battle.AttributeInfo value : attributeInfos){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.attributeInfos.clear();
	}
	public short getCode(){
		return this.code;
	}
	public int getRoleId(){
		return this.roleId;
	}
	public void setRoleId(int _roleId){
		this.roleId = _roleId;
	}
	public int getRoleLevel(){
		return this.roleLevel;
	}
	public void setRoleLevel(int _roleLevel){
		this.roleLevel = _roleLevel;
	}
	public List<com.dykj.rpg.protocol.game2battle.AttributeInfo> getAttributeInfos(){
		return this.attributeInfos;
	}
	public void setAttributeInfos(List<com.dykj.rpg.protocol.game2battle.AttributeInfo> _attributeInfos){
		this.attributeInfos = _attributeInfos;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		roleId = (int)dch.bytesToParam("Integer");
		roleLevel = (int)dch.bytesToParam("Integer");
		attributeInfos = (List<com.dykj.rpg.protocol.game2battle.AttributeInfo>)dch.bytesToParam("List<com.dykj.rpg.protocol.game2battle.AttributeInfo>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(roleId)){
			return false;
		}
		if(!ech.paramToBytes(roleLevel)){
			return false;
		}
		if(!ech.paramToBytes(attributeInfos)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "EnterBattleRoleInfo [code=" + code +",roleId=" + roleId +",roleLevel=" + roleLevel +",attributeInfos=" + attributeInfos +"]";
	}
}
