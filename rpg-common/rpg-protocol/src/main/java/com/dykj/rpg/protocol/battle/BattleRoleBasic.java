package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleRoleBasic extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9019;
	//角色在地图中分配的唯一id
	private int id;
	//角色类型 1=玩家，2=小怪，3=boss,4=npc
	private byte type;
	//角色模型的id
	private int roleId;
	//角色等级
	private int level;
	//角色最大血量
	private int maxBlood;
	//角色最大魂能量值
	private int maxSoulEnergy;
	//角色技能资源类型
	private int skillSourceType;
	//角色最大技能资源值
	private int maxSkillSourceNum;
	public BattleRoleBasic(){
	}
	public BattleRoleBasic(int id,byte type,int roleId,int level,int maxBlood,int maxSoulEnergy,int skillSourceType,int maxSkillSourceNum){
		this.id = id;
		this.type = type;
		this.roleId = roleId;
		this.level = level;
		this.maxBlood = maxBlood;
		this.maxSoulEnergy = maxSoulEnergy;
		this.skillSourceType = skillSourceType;
		this.maxSkillSourceNum = maxSkillSourceNum;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.id = 0;
		this.type = 0;
		this.roleId = 0;
		this.level = 0;
		this.maxBlood = 0;
		this.maxSoulEnergy = 0;
		this.skillSourceType = 0;
		this.maxSkillSourceNum = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getId(){
		return this.id;
	}
	public void setId(int _id){
		this.id = _id;
	}
	public byte getType(){
		return this.type;
	}
	public void setType(byte _type){
		this.type = _type;
	}
	public int getRoleId(){
		return this.roleId;
	}
	public void setRoleId(int _roleId){
		this.roleId = _roleId;
	}
	public int getLevel(){
		return this.level;
	}
	public void setLevel(int _level){
		this.level = _level;
	}
	public int getMaxBlood(){
		return this.maxBlood;
	}
	public void setMaxBlood(int _maxBlood){
		this.maxBlood = _maxBlood;
	}
	public int getMaxSoulEnergy(){
		return this.maxSoulEnergy;
	}
	public void setMaxSoulEnergy(int _maxSoulEnergy){
		this.maxSoulEnergy = _maxSoulEnergy;
	}
	public int getSkillSourceType(){
		return this.skillSourceType;
	}
	public void setSkillSourceType(int _skillSourceType){
		this.skillSourceType = _skillSourceType;
	}
	public int getMaxSkillSourceNum(){
		return this.maxSkillSourceNum;
	}
	public void setMaxSkillSourceNum(int _maxSkillSourceNum){
		this.maxSkillSourceNum = _maxSkillSourceNum;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		id = (int)dch.bytesToParam("Integer");
		type = (byte)dch.bytesToParam("Byte");
		roleId = (int)dch.bytesToParam("Integer");
		level = (int)dch.bytesToParam("Integer");
		maxBlood = (int)dch.bytesToParam("Integer");
		maxSoulEnergy = (int)dch.bytesToParam("Integer");
		skillSourceType = (int)dch.bytesToParam("Integer");
		maxSkillSourceNum = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(id)){
			return false;
		}
		if(!ech.paramToBytes(type)){
			return false;
		}
		if(!ech.paramToBytes(roleId)){
			return false;
		}
		if(!ech.paramToBytes(level)){
			return false;
		}
		if(!ech.paramToBytes(maxBlood)){
			return false;
		}
		if(!ech.paramToBytes(maxSoulEnergy)){
			return false;
		}
		if(!ech.paramToBytes(skillSourceType)){
			return false;
		}
		if(!ech.paramToBytes(maxSkillSourceNum)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleRoleBasic [code=" + code +",id=" + id +",type=" + type +",roleId=" + roleId +",level=" + level +",maxBlood=" + maxBlood +",maxSoulEnergy=" + maxSoulEnergy +",skillSourceType=" + skillSourceType +",maxSkillSourceNum=" + maxSkillSourceNum +"]";
	}
}
